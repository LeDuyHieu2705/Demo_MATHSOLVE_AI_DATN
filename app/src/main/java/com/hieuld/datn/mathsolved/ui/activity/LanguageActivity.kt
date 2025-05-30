package com.hieuld.datn.mathsolved.ui.activity

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.data.enums.EnumSelectLanguage
import com.hieuld.datn.mathsolved.databinding.ActivityLanguageBinding
import com.hieuld.datn.mathsolved.respository.OnAdapterClick
import com.hieuld.datn.mathsolved.ui.adapter.LanguageAdapter
import com.hieuld.datn.mathsolved.ui.viewmodel.LanguageViewModel
import com.hieuld.datn.mathsolved.utils.Constants
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.setInVisible
import com.hieuld.datn.mathsolved.utils.openActivityAndClearApp
import com.hieuld.datn.mathsolved.utils.show

class LanguageActivity :
    BaseActivity<BaseViewModel, ActivityLanguageBinding>(BaseViewModel::class) {
    private var listLanguage = arrayListOf<LanguageViewModel>()

    private lateinit var adapterSelectLanguage: LanguageAdapter

    private var isGoToFromMenu = false

    private val TAG = "SelectLanguageActivity"

    private var languageSeleted = LanguageViewModel()


    override fun inflateViewBinding(inflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(inflater)
    }

    override fun initView() {
        isGoToFromMenu = intent.getBooleanExtra(Constants.FROM_MENU, false)
        initList()

        initViewClick()
    }

    private fun initList() {
        for (language in enumValues<EnumSelectLanguage>()) {
            val language = LanguageViewModel(
                id = language.id,
                nameLanguage = language.nameLanguage,
                flag = language.flag,
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
//            loadAdsOnView()
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
                    layoutNext.show()
                } else {
                    btnBack.setInVisible()
                }

                btnDone.show()
                layoutNext.show()
            } else {
                btnBack.setInVisible()
                btnDone.setInVisible()
                layoutNext.setInVisible()
            }

            btnBack.setOnClickListener {
                finish()
            }

            headerTxt.setText(R.string.title_language)


            adapterSelectLanguage = LanguageAdapter(object : OnAdapterClick {
                override fun onSelect(data: Any) {
                    val dataSelected = data as LanguageViewModel
                    SLog.d("dataSelected: $dataSelected")
                    listLanguage.map { selectLanguageModel ->
                        selectLanguageModel.isSelect =
                            selectLanguageModel.code == dataSelected.code
                    }
                    languageSeleted = dataSelected
                    adapterSelectLanguage?.notifyDataSetChanged()

                    btnDone.hide()
                    progressLoading.show()
                    layoutNext.show()

                    handler.postDelayed({
                        progressLoading.hide()
                        btnDone.show()
                    }, 3000)


                }

            })

            rcvLanguage.apply {
                layoutManager = LinearLayoutManager(this@LanguageActivity)
                adapter = adapterSelectLanguage
            }

            adapterSelectLanguage.submitList(listLanguage)


            btnDone.setOnClickListener {
                if (languageSeleted != null) {
                    onSaveLanguage()
                }
            }
        }
    }

    private fun onSaveLanguage() {
        reloadActivity(languageSeleted.code)
        if (!isGoToFromMenu) {
            if (!isFinishing) {
                openActivityAndClearApp(OnboardingActivity::class.java)
                finish()
            }
        } else {
            handler.postDelayed({
                openActivityAndClearApp(MainActivity::class.java)
            }, 500)
        }
    }

    private fun reloadActivity(lang: String) {
        diFileComponent.sharedPreferenceUtils.selectedLanguageCode = lang

    }


}