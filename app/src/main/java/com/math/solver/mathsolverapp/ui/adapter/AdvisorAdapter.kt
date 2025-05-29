package com.math.solver.mathsolverapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.utils.SLog
import com.math.solver.mathsolverapp.utils.hide
import com.math.solver.mathsolverapp.utils.show
import com.pixelcarrot.base64image.Base64Image

class AdvisorAdapter(
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
//        private val tvMathView: MTMathView = itemView.findViewById(R.id.mathView)
        private val chatDolphin: LinearLayout? = itemView.findViewById(R.id.chatDolphin)
        private val vOther: View? = itemView.findViewById(R.id.vOther)

        fun bind(message: Message, position: Int) {
            tvAIMessage.text = if (message.showLoading) "Load..." else message.content
            SLog.d("adapter message: ${message.content}")
//            tvMathView.apply {
//                latex = if (message.showLoading) "Load..." else message.content
//                textColor = Color.parseColor("#000000")
//                fontSize = 50f
//            }

            chatDolphin?.visibility = View.VISIBLE

            vOther?.visibility = if (message.showVOther) View.VISIBLE else View.GONE

            vOther?.setOnClickListener {
                onDeleteClick?.invoke(position)
            }
        }
    }
}