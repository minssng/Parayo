package com.example.parayo.api

import com.example.parayo.api.response.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 토큰 갱신 API를 정의한 인터페이스
 */

interface ParayoRefreshApi {
    @POST("/api/v1/refresh_token")
    suspend fun refreshToken(
        // 토큰 갱신 API가 요구하는 스펙에 맞추어 쿼리 파라미터로 grant_type=refresh_token을 추가
        @Query("grant_type") grantType: String = "refresh_token"
    ): ApiResponse<String>

    companion object {
        val instance = ApiGenerator()
            .generateRefreshClient(ParayoRefreshApi::class.java)
    }
}