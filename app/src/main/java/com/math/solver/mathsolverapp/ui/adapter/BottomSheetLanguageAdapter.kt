    package com.math.solver.mathsolverapp.ui.adapter

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.DiffUtil
    import androidx.recyclerview.widget.ListAdapter
    import androidx.recyclerview.widget.RecyclerView
    import com.math.solver.mathsolverapp.databinding.ItemBottomsheetLanguageTranslateBinding
    import com.math.solver.mathsolverapp.respository.OnAdapterClick
    import com.math.solver.mathsolverapp.ui.viewmodel.BottomSheetViewModel
    import com.math.solver.mathsolverapp.utils.SLog
    import com.math.solver.mathsolverapp.utils.clickOnce
    import com.math.solver.mathsolverapp.utils.hide
    import com.math.solver.mathsolverapp.utils.show

    class BottomSheetLanguageAdapter(
        private val onAdapterClick: OnAdapterClick
    ) : ListAdapter<BottomSheetViewModel, BottomSheetLanguageAdapter.SelectLangVH>(diffCallback) {


        companion object {

            val diffCallback = object : DiffUtil.ItemCallback<BottomSheetViewModel>() {
                override fun areItemsTheSame(
                    oldItem: BottomSheetViewModel,
                    newItem: BottomSheetViewModel
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: BottomSheetViewModel,
                    newItem: BottomSheetViewModel
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
        }

        class SelectLangVH constructor(val binding: ItemBottomsheetLanguageTranslateBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bindData(
                item: BottomSheetViewModel,
                onAdapterClick: OnAdapterClick
            ) {
                binding.apply {
                    try {
                        txtLanguage.setText(item.nameLanguage)

                        if (item.isSelect) {

                            imvTick.show()

                        } else {
                            imvTick.hide()

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
                    val view = ItemBottomsheetLanguageTranslateBinding.inflate(
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