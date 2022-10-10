package a.tlib.utils.retrofit

import a.tlib.utils.FileUtil.fileType
import a.tlib.utils.defSP
import a.tlib.utils.retrofit.observer.LoadOnSubscribe
import a.tlib.utils.retrofit.observer.ProgressReqObserver
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.security.MessageDigest

/**
 * @author 田桂森 2021/3/25 0025
 */
object FileDownloadUtil {
    var FileDownStatus by defSP(0)

    /**
     * 文件下载
     * @param url
     * @param loadLis
     */
    @JvmStatic
    fun downLoadFile(url: String, loadLis: DownLoadLis<File>) {
        val filePath: String = fileType
        val tempFile: File = getTempFile(url, filePath)
        val loadOnSubscribe = LoadOnSubscribe()
        val downloadObservable = Observable.just(url)
                .map<String>(object : Function<String, String> {
                    @Throws(java.lang.Exception::class)
                    override fun apply(downUrl: String): String {
                        val targetFile: File = getFile(downUrl, filePath)
                        return if (targetFile.exists()) {
                            FileDownStatus = 4//下载完成
                            "文件已下载"
                        } else {
                            "bytes=" + tempFile.length() + "-"
                        }
                    }
                })
                .flatMap<ResponseBody> { downParam ->
                    Log.d("retrofit", "文件下载开始---" + Thread.currentThread().name)
                    if (downParam.startsWith("bytes=")) {
                        otherApi.download(downParam, url)
                    } else {
                        FileDownStatus = 4
                        null
                    }
                }
                .map<File>(object : Function<ResponseBody, File> {
                    @Throws(java.lang.Exception::class)
                    override fun apply(responseBody: ResponseBody): File? {
                        try {
                            Log.d("retrofit", "文件下载 响应返回---" + Thread.currentThread().name)
                            return saveFile(loadOnSubscribe, responseBody, url, filePath)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        return null
                    }
                })
        Observable.merge(Observable.create(loadOnSubscribe), downloadObservable)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ProgressReqObserver<Any>() {
                    protected override fun onProgress(percent: String) {
                        loadLis.onProgress(percent)
                    }

                    protected override fun onSuccess(file: Any) {
                        if (file is File) {
                            loadLis.onProgress("100")
                            val mainHandler = Handler(Looper.getMainLooper())
                            mainHandler.post {
                                loadLis.onSuccess(file as File?) //已在主线程中，可以更新UI
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (FileDownStatus == 4) {
                            val targetFile: File = getFile(url, filePath)
                            loadLis.onProgress("100")
                            loadLis.onSuccess(targetFile)
                        } else {
                            FileDownStatus = 3
                            loadLis.onFail()
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        if (FileDownStatus != 4) {
                            FileDownStatus = 3
                        }
                    }
                })
    }

    /**
     * 根据ResponseBody 写文件
     *
     * @param responseBody
     * @param url
     * @param filePath     文件保存路径
     * @return
     */
    @JvmStatic
    private fun saveFile(loadOnSubscribe: LoadOnSubscribe?, responseBody: ResponseBody, url: String, filePath: String): File? {
        val tempFile = createTempFile(url, filePath)
        var file: File? = null
        try {
            file = writeFileToDisk(loadOnSubscribe, responseBody, tempFile.absolutePath)
            if (FileDownStatus == 4) {
                val renameSuccess = reNameFile(url, tempFile.path)
                return getFile(url, filePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    /**
     * 单线程 断点下载
     *
     * @param loadOnSubscribe
     * @param responseBody
     * @param filePath
     * @return
     * @throws IOException
     */
    @JvmStatic
    @SuppressLint("DefaultLocale")
    @Throws(IOException::class)
    private fun writeFileToDisk(loadOnSubscribe: LoadOnSubscribe?, responseBody: ResponseBody, filePath: String?): File {
        val totalByte = responseBody.contentLength()
        Log.d("retrofit", "文件下载 写数据" + "---" + Thread.currentThread().name)
        val file = File(filePath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        } else {
            if (null != loadOnSubscribe) {
                loadOnSubscribe.setmSumLength(file.length() + totalByte)
                loadOnSubscribe.onRead(file.length())
            }
        }
        FileDownStatus = 1//正在下载
        val randomAccessFile = RandomAccessFile(file, "rwd")
        val tempFileLen = file.length()
        randomAccessFile.seek(tempFileLen)
        val buffer = ByteArray(1024 * 4)
        val `is` = responseBody.byteStream()
        var downloadByte: Long = 0
        while (true) {
            val len = `is`.read(buffer)
            if (len == -1) { //下载完成
                if (null != loadOnSubscribe) loadOnSubscribe.clean()
                FileDownStatus = 4 //下载完成
                break
            }
            if (FileDownStatus == 2 || FileDownStatus == 3) break //暂停或者取消 停止下载
            randomAccessFile.write(buffer, 0, len)
            downloadByte += len.toLong()
            if (null != loadOnSubscribe) loadOnSubscribe.onRead(len.toLong())
        }
        `is`.close()
        randomAccessFile.close()
        return file
    }

    /**
     * 生成临时文件
     *
     * @param url
     * @param filePath
     * @return
     */
    @JvmStatic
    private fun createTempFile(url: String, filePath: String): File {
        val md5 = getMD5(url) + ".temp"
        return fileIsExists("$filePath/$md5")
    }

    /**
     * 判断指定路径的 文件 是否存在，不存在创建文件
     *
     * @param filePath
     * @return
     */
    @JvmStatic
    private fun fileIsExists(filePath: String): File {
        val file = File(filePath)
        try {
            if (!file.isFile) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    /**
     * 根据 url 重命名 指定的 文件（路径）
     *
     * @param url
     * @param oldPath 路径
     */
    @JvmStatic
    private fun reNameFile(url: String, oldPath: String?): Boolean {
        val fileName = getFileName(url)
        val oldFile = File(oldPath)
        return oldFile.renameTo(File(oldFile.parent, fileName))
    }

    /**
     * 根据传入的 String 生成MD5
     *
     * @param s
     */
    @JvmStatic
    private fun getMD5(s: String): String? {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
//        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        //        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return try {
            val btInput = s.toByteArray(charset("utf-8"))
            val mdInst = MessageDigest.getInstance("MD5")
            mdInst.update(btInput)
            val md = mdInst.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 根据 url 和 指定的路径，获取文件
     *
     * @param url
     * @param path
     * @return
     */
    @JvmStatic
    private fun getFile(url: String, path: String): File {
        val fileName = getFileName(url)
        return File(path, fileName)
    }

    /**
     * 根据 url 生成 文件名
     *
     * @param url
     */
    @JvmStatic
    private fun getFileName(url: String): String {
        val fileName: String
        val md5Name = getMD5(url)
        fileName = if (url.indexOf("?") == -1) {
            md5Name + url.substring(url.lastIndexOf("."))
        } else {
            val temp = url.substring(0, url.indexOf("?"))
            if (temp.indexOf(".") == -1) {
                "$md5Name.temp"
            } else {
                md5Name + temp.substring(temp.lastIndexOf("."))
            }
        }
        return fileName
    }

    /**
     * 获取临时文件 名称
     * @param url
     * @param filePath
     * @return
     */
    @JvmStatic
    private fun getTempFile(url: String, filePath: String): File {
        val md5: String = getMD5(url).toString() + ".temp"
        return File("$filePath/$md5")
    }

}