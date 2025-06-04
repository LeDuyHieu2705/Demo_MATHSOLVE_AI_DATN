package com.hieuld.datn.mathsolved.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.utils.commons.utils.show
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
        private val webView: WebView = itemView.findViewById(R.id.webView)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val btnCopy: ImageView = itemView.findViewById(R.id.btnCopy)
        private val btnRemoveFavourite: ImageView = itemView.findViewById(R.id.btnRemoveFavourite)
        private val htmlTemplate = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='utf-8'>" +
                "<style>" +
                "body { font-family: sans-serif; padding: 16px; }" +
                ".MathJax_Display { text-align: center; margin: 1em 0; }" +
                "</style>" +  // 1. Cấu hình MathJax PHẢI trước khi load script
                "<script type='text/javascript'>" +
                "window.MathJax = {" +
                "  tex: {" +
                "    inlineMath: [['$', '$'], ['\\\\(', '\\\\)']]," +
                "    displayMath: [['$$', '$$'], ['\\\\[', '\\\\]']]," +
                "    packages: ['base', 'mhchem']" +
                "  }," +
                "  loader: { load: ['[tex]/mhchem'] }," +
                "  startup: {" +
                "    ready: function() {" +
                "      console.log('MathJax ready');" +
                "      MathJax.startup.defaultReady();" +
                "    }" +
                "  }" +
                "};" +
                "</script>" +  // 2. Tải MathJax và Marked
                "<script src='file:///android_asset/marked.min.js'></script>" +
                "<script src='file:///android_asset/mathjax/es5/tex-chtml.js'></script>" +
                "</head>" +
                "<body>" +
                "<div id='content'></div>" +
                "<script>" +
                "const rawMd = `%s`;" +
                "document.getElementById('content').innerHTML = marked.parse(rawMd);" +
                "window.MathJax && MathJax.typeset && MathJax.typeset();" +
                "</script>" +
                "</body>" +
                "</html>"
        fun bind(message: String, timestamp: Long, position: Int) {
//            mathViewContent.apply {
//                text = message
//                pixelScaleType = MathView.Scale.SCALE_DP
//                setTextSize(14)
//                textColor = "#000000"
//            }

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true


            webView.show()
            displayMarkdown(message)


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
        private fun displayMarkdown(rawMarkdown: String) {
            // Bước 1: Chuẩn hóa chuỗi markdown từ API (thay \\n -> \n thật)
            val cleanMarkdown = rawMarkdown.replace("\\n", "\n")

            // Bước 2: Convert các biểu thức toán inline từ $$...$$ → $...$
            val inlineFixed = cleanMarkdown.replace("\\$\\$(.*?)\\$\\$".toRegex(), "\\$$1\\$")

            // Bước 3: Escape nếu cần (trong trường hợp nhúng vào JS)
// (Có thể bỏ nếu không cần truyền qua JS template)
            val escaped = inlineFixed
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("$", "\\$")

            // Load vào WebView
            val htmlContent: String = String.format(htmlTemplate, escaped)
            webView.loadDataWithBaseURL(
                "file:///android_asset/",
                htmlContent,
                "text/html",
                "utf-8",
                null
            )

//        // Bước 1: Chuẩn hóa chuỗi markdown từ API (thay \\n -> \n thật)
//        String cleanMarkdown = rawMarkdown.replace("\\n", "\n");
//
//        // Bước 2: Escape ký tự đặc biệt để truyền vào JavaScript (cẩn thận $ và `)
//        String escapedMarkdown = cleanMarkdown
//                .replace("\\", "\\\\")     // escape backslash
//                .replace("`", "\\`")       // escape backtick
//                .replace("$", "\\$");      // escape dollar sign for LaTeX
//
//        // Bước 3: Gắn vào HTML và hiển thị
//        String htmlContent = String.format(htmlTemplate, escapedMarkdown);
//        webView.loadDataWithBaseURL("file:///android_asset/", htmlContent, "text/html", "utf-8", null);
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