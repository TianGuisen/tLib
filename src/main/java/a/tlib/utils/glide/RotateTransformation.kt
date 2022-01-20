package a.tlib.utils.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * @author 田桂森 2021/3/25 0025
 * 图片旋转
 */
class RotateTransformation(rotateRotationAngle: Float) : BitmapTransformation() {
    private var rotateRotationAngle = 0f

    init {
        this.rotateRotationAngle = rotateRotationAngle
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotateRotationAngle)
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true)
    }
}