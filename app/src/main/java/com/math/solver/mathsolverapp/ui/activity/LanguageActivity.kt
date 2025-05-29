package com.math.solver.mathsolverapp.ui.activity

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.data.enums.EnumSelectLanguage
import com.math.solver.mathsolverapp.databinding.ActivityLanguageBinding
import com.math.solver.mathsolverapp.respository.OnAdapterClick
import com.math.solver.mathsolverapp.ui.adapter.LanguageAdapter
import com.math.solver.mathsolverapp.ui.viewmodel.LanguageViewModel
import com.math.solver.mathsolverapp.utils.Constants
import com.math.solver.mathsolverapp.utils.SLog
import com.math.solver.mathsolverapp.utils.hide
import com.math.solver.mathsolverapp.utils.openActivityAndClearApp
import com.math.solver.mathsolverapp.utils.setInVisible
import com.math.solver.mathsolverapp.utils.show

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
                selectLanguageModel.isSelect = true
                languageSeleted = selectLanguageModel
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
        SLog.d("sobu", "reloadActivity: lang: $lang")
        diFileComponent.sharedPreferenceUtils.selectedLanguageCode = lang

    }


}