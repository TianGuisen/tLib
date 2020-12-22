package a.tlib.utils.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author 田桂森 2020/5/10 0010
 * MultipartBody上传文字和图片
 *
@POST("")
@Multipart
fun post(@Part partList: List<MultipartBody.Part>): Single<ResWrapper<Any>>
 */
class RequestMultipartBody {
    companion object {
        fun create() = RequestMultipartBody()
    }

    private val parts = mutableListOf<MultipartBody.Part>()
    fun putText(key: String, value: String): RequestMultipartBody {
        val textData = MultipartBody.Part.createFormData(key, value)
        parts.add(textData)
        return this
    }

    fun putImg(name: String, fileName: String, file: File): RequestMultipartBody {
        val imageBody: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val filedata = MultipartBody.Part.createFormData(name, fileName, imageBody)
        parts.add(filedata)
        return this
    }

    fun putImgs(name: String, fileName: String, fileList: MutableList<File>): RequestMultipartBody {
        var fileName2=fileName
        fileList.forEach { file ->
            fileName2=fileName2+1
            val imageBody: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val filedata = MultipartBody.Part.createFormData(name, fileName2, imageBody)
            parts.add(filedata)
        }
        return this
    }

    fun build() = parts
}