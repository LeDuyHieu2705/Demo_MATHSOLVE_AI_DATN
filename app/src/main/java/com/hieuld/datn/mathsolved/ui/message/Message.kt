package com.hieuld.datn.mathsolved.ui.message



data class Message(
    val content: String,
    val isUser: Boolean,
    val showLoading: Boolean = false,
    val showVOther: Boolean = false,
    val imvBase64: String? = null,
)
