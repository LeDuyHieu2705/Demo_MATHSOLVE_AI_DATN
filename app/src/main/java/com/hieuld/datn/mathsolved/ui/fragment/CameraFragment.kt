package com.hieuld.datn.mathsolved.ui.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.google.android.material.tabs.TabLayout
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.data.enums.EnumSubject
import com.hieuld.datn.mathsolved.databinding.FragmentCameraBinding
import com.hieuld.datn.mathsolved.fragment.BaseFragment
import com.hieuld.datn.mathsolved.ui.activity.CropImageActivity
import com.hieuld.datn.mathsolved.ui.dialog.BottomSheetSelectLanguage
import com.hieuld.datn.mathsolved.utils.Constants.Companion.TYPE_SUBJECT_SELECTED
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity
import com.hieuld.datn.mathsolved.utils.commons.utils.show
import java.io.FileNotFoundException

class CameraFragment : BaseFragment<BaseViewModel,FragmentCameraBinding>(BaseViewModel::class){
    private var isTurnOnFlash = false
    private var typeSubjectSelected = EnumSubject.MATH.typeSub
    private var selectedLanguage = "English" // Ngôn ngữ mặc định
    private var selectedLanguageCode = "en" // Mã ngôn ngữ mặc định

    companion object {
        fun newInstance(): CameraFragment {
            return CameraFragment()
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCameraBinding {
        return FragmentCameraBinding.inflate(inflater)
    }

    override fun initView() {
        viewBinding.apply {
            // Mặc định ẩn tvLangSelected khi khởi tạo
            tvLangSelected.hide()

            // Thiết lập Camera
            cameraView.setLifecycleOwner(this@CameraFragment)
            cameraView.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    super.onPictureTaken(result)
                    showLoading()

                    result.toBitmap(3000, 4000) { bitmap ->
                        if (bitmap != null) {
                            CropImageActivity.bitmapResult = bitmap
                            hideLoading()
                            requireContext().openActivity(CropImageActivity::class.java) {
                                putString(TYPE_SUBJECT_SELECTED, typeSubjectSelected)
                                // Truyền thông tin ngôn ngữ nếu đang ở chế độ Translate
                                if (typeSubjectSelected == EnumSubject.TRANSLATE.typeSub) {
                                    putString("SELECTED_LANGUAGE", selectedLanguage)
                                    putString("SELECTED_LANGUAGE_CODE", selectedLanguageCode)
                                }
                            }
                        }
                    }
                }
            })

            // Thiết lập TabLayout
            setupTabLayout()

            // Thiết lập click listener cho tvLangSelected
            setupLanguageSelectionClick()
        }
    }

    private fun setupLanguageSelectionClick() {
        viewBinding.tvLangSelected.clickOnce {
            SLog.d("tvLangSelected clicked - typeSubjectSelected: $typeSubjectSelected")
            // Chỉ cho phép click khi đang ở tab Translate
            if (typeSubjectSelected == EnumSubject.TRANSLATE.typeSub) {
                SLog.d("Clicked on language selection - showing BottomSheet")
                showLanguageSelectionBottomSheet()
            } else {
                SLog.d("Not in Translate mode, click ignored")
            }
        }
    }

    private fun setupTabLayout() {
        viewBinding.tabOptions.apply {
            tabGravity = TabLayout.GRAVITY_CENTER

            // Clear existing tabs (if any)
            removeAllTabs()

            // Add tabs with proper titles
            addTab(newTab().setText(getString(EnumSubject.MATH.nameSub)))
            addTab(newTab().setText(getString(EnumSubject.CHECK_GRAMMAR.nameSub)))
            addTab(newTab().setText(getString(EnumSubject.TRANSLATE.nameSub)))

            // Set default selected tab
            selectTab(getTabAt(0))

            // Add tab selection listener
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    handleTabSelection(tab.position)

                    animateTabSelection(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    resetTabAnimation(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    SLog.d("Tab reselected: ${tab.text}")
                }
            })
        }
    }

    private fun handleTabSelection(position: Int) {
        when (position) {
            0 -> {
                SLog.d("Đã chọn tab: Math")
                typeSubjectSelected = EnumSubject.MATH.typeSub
                updateUIForSubject(EnumSubject.MATH)
            }
            1 -> {
                SLog.d("Đã chọn tab: Check Grammar")
                typeSubjectSelected = EnumSubject.CHECK_GRAMMAR.typeSub
                updateUIForSubject(EnumSubject.CHECK_GRAMMAR)
            }
            2 -> {
                SLog.d("Đã chọn tab: Translate")
                typeSubjectSelected = EnumSubject.TRANSLATE.typeSub
                updateUIForSubject(EnumSubject.TRANSLATE)
            }
            else -> {
                SLog.d("Đã chọn tab: Mặc định (Math)")
                typeSubjectSelected = EnumSubject.MATH.typeSub
                updateUIForSubject(EnumSubject.MATH)
            }
        }
    }


    private fun updateUIForSubject(subject: EnumSubject) {
        viewBinding.apply {
            when (subject) {
                EnumSubject.MATH -> {
                    hideLanguageSelection()
                    tvInstruction2.text = "Take the picture of a math problem"
                }
                EnumSubject.CHECK_GRAMMAR -> {
                    tvInstruction2.text = "Take the picture of a grammar problem"
                    hideLanguageSelection()
                }
                EnumSubject.TRANSLATE -> {
                    tvInstruction2.text = "Take the picture of a translate problem"
                    showLanguageSelection()
                }
            }
        }
    }


    private fun showLanguageSelection() {
        viewBinding.apply {
            tvLangSelected.show()
            updateSelectedLanguageUI()
            SLog.d("Hiển thị UI chọn ngôn ngữ cho Translate")
        }
    }

    private fun hideLanguageSelection() {
        viewBinding.apply {
            tvLangSelected.hide()
            SLog.d("Ẩn UI chọn ngôn ngữ")
        }
    }

    private fun showLanguageSelectionBottomSheet() {
        SLog.d("Creating BottomSheet with current language: $selectedLanguageCode")

        try {
            val bottomSheet = BottomSheetSelectLanguage.newInstance(
                lang = selectedLanguageCode
            ) { selectedLanguageName ->
                SLog.d("=== CALLBACK RECEIVED ===")
                SLog.d("Callback received with language: $selectedLanguageName")

                requireActivity().runOnUiThread {
                    handleLanguageSelection(selectedLanguageName)
                }
            }

            bottomSheet.show(parentFragmentManager, "LanguageSelectionBottomSheet")
            SLog.d("BottomSheet shown successfully")

        } catch (e: Exception) {
            SLog.d("Error showing BottomSheet: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun handleLanguageSelection(selectedLanguageName: String) {
        SLog.d("handleLanguageSelection called with: $selectedLanguageName")

        selectedLanguage = selectedLanguageName

        updateLanguageCode(selectedLanguageName)

        // Cập nhật giao diện
        updateSelectedLanguageUI()

        SLog.d("Language selection completed: $selectedLanguageName (Code: $selectedLanguageCode)")
    }
    private fun updateLanguageCode(languageName: String) {
        SLog.d("updateLanguageCode called with: $languageName")

        selectedLanguageCode = when (languageName.lowercase().trim()) {
            getString(R.string.txt_english).lowercase().trim() -> "en"
            getString(R.string.txt_vietnamese).lowercase().trim() -> "vi"
            // Thêm các ngôn ngữ khác từ enum khi bạn uncomment
            else -> {
                SLog.d("Unknown language: $languageName, defaulting to English")
                "en"
            }
        }
    }

    private fun updateSelectedLanguageUI() {
        if (typeSubjectSelected == EnumSubject.TRANSLATE.typeSub) {
            viewBinding.apply {
                val displayText = "Output Language: $selectedLanguage "
                tvLangSelected.text = displayText
            }
        } else {
        }
    }

    private fun animateTabSelection(tab: TabLayout.Tab) {
        tab.view?.animate()
            ?.scaleX(1.1f)
            ?.scaleY(1.1f)
            ?.setDuration(150)
            ?.withEndAction {
                tab.view?.animate()
                    ?.scaleX(1.0f)
                    ?.scaleY(1.0f)
                    ?.setDuration(150)
                    ?.start()
            }
            ?.start()
    }

    private fun resetTabAnimation(tab: TabLayout.Tab) {
        tab.view?.animate()
            ?.scaleX(1.0f)
            ?.scaleY(1.0f)
            ?.setDuration(100)
            ?.start()
    }

    override fun clickView() {
        viewBinding.apply {
            btnCamera.clickOnce {
                SLog.d("btnTakePicture")
                cameraView.takePicture()
            }

            btnFlash.clickOnce {
                isTurnOnFlash = !isTurnOnFlash
                SLog.d("btnFlash.setOnClickListener: $isTurnOnFlash")

                if (isTurnOnFlash) {
                    cameraView.flash = Flash.TORCH
                } else {
                    cameraView.flash = Flash.OFF
                }
            }

            btnPicture.setOnClickListener {
                showChooserIntent()
            }
        }
    }

    private val intentChooser =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val selectedImageUri: Uri? = data.data
                    try {
                        selectedImageUri?.let {
                            val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri)
                            val bitmapResult = BitmapFactory.decodeStream(inputStream)

                            if (bitmapResult != null) {
                                CropImageActivity.bitmapResult = bitmapResult
                                requireContext().openActivity(CropImageActivity::class.java) {
                                    putString(TYPE_SUBJECT_SELECTED, typeSubjectSelected)
                                    // Truyền thông tin ngôn ngữ nếu đang ở chế độ Translate
                                    if (typeSubjectSelected == EnumSubject.TRANSLATE.typeSub) {
                                        putString("SELECTED_LANGUAGE", selectedLanguage)
                                        putString("SELECTED_LANGUAGE_CODE", selectedLanguageCode)
                                    }
                                }
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    private fun showChooserIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        intentChooser.launch(Intent.createChooser(intent, "Select Picture"))
    }

    override fun onResume() {
        super.onResume()
        viewBinding.cameraView.open()
    }

    override fun onDestroy() {
        try {
            if (viewBinding != null) {
                if (viewBinding.cameraView != null) {
                    viewBinding.cameraView.destroy()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.cameraView.close()
    }
}