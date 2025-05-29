package com.math.solver.mathsolverapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.utils.SLog
import com.math.solver.mathsolverapp.utils.hide
import com.math.solver.mathsolverapp.utils.show
import com.pixelcarrot.base64image.Base64Image
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

        // Thêm loading components
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
        }

        private fun showLoadingState(loadingText: String) {
            // Ẩn content views
            tvAIMessage.hide()
            mathView2.hide()

            // Hiển thị loading views
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