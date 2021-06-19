package a.tlib.utils.retrofit

/**
 * 和后台约定的业务错误
 * 为什么一个状态会有多个常量？因为后台太骚了
 */
object ResCode {
    //重新登录
    const val TOKEN_OVERDUE = 4040
    const val TOKEN_OVERDUE2 = 401

    //失败
    const val RESPONSE_ERROR = -1
    const val RESPONSE_ERROR2 = 1003
    const val RESPONSE_ERROR3 = 422

    //成功
    const val RESPONSE_SUCCESS = 1
    const val RESPONSE_SUCCESS2 = 1001
    const val RESPONSE_SUCCESS3 = 200
}

enum class ApiErrorType(val code: Int, val message: String) {
    //非业务错误
    INTERNAL_SERVER_ERROR(500, "服务器错误"),
    BAD_GATEWAY(502, "服务器错误"),
    GATEWAY_TIMEOUT(504, "网络连接异常"),
    NOT_FOUND(404, "HTTP 404 Not Found"),
    CONNECTION_TIMEOUT(408, "连接超时"),
    UNKNOWN_HOST(498, "网络连接异常"),
    NETWORK_NOT_CONNECT(499, "网络连接异常"),
    UNEXPECTED_ERROR(700, "未知错误");
}
