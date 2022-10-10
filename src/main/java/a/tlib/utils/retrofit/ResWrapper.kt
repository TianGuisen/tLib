package a.tlib.utils.retrofit


data class ResWrapper<out T>(val code: Int = -1,
                             val message: String? = null,
                             val data: T?,
                             val timestamp: Long = 0) {
    override fun toString(): String {
        return "ResWrapper(code=$code, message=$message, data=$data)"
    }

    /**
     * 未登录
     */
    fun notLogin() = code == ResCode.TOKEN_OVERDUE || code == ResCode.TOKEN_OVERDUE2

    /**
     * 成功
     */
    fun isSuccess() = code == ResCode.RESPONSE_SUCCESS || code == ResCode.RESPONSE_SUCCESS2 || code == ResCode.RESPONSE_SUCCESS3
}
