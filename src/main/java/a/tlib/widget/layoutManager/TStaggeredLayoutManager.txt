package a.tlib.widget.layoutManager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author 田桂森 2021/1/21 0021
 * 处理滑动bug
 * https://blog.csdn.net/qq_28055429/article/details/60879680
 */
public class TStaggeredLayoutManager extends StaggeredGridLayoutManager {


    public TStaggeredLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TStaggeredLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
