package com.hieuld.datn.mathsolved.network

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object AIServiceInterceptor : Interceptor {
    private var token: String = "guest_user";

    private var refreshToken: String = "guest_user";

    fun setToken(token: String) {

//        SLog.d("SocialServiceInterceptor","old token: ${this.token}")


        this.token = if (token.isNullOrEmpty()) {
            "guest_user"
        } else "Bearer $token"


//        SLog.d("SocialServiceInterceptor","new token: ${this.token}")
    }

    fun getToken() : String = this.token

    fun setRefreshToken(token: String) {
        this.refreshToken = if (token.isNullOrEmpty()) {
            "guest_user"
        } else "Bearer $token"
    }

    fun onGetRefreshToken() : String = this.refreshToken


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null) {
            if (token.isNotEmpty()) {
                request = request.newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("access-token", token)
                    .build()
            }
        }

        return chain.proceed(request)
    }



    fun getKeyHash(context: Context): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName

            //Retriving package info
            packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            println("Package Name: " + context.applicationContext.packageName)
            for (signature in packageInfo.signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))

                // String key = new String(Base64.encodeBytes(md.digest()));
                println("Key Hash: $key")
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            println("Name not found$e1")
        } catch (e: NoSuchAlgorithmException) {
            println("No such an algorithm$e")
        } catch (e: Exception) {
            println("Exception$e")
        }
        return key
    }
}