package com.junli.retrofit_demo.network

import com.junli.retrofit_demo.network.domain.NormalUser
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoutiqueNormalUserApi {
    @GET("getNormalUserById?")
    suspend fun getNormalUser(@Query("normalUserId") normalUserID: Long): NormalUser

}