package a.tlib.utils.retrofit.converter

import a.tlib.utils.retrofit.observer.LoadOnSubscribe
import android.util.ArrayMap
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.File
import java.io.IOException

/**
 * 上传文件 请求转换器
 * 如果要实现进度监听，Api中方法的返回类型需要是Observable而不是Single
 * 因为Single没有onNext方法，进度观察在Observable的onNext方法中。
 */
class FileRequestBodyConverter : Converter<ArrayMap<String, Any>, RequestBody> {
    //进度发射器
    var uploadOnSubscribe: LoadOnSubscribe? = null

    @Throws(IOException::class)
    override fun convert(params: ArrayMap<String, Any>): RequestBody {
        uploadOnSubscribe = params["LoadOnSubscribe"] as LoadOnSubscribe?
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        params.forEach {
            if (it.key == "LoadOnSubscribe") {
                uploadOnSubscribe = it.value as LoadOnSubscribe?
                return@forEach
            } else if (it.key == "fileList") {
                filesToMultipartBody(builder, it.value as List<File>)
                return@forEach
            } else if (it.key == "file") {
                fileToMultipartBody(builder, it.value as File)
                return@forEach
            } else {
                builder.addFormDataPart(it.key, it.value.toString())
            }
        }
        return builder.build()
    }

    /**
     * 用于把 File集合 或者 File路径集合 转化成 MultipartBody
     *
     * @param <T>   泛型（File 或者 String）
     * @param files File列表或者 File 路径列表
     * @return MultipartBody（retrofit 多文件文件上传）
    </T> */
    @Synchronized
    fun filesToMultipartBody(builder: MultipartBody.Builder, files: List<File>): MultipartBody.Builder {
        var sumLeng = 0L
        for (file in files) {
            sumLeng += file.length()
            // TODO 为了简单起见，没有判断file的类型
            val requestBody = FileProgressRequestBody(file, "multipart/form-data", uploadOnSubscribe)
            builder.addFormDataPart("file", file.name, requestBody)
        }
        uploadOnSubscribe?.setmSumLength(sumLeng)
        return builder
    }

    @Synchronized
    fun fileToMultipartBody(builder: MultipartBody.Builder, file: File): MultipartBody.Builder {
        var sumLeng = 0L
        sumLeng += file.length()
        // TODO 为了简单起见，没有判断file的类型
        val requestBody = FileProgressRequestBody(file, "multipart/form-data", uploadOnSubscribe)
        builder.addFormDataPart("file", file.name, requestBody)
        uploadOnSubscribe?.setmSumLength(sumLeng)
        return builder
    }
}