package a.tlib.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import a.tlib.R
import a.tlib.utils.retrofit.LoadView
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * @author 田桂森 2020/5/23 0023
 * 列表页面继承这个，集合bean需要实现IRVListBean
 */
abstract class BaseRVAct<T : IRVListBean, B : BaseQuickAdapter<T, BaseViewHolder>> : BaseActivity(), IBaseRV<T, B> {
    override val layoutId = R.layout.act_common_list
    abstract override var adapter: B
    override lateinit var rv: RecyclerView
    override var srl: SmartRefreshLayout? = null
    override var lv: LoadView? = null
    override var page = 1//有的接口用的page
    override var lastId: String = ""//有的接口用的lastId
    override var enableloadMore: Boolean = true
    override fun initView() {
        rv = findViewById(R.id.rv)
        rv.layoutManager=LinearLayoutManager(this)
        srl = findViewById(R.id.srl)
        lv = findViewById(R.id.lv)
        initRVView()
    }

}
