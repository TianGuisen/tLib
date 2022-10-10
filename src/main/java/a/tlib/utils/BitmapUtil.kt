package a.tlib.utils

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.LruCache
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.tools.PictureFileUtils
import java.io.*


/**
 * bitmap工具类
 * https://github.com/HuanTanSheng/EasyPhotos
 * 用来保存图片
 */
object BitmapUtil {

    /**
     * 回收bitmap
     *
     * @param bitmap 回收的bitmap
     */
    fun recycle(bitmap: Bitmap?) {
        var bitmap = bitmap
        if (null != bitmap && !bitmap.isRecycled) {
            bitmap.recycle()
            bitmap = null
        }
        System.gc()
    }

    fun recycle(vararg bitmaps: Bitmap?) {
        for (b in bitmaps) {
            recycle(b)
        }
    }

    fun recycle(bitmaps: List<Bitmap?>) {
        for (b in bitmaps) {
            recycle(b)
        }
    }

    /**
     * 保存Bitmap到指定文件夹
     * @param act         上下文
     * @param dirPath     文件夹全路径
     * @param bitmap      bitmap
     * @param namePrefix  保存文件的前缀名，文件最终名称格式为：前缀名+自动生成的唯一数字字符+.png
     * @param notifyMedia 是否更新到媒体库
     */
    fun saveBitmapToDir(act: Activity, dirPath: String,
                        namePrefix: String, bitmap: Bitmap,
                        notifyMedia: Boolean, onFinish: ((File?) -> Unit)? = null) {
        Thread(Runnable {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                //android10+
                saveBitmapToDirQ(act, dirPath, namePrefix, bitmap, notifyMedia, onFinish)
                return@Runnable
            }
            val dirF = File(dirPath)
            if (!dirF.exists() || !dirF.isDirectory) {
                if (!dirF.mkdirs()) {
                    onFinish?.invoke(null)
                    return@Runnable
                }
            }
            try {
                val writeFile = File.createTempFile(namePrefix, ".png", dirF)
                var fos: FileOutputStream? = null
                fos = FileOutputStream(writeFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
                if (notifyMedia) {
                    FileUtil.notifySystemToScan(dirF)
//                    val filePath = writeFile.absolutePath
//                    val files = arrayOf(filePath)
//                    MediaScannerConnection.scanFile(act.applicationContext,
//                            files, null,
//                            null)
                }
                act.runOnUiThread { onFinish?.invoke(writeFile) }
            } catch (e: IOException) {
            }
        }).start()
    }

    private fun saveBitmapToDirQ(act: Activity, dirPath: String,
                                 namePrefix: String, bitmap: Bitmap,
                                 notifyMedia: Boolean, onFinish: ((File) -> Unit)? = null) {
        val dataTake = System.currentTimeMillis()
        val jpegName = "$namePrefix$dataTake.png"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, jpegName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        var dirIndex = dirPath.lastIndexOf("/")
        if (dirIndex == dirPath.length) {
            val dirPath2 = dirPath.substring(0, dirIndex - 1)
            dirIndex = dirPath2.lastIndexOf("/")
        }
        val dirName = dirPath.substring(dirIndex + 1)
        values.put("relative_path", "DCIM/$dirName")
        //        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/" + dirName);
        val external: Uri
        val resolver = act.contentResolver
        val status = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        external = if (status == Environment.MEDIA_MOUNTED) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        }
        val insertUri = resolver.insert(external, values) ?: return
        val os: OutputStream?
        try {
            os = resolver.openOutputStream(insertUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            if (os != null) {
                os.flush()
                os.close()
            }
            if (notifyMedia) {
                FileUtil.notifySystemToScan(insertUri)
            }
            act.runOnUiThread {
                val uriPath: String = PictureFileUtils.getPath(act, insertUri)
                if (null != uriPath) {
                    onFinish?.invoke(File(uriPath))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 把View画成Bitmap
     */
    fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    /**
     * scrollview转bitmap
     */
    fun createBitmapFromScrollView(scrollView: NestedScrollView): Bitmap {
        var h = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }

    /**
     * recycleview截图
     */
    fun createBitmapFromRecycler(view: RecyclerView): Bitmap {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmaCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder = adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                holder.itemView.layout(0, 0, holder.itemView.measuredWidth,
                        holder.itemView.measuredHeight)
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmaCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }
            bigBitmap = Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            val lBackground = view.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }
            for (i in 0 until size) {
                val bitmap = bitmaCache[i.toString()]
                bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }
        }
        return bigBitmap!!
    }

    /**
     * 微信小程序用
     */
    fun bmpToByteArray(bmp: Bitmap): ByteArray? {
        val newBmp: Bitmap
        var width = bmp.width
        var height = bmp.height
//        if (width > height) {
//            height = height * 150 / width
//            width = 150
//        } else {
//            width = width * 150 / height
//            height = 150
//        }
        newBmp = Bitmap.createScaledBitmap(bmp, width, height, true)
        val output = ByteArrayOutputStream()
        newBmp.compress(CompressFormat.JPEG, 100, output)
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (!bmp.isRecycled) {
                bmp.recycle()
            }
            if (!newBmp.isRecycled) {
                newBmp.recycle()
            }
        }
        return result
    }
}