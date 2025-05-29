package com.hieuld.datn.mathsolved.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hieuld.datn.mathsolved.base.BaseBottomSheet
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.data.enums.EnumBottomSheetLanguage
import com.hieuld.datn.mathsolved.databinding.BottomSheetSelectLanguageBinding
import com.hieuld.datn.mathsolved.respository.OnAdapterClick
import com.hieuld.datn.mathsolved.ui.adapter.BottomSheetLanguageAdapter
import com.hieuld.datn.mathsolved.ui.viewmodel.BottomSheetViewModel
import com.hieuld.datn.mathsolved.utils.show
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.setInVisible

class BottomSheetSelectLanguage : BaseBottomSheet<BaseViewModel, BottomSheetSelectLanguageBinding>(
    BaseViewModel::class) {

    private var onDone: ((String) -> Unit)? = null

    private var langSelected: String = "en"

    private var listLanguage = arrayListOf<BottomSheetViewModel>()

    private lateinit var adapterSelectLanguage: BottomSheetLanguageAdapter

    private var isGoToFromMenu = false

    private val TAG = "SelectLanguageActivity"

    private var languageSeleted = BottomSheetViewModel()
    companion object {
        fun newInstance(
            lang: String,
            onOk: (String) -> Unit
        ) : BottomSheetSelectLanguage {
            val fragment = BottomSheetSelectLanguage()
            fragment.onDone = onOk
            fragment.langSelected = lang
            return fragment
        }
    }


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BottomSheetSelectLanguageBinding {
        return BottomSheetSelectLanguageBinding.inflate(inflater)
    }
    override fun initialize() {

        viewBinding.apply {
            initList()
            initViewClick()
        }


    }
    private fun initList() {
        for (language in enumValues<EnumBottomSheetLanguage>()) {
            val language = BottomSheetViewModel(
                id = language.id,
                nameLanguage = requireContext().getString(language.nameLanguage),
                code = language.code,
                false
            )
            listLanguage.add(language)
        }
        // Lấy mã ngôn ngữ
        val languageCode =
            if (isGoToFromMenu || diFileComponent.sharedPreferenceUtils.isSecondOpenApp) {
                diFileComponent.sharedPreferenceUtils.selectedLanguageCode
            } else {
                ""
            }

        if (isGoToFromMenu || diFileComponent.sharedPreferenceUtils.isSecondOpenApp) {
//
        }
        SLog.d("LanguageCode", "Mã ngôn ngữ của ứng dụng: $languageCode")
        for (selectLanguageModel in listLanguage) {
            if (selectLanguageModel.code == languageCode) {
                // Nếu tìm thấy phần tử trùng với languageCode
                selectLanguageModel.isSelect = true // Cập nhật isSelect
                languageSeleted = selectLanguageModel // Gán languageSeleted
                break // Kết thúc vòng lặp
            }
        }

    }
    private fun initViewClick() {
        if (!diFileComponent.sharedPreferenceUtils.is_tracking_first_open_language) {
            diFileComponent.sharedPreferenceUtils.is_tracking_first_open_language = true
        }

        viewBinding.apply {
            if (diFileComponent.sharedPreferenceUtils.isSecondOpenApp) {
                if (isGoToFromMenu) {
                    btnBack.show()
                } else {
                    btnBack.setInVisible()
                }
            } else {
                btnBack.setInVisible()
            }

            adapterSelectLanguage = BottomSheetLanguageAdapter(object : OnAdapterClick {
                override fun onSelect(data: Any) {
                    val dataSelected = data as BottomSheetViewModel

                    SLog.d("dataSelected: $dataSelected")
                    SLog.d("Selected language: ${dataSelected.nameLanguage}")

                    // Cập nhật trạng thái selection
                    listLanguage.forEach { it.isSelect = it.code == dataSelected.code }
                    languageSeleted = dataSelected
                    adapterSelectLanguage?.notifyDataSetChanged()

                    // Gọi callback với tên ngôn ngữ
                    dataSelected.nameLanguage?.let { languageName ->
                        SLog.d("Invoking callback with language: $languageName")
                        onDone?.invoke(languageName)
                    }

                    // Đóng BottomSheet
                    dismiss()


                }
            })

            rcvbottomsheettLanguage.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterSelectLanguage
            }

            adapterSelectLanguage.submitList(listLanguage)
        }
    }


}