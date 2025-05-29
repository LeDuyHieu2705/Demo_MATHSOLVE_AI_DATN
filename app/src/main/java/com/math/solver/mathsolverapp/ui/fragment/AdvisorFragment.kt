package com.math.solver.mathsolverapp.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.math.solver.mathsolverapp.databinding.FragmentAdvisorBinding
import com.math.solver.mathsolverapp.fragment.BaseFragment
import com.math.solver.mathsolverapp.ui.adapter.AdvisorAdapter
import com.math.solver.mathsolverapp.ui.adapter.Message
import com.math.solver.mathsolverapp.ui.viewmodel.NetworkViewModel
import com.math.solver.mathsolverapp.utils.SLog
import com.pixelcarrot.base64image.Base64Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdvisorFragment :
    BaseFragment<NetworkViewModel, FragmentAdvisorBinding>(NetworkViewModel::class) {

    private var imageBase64 = ""
    private val messages = mutableListOf<Message>()
    private lateinit var advisorAdapter: AdvisorAdapter
    private var lastAIMessagePosition = -1


    private var isInitFromMain = true


    companion object {
        var bitmapResult: Bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)

        fun newInstance(
            isFromMain: Boolean = false
        ): AdvisorFragment {
            val fragment = AdvisorFragment()
            fragment.isInitFromMain = isFromMain
            return fragment
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdvisorBinding {
        return FragmentAdvisorBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.root.viewTreeObserver.addOnGlobalLayoutListener {
        }
    }

    override fun preLoadData() {
        super.preLoadData()

        if (isInitFromMain) return

        Base64Image.encode(bitmapResult) { base64 ->
            imageBase64 = base64 ?: ""
            Log.d("AdvisorFragment", "Base64 image = $imageBase64")

            if (imageBase64.isNotEmpty()) {
                viewModel.chatWithAI(imageBase64)
                addUserImage("Math: ")
            }
        }
    }

    override fun initView() {
        viewBinding.apply {
            val layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = false  // Hiển thị các item từ dưới lên
                reverseLayout = false  // Không đảo ngược thứ tự
            }
            recyclerViewMessages.layoutManager = layoutManager

            advisorAdapter = AdvisorAdapter(messages) { position ->
                getAlternativeAnswer(position)
            }
            recyclerViewMessages.adapter = advisorAdapter

            // Focus Edt
            setupEditTextBehavior()

            // Xử lý khi bàn phím hiển thị/ẩn
            setupKeyboardBehavior()

            //
            setupButtons()
        }
    }

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

            // Thêm sự kiện khi EditText nhận focus
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
                    // Thay đổi nút gửi từ mờ sang sáng nếu có nội dung
                    viewBinding.btnSend.alpha = if (s?.isNotEmpty() == true) 1.0f else 0.5f

                    // Cuộn xuống dưới mỗi khi có thay đổi để đảm bảo EditText luôn hiển thị
                    lifecycleScope.launch {
                        scrollToBottom()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    private fun setupKeyboardBehavior() {
    }


    private fun setupButtons() {
        viewBinding.apply {
            btnBack.setOnClickListener {
            }

            btnSend.apply {
                alpha = 0.5f

                setOnClickListener {
                    val message = editMessage.text.toString().trim()
                    if (message.isNotEmpty()) {

                        hideVOtherInLastAIMessage()

                        addUserMessage(message)

                        editMessage.text?.clear()
                        editMessage.hideKeyboard()

                        editMessage.requestFocus()
                        scrollToBottom()
                    }
                }
            }

            btnMic.setOnClickListener {
                showToast("Mic feature is coming soon")
            }

            btnFile.setOnClickListener {
                showToast("File upload feature is coming soon")
            }
        }
    }

    private fun showKeyboard(editText: EditText) {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            viewBinding.recyclerViewMessages.smoothScrollToPosition(messages.size - 1)
        }
    }

    // Xử lý khi người dùng click vào vOther để nhận câu trả lời thay thế
    private fun getAlternativeAnswer(position: Int) {
        if (position >= 0 && position < messages.size) {
            val message = messages[position]
            if (!message.isUser) {
                // Gắn trạng thái loading cho tin nhắn hiện tại
                messages[position] = Message(
                    content = "Let me think of another approach...",
                    isUser = false,
                    showLoading = true,
                    showVOther = false
                )
                advisorAdapter.notifyItemChanged(position)

                val userMessage = findUserMessageForAI(position)

                // Tạo câu trả lời mới
                lifecycleScope.launch {
                    delay(1500)
                    val alternativeAnswer = getRandomAlternativeAnswer(userMessage)

                    // Cập nhật tin nhắn với câu trả lời mới
                    messages[position] = Message(
                        content = alternativeAnswer,
                        isUser = false,
                        showLoading = false,
                        showVOther = true
                    )
                    advisorAdapter.notifyItemChanged(position)

                    // Cuộn đến vị trí đã được cập nhật
                    viewBinding.recyclerViewMessages.smoothScrollToPosition(position)
                }
            }
        }
    }

    override fun observerData() {
        super.observerData()

        lifecycleScope.launch {
            viewModel.chatResponse.collect { response ->
                if (response.isNotEmpty()) {
                    addAIResponse(response)

                    if (lastAIMessagePosition != -1 && lastAIMessagePosition < messages.size) {
                        messages[lastAIMessagePosition] = messages[lastAIMessagePosition].copy(
                            showVOther = true,
                            showLoading = false
                        )
                        advisorAdapter.notifyItemChanged(lastAIMessagePosition)
                    }

                    viewModel.resetChatResponse()
                }
            }
        }
    }

    private fun findUserMessageForAI(aiPosition: Int): String {
        // Tìm tin nhắn người dùng gần nhất trước tin nhắn AI
        for (i in aiPosition - 1 downTo 0) {
            if (i < messages.size && messages[i].isUser) {
                return messages[i].content
            }
        }
        return ""
    }

    private fun getRandomAlternativeAnswer(userQuestion: String): String {
        // Câu trả lời thay thế
        val alternatives = listOf(
            "Here's another approach to solve \"$userQuestion\"."
        )

        return alternatives.random()
    }

    private fun hideVOtherInLastAIMessage() {
        // Tìm tin nhắn AI cuối và ẩn vOther
        if (lastAIMessagePosition != -1 && lastAIMessagePosition < messages.size) {
            val message = messages[lastAIMessagePosition]
            if (!message.isUser && message.showVOther) {
                messages[lastAIMessagePosition] = Message(
                    content = message.content,
                    isUser = message.isUser,
                    showLoading = message.showLoading,
                    showVOther = false
                )
                advisorAdapter.notifyItemChanged(lastAIMessagePosition)
            }
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT)
            .show()
    }
    private fun addUserImage(message: String) {
        SLog.d("addUserImage message : $message")
        messages.add(
            Message(
                content = message,
                imvBase64 = imageBase64,
                isUser = true
            )
        )
        imageBase64 = ""
        advisorAdapter.notifyItemInserted(messages.size - 1)

        // Đảm bảo tin nhắn đầu tiên hiển thị ở trên cùng
        if (messages.size == 1) {
            viewBinding.recyclerViewMessages.scrollToPosition(0)
        } else {
            scrollToBottom()
        }
    }
    private fun addUserMessage(message: String) {
        SLog.d("addUserMessage message: $message")
        hideVOtherInLastAIMessage()
        messages.add(
            Message(
                content = message,
                isUser = true
            )
        )
        advisorAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()


        lastAIMessagePosition = messages.size
        messages.add(
            Message(
                content = "Thinking...",
                isUser = false,
                showLoading = true
            )
        )
        advisorAdapter.notifyItemInserted(lastAIMessagePosition)
        scrollToBottom()

        // Gửi tin nhắn đến AI
        viewModel.chatWithAIText(message)
    }

    private fun addAIResponse(message: String) {
        SLog.d("addAIResponse message: $message")

        if (lastAIMessagePosition != -1 && lastAIMessagePosition < messages.size &&
            messages[lastAIMessagePosition].showLoading) {
            messages.removeAt(lastAIMessagePosition)
            advisorAdapter.notifyItemRemoved(lastAIMessagePosition)
        }

        messages.add(Message(message, isUser = false, showVOther = true))
        lastAIMessagePosition = messages.size - 1  // Lưu vị trí của tin nhắn AI cuối cùng
        advisorAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    override fun onPause() {
        super.onPause()
        // Ẩn bàn phím khi Fragment bị tạm dừng
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(viewBinding.editMessage.windowToken, 0)
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}
