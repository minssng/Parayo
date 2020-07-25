package com.example.parayo.api

import android.content.Intent
import com.example.parayo.App
import com.example.parayo.common.Prefs
import com.example.parayo.signin.SigninActivity
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.intentFor

class TokenRefreshInterceptor : Interceptor, AnkoLogger {
    override fun intercept(chain: Interceptor.Chain): Response {
        debug("토큰 갱신 요청")
        val original = chain.request()
        val request = original.newBuilder().apply {
            // API 토큰과 마찬가지로 SharedPreferences에 refreshToken이 존재하는 경우
            // Authorization 헤더에 추가
            Prefs.refreshToken?.let { header("Authorization", it) }
            method(original.method(), original.body())
        }.build()

        val response = chain.proceed(request)

        // 응답 객체를 받아 응답 코드가 401인 경우 로그인 화면으로 이동
        if(response.code() == 401) {
            App.instance.run {
               val intent = intentFor<SigninActivity>().apply {
                   addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                   addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               }
                startActivity(intent)
            }
        }

        return response
    }
}