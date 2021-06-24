package a.tlib.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*

/**
 * @author 田桂森 2019/5/16
 */
object ActStackManager : Application.ActivityLifecycleCallbacks {
    /**
     * APP前后台状态切换,true是到前台，false是到后台了
     */
    const val APP_FRONT_STATUS = "app_front_status"
    var isFront = false

    //打开的Activity数量统计
    private var activityStartCount = 0
    private val stack: Stack<Activity>

    init {
        stack = Stack()
    }

    @JvmStatic
    fun register(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    @JvmStatic
    fun unRegister(app: Application) {
        app.unregisterActivityLifecycleCallbacks(this)
    }

    /**
     * @param activity 需要添加进栈管理的activity
     */
    @JvmStatic
    fun addActivity(activity: Activity?) {
        stack.add(activity)
    }

    @JvmStatic
    fun getSize() = stack.size

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return
     */
    @JvmStatic
    fun removeActivity(activity: Activity?): Boolean {
        return stack.remove(activity)
    }

    /**
     * 判断要跳转的页面是否是当前页面
     */
    @JvmStatic
    fun isStartOldActivity(activity: Activity): Boolean {
        if (getTopActivity().localClassName.equals(activity.localClassName)) {
            return true
        }
        return false
    }

    /**
     * 获取栈顶部的act
     */
    @JvmStatic
    fun getTopActivity(): Activity {
        return stack.peek()
    }

    /**
     * @param activity 查询指定activity在栈中的位置，从栈顶开始
     * @return
     */
    @JvmStatic
    fun searchActivity(activity: Activity): Int {
        return stack.search(activity)
    }

    /**
     * @param activity 将指定的activity从栈中删除然后finish()掉
     */
    @JvmStatic
    fun finishActivity(activity: Activity) {
        activity.finish()
        stack.remove(activity)
    }

    /**
     * @param activity 将指定的activity从栈中删除然后finish()掉
     */
    @JvmStatic
    fun finishActivity(activity: Class<out Activity>) {
        findActivity(activity)?.let {
            it.finish()
            stack.remove(it)
        }
    }

    /**
     * @param activity 将指定类名的activity从栈中删除并finish()掉
     */
    @JvmStatic
    fun finishActivityClass(activity: Class<out Activity>?) {
        if (activity != null) {
            val iterator = stack.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.javaClass == activity) {
                    iterator.remove()
                    finishActivity(next)
                }
            }
        }
    }

    /**
     * 销毁所有的activity
     */
    @JvmStatic
    fun finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish()
        }
    }

    /**
     * 通过class找到activity
     */
    @JvmStatic
    fun findActivity(activity: Class<out Activity>): Activity? {
        stack.forEach {
            if (it.javaClass == activity) {
                return it
            }
        }
        return null
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("onCreateAct", activity?.javaClass?.simpleName)//用于快速定位页面对应的代码
        addActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {
        activityStartCount++;
        //数值从0变到1说明是从后台切到前台
        if (activityStartCount == 1) {
            //从后台切到前台
            isFront=true
            LiveEventBus.get(APP_FRONT_STATUS, Boolean::class.java).post(true)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
        activityStartCount--;
        //数值从1到0说明是从前台切到后台
        if (activityStartCount == 0) {
            //从前台切到后台
            isFront=false
            LiveEventBus.get(APP_FRONT_STATUS, Boolean::class.java).post(false)
        }
    }
}