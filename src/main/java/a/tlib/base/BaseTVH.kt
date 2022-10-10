package a.tlib.base

import a.tlib.utils.load
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ruffian.library.widget.RTextView
import com.ruffian.library.widget.RView

/**
 * @author 田桂森 2020/12/22 0022
 */
open class BaseTVH : BaseViewHolder {
    constructor(view: View) : super(view) {
        view.setTag(this)
    }

    fun gone(id: Int, boolean: Boolean = true) {
        getView<View>(id).let {
            it.visibility = if (boolean) View.GONE else View.VISIBLE
        }
    }

    fun gone(vararg ids: Int, boolean: Boolean = true) {
        ids.forEach {
            getView<View>(it).let {
                it.visibility = if (boolean) View.GONE else View.VISIBLE
            }
        }
    }

    fun show(id: Int, boolean: Boolean = true) {
        getView<View>(id).let {
            it.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
        }
    }

    fun show(vararg ids: Int, boolean: Boolean = true) {
        ids.forEach {
            getView<View>(it).let {
                it.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    fun showGone(id: Int, boolean: Boolean = true) {
        getView<View>(id).let {
            it.visibility = if (boolean) View.VISIBLE else View.GONE
        }
    }

    fun showGone(vararg ids: Int, boolean: Boolean = true) {
        ids.forEach {
            getView<View>(it).let {
                it.visibility = if (boolean) View.VISIBLE else View.GONE
            }
        }
    }

    fun hide(id: Int, boolean: Boolean = true) {
        getView<View>(id).let {
            it.visibility = if (boolean) View.INVISIBLE else View.VISIBLE
        }
    }

    fun hide(vararg ids: Int, boolean: Boolean = true) {
        ids.forEach {
            getView<View>(it).let {
                it.visibility = if (boolean) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    fun isShow(id: Int) = getView<View>(id).visibility == View.VISIBLE
    fun isHide(id: Int) = getView<View>(id).visibility == View.INVISIBLE || getView<View>(id).visibility == View.GONE

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

    fun setTVSelect(id: Int, select: Boolean) {
        getView<RTextView>(id).let {
            it.isSelected = select
        }
    }
}