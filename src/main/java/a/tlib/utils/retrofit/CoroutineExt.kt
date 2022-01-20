package a.tlib.utils.retrofit

import kotlinx.coroutines.*
import okio.IOException
import retrofit2.Retrofit
import java.net.ConnectException
import kotlin.contracts.InvocationKind
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * @author 田桂森 2022/1/20 0020
 * 协程扩展
 */
/**
 * 切换到主线程里执行
 */
suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Main, block)
/**
 * 切换到io里执行
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)
