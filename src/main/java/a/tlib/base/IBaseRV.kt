package a.tlib.base

import a.tlib.utils.retrofit.LoadView
import a.tlib.widget.TRecyclerView
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lb.baselib.retrofit.ResCode
import com.lb.baselib.retrofit.ResWrapper
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * @author 田桂森 2020/5/25 0025
 */
interface IBaseRV<T : IRVListBean, B : BaseQuickAdapter<T, out BaseViewHolder>> {
    var page: Int //有的接口用的page
    var lastId: String//有的接口用的lastId
    var adapter: B
    var rv: TRecyclerView
    var srl: SmartRefreshLayout?
    var lv: LoadView?
    var enableloadMore: Boolean
    fun initRVView() {
        rv.adapter = adapter
        Log.d("onCreateAdapter", adapter.javaClass.simpleName)//用于快速定位
        srl?.setOnRefreshListener {
            page = 1
            lastId = ""
            srl?.resetNoMoreData()
            loadListData()
            onRefresh()
        }
        srl?.setOnLoadMoreListener {
            srl?.resetNoMoreData()
            loadListData()
            onLoadMore()
        }
        lv?.setOnRetryClickListener {
            page = 1
            lastId = ""
            srl?.resetNoMoreData()
            lv?.showLoading()
            loadListData()
        }
        onViewInited()
    }

    fun isFirstPage(): Boolean {
        return page == 1 && lastId == ""
    }

    fun showContent() {
        lv?.showContent()
        rv.hideShimmerAdapter()
    }

    /**
     * 显示LoadView的loading和rv的loading
     * 如果传入了itemLoadingLayoutId，那么将不使用LoadView的loading动画
     * @itemLoadingLayoutId rv的item loading布局
     */
    fun showLoaing(itemLoadingLayoutId: Int = 0) {
        if (itemLoadingLayoutId != 0) {
            rv.setDemoLayoutReference(itemLoadingLayoutId)
        } else {
            lv?.showLoading()
        }
        rv.showShimmerAdapter()
    }

    fun showError() {
        lv?.showError()
        rv.hideShimmerAdapter()
    }

    fun showEmpty() {
        lv?.showEmpty()
        rv.hideShimmerAdapter()
    }

    fun showLogin() {
        lv?.showLogin()
        rv.hideShimmerAdapter()
    }

    /**
     * 设置背景色
     */
    fun setBackgroundColor(color: Int) {
        rv.setBackgroundColor(color)
        lv?.setBackgroundColor(color)
    }

    /**
     * 开启或禁止下拉刷新，默认开启
     */
    fun setEnableRefresh(b: Boolean) {
        srl?.setEnableRefresh(b)
    }

    /**
     * 开启或禁止加载更多，默认开启
     * 关闭后不用考虑page自增
     */
    fun setEnableLoadMore(b: Boolean) {
        srl?.setEnableLoadMore(b)
        enableloadMore = b
    }

    /**
     * 主动刷新，重置页码
     */
    fun refresh() {
        page = 1
        lastId = ""
        loadListData()
    }

    fun onRefresh() {

    }

    fun onLoadMore() {

    }

    /**
     *成功处理
     */
    fun loadSuccess(list: MutableList<T>? = null) {
        if (page == 1 && lastId.isEmpty()) {
            //第一页
            if (list.isNullOrEmpty()) {
                showEmpty()
                srl?.setNoMoreData(true)
            } else {
                page++
                lastId = list.last().getLastId()
                showContent()
            }
            adapter.setList(list)
            srl?.finishRefresh(true)
        } else {
            //加载更多
            if (list.isNullOrEmpty()) {
                srl?.setNoMoreData(true)
            } else {
                page++
                lastId = list.last().getLastId()
                srl?.setNoMoreData(false)
                adapter.addData(list)
            }
            srl?.finishLoadMore(true)
        }
        if (!enableloadMore) {
            page = 1
            lastId = ""
        }
    }

    abstract fun onViewInited()

    abstract fun loadListData()


    /**
     *失败处理
     */
    fun loadFailure(it: ResWrapper<*>?) {
        if (it != null && it.code == ResCode.TOKEN_OVERDUE) {
            showLogin()
        } else {
            showError()
        }
        srl?.finishRefresh(false)
        srl?.finishLoadMore(false)
    }
}