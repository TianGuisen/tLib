package a.tlib.utils.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


/**
 * @author 田桂森 2020/3/12
 */
object GlideUtil {

    @SuppressLint("CheckResult")
    @JvmStatic
    fun load(ctx: Context? = null, url: Any? = null, iv: ImageView? = null, ro: RequestOptions? = null) {
        if (ctx != null && iv != null) {
            val requestBuilder = Glide.with(ctx).load(url)
            if (ro != null) {
                requestBuilder.apply(ro)
            }
            requestBuilder.into(iv)
        }
    }

    @SuppressLint("CheckResult")
    @JvmStatic
    fun load(act: Activity? = null, url: Any? = null, iv: ImageView? = null, ro: RequestOptions? = null) {
        if (act != null && !act.isDestroyed && iv != null) {
            val requestBuilder = Glide.with(act).load(url)
            if (ro != null) {
                requestBuilder.apply(ro)
            }
            requestBuilder.into(iv)
        }
    }

    @SuppressLint("CheckResult")
    @JvmStatic
    fun load(fra: Fragment? = null, url: Any? = null, iv: ImageView? = null, ro: RequestOptions? = null) {
        if (fra != null && fra.activity != null && !fra.activity!!.isDestroyed && iv != null) {
            val requestBuilder = Glide.with(fra).load(url)
            if (ro != null) {
                requestBuilder.apply(ro)
            }
            requestBuilder.into(iv)
        }
    }

    @SuppressLint("CheckResult")
    @JvmStatic
    fun loadGif(ctx: Context? = null, url: Any? = null, iv: ImageView? = null, ro: RequestOptions? = null) {
        if (ctx != null && iv != null) {
            val requestBuilder = Glide.with(ctx).asGif().load(url)
                    .listener(object : RequestListener<GifDrawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            resource?.setLoopCount(GifDrawable.LOOP_INTRINSIC)
                            return false
                        }
                    })
            if (ro != null) {
                requestBuilder.apply(ro)
            }
            requestBuilder.into(iv)
        }
    }
}