package a.tlib.utils.retrofit


data class ResWrapper<out T>(val code: Int = -1,
                             val message: String? = null,
                             val data: T?,
                             val timestamp: Long? = 0) {
    override fun toString(): String {
        return "ResWrapper(code=$code, message=$message, data=$data)"
    }
}
