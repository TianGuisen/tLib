package a.tlib.base

import a.tlib.utils.load
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ruffian.library.widget.RView
import com.ruffian.library.widget.helper.RBaseHelper
import com.ruffian.library.widget.iface.RHelper

/**
 * @author 田桂森 2020/12/22 0022
 */
open class MyViewHolder(view: View) : BaseViewHolder(view) {

    fun setImageUrl(id: Int, url: Any? = null, ro: RequestOptions? = null) {
        if (url == null) return
        getView<ImageView>(id).let {
            it.load(it.context, url, ro)
        }
    }

    fun setCBCheck(id: Int, check: Boolean) {
        getView<CheckBox>(id).let {
            it.isChecked = check
        }
    }

    fun setViewSelect(id: Int, select: Boolean) {
        getView<RView>(id).let {
            it.isSelected = select
        }
    }
    
}