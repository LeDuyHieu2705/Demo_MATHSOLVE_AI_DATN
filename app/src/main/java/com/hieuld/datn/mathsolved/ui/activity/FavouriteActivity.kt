package com.hieuld.datn.mathsolved.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.hieuld.datn.mathsolved.R
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
        // Create custom dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_clear_all_favourite)

        // Make dialog cancelable
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        // Set dialog window properties
        dialog.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)

            // Add scale animation for clear all dialog
            window.attributes.windowAnimations = R.style.DialogScaleAnimation
        }

        // Get dialog views
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)
        val btnClearAll = dialog.findViewById<MaterialButton>(R.id.btnClearAll)

        // Set click listeners
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnClearAll.setOnClickListener {
            clearAllFavourites()
            dialog.dismiss()
        }

        // Show dialog
        dialog.show()
    }

    private fun clearAllFavourites() {
        SimpleFavouriteManager.clearAllFavourites(this)
        adapter.updateFavourites(emptyList())
        updateUIState(true)
        Toast.makeText(this, getString(R.string.toast_all_cleared), Toast.LENGTH_SHORT).show()
    }

    private fun removeFavouriteMessage(message: String, position: Int) {
        // Remove từ storage
        SimpleFavouriteManager.removeFromFavourite(this, message)

        // Load lại danh sách và cập nhật adapter
        val updatedItems = SimpleFavouriteManager.getFavouriteMessagesWithTimestamp(this)
        val sortedItems = updatedItems.sortedByDescending { it.second }

        adapter.updateFavourites(sortedItems)
        updateUIState(sortedItems.isEmpty())

        Toast.makeText(this, getString(R.string.toast_removed_from_favourite), Toast.LENGTH_SHORT).show()
    }

    private fun copyToClipboard(message: String) {
        val clipboard = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Favourite Message", message)
        clipboard.setPrimaryClip(clip)
    }
}