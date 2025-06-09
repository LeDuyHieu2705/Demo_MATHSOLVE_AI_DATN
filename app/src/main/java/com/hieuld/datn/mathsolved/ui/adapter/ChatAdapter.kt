package com.hieuld.datn.mathsolved.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.ui.message.Message
import com.hieuld.datn.mathsolved.utils.commons.managers.SimpleFavouriteManager
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.show
import com.pixelcarrot.base64image.Base64Image

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



//        private val mathView2: MathView = itemView.findViewById(R.id.mathView2)
        private val webview: WebView = itemView.findViewById(R.id.webView)



        private val chatDolphin: LinearLayout? = itemView.findViewById(R.id.chatDolphin)
        private val vOther: View? = itemView.findViewById(R.id.vOther)
        private val btnOtherAnswer: MaterialCardView = itemView.findViewById(R.id.btnOtherAnswer)
        private val btnCopy: MaterialCardView = itemView.findViewById(R.id.BtnCopy)
        private val btnFavourite: MaterialCardView = itemView.findViewById(R.id.BtnFavourite)

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


            // Khởi tạo WebView
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true // Bật DOM storage

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
            if (!message.showLoading && message.content.isNotEmpty() && !message.isUser) {
                btnFavourite.setOnClickListener {
                    handleFavouriteClick(message, position)
                }
            }
        }
        private fun copyText(context: Context, text: String) {
            try {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("AI Response", text)
                clipboard.setPrimaryClip(clip)

            } catch (e: Exception) {
                SLog.d("Error copying to clipboard: ${e.message}")
            }
        }
        private fun updateFavouriteButton(message: Message) {

            val isFavourite = SimpleFavouriteManager.isFavourite(itemView.context, message.content)

            val imageView = btnFavourite.findViewById<ImageView>(R.id.imvFavourite)
            imageView?.let {
                if (isFavourite) {

                    it.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
                    )

                } else {

                }
            }
        }
        private fun handleFavouriteClick(message: Message, position: Int) {

            val context = itemView.context
            val currentFavouriteState = SimpleFavouriteManager.isFavourite(context, message.content)
            val newFavouriteState = !currentFavouriteState

            try {
                if (newFavouriteState) {
                    // Thêm vào favourite
                    SimpleFavouriteManager.addToFavourite(context, message.content)
                    SLog.d("Da them : $newFavouriteState")
                } else {
                    SimpleFavouriteManager.removeFromFavourite(context, message.content)
                    SLog.d("Xoa : $newFavouriteState")
                }
                message.isFavourite = newFavouriteState
                updateFavouriteButton(message)


            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi cập nhật yêu thích", Toast.LENGTH_SHORT).show()
            }
        }

        private fun showLoadingState(loadingText: String) {
//            mathView2.hide()
            webview.hide()

            // Hiển thị loading views
            loadingContainer?.show()
            progressBar?.show()
            if (loadingContainer == null) {

                /*mathView2.apply {
                    show()
                    text = loadingText
                    pixelScaleType = MathView.Scale.SCALE_DP
                    setTextSize(16)
                    textColor = "#666666"
                }*/

                webview.show()


                displayMarkdown(loadingText)


            }
        }

        private fun showNormalState(content: String) {
            loadingContainer?.hide()
            progressBar?.hide()
            tvLoadingText?.hide()


            /*mathView2.apply {
                show()
                text = content
                pixelScaleType = MathView.Scale.SCALE_DP
                setTextSize(16)
                textColor = "#000000"
            }*/

            webview.show()

            displayMarkdown(content)

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
            webview.loadDataWithBaseURL(
                "file:///android_asset/",
                htmlContent,
                "text/html",
                "utf-8",
                null
            )

//        // B  nước 1: Chuẩn hóa chuỗi markdown từ API (thay \\n -> \n thật)
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
}