package a.tlib.utils

import a.tlib.LibApp.app
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.util.concurrent.ExecutionException

/**
 * @author 田桂森 2020/1/6 0006
 */
object FileUtil {
    /**
     * 文件名前缀，目前用在图片，使用时候再加上时间戳
     */
    @JvmField
    val fileNameprefix = "youbo"

    /**
     * 文件存放路径
     */
    val fileType = Environment.getExternalStorageDirectory().toString() + "/youbo/"


    /**
     * 清理缓存
     */
    fun clearCache() {
        GlobalScope.launch {
            Glide.getPhotoCacheDir(app)
            Glide.get(app).clearDiskCache()
            Glide.get(app).clearDiskCache()
            scanImage(app, fileType)
            try {
                val filses = File(fileType).listFiles()
                filses.forEach {
                    it.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun scanImage(activity: Context, file: String) {  // 扫描图片文件
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE) //ACTION_MEDIA_SCANNER_SCAN_DIR
        scanIntent.setData(Uri.fromFile(File(file)))
        activity.sendBroadcast(scanIntent)
    }

    fun formetFileSize(fileS: Long): String {
        val df = DecimalFormat("#.0")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            fileList.forEach {
                if (it.isDirectory) {
                    size += getFolderSize(it)
                } else {
                    size += it.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    @Throws(java.lang.Exception::class)
    fun getTotalCacheSize(context: Context): Long {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir!!)
        }
        return cacheSize
    }
    
    fun deleteFiles(root: File) {
        val files = root.listFiles()
        if (files != null) {
            for (f in files) {
                if (!f.isDirectory && f.exists()) { // 判断是否存在
                    try {
                        f.delete()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    //保存网络图片到相册
    @JvmStatic
    fun saveImageUrlToGallery(context: Activity, url: String?) {
        try {
            ToastUtil.normal("下载成功")
            GlobalScope.launch {
                Glide.with(context).asBitmap().load(url).into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        BitmapUtil.saveBitmapToDir(context, fileType, fileNameprefix, resource, true)
                    }
                })
            }
        } catch (e: ExecutionException) {
        } catch (e: InterruptedException) {
        }
    }

    /**
     * 查看一个文件是否存在
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    @JvmStatic
    fun fileExists(downloadPath: String?, fileName: String?): Boolean {
        return File(downloadPath, fileName).exists()
    }

    /**
     * 创建一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return 文件
     */
    @JvmStatic
    fun createFile(downloadPath: String?, fileName: String?): File? {
        return File(downloadPath, fileName)
    }
    @JvmStatic
    fun imageToBase64(path: String?): String {
        if (TextUtils.isEmpty(path)) {
            return ""
        }
        var `is`: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            `is` = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(`is`.available())
            //写入数组
            `is`.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result!!
    }
}