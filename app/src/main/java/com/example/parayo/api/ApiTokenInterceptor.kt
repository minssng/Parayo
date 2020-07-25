package com.example.parayo.api

import com.example.parayo.common.Prefs
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * API 요청의 Authorization 헤더에 토큰을 추가해주는 class
 */
// OKHttp의 Interceptor를 상속받고 intercept() 함수를 구현
class ApiTokenInterceptor : Interceptor, AnkoLogger {
    override fun intercept(chain: Interceptor.Chain): Response {
        debug("API 요청")
        val original = chain.request() // chain.request()로 원래의 요청 객체를 가져옴
        val request = original.newBuilder().apply { // original.newBuilder() 함수는 새 요청 빌더 객체를 만들어줌
            Prefs.token?.let { header("Autorization", it) } // SharedPreferences에 토큰 값이 있는 경우 Authorization 헤더에 토큰을 추가
            method(original.method(), original.body()) // 새 요청에 원 요청의 HTTP 메서드와 바디를 넣어줌
        }.build()

        return chain.proceed(request) // 새 요청을 기반으로 HTTP 요청에 대한 응답을 생성해 반환
    }
}