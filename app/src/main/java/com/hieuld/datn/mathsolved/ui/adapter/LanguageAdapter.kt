package com.hieuld.datn.mathsolved.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hieuld.datn.mathsolved.databinding.ItemSelectLanguageBinding
import com.hieuld.datn.mathsolved.respository.OnAdapterClick
import com.hieuld.datn.mathsolved.ui.viewmodel.LanguageViewModel
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.show

class LanguageAdapter(
    private val onAdapterClick: OnAdapterClick
) : ListAdapter<LanguageViewModel, LanguageAdapter.SelectLangVH>(diffCallback) {

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<LanguageViewModel>() {
            override fun areItemsTheSame(
                oldItem: LanguageViewModel,
                newItem: LanguageViewModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: LanguageViewModel,
                newItem: LanguageViewModel
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class SelectLangVH constructor(val binding: ItemSelectLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(
            item: LanguageViewModel,
            onAdapterClick: OnAdapterClick
        ) {
            binding.apply {
                try {
                    Glide.with(itemView.context)
                        .load(item.flag)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView)
                    txtLanguage.setText(item.nameLanguage)

                    if (item.isSelect) {
//                        ViewCompat.setBackgroundTintList(
//                            btnMain,
//                            ColorStateList.valueOf(Color.parseColor("#BEF8D1"))
//                        )
                        imvTick.show()

                    } else {
                        imvTick.hide()
//                        ViewCompat.setBackgroundTintList(
//                            btnMain,
//                            ColorStateList.valueOf(Color.parseColor("#ECECF4"))
//                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                btnMain.clickOnce {
                    SLog.d("btnMain.clickOnce {")
                    onAdapterClick.onSelect(item)
                }
            }
        }

        companion object {
            fun onBind(parent: ViewGroup): SelectLangVH {
                val view = ItemSelectLanguageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SelectLangVH(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectLangVH {
        return SelectLangVH.onBind(parent)
    }

    override fun onBindViewHolder(holder: SelectLangVH, position: Int) {
        holder.bindData(getItem(position), onAdapterClick)
    }
}