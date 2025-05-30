package com.hieuld.datn.mathsolved.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hieuld.datn.mathsolved.R
import com.zanvent.mathview.MathView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavouriteAdapter(
    private val messages: MutableList<String>,
    private val timestamps: MutableList<Long>,
    private val onCopyClick: ((String) -> Unit)? = null,
    private val onRemoveClick: ((String, Int) -> Unit)? = null
) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite_message, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(messages[position], timestamps.getOrNull(position) ?: System.currentTimeMillis(), position)
    }

    override fun getItemCount(): Int = messages.size

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mathViewContent: MathView = itemView.findViewById(R.id.mathViewContent)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val btnCopy: ImageView = itemView.findViewById(R.id.btnCopy)
        private val btnRemoveFavourite: ImageView = itemView.findViewById(R.id.btnRemoveFavourite)

        fun bind(message: String, timestamp: Long, position: Int) {
            mathViewContent.apply {
                text = message
                pixelScaleType = MathView.Scale.SCALE_DP
                setTextSize(14)
                textColor = "#000000"
            }

            // Sử dụng timestamp được truyền vào thay vì thời gian hiện tại
            tvTimestamp.text = formatTimestamp(timestamp)

            btnRemoveFavourite.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
            )

            btnCopy.setOnClickListener {
                onCopyClick?.invoke(message)
            }

            btnRemoveFavourite.setOnClickListener {
                showRemoveDialog(itemView.context, message, position)
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        private fun showRemoveDialog(context: Context, message: String, position: Int) {
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Xóa khỏi yêu thích")
                .setMessage("Bạn có chắc chắn muốn xóa câu trả lời này khỏi danh sách yêu thích?")
                .setPositiveButton("Xóa") { _, _ ->
                    onRemoveClick?.invoke(message, position)
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    fun addFavouriteItem(message: String) {
        messages.add(message)
        timestamps.add(System.currentTimeMillis())
        notifyItemInserted(messages.size - 1)
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < messages.size) {
            messages.removeAt(position)
            if (position < timestamps.size) {
                timestamps.removeAt(position)
            }
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, messages.size)
        }
    }

    fun updateMessages(newMessages: List<String>, newTimestamps: List<Long> = emptyList()) {
        messages.clear()
        messages.addAll(newMessages)

        timestamps.clear()
        if (newTimestamps.isNotEmpty()) {
            timestamps.addAll(newTimestamps)
        } else {
            // Nếu không có timestamps, tạo timestamp hiện tại cho tất cả
            repeat(newMessages.size) {
                timestamps.add(System.currentTimeMillis())
            }
        }

        notifyDataSetChanged()
    }

    fun updateFavourites(favouriteItems: List<Pair<String, Long>>) {
        messages.clear()
        timestamps.clear()

        favouriteItems.forEach { (message, timestamp) ->
            messages.add(message)
            timestamps.add(timestamp)
        }

        notifyDataSetChanged()
    }
}