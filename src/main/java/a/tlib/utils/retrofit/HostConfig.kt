package a.tlib.utils.retrofit

import a.tlib.BuildConfig
import a.tlib.utils.defSP
import androidx.annotation.IntDef


object HostConfig {
    const val RELEASE = 0
    const val DEBUG = 1
    const val BETA = 2
    var liveHost: String = ""
    var newHost: String = ""
    var H5Host: String = ""

    /**
     * 网络环境存储
     */
    var environmentSP by defSP(if (BuildConfig.IS_DEBUG) DEBUG else RELEASE)
    var isTestEnvironment = false
    var isReleaseEnvironment = true

    /**
     * 切换环境
     */
    fun setEnvironment(@HostEnvironment e: Int) {
        environmentSP = e
        when (e) {
            RELEASE -> {
                isTestEnvironment = false
                isReleaseEnvironment = true
                newHost = "https://mms.youboi.com/"
                liveHost = "https://newlive.youboi.com/"
                H5Host = "https://h5.youboi.com/"
            }
            DEBUG -> {
                isTestEnvironment = true
                isReleaseEnvironment = false
                newHost = "http://mms.youboe.com/"
                liveHost = "http://newlive.youboe.com/"
                H5Host = "http://h5.youboe.com/"
            }
            BETA -> {
                isTestEnvironment = false
                isReleaseEnvironment = false
                newHost = "http://beta.mms.youboi.com/"
                liveHost = "http://beta.newlive.youboi.com/"
                H5Host = "http://beta.h5.youboi.com/"
            }
        }
    }


    //kotlin注解没有约束效果，java有。。
    @IntDef(RELEASE, DEBUG, BETA)
    annotation class HostEnvironment
}