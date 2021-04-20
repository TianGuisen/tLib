package a.tlib.widget.recyclerDecoration;

import androidx.annotation.ColorInt;

/**
 * Created by mac on 2017/5/17.
 */

public class Y_SideLine {

    public boolean isHave = false;
    /**
     * A single color value in the form 0xAARRGGBB.
     **/
    public int color;
    /**
     * 单位Pt
     */
    public float widthPt;
    /**
     * startPaddingPt,分割线起始的padding，水平方向左为start，垂直方向上为start
     * enPtaddingPt,分割线尾部的padding，水平方向右为end，垂直方向下为end
     */
    public float startPaddingPt;
    public float enPtaddingPt;

    public Y_SideLine(boolean isHave, @ColorInt int color, float widthPt, float startPaddingPt, float enPtaddingPt) {
        this.isHave = isHave;
        this.color = color;
        this.widthPt = widthPt;
        this.startPaddingPt = startPaddingPt;
        this.enPtaddingPt = enPtaddingPt;
    }

    public float getStartPaddingPt() {
        return startPaddingPt;
    }

    public void setStartPaddingPt(float startPaddingPt) {
        this.startPaddingPt = startPaddingPt;
    }

    public float getEnPtaddingPt() {
        return enPtaddingPt;
    }

    public void setEnPtaddingPt(float enPtaddingPt) {
        this.enPtaddingPt = enPtaddingPt;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setHave(boolean have) {
        isHave = have;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getWidthPt() {
        return widthPt;
    }

    public void setWidthPt(float widthPt) {
        this.widthPt = widthPt;
    }
}
