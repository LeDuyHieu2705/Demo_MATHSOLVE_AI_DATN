package com.math.solver.mathsolverapp.network

import com.math.solver.mathsolverapp.data.models.ResChatServer
import com.math.solver.mathsolverapp.data.models.ResDataUser
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