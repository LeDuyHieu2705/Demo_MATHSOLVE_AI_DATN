package com.hieuld.datn.mathsolved.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResDataUser(
    @SerializedName("image") @Expose var image: String= "" ,
    @SerializedName("language") @Expose var language: String ="",
    @SerializedName("message") @Expose var message: String = "",
    @SerializedName("param1") @Expose var param1: String = "1",
    @SerializedName("param2") @Expose var param2: String = "1",
    @SerializedName("subject_code") @Expose var subject_code: String = "",
    @SerializedName("user_id") @Expose var user_id: String = "user-123",

    ) : Serializable
