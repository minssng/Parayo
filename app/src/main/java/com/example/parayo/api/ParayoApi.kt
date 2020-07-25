package com.example.parayo.api

import com.example.parayo.api.request.ProductRegistrationRequest
import com.example.parayo.api.request.SigninRequest
import com.example.parayo.api.request.SignupRequest
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductImageUploadResponse
import com.example.parayo.api.response.SigninResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ParayoApi {
    // HTTP의 GET 메서드로 해당 URI를 호출한다는 것을 의미
    @GET("/api/v1/hello")
    suspend fun hello(): ApiResponse<String>
    // Retrofit 2.6.0 부터는 코틀린 코루틴을 지원하기 때문에 RxKotlin 등의 별도 라이브러리를
    // 사용하지 않고도 비동기 호출을 지원. 이를 이용하기 위해서는 API 인터페이스를 suspend
    // 함수로 선언해주면 됨. 함수의 반환값으로는 API의 응답 타입을 정의

    @POST("/api/v1/user")
    suspend fun signup(@Body signupRequest: SignupRequest) : ApiResponse<Void>
    // @Body 애노테이션은 파라미터의 값을 HTTP의 요청 본문에 쓰도록 지시함. 이렇게 설정된 파라미터는
    // URI에 노출되지 않으므로 HTTPS를 이용한 암호화 통신을 통해 보안을 강화할 수 있음.

    companion object {
        // instance라는 정적 필드에 Retrofit이 생성해준 ParayoApi 인터페이스의 구현체를 넣어줌.
        val instance = ApiGenerator()
            .generate(ParayoApi::class.java)
    }

    @POST("/api/v1/signin")
    suspend fun signin(@Body signinRequest: SigninRequest)
        : ApiResponse<SigninResponse>

    // 파일업로드가 필요한 API에는 일반적으로 @Multipart 애노테이션을 붙여 이 요청의 바디가
    // multi-part임을 알려야 함. 그리고 @Multipart로 설정된 API 요청의 파라미터들은
    // @Multipart로 설정된 API 요청의 파라미터들은 @Part 애노테이션을 붙여 이 파라미터가
    // multi-part 요청의 일부임을 알려야 함.
    @Multipart
    @POST("/api/v1/product_images")
    suspend fun uploadProductImages(
        @Part images: MultipartBody.Part
    ): ApiResponse<ProductImageUploadResponse>

    @POST("/api/v1/products")
    suspend fun registerProduct(
        @Body request: ProductRegistrationRequest
    ): ApiResponse<Response<Void>>
}
