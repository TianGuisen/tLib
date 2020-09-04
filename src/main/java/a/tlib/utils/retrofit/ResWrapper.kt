package com.lb.baselib.retrofit


data class ResWrapper<out T>(val code: Int = -1, val message: String? = null, val data: T?) {
    override fun toString(): String {
        return "ResWrapper(code=$code, message=$message, data=$data)"
    }
}
