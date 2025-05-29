package com.hieuld.datn.mathsolved.network

import com.hieuld.datn.mathsolved.data.models.ResChatServer
import com.hieuld.datn.mathsolved.data.models.ResDataUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface RetrofitService {

    @GET
    suspend fun getData(
        @Url url: String
    ): Response<ResDataUser>

    @POST
    suspend fun callChatServer(
        @Url url: String,
        @Body body: ResDataUser
    ): Response<ResChatServer>

}