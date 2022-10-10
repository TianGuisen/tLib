package a.tlib.utils

import a.tlib.LibApp
import a.tlib.LibApp.app
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.reflect.Method
import java.text.DecimalFormat
import java.util.*
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
     * android/data/com.youbo.video/
     */
    @JvmField
    val fileType = app.getExternalFilesDir(null).toString() + "/youbo/"

    private const val sBufferSize = 524288

    /**
     * 清理缓存
     */
    @JvmStatic
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

    @JvmStatic
    fun scanImage(activity: Context, file: String) {  // 扫描图片文件
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE) //ACTION_MEDIA_SCANNER_SCAN_DIR
        scanIntent.setData(Uri.fromFile(File(file)))
        activity.sendBroadcast(scanIntent)
    }

    @JvmStatic
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
    @JvmStatic
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
    @JvmStatic
    @Throws(java.lang.Exception::class)
    fun getTotalCacheSize(context: Context): Long {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir!!)
        }
        return cacheSize
    }

    @JvmStatic
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
    fun createFile(downloadPath: String, fileName: String): File? {
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

    /**
     * 从assets目录中复制某文件内容
     *
     * @param assetFileName assets目录下的文件
     * @param newFileName   复制到/data/data/package_name/files/目录下文件名
     */
    @JvmStatic
    fun copyAssetsToAppFiles(context: Context, assetFileName: String, newFileName: String) {
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            `is` = context.getAssets().open(assetFileName)
            fos = context.openFileOutput(newFileName, Context.MODE_PRIVATE)
            var byteCount = 0
            val buffer = ByteArray(1024)
            while (`is`.read(buffer).also { byteCount = it } != -1) {
                fos!!.write(buffer, 0, byteCount)
            }
            fos!!.flush()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
                fos?.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 通过path获取文件名
     */
    @JvmStatic
    fun getFileNameFromPath(path: String?, split: Char = '/'): String? {
        if (path.isNullOrBlank()) return null
        val cut = path.lastIndexOf(split)
        if (cut != -1) return path.substring(cut + 1)
        return path
    }

    /**
     * 通过uri获取文件扩展名
     */
    @JvmStatic
    fun getExtension(uri: Uri?): String {
        var name = if (uri == null) return "" else ""
        app.contentResolver.query(uri, null, null, null, null)
                ?.use { c: Cursor ->
                    if (c.moveToFirst()) name = getExtension(c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME)))
                }
        return name
    }

    /**
     * url : https://app-xxx-oss/xxx.gif
     * fileName : xxx.gif
     * @return 默认返回gif, fullExtension=false ;
     *         substring时不加1为 .gif, fullExtension=true
     */
    @JvmStatic
    private fun getExtension(pathOrName: String?, split: Char, fullExtension: Boolean = false): String {
        if (pathOrName.isNullOrBlank()) return ""
        val dot = pathOrName.lastIndexOf(split)
        return if (dot != -1) pathOrName.substring(
                if (fullExtension) dot
                else (dot + 1)
        ).toLowerCase(Locale.getDefault())
        else "" // No extension.
    }

    /**
     * 通过path获取文件扩展名，比如jpg
     */
    @JvmStatic
    fun getExtension(pathOrName: String): String = getExtension(pathOrName, '.', false)

    /**
     * 通过path获取文件扩展名，比如.jpg
     */
    @JvmStatic
    fun getExtensionFull(pathOrName: String): String = getExtension(pathOrName, '.', true)

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    @JvmStatic
    fun uri2File(uri: Uri?): File? {
        if (uri == null) return null
        val file: File? = uri2FileReal(uri)
        return file ?: copyUri2Cache(uri)
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    @JvmStatic
    private fun uri2FileReal(uri: Uri): File? {
        Log.d("UriUtils", uri.toString())
        val authority = uri.authority
        val scheme = uri.scheme
        val path = uri.path
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null) {
            val externals = arrayOf("/external/", "/external_path/")
            var file: File? = null
            for (external in externals) {
                if (path.startsWith(external)) {
                    file = File(Environment.getExternalStorageDirectory().absolutePath
                            + path.replace(external, "/"))
                    if (file.exists()) {
                        Log.d("UriUtils", "$uri -> $external")
                        return file
                    }
                }
            }
            file = null
            if (path.startsWith("/files_path/")) {
                file = File(LibApp.app.getFilesDir().getAbsolutePath()
                        .toString() + path.replace("/files_path/", "/"))
            } else if (path.startsWith("/cache_path/")) {
                file = File(LibApp.app.getCacheDir().getAbsolutePath()
                        .toString() + path.replace("/cache_path/", "/"))
            } else if (path.startsWith("/external_files_path/")) {
                file = File(LibApp.app.getExternalFilesDir(null)?.getAbsolutePath()
                        .toString() + path.replace("/external_files_path/", "/"))
            } else if (path.startsWith("/external_cache_path/")) {
                file = File(LibApp.app.getExternalCacheDir()?.getAbsolutePath()
                        .toString() + path.replace("/external_cache_path/", "/"))
            }
            if (file != null && file.exists()) {
                Log.d("UriUtils", "$uri -> $path")
                return file
            }
        }
        return if (ContentResolver.SCHEME_FILE == scheme) {
            if (path != null) return File(path)
            Log.d("UriUtils", "$uri parse failed. -> 0")
            null
        } // end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(LibApp.app, uri)) {
            if ("com.android.externalstorage.documents" == authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return File(Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    val mStorageManager: StorageManager = LibApp.app.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                    try {
                        val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                        val getVolumeList: Method = mStorageManager.javaClass.getMethod("getVolumeList")
                        val getUuid: Method = storageVolumeClazz.getMethod("getUuid")
                        val getState: Method = storageVolumeClazz.getMethod("getState")
                        val getPath: Method = storageVolumeClazz.getMethod("getPath")
                        val isPrimary: Method = storageVolumeClazz.getMethod("isPrimary")
                        val isEmulated: Method = storageVolumeClazz.getMethod("isEmulated")
                        val result: Any = getVolumeList.invoke(mStorageManager)
                        val length: Int = (result as Array<Any>).size
                        for (i in 0 until length) {
                            val storageVolumeElement: Any = (result as Array<Any>)[i]
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);
                            val mounted = Environment.MEDIA_MOUNTED == getState.invoke(storageVolumeElement) || Environment.MEDIA_MOUNTED_READ_ONLY == getState.invoke(storageVolumeElement)

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue

                            //Primary storage is already handled.
                            if ((isPrimary.invoke(storageVolumeElement) as Boolean)
                                    && (isEmulated.invoke(storageVolumeElement)) as Boolean) {
                                continue
                            }
                            val uuid = getUuid.invoke(storageVolumeElement) as String
                            if (uuid != null && uuid == type) {
                                return File(getPath.invoke(storageVolumeElement).toString() + "/" + split[1])
                            }
                        }
                    } catch (ex: java.lang.Exception) {
                        Log.d("UriUtils", "$uri parse failed. $ex -> 1_0")
                    }
                }
                Log.d("UriUtils", "$uri parse failed. -> 1_0")
                null
            } // end 1_0
            else if ("com.android.providers.downloads.documents" == authority) {
                var id = DocumentsContract.getDocumentId(uri)
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtils", "$uri parse failed(id is null). -> 1_1")
                    return null
                }
                if (id.startsWith("raw:")) {
                    return File(id.substring(4))
                } else if (id.startsWith("msf:")) {
                    id = id.split(":".toRegex()).toTypedArray()[1]
                }
                var availableId: Long = 0
                availableId = try {
                    id.toLong()
                } catch (e: java.lang.Exception) {
                    return null
                }
                val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                )
                for (contentUriPrefix in contentUriPrefixesToTry) {
                    val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId)
                    try {
                        val file: File? = getFileFromUri(contentUri, "1_1")
                        if (file != null) {
                            return file
                        }
                    } catch (ignore: java.lang.Exception) {
                    }
                }
                Log.d("UriUtils", "$uri parse failed. -> 1_1")
                null
            } // end 1_1
            else if ("com.android.providers.media.documents" == authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                val contentUri: Uri
                contentUri = if ("image" == type) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                } else {
                    Log.d("UriUtils", "$uri parse failed. -> 1_2")
                    return null
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                getFileFromUri(contentUri, "1_2", selection, selectionArgs)
            } // end 1_2
            else if (ContentResolver.SCHEME_CONTENT == scheme) {
                getFileFromUri(uri, "1_3")
            } // end 1_3
            else {
                Log.d("UriUtils", "$uri parse failed. -> 1_4")
                null
            } // end 1_4
        } // end 1
        else if (ContentResolver.SCHEME_CONTENT == scheme) {
            getFileFromUri(uri, "2")
        } // end 2
        else {
            Log.d("UriUtils", "$uri parse failed. -> 3")
            null
        } // end 3
    }

    @JvmStatic
    private fun getFileFromUri(uri: Uri, code: String, selection: String? = null, selectionArgs: Array<String>? = null): File? {
        if ("com.google.android.apps.photos.content" == uri.authority) {
            if (!TextUtils.isEmpty(uri.lastPathSegment)) {
                return File(uri.lastPathSegment)
            }
        } else if ("com.tencent.mtt.fileprovider" == uri.authority) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                val fileDir = Environment.getExternalStorageDirectory()
                return File(fileDir, path!!.substring("/QQBrowser".length, path.length))
            }
        } else if ("com.huawei.hidisk.fileprovider" == uri.authority) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                return File(path!!.replace("/root", ""))
            }
        }
        val cursor: Cursor? = LibApp.app.getContentResolver().query(
                uri, arrayOf("_data"), selection, selectionArgs, null)
        if (cursor == null) {
            Log.d("UriUtils", "$uri parse failed(cursor is null). -> $code")
            return null
        }
        return try {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("_data")
                if (columnIndex > -1) {
                    File(cursor.getString(columnIndex))
                } else {
                    Log.d("UriUtils", "$uri parse failed(columnIndex: $columnIndex is wrong). -> $code")
                    null
                }
            } else {
                Log.d("UriUtils", "$uri parse failed(moveToFirst return false). -> $code")
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d("UriUtils", "$uri parse failed. -> $code")
            null
        } finally {
            cursor.close()
        }
    }

    @JvmStatic
    private fun copyUri2Cache(uri: Uri): File? {
        Log.d("UriUtils", "copyUri2Cache() called")
        var `is`: InputStream? = null
        return try {
            `is` = LibApp.app.getContentResolver().openInputStream(uri)
            val file = File(LibApp.app.getCacheDir(), "" + System.currentTimeMillis())
            writeFileFromIS(file, `is`)
            file
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    /**
     * 写入文件
     *
     * @param file     The file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    @JvmStatic
    fun writeFileFromIS(file: File, `is`: InputStream?, append: Boolean = false, listener: ((Double) -> Unit)? = null): Boolean {
        if (`is` == null || !createOrExistsFile(file)) {
            Log.e("FileIOUtils", "create file <$file> failed.")
            return false
        }
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(FileOutputStream(file, append), sBufferSize)
            if (listener == null) {
                val data = ByteArray(sBufferSize)
                var len: Int
                while (`is`.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                }
            } else {
                val totalSize = `is`.available().toDouble()
                var curSize = 0
                listener.invoke(0.0)
                val data = ByteArray(sBufferSize)
                var len: Int
                while (`is`.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                    curSize += len
                    listener.invoke(curSize / totalSize)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 创建一个文件，如果已经存在则什么也不做
     */
    @JvmStatic
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 创建一个不存在的目录，如果已经存在则什么也不做。
     */
    @JvmStatic
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 通过Uri获取文件名
     */
    @JvmStatic
    fun getFileNameFromUri(uri: Uri?): String? {
        if (uri == null) return null
        var filename: String? = null

        val resolver = LibApp.app.contentResolver
        val mimeType = resolver.getType(uri)

        if (mimeType == null) {
            filename = getFileNameFromPath(getFileAbsolutePath(app, uri))
        } else {
            resolver.query(uri, null, null, null, null)?.use { c: Cursor ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                c.moveToFirst()
                filename = c.getString(nameIndex)
            }
        }
        return filename
    }

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换 兼容Android 10
     *
     * @param context
     * @param uri
     */
    @JvmStatic
    fun getFileAbsolutePath(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) {
            return null
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, uri)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return app.getExternalFilesDir(null).toString() + "/" + split[1]
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type || "audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            }
        } // MediaStore (and general)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, uri)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                uri.lastPathSegment
            } else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */
    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun uriToFileApiQ(context: Context, uri: Uri): String? {
        var file: File? = null
        //android10以上转换
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            file = File(uri.path)
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件复制到沙盒目录
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                try {
                    val `is` = contentResolver.openInputStream(uri)
                    val cache = File(context.externalCacheDir!!.absolutePath, displayName)
                    val fos = FileOutputStream(cache)
                    FileUtils.copy(`is`!!, fos)
                    file = cache
                    fos.close()
                    `is`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file!!.absolutePath
    }

    /**
     * 4.4以下uri转path
     */
    @JvmStatic
    private fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)

//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    @JvmStatic
    private fun getDataColumn(context: Context, uri: Uri, selection: String? = null, selectionArgs: Array<String>? = null): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * 通知系统扫描文件
     */
    @JvmStatic
    fun notifySystemToScan(filePath: String) {
        notifySystemToScan(File(filePath))
    }

    /**
     * 通知系统扫描文件
     */
    @JvmStatic
    fun notifySystemToScan(file: File) {
        if (!file.exists()) return
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.parse("file://" + file.absolutePath)
        LibApp.app.sendBroadcast(intent)
    }

    /**
     * 通知系统扫描文件
     */
    @JvmStatic
    fun notifySystemToScan(uri: Uri) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = uri
        LibApp.app.sendBroadcast(intent)
    }

}