package com.ukaka.ridesharingapp.data.remote

import com.ukaka.ridesharingapp.domain.models.AccessToken
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UberAuthService {
    @FormUrlEncoded
    @POST("oauth/v2/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String
    ): Response<AccessToken>
}