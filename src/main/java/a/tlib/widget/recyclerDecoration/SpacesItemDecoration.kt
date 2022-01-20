package a.tlib.widget.recyclerDecoration

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author 田桂森 2020/12/31 0031
 * https://github.com/yanyusong/Y_DividerItemDecoration
 * LinearLayoutManager的分隔线
 */
class SpacesItemDecoration : Y_DividerItemDecoration {

    var sizePt = 0f
    var startSize: Float = 0f
    var endSize: Float = 0f
    var color = Color.parseColor("#00000000")

    /**
     *@sizePt 间距
     * @startSize 第一个item的起始间距，默认为sizePt，传0无起始间距
     * @endSize 第一个item的最后间距，默认为sizePt，传0无最后间距
     * @color 颜色，默认透明
     */
    constructor(context: Context, sizePt: Float, startSize: Float? = null, endSize: Float? = null, color: Int = Color.TRANSPARENT) : super(context) {
        this.sizePt = sizePt
        this.color = color
        if (startSize == null) {
            this.startSize = sizePt
        } else {
            this.startSize = startSize
        }
        if (endSize == null) {
            this.endSize = sizePt
        } else {
            this.endSize = endSize
        }
    }

    override fun getDivider(itemPosition: Int, recyclerView: RecyclerView): Y_Divider {
        var y_Divider: Y_Divider
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            when (itemPosition) {
                0 -> {
                    y_Divider = Y_DividerBuilder()
                            .setTopSideLine(true, color, startSize, 0f, 0f)
                            .setBottomSideLine(true, color, sizePt, 0f, 0f)
                            .create()
                }
                recyclerView.adapter?.itemCount!! - 1->{
                    y_Divider = Y_DividerBuilder()
                            .setBottomSideLine(true, color, endSize, 0f, 0f)
                            .create() 
                }
                else -> {
                    y_Divider = Y_DividerBuilder()
                            .setBottomSideLine(true, color, sizePt, 0f, 0f)
                            .create()
                }
            }
        } else {
            when (itemPosition) {
                0 -> {
                    y_Divider = Y_DividerBuilder()
                            .setLeftSideLine(true, color, startSize, 0f, 0f)
                            .setRightSideLine(true, color, sizePt, 0f, 0f)
                            .create()
                }
                recyclerView.adapter?.itemCount!! - 1->{
                    y_Divider = Y_DividerBuilder()
                            .setRightSideLine(true, color, endSize, 0f, 0f)
                            .create()
                }
                else -> {
                    y_Divider = Y_DividerBuilder()
                            .setRightSideLine(true, color, sizePt, 0f, 0f)
                            .create()
                }
            }
        }
        return y_Divider
    }
}