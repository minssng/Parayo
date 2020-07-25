package com.example.parayo.product.registration

import com.example.parayo.api.ParayoApi
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductImageUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.io.File

class ProductImageUploader : AnkoLogger {

    // upload() 함수는 파일 객체를 받아 API 요청에 맞는 파라미터를 생성하고 업로드 API를 호출하는
    // 함수로 API로 호출하기 위해서 suspend 함수로 선언. 네트워크 요청이 일어나는 곳이기 때문에
    // withContext(Dispatchers.IO)로 IO 스레드에서 수행되도록 해야 함.
    suspend fun upload(imageFile: File) = try {
        val part = makeImagePart(imageFile)
        withContext(Dispatchers.IO) {
            ParayoApi.instance.uploadProductImages(part)
        }
    } catch (e: Exception) {
        error("상품 이미지 등록 오류", e)
        ApiResponse.error<ProductImageUploadResponse>(
            "알 수 없는 오류가 발생했습니다."
        )
    }

    private fun makeImagePart(imageFile: File): MultipartBody.Part {
        val mediaType = MediaType.parse("multipart/form-data") // Mediatype.parse()는 HTTP 요청이나 바디에 사용될 컨텐츠의 타입을 지정하는 MediaType 객체를 만들어 줌. 파일 업로드를 위해서는 일반적으로 multipart/form-data 타입이 사용되므로 이 MediaType 을 설정.
        val body = RequestBody.create(mediaType, imageFile) // HTTP 요청의 바디를 생성해줌.

        // 파라미터들을 이용해 멀티파트 요청의 바디 파트를 생성.
        return MultipartBody.Part
            .createFormData("image", imageFile.name, body)
    }
}