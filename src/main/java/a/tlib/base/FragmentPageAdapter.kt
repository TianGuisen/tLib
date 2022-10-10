package a.tlib.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

open class FragmentPageAdapter constructor(fm: FragmentManager, private val mFragments: List<Fragment>) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getItem(index: Int): Fragment {
        return mFragments[index]
    }

    //不销毁页面
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }
}

/**
 * BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,当前显示的Fragment会被执行到onResume,而其他的Fragment的生命周期只会执行到onStart
 */
open class FragmentPageAdapter2 constructor(fm: FragmentManager, private val mFragments: List<Fragment>) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getItem(index: Int): Fragment {
        return mFragments[index]
    }

    //不销毁页面
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }
}