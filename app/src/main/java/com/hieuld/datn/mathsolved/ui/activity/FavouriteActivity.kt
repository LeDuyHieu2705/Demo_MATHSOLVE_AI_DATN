package com.hieuld.datn.mathsolved.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.ActivityFavouriteBinding
import com.hieuld.datn.mathsolved.ui.adapter.FavouriteAdapter
import com.hieuld.datn.mathsolved.utils.commons.managers.SimpleFavouriteManager

class FavouriteActivity :
    BaseActivity<BaseViewModel, ActivityFavouriteBinding>(BaseViewModel::class) {

    private lateinit var adapter: FavouriteAdapter

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityFavouriteBinding {
        return ActivityFavouriteBinding.inflate(inflater)
    }

    override fun initView() {
        setupRecyclerView()
        setupClickListeners()
        loadFavouriteMessages()
    }

    private fun setupRecyclerView() {
        adapter = FavouriteAdapter(
            messages = mutableListOf(),
            timestamps = mutableListOf(),
            onCopyClick = { message ->
                copyToClipboard(message)
            },
            onRemoveClick = { message, position ->
                removeFavouriteMessage(message, position)
            }
        )

        viewBinding.recyclerViewFavourite.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
            adapter = this@FavouriteActivity.adapter
        }
    }

    private fun setupClickListeners() {
        viewBinding.btnBack.setOnClickListener {
            finish()
        }

        viewBinding.tvClearAll.setOnClickListener {
            showClearAllDialog()
        }
    }

    private fun loadFavouriteMessages() {
        val favouriteItems = SimpleFavouriteManager.getFavouriteMessagesWithTimestamp(this)

        if (favouriteItems.isNotEmpty()) {
            // Sắp xếp theo timestamp
            val sortedItems = favouriteItems.sortedByDescending { it.second }
            adapter.updateFavourites(sortedItems)
            updateUIState(false)
        } else {
            updateUIState(true)
        }
    }

    private fun updateUIState(isEmpty: Boolean) {
        viewBinding.apply {
            if (isEmpty) {
                layoutEmpty.visibility = View.VISIBLE
                recyclerViewFavourite.visibility = View.GONE
                tvClearAll.visibility = View.GONE
            } else {
                layoutEmpty.visibility = View.GONE
                recyclerViewFavourite.visibility = View.VISIBLE
                tvClearAll.visibility = View.VISIBLE
            }
        }
    }

    private fun showClearAllDialog() {
        AlertDialog.Builder(this)
            .setTitle("Xóa tất cả")
            .setMessage("Bạn có chắc chắn muốn xóa tất cả câu trả lời yêu thích?")
            .setPositiveButton("Xóa") { _, _ ->
                clearAllFavourites()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun clearAllFavourites() {
        SimpleFavouriteManager.clearAllFavourites(this)
        adapter.updateFavourites(emptyList())
        updateUIState(true)
        Toast.makeText(this, "Đã xóa tất cả yêu thích", Toast.LENGTH_SHORT).show()
    }

    private fun removeFavouriteMessage(message: String, position: Int) {
        // Remove từ storage
        SimpleFavouriteManager.removeFromFavourite(this, message)

        // Load lại danh sách và cập nhật adapter
        val updatedItems = SimpleFavouriteManager.getFavouriteMessagesWithTimestamp(this)
        val sortedItems = updatedItems.sortedByDescending { it.second }

        adapter.updateFavourites(sortedItems)
        updateUIState(sortedItems.isEmpty())

        Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
    }

    private fun copyToClipboard(message: String) {
        val clipboard = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Favourite Message", message)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Đã sao chép", Toast.LENGTH_SHORT).show()
    }
}