package a.tlib.base

import a.tlib.R
import a.tlib.utils.retrofit.LoadView
import a.tlib.widget.TRecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author 田桂森 2020/5/23 0023
 * 列表页面继承这个，集合bean需要实现IRVListBean
 */
abstract class BaseTRVAct<T, B : BaseQuickAdapter<T, out BaseViewHolder>>(@androidx.annotation.LayoutRes contentLayoutId:Int=R.layout.act_common_list) 
    : BaseTActivity(contentLayoutId), IBaseRV<T, B> {
    /**
     * 这是最常用的布局，通常需要重写布局
     */
    abstract override var adapter: B
    override lateinit var rv: TRecyclerView
    override var srl: SmartRefreshLayout? = null
    override var lv: LoadView? = null
    override var page = 1//有的接口用的page
    override var lastId: String = ""//有的接口用的lastId
    override var enableloadMore: Boolean = true
    override var enableloadRefresh: Boolean = true
    override var itemLoadingLayoutId: Int = 0

    override fun initView() {
        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        srl = findViewById(R.id.srl)
        lv = findViewById(R.id.lv)
        initRVView()
    }

}
