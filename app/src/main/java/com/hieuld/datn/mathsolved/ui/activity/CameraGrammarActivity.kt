package com.hieuld.datn.mathsolved.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayout
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.ActivityCameraGrammarBinding
import com.hieuld.datn.mathsolved.ui.dialog.BottomSheetSelectLanguage
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity
import java.io.FileNotFoundException

class CameraGrammarActivity : BaseActivity<BaseViewModel, ActivityCameraGrammarBinding>(BaseViewModel::class) {

    private var isTurnOnFlash = false



    override fun inflateViewBinding(inflater: LayoutInflater): ActivityCameraGrammarBinding {
        return ActivityCameraGrammarBinding.inflate(inflater)
    }

    override fun initView() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

        viewBinding.apply {


            cameraView.setLifecycleOwner(this@CameraGrammarActivity)
            cameraView.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    super.onPictureTaken(result)

                    showLoading()

                    result.toBitmap(3000, 4000) { bitmap ->

                        if (bitmap != null) {
                            CropImageActivity.bitmapResult = bitmap

                            hideLoading()

                            openActivity(CropImageActivity::class.java)
                        }

                    }

                }

            })


            tabOptions.tabGravity = TabLayout.GRAVITY_CENTER
            tabOptions.addTab(tabOptions.newTab().setText("Check grammar"))
            tabOptions.addTab(tabOptions.newTab().setText("Translate"))

            tabOptions.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            SLog.d("Tab selected: Check grammar")

                        }
                        1 -> {
                            SLog.d("Tab selected: Translate")

                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            btnBack.clickOnce {
                finish()
            }
        }
    }

//



    override fun clickView() {
        viewBinding.apply {


            btnCamera.clickOnce  {
                SLog.d("btnTakePicture")
                cameraView.takePicture()
            }


            btnFlash.clickOnce {
                isTurnOnFlash = !isTurnOnFlash
                SLog.d( "btnFlash.setOnClickListener: $isTurnOnFlash")

                if (isTurnOnFlash) {
                    cameraView.flash = Flash.TORCH
//                    imvFlash.setImageResource(R.drawable.ic_camera_flash_off)
                } else {
                    cameraView.flash = Flash.OFF
//                    imvFlash.setImageResource(R.drawable.ic_camera_flash)
                }

            }

            btnPicture.setOnClickListener {
                showChooserIntent()
            }




            tvLangSelected.clickOnce {
                val bts = BottomSheetSelectLanguage.newInstance(
                    "en",
                    onOk = {
                        Log.d("hihihi","language seleteced: $it")
                    }
                )

                if (!isFinishing) {
                    bts.show(supportFragmentManager, bts.tag)
                }
            }
            viewBinding.tvLangSelected.setOnClickListener {
                BottomSheetSelectLanguage.newInstance("en") { selectedText ->
                    viewBinding.tvLangSelected.text = "Output language: $selectedText"
                }.show(supportFragmentManager, "BottomSheetLanguage")
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
                            val inputStream = contentResolver.openInputStream(selectedImageUri)
                            val bitmapResult = BitmapFactory.decodeStream(inputStream)
                            // Xử lý bitmap ở đây

                            if (bitmapResult != null) {
                                CropImageActivity.bitmapResult = bitmapResult

                                openActivity(CropImageActivity::class.java)
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
        super.onDestroy()
        viewBinding.cameraView.destroy()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.cameraView.close()

    }


}