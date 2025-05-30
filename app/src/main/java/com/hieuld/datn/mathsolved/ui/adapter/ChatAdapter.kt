package com.hieuld.datn.mathsolved.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.ui.message.Message
import com.pixelcarrot.base64image.Base64Image
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.show
import com.zanvent.mathview.MathView

class ChatAdapter(
    private val messages: MutableList<Message>,
    private val onDeleteClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_message, parent, false)
                UserMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ai_message, parent, false)
                AIMessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is AIMessageViewHolder -> holder.bind(message, position)
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserMessage: TextView = itemView.findViewById(R.id.tvUserMessage)
        private val imvUserMessage: ImageView = itemView.findViewById(R.id.imvImage)

        fun bind(message: Message) {
            tvUserMessage.text = message.content

            if (message.imvBase64.isNullOrEmpty()) {
                imvUserMessage.hide()
            } else {
                imvUserMessage.show()

                Base64Image.decode(message.imvBase64) { bitmap ->
                    imvUserMessage.setImageBitmap(bitmap)
                }
            }
        }
    }

    inner class AIMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAIMessage: TextView = itemView.findViewById(R.id.tvAIMessage)
        private val mathView2: MathView = itemView.findViewById(R.id.mathView2)
        private val chatDolphin: LinearLayout? = itemView.findViewById(R.id.chatDolphin)
        private val vOther: View? = itemView.findViewById(R.id.vOther)
        private val btnOtherAnswer: MaterialCardView = itemView.findViewById(R.id.btnOtherAnswer)
        private val btnCopy: MaterialCardView = itemView.findViewById(R.id.BtnCopy)

        private val loadingContainer: LinearLayout? = itemView.findViewById(R.id.loadingContainer)
        private val progressBar: ProgressBar? = itemView.findViewById(R.id.progressBar)
        private val tvLoadingText: TextView? = itemView.findViewById(R.id.tvLoadingText)

        fun bind(message: Message, position: Int) {
            SLog.d("adapter message: ${message.content}")

            if (message.showLoading) {
                // Hiển thị loading state
                showLoadingState(message.content)
            } else {
                // Hiển thị content bình thường
                showNormalState(message.content)
            }

            chatDolphin?.visibility = View.VISIBLE
            vOther?.visibility = if (message.showVOther && !message.showLoading) View.VISIBLE else View.GONE

            btnOtherAnswer.setOnClickListener {
                if (!message.showLoading) {
                    onDeleteClick?.invoke(position)
                }
            }

            btnCopy.setOnClickListener {
                if (!message.showLoading && message.content.isNotEmpty()) {
                    copyText(itemView.context, message.content)
                }
            }
        }

        private fun copyText(context: Context, text: String) {
            try {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("AI Response", text)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(context, "Đã sao chép câu trả lời", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                SLog.d("Error copying to clipboard: ${e.message}")
                Toast.makeText(context, "Lỗi khi sao chép", Toast.LENGTH_SHORT).show()
            }
        }

        private fun showLoadingState(loadingText: String) {
            tvAIMessage.hide()
            mathView2.hide()

            loadingContainer?.show()
            progressBar?.show()
            if (loadingContainer == null) {

                mathView2.apply {
                    show()
                    text = loadingText
                    pixelScaleType = MathView.Scale.SCALE_DP
                    setTextSize(16)
                    textColor = "#666666"
                }
            }
        }

        private fun showNormalState(content: String) {
            loadingContainer?.hide()
            progressBar?.hide()
            tvLoadingText?.hide()

            mathView2.apply {
                show()
                text = content
                pixelScaleType = MathView.Scale.SCALE_DP
                setTextSize(16)
                textColor = "#000000"
            }
        }
    }
}