package com.example.parayo.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiGenerator {
    fun <T> generate(api: Class<T>): T = Retrofit.Builder()
        .baseUrl(HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient())
        .build()
        .create(api)

    // 토큰 갱신용 API 객체를 만들기 위한 제너레이트 함수가 추가. 클라이언트는
    // refreshClient()를 통해 생성
    fun <T> generateRefreshClient(api: Class<T>): T = Retrofit.Builder()
        .baseUrl(HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .client(refreshClient())
        .build()
        .create(api)

    // Retrofit과 연계할  HTTP 통신 객체를 생성하는 함수. Retrofit에서는 기본적으로 OkHttp 클라이언트를 사용하도록 되어 있음.
    // OkHttp 또한 많이 쓰이는 HTTP 통신 라이브러리. HttpLoggingInterceptor에서는 API의 응답 결과를 로그로 확인하기 위해
    // 별도로 HTTP 바디를 로깅해주도록 설정해 OkHttpClient에 추가 해줌.
    private fun httpClient() =
        OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor())
            addInterceptor(ApiTokenInterceptor())
            authenticator(TokenAuthenticator())
        }.build()

    // 새 HTTP 클라이언트를 생성하는 함수. 앞서 만든 TokenRefreshInterceptor 객체를
    // 인터셉터로 추가
    private fun refreshClient() =
        OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor())
            addInterceptor(TokenRefreshInterceptor())
        }.build()

    private fun httpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    companion object {
        // HOST를 하드코딩 했지만 실제로는 build variants 등을 이용해서 개발 환경, 운영환경의 빌드 설정에 따라 자동으로 주소를
        // 선택하게 만들어주는 편이 운영환경에 개발서버 주소를 입력하는 실수 등을 방지할 수 있음. 그리고 확장성을 위해서는 IP보다는
        // 도메인을 사용하는 편이 좋지만, 이 책에서는 개발에 집중하기 위해 build variants나 도메인과 같은 요소에 대해서는 다루지 않음.
        // HOST의 값으로 사용된 10.0.2.2라는 아이피는 안드로이드 에뮬레이터에서 로컬호스트에 띄운 서버를 지칭할 때 사용되는 아이피.
        const val HOST = "http://10.0.2.2:8080"
    }
}
