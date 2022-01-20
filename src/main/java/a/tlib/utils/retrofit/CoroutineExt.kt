package a.tlib.utils.retrofit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.contracts.InvocationKind
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * @author 田桂森 2022/1/20 0020
 */
suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Main, block)
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)