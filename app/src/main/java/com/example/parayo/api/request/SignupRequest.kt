package com.example.parayo.api.request

import android.util.Patterns

class SignupRequest(
    val email: String?,
    val password: String?,
    val name: String?
) {
    // 각각의 필드 값을 검증하는 함수들을 SignupRequest에 포함시킴. 안드로이드 SDK에는 Patterns 유틸리티에
    // 이메일 검증을 도와주는 EMAIL_ADDRESS 정규표현식이 미리 준비되어 있음.
    fun isNotValidEmail() =
        email.isNullOrBlank()
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isNotValidPassword() =
        password.isNullOrBlank() || password.length !in 8..20

    fun isNotValidName() =
        name.isNullOrBlank() || name.length !in 2..20
}