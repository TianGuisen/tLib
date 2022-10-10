package a.tlib.base

import a.tlib.R
import a.tlib.utils.retrofit.LoadView
import a.tlib.widget.TRecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author 田桂森 2020/5/7 0007
 * 列表页面继承这个，集合bean需要实现IRVListBean
 */
abstract class BaseTRVFra<T, B : BaseQuickAdapter<T, out BaseViewHolder>>(@androidx.annotation.LayoutRes layoutId:Int= R.layout.fra_common_list) : BaseTFragment(layoutId), IBaseRV<T, B> {
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
        view?.apply {
            rv = findViewById(R.id.rv)
            rv.layoutManager = LinearLayoutManager(context)
            srl = findViewById(R.id.srl)
            lv = findViewById(R.id.lv)
        }
        initRVView()
    }

}