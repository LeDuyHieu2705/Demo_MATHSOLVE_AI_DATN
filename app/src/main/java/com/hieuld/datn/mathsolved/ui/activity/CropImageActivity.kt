package com.hieuld.datn.mathsolved.ui.activity

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.view.LayoutInflater
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.data.enums.EnumSubject
import com.hieuld.datn.mathsolved.databinding.ActivityCropImageBinding
import com.hieuld.datn.mathsolved.ui.cropper.CropImage
import com.hieuld.datn.mathsolved.ui.cropper.CropImageOptions
import com.hieuld.datn.mathsolved.ui.cropper.CropImageView
import com.hieuld.datn.mathsolved.utils.Constants.Companion.TYPE_SUBJECT_SELECTED
import com.hieuld.datn.mathsolved.utils.commons.utils.SLog
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity

class CropImageActivity :
    BaseActivity<BaseViewModel, ActivityCropImageBinding>(BaseViewModel::class),
    CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener,
    CropImageView.OnSetCropWindowChangeListener,
    CropImageView.OnSetCropOverlayMovedListener,
    CropImageView.SetWindowAnimation {
    companion object {
        var bitmapResult: Bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
    }

    private var options: CropImageOptions = CropImageOptions()


    private var typeSubjectSelected = EnumSubject.MATH.typeSub

    private var selectedLanguageCode = "en" // Mã ngôn ngữ mặc định

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityCropImageBinding {
        return ActivityCropImageBinding.inflate(inflater)
    }


    override fun preLoadData() {
        super.preLoadData()
        typeSubjectSelected = intent.getStringExtra(TYPE_SUBJECT_SELECTED) ?: EnumSubject.MATH.typeSub


        SLog.d("typeSubjectSelected typeSubjectSelected: $typeSubjectSelected")
    }

    override fun initView() {
        viewBinding.apply {
            cropImage.apply {
                options.initialCropWindowPaddingRatio = 0.22f
                options.guidelines = CropImageView.Guidelines.OFF
                setImageBitmap(bitmapResult)
                setImageCropOptions(options)
                setOnSetImageUriCompleteListener(this@CropImageActivity)
                setOnCropImageCompleteListener(this@CropImageActivity)
                setOnCropWindowChangedListener(this@CropImageActivity)
                setOnSetCropOverlayMovedListener(this@CropImageActivity)
                setOnWindowScanChange(this@CropImageActivity)
            }

            btnTick.clickOnce {
                cropImage()
            }
            btnReturn.clickOnce {
                clickBack()
            }
        }
    }

    private fun cropImage() {
        SLog.d("cropImage()")
        val cropImageOptions = CropImageOptions()
        viewBinding.cropImage.croppedImageAsync(
            saveCompressFormat = cropImageOptions.outputCompressFormat,
            saveCompressQuality = cropImageOptions.outputCompressQuality,
            reqWidth = cropImageOptions.outputRequestWidth,
            reqHeight = cropImageOptions.outputRequestHeight,
            options = cropImageOptions.outputRequestSizeOptions,
            customOutputUri = cropImageOptions.customOutputUri,
        )
    }

    private fun clickBack() {
        finish()
    }


    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {

    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {

        viewBinding.apply {
            if (result.error == null) {

                val imageBitmap = if (cropImage.cropShape == CropImageView.CropShape.OVAL) {
                    result.bitmap?.let(CropImage::toOvalBitmap)
                } else {
                    result.bitmap
                }

                ChatActivity.bitmapResult =
                    imageBitmap ?: Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)

                openActivity(ChatActivity::class.java) {
                    putString(TYPE_SUBJECT_SELECTED, typeSubjectSelected)
                }
//                openActivity(AdvisorFragment::class.java)


//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragmentContainer, AdvisorFragment.newInstance(isFromMain = false))
//                    .commitAllowingStateLoss()

            } else {
//                showToastOnceEvery15Seconds(getString(R.string.crop_failed, result.error.message))
            }
        }


    }

    override fun onCropWindowChanged() {

    }

    override fun onCropOverlayMoved(rect: Rect?) {

    }

    override fun onSetScanLoading(rect: RectF, left: Float, right: Float) {

    }


}