package com.example.nano.data.dao

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountHttpDao {
    // 注册用户
    @FormUrlEncoded
    @POST("user")
    suspend fun register(
        @Field("code") code: String,
        @Field("name") name: String,
        @Field("pwd") pwd: String,
        @Field("avatar") avatar: String
    ): String
}