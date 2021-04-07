package a.tlib.utils.glide

import a.tlib.R
import a.tlib.utils.AutoSizeUtil
import a.tlib.utils.load
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author 田桂森 2021/3/23 0023
 */
class GlidePagerAdapter(var list: MutableList<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.load(container.context, list[position], RequestOptions().fitCenter())
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

class GlidePagerAdapter2(var list: MutableList<String>) : PagerAdapter() {
    var op = RequestOptions()
            .fitCenter()
            .transform(RotateTransformation(90f))

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.load(container.context, list[position], op)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}