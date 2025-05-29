package com.math.solver.mathsolverapp.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.databinding.ActivityChatBinding
import com.math.solver.mathsolverapp.ui.adapter.ChatAdapter
import com.math.solver.mathsolverapp.ui.adapter.Message
import com.math.solver.mathsolverapp.ui.viewmodel.NetworkViewModel
import com.math.solver.mathsolverapp.utils.SLog
import com.pixelcarrot.base64image.Base64Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : BaseActivity<NetworkViewModel, ActivityChatBinding>(NetworkViewModel::class){
    private var imageBase64 = ""
    private val messages = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter
    private var lastAIMessagePosition = -1
    private var isAIProcessing = false

    companion object {
        var bitmapResult: Bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityChatBinding {
        return ActivityChatBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    /**
     * Tải và xử lý dữ liệu ban đầu
     * Chuyển đổi bitmap thành base64 và gửi lên AI để phân tích
     */
    override fun preLoadData() {
        super.preLoadData()

        Base64Image.encode(bitmapResult) { base64 ->
            imageBase64 = base64 ?: ""
            Log.d("AdvisorFragment", "Base64 image = $imageBase64")

            if (imageBase64.isNotEmpty()) {
                setAIProcessing(true)
                addUserImage("Math: ")
                addAILoadingMessage("Đang phân tích hình ảnh...")
                viewModel.chatWithAI(imageBase64)
            }
        }
    }

    /**
     * Thiết lập trạng thái xử lý AI và cập nhật UI
     */
    private fun setAIProcessing(processing: Boolean) {
        isAIProcessing = processing
        updateSendButtonState()
    }

    /**
     * Cập nhật trạng thái button Send dựa trên input và trạng thái AI
     */
    private fun updateSendButtonState() {
        viewBinding.btnSend.apply {
            val hasText = viewBinding.editMessage.text?.isNotEmpty() == true
            val shouldEnable = hasText && !isAIProcessing

            alpha = if (shouldEnable) 1.0f else 0.5f
            isEnabled = shouldEnable
        }
    }

    /**
     * Khởi tạo RecyclerView và các component UI chính
     */
    override fun initView() {
        viewBinding.apply {
            val layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = false
                reverseLayout = false
            }
            recyclerViewMessages.layoutManager = layoutManager

            chatAdapter = ChatAdapter(messages) { position ->
                getAlternativeAnswer(position)
            }
            recyclerViewMessages.adapter = chatAdapter

            setupEditTextBehavior()
            setupButtons()
        }
    }

    /**
     * Thiết lập hành vi cho EditText input message
     */
    private fun setupEditTextBehavior() {
        viewBinding.editMessage.apply {
            setOnClickListener {
                if (!isFocused) {
                    requestFocus()
                    showKeyboard(this)
                }

                lifecycleScope.launch {
                    delay(100)
                    scrollToBottom()
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    lifecycleScope.launch {
                        delay(100)
                        scrollToBottom()
                    }
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateSendButtonState()
                    lifecycleScope.launch {
                        scrollToBottom()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    /**
     * Thiết lập các button listeners (Back, Send, Mic, File)
     */
    private fun setupButtons() {
        viewBinding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnSend.apply {
                alpha = 0.5f

                setOnClickListener {
                    val message = editMessage.text.toString().trim()
                    if (message.isNotEmpty() && !isAIProcessing) {
                        addUserMessage(message)
                        editMessage.text?.clear()
                        editMessage.hideKeyboard()
                        editMessage.requestFocus()
                        scrollToBottom()
                    }

                }
            }

            btnMic.setOnClickListener {
                if (!isAIProcessing) {
                }
            }

            btnFile.setOnClickListener {
                if (!isAIProcessing) {
                }
            }
        }
    }

    /**
     * Hiển thị bàn phím cho EditText
     */
    private fun showKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Scroll RecyclerView xuống cuối danh sách
     */
    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            viewBinding.recyclerViewMessages.smoothScrollToPosition(messages.size - 1)
        }
    }

    /**
     * Xử lý khi người dùng click button "Other" để lấy câu trả lời thay thế
     * GỌI API để lấy câu trả lời thực từ server
     */
    private fun getAlternativeAnswer(position: Int) {
        if (position >= 0 && position < messages.size) {
            val message = messages[position]
            if (!message.isUser && !isAIProcessing) {
                setAIProcessing(true)

                val userMessage = findUserMessageForAI(position)

                // Thay đổi message thành loading state
                messages[position] = Message(
                    content = "Đang tìm cách giải khác...",
                    isUser = false,
                    showLoading = true,
                    showVOther = false
                )
                chatAdapter.notifyItemChanged(position)

                // Scroll để user thấy loading state
                viewBinding.recyclerViewMessages.smoothScrollToPosition(position)

                // GỌI API để lấy alternative answer
                lastAIMessagePosition = position
                if (userMessage.isNotEmpty()) {
                    viewModel.chatWithAIText(userMessage)
                } else {
                    // Fallback nếu không tìm thấy user message
                    viewModel.chatWithAI(imageBase64)
                }
            }
        }
    }

    /**
     * Quan sát dữ liệu response từ ViewModel
     * Xử lý khi AI trả về câu trả lời
     */
    override fun observerData() {
        super.observerData()

        lifecycleScope.launch {
            viewModel.chatResponse.collect { response ->
                if (response.isNotEmpty()) {
                    // Fix: Kiểm tra xem có phải là update cho alternative answer không
                    if (lastAIMessagePosition != -1 && lastAIMessagePosition < messages.size &&
                        messages[lastAIMessagePosition].showLoading) {
                        // Đây là response cho alternative answer
                        updateAIMessageAtPosition(lastAIMessagePosition, response)
                    } else {
                        // Đây là response mới
                        addAIResponse(response)
                    }

                    setAIProcessing(false)
                    viewModel.resetChatResponse()
                }
            }
        }
    }

    /**
     * Cập nhật AI message tại vị trí cụ thể (dùng cho alternative answer)
     */
    private fun updateAIMessageAtPosition(position: Int, response: String) {
        if (position >= 0 && position < messages.size) {
            messages[position] = Message(
                content = response,
                isUser = false,
                showLoading = false,
                showVOther = true
            )
            chatAdapter.notifyItemChanged(position)
            viewBinding.recyclerViewMessages.smoothScrollToPosition(position)
        }
    }

    /**
     * Tìm câu hỏi của user tương ứng với AI message position
     */
    private fun findUserMessageForAI(aiPosition: Int): String {
        for (i in aiPosition - 1 downTo 0) {
            if (i < messages.size && messages[i].isUser) {
                return messages[i].content
            }
        }
        return ""
    }

    /**
     * Thêm message hình ảnh từ user vào chat
     * Được gọi khi user gửi ảnh toán học lên
     */
    private fun addUserImage(message: String) {
        SLog.d("addUserImage message : $message")
        messages.add(
            Message(
                content = message,
                imvBase64 = imageBase64,
                isUser = true
            )
        )
        // Fix: Không reset imageBase64 ở đây vì có thể cần dùng lại cho alternative answer
        chatAdapter.notifyItemInserted(messages.size - 1)

        if (messages.size == 1) {
            viewBinding.recyclerViewMessages.scrollToPosition(0)
        } else {
            scrollToBottom()
        }
    }

    /**
     * Thêm AI loading message
     * Hiển thị trạng thái loading khi AI đang xử lý
     */
    private fun addAILoadingMessage(loadingText: String) {
        lastAIMessagePosition = messages.size
        messages.add(
            Message(
                content = loadingText,
                isUser = false,
                showLoading = true,
                showVOther = false
            )
        )
        chatAdapter.notifyItemInserted(lastAIMessagePosition)
        scrollToBottom()
    }

    /**
     * Thêm message text từ user vào chat
     */
    private fun addUserMessage(message: String) {
        SLog.d("addUserMessage message: $message")


        messages.add(
            Message(
                content = message,
                isUser = true
            )
        )
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
        setAIProcessing(true)

        addAILoadingMessage("Đang suy nghĩ...")

        viewModel.chatWithAIText(message)
    }

    /**
     * Thêm response từ AI vào chat
     * Thay thế loading message bằng actual response
     */
    private fun addAIResponse(message: String) {
        SLog.d("addAIResponse message: $message")

        // Xóa loading message nếu tồn tại
        if (lastAIMessagePosition != -1 && lastAIMessagePosition < messages.size &&
            messages[lastAIMessagePosition].showLoading) {
            messages.removeAt(lastAIMessagePosition)
            chatAdapter.notifyItemRemoved(lastAIMessagePosition)
        }

        // Thêm response thực tế
        messages.add(Message(message, isUser = false, showVOther = true))
        lastAIMessagePosition = messages.size - 1
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }


    private fun removeAllPreviousMessages() {
        // Tìm vị trí của image message đầu tiên
        var imageMessageIndex = -1
        for (i in messages.indices) {
            if (messages[i].isUser && !messages[i].imvBase64.isNullOrEmpty()) {
                imageMessageIndex = i
                break
            }
        }

        // Xóa tất cả message sau image message
        if (imageMessageIndex != -1 && messages.size > imageMessageIndex + 1) {
            val startRemoveIndex = imageMessageIndex + 1
            val removeCount = messages.size - startRemoveIndex

            // Xóa từ cuối lên đầu để tránh lỗi index
            for (i in messages.size - 1 downTo startRemoveIndex) {
                messages.removeAt(i)
            }

            chatAdapter.notifyItemRangeRemoved(startRemoveIndex, removeCount)
        }

        // Reset lastAIMessagePosition sau khi xóa
        lastAIMessagePosition = -1
    }


    override fun onPause() {
        super.onPause()
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(viewBinding.editMessage.windowToken, 0)
    }


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}