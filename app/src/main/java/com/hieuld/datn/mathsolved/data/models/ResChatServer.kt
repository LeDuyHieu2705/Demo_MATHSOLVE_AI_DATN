package com.hieuld.datn.mathsolved.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResChatServer(
    @SerializedName("code") @Expose var code: Int = 0,
    @SerializedName("message") @Expose var message: String = "",
    @SerializedName("data") @Expose var data: DataChatServer = DataChatServer(),
) : Serializable {
    data class DataChatServer(
        @SerializedName("conversation_id") @Expose var conversation_id: String = "",
        @SerializedName("response") @Expose var response: String = "",
        @SerializedName("role") @Expose var role: String = "",
        @SerializedName("is_out_of_scope") @Expose var is_out_of_scope: Boolean = false,
    ) : Serializable


}
