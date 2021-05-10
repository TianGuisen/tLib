package a.tlib.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Process
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.jeremyliao.liveeventbus.utils.AppUtils

object LocationUtils {

    /**
     * 判断定位权限是否开启
     */
    @SuppressLint("WrongConstant")
    fun locationEnabled(context: Context): Boolean {
        var isEnabled: Boolean
        //sdk大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isEnabled = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            isEnabled = PermissionChecker.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid(), context.packageName) == PackageManager.PERMISSION_GRANTED &&
                    PermissionChecker.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION, Process.myPid(), Process.myUid(), context.packageName) == PackageManager.PERMISSION_GRANTED
        }
        return isEnabled
    }


    /**
     * 获取位置信息
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Location? {
        //获取地理位置管理器
        val mLocationManager = AppUtils.getApplicationContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager.getProviders(true)
        var location: Location? = null
        for (provider in providers) {
            val l = mLocationManager.getLastKnownLocation(provider) ?: continue
            if (location == null || l.accuracy < location.accuracy) {
                location = l
            }
        }
        return location
    }

}