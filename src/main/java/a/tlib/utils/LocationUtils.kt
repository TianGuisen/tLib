package a.tlib.utils

import a.tlib.LibApp
import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import java.io.IOException
import java.util.*

/**
 * 定位工具
isGpsEnabled     : 判断 Gps 是否可用
isLocationEnabled: 判断定位是否可用
openGpsSettings  : 打开 Gps 设置界面
register         : 注册
unregister       : 注销
getAddress       : 根据经纬度获取地理位置
getCountryName   : 根据经纬度获取所在国家
getLocality      : 根据经纬度获取所在地
getStreet        : 根据经纬度获取所在街道
isBetterLocation : 是否更好的位置
isSameProvider   : 是否相同的提供者
 * 
 * 
 */
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
        val mLocationManager = LibApp.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    private const val TWO_MINUTES = 1000 * 60 * 2

    private var mListener: OnLocationChangeListener? = null
    private var myLocationListener: MyLocationListener? = null
    private var mLocationManager: LocationManager? = null

    /**
     * 判断Gps是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isGpsEnabled(): Boolean {
        val lm = LibApp.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 判断定位是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isLocationEnabled(): Boolean {
        val lm = LibApp.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }

    /**
     * 打开Gps设置界面
     */
    @JvmStatic
    fun openGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        LibApp.app.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 注册
     *
     * 使用完记得调用[.unregister]
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
     *
     * 如果`minDistance`为0，则通过`minTime`来定时更新；
     *
     * `minDistance`不为0，则以`minDistance`为准；
     *
     * 两者都为0，则随时刷新。
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return `true`: 初始化成功<br></br>`false`: 初始化失败
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    fun register(minTime: Long, minDistance: Long, listener: OnLocationChangeListener?): Boolean {
        if (listener == null) return false
        mLocationManager = LibApp.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("LocationUtils", "无法定位，请打开定位服务")
            return false
        }
        mListener = listener
        val provider = mLocationManager!!.getBestProvider(getCriteria()!!, true)
        val location = mLocationManager!!.getLastKnownLocation(provider!!)
        if (location != null) listener.getLastKnownLocation(location)
        if (myLocationListener == null) myLocationListener = MyLocationListener()
        mLocationManager!!.requestLocationUpdates(provider, minTime, minDistance.toFloat(), myLocationListener!!)
        return true
    }

    /**
     * 注销
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_COARSE_LOCATION)
    fun unregister() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager!!.removeUpdates(myLocationListener!!)
                myLocationListener = null
            }
            mLocationManager = null
        }
        if (mListener != null) {
            mListener = null
        }
    }

    /**
     * 设置定位参数
     *
     * @return [Criteria]
     */
    @JvmStatic
    private fun getCriteria(): Criteria? {
        val criteria = Criteria()
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.accuracy = Criteria.ACCURACY_FINE
        // 设置是否要求速度
        criteria.isSpeedRequired = false
        // 设置是否允许运营商收费
        criteria.isCostAllowed = false
        // 设置是否需要方位信息
        criteria.isBearingRequired = false
        // 设置是否需要海拔信息
        criteria.isAltitudeRequired = false
        // 设置对电源的需求
        criteria.powerRequirement = Criteria.POWER_LOW
        return criteria
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [Address]
     */
    @JvmStatic
    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(LibApp.app, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    @JvmStatic
    fun getCountryName(latitude: Double, longitude: Double): String? {
        val address = getAddress(latitude, longitude)
        return if (address == null) "unknown" else address.countryName
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    @JvmStatic
    fun getLocality(latitude: Double, longitude: Double): String? {
        val address = getAddress(latitude, longitude)
        return if (address == null) "unknown" else address.locality
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    @JvmStatic
    fun getStreet(latitude: Double, longitude: Double): String? {
        val address = getAddress(latitude, longitude)
        return if (address == null) "unknown" else address.getAddressLine(0)
    }

    /**
     * 是否更好的位置
     *
     * @param newLocation         The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isBetterLocation(newLocation: Location, currentBestLocation: Location?): Boolean {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true
        }

        // Check whether the new location fix is newer or older
        val timeDelta = newLocation.time - currentBestLocation.time
        val isSignificantlyNewer = timeDelta > TWO_MINUTES
        val isSignificantlyOlder = timeDelta < -TWO_MINUTES
        val isNewer = timeDelta > 0

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false
        }

        // Check whether the new location fix is more or less accurate
        val accuracyDelta = (newLocation.accuracy - currentBestLocation.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200

        // Check if the old and new location are from the same provider
        val isFromSameProvider = isSameProvider(newLocation.provider, currentBestLocation.provider)

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true
        } else if (isNewer && !isLessAccurate) {
            return true
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true
        }
        return false
    }

    /**
     * 是否相同的提供者
     *
     * @param provider0 提供者1
     * @param provider1 提供者2
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isSameProvider(provider0: String?, provider1: String?): Boolean {
        return if (provider0 == null) {
            provider1 == null
        } else provider0 == provider1
    }

    private class MyLocationListener : LocationListener {
        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        override fun onLocationChanged(location: Location) {
            if (mListener != null) {
                mListener!!.onLocationChanged(location)
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            if (mListener != null) {
                mListener!!.onStatusChanged(provider, status, extras)
            }
            when (status) {
                LocationProvider.AVAILABLE -> Log.d("LocationUtils", "当前GPS状态为可见状态")
                LocationProvider.OUT_OF_SERVICE -> Log.d("LocationUtils", "当前GPS状态为服务区外状态")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d("LocationUtils", "当前GPS状态为暂停服务状态")
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        override fun onProviderEnabled(provider: String) {}

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        override fun onProviderDisabled(provider: String) {}
    }

    interface OnLocationChangeListener {
        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        fun getLastKnownLocation(location: Location?)

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        fun onLocationChanged(location: Location?)

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) //位置状态发生改变
    }
}