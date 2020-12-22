package a.tlib.base

import a.tlib.utils.load
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author 田桂森 2020/12/22 0022
 */
class MyViewHolder(view: View) : BaseViewHolder(view) {

    fun setImageUrl(id: Int, url: Any? = null, ro: RequestOptions? = null) {
        if (url == null) return
        getView<ImageView>(id).let {
            it.load(it.context, url, ro)
        }
    }
}