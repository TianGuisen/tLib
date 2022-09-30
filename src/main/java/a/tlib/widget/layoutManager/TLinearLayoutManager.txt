package a.tlib.widget.layoutManager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * https://blog.csdn.net/qq_28055429/article/details/60879680
 * https://stackoverflow.com/questions/30458640/recyclerview-java-lang-indexoutofboundsexception-inconsistency-detected-inval
 * 控件内部bug
 */
public class TLinearLayoutManager extends LinearLayoutManager {

    public TLinearLayoutManager(Context context) {
        super(context);
    }

    public TLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
}
