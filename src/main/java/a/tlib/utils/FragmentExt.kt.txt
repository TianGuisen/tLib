/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package a.tlib.utils

import android.app.Activity
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * 给fragment添加数据
 */
fun <T : Fragment> T.putData(vararg pairs: Pair<String, *>) = apply {
    arguments = bundleOf(*pairs)
}

/**
 * fragment获取数据
 */
fun <T> Fragment.arguments(key: String) = lazy<T?> {
    arguments?.get(key)?.let {
        return@lazy  it as T
    }
    null
}

/**
 * fragment获取activity中的数据
 */
fun <T> Fragment.extras(key: String) = lazy<T?> {
    activity?.extras<T>(key)?.let {
        return@lazy it as T
    }
    null
}

/**
 * fragment获取数据
 */
fun <T> Fragment.arguments(key: String, default: T) = lazy {
    arguments?.get(key)?.let {
        return@lazy it as T
    }
    default
}

/**
 * fragment获取activity中的数据
 */
fun <T> Fragment.extras(key: String, default: T) = lazy {
    activity?.extras<T>(key)?.let {
        return@lazy it
    }
    default
}

/**
 * fragment安全获取数据
 */
fun <T> Fragment.safeArguments(key: String) = lazy {
    arguments!!.get(key) as T
}

/**
 * fragment获取acitivity中的数据，获取安全intent数据，强制不为null，是null会抛异常
 * val s by safeExtras<String>(S)
 */
fun <T> Fragment.safeExtras(key: String) = lazy {
    activity?.extras<T>(key) as T
}
