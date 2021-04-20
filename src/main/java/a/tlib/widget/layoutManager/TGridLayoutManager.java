package a.tlib.widget.layoutManager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author 田桂森 2021/1/21 0021
 * 处理滑动bug
 * https://blog.csdn.net/qq_28055429/article/details/60879680
 */
public class TGridLayoutManager extends GridLayoutManager {
    public TGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public TGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
