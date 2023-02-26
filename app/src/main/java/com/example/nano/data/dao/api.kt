package com.example.nano.data.dao

import com.example.nano.data.HttpResult
import retrofit2.http.*

interface Api {
    // 注册用户
    @FormUrlEncoded
    @POST("users")
    suspend fun register(
        @Field("acct") acct: String,
        @Field("name") name: String,
        @Field("pwd") pwd: String,
        @Field("avatar") avatar: String
    ): HttpResult<String>

    @FormUrlEncoded
    @PUT("users")
    suspend fun login(
        @Field("acct") acct: String,
        @Field("pwd") pwd: String
    ): HttpResult<String>
}