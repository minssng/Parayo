package com.example.parayo.api

import com.example.parayo.api.response.ApiResponse
import com.example.parayo.common.Prefs
import com.example.parayo.common.Prefs.refreshToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

// Authenticator를 상속받아 authenticate() 함수를 구현
class TokenAuthenticator : Authenticator, AnkoLogger {
    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        if (response.code() == 401) { // 응답코드가 401인 경우 토큰 갱신 로직으로 진입하도록 함
            debug("토큰 갱신 필요")
            return runBlocking { // API가 suspend 함수인 관계로 runBlicking {}을 이용해 코루틴을 실행. runBlocking 함수는 블록의 마지막 줄을 반환값으로 사용
                val tokenResponse = refreshToken() // 토큰 갱신 API를 호출

                if (tokenResponse.success) {
                    debug("토큰 갱신 성공")
                    Prefs.token = tokenResponse.data // 토큰이 갱신된 경우 SharedPreferences에 새 토큰을 덮어써줌
                } else { // 토큰 갱신이 실패한 경우 로그아웃 처리를 위해 기존 토큰들을 제거
                    error("토큰 갱신 실패")
                    Prefs.token = null
                    Prefs.refreshToken = null
                }

                // 토큰이 성공적으로 갱신된 경우 새 토큰으로 새 요청을 만들어 반환
                Prefs.token?.let { token ->
                    debug("토큰 = $token")
                    response.request()
                        .newBuilder()
                        .header("Authorization", token)
                        .build()
                }
            }
        }

        return null
    }

    private suspend fun refreshToken() =
        withContext(Dispatchers.IO) {
            try {
                ParayoRefreshApi.instance.refreshToken()
            } catch (e: Exception) {
                ApiResponse.error<String>("인증 실패")
            }
        }
}