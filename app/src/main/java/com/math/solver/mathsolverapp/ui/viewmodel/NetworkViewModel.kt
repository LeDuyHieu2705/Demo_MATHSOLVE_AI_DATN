package com.math.solver.mathsolverapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.data.models.ResDataUser
import com.math.solver.mathsolverapp.network.RetrofitHelper
import com.math.solver.mathsolverapp.network.RetrofitService
import com.math.solver.mathsolverapp.utils.DataConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NetworkViewModel : BaseViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private val retrofitService: RetrofitService = RetrofitHelper.apiServiceAIAnywhere
    private var job = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)
    private val _dataUser = MutableLiveData<ResDataUser>()
    private val _chatResponse = MutableStateFlow("")
    val chatResponse: StateFlow<String> get() = _chatResponse
    val dataUser: MutableLiveData<ResDataUser> get() = _dataUser

    fun getDataUser() {

        val urlRes = "https://app.cscmobicorp.com/aimath/api/v1/chat"

        job.launch {

            try {

                val response = retrofitService.getData(urlRes)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _dataUser.postValue(it)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
    fun resetChatResponse() {
        job.launch {
            _chatResponse.emit("")
        }
    }


    fun chatWithAI(imageBase64: String) {
        if (imageBase64.isEmpty()) return

        val req = ResDataUser(
            image = imageBase64,
            language = "vi",
            message = "Giải phương trình", // default nội dung
            param1 = "1",
            param2 = "1",
            subject_code = "MATHEMATICS",
            user_id = "user-123"
        )

        val urlRes = "${DataConstants.urlHost}api/v1/chat"

        job.launch {
            try {
                val response = retrofitService.callChatServer(urlRes, req)
                Log.d("AI_IMAGE", "response: $response")
                Log.d("AI_IMAGE", "body: ${response.body()}")

                response.body()?.let {
                    _chatResponse.emit(it.data.response ?: "Không có phản hồi từ AI.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _chatResponse.emit("Đã xảy ra lỗi khi gọi AI.")
            }
        }
    }
    fun chatWithAIText(userMessage: String) {
        if (userMessage.isEmpty()) return

        val req = ResDataUser(
            image = "", // không gửi ảnh
            language = "vi",
            message = userMessage,
            param1 = "1",
            param2 = "1",
            subject_code = "MATHEMATICS",
            user_id = "user-123"
        )

        val urlRes = "${DataConstants.urlHost}api/v1/chat"

        job.launch {
            try {
                val response = retrofitService.callChatServer(urlRes, req)
                Log.d("AI_TEXT", "response: $response")
                Log.d("AI_TEXT", "body: ${response.body()}")

                response.body()?.let {
                    _chatResponse.emit(it.data.response ?:"")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _chatResponse.emit("Đã xảy ra lỗi khi gọi AI.")
            }
        }
    }



}