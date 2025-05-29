package com.math.solver.mathsolverapp.ui.activity

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import androidx.core.graphics.createBitmap
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.databinding.FragmentAdvisorBinding
import com.math.solver.mathsolverapp.ui.viewmodel.NetworkViewModel
import com.pixelcarrot.base64image.Base64Image

class AdvisorActivity : BaseActivity<NetworkViewModel,FragmentAdvisorBinding>(NetworkViewModel::class){

    private var imageBase64 = ""

    companion object {
        var bitmapResult: Bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
    }

    override fun preLoadData() {
        super.preLoadData()

        Base64Image.encode(bitmapResult ?: createBitmap(128, 128, Bitmap.Config.ARGB_8888)) { bas64 ->
            imageBase64 = bas64 ?: ""

            Log.d("hihihi", "base64 image = $imageBase64")

            if (!imageBase64.isNullOrEmpty()) {
                viewModel.chatWithAI(imageBase64)
            }


        }


    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentAdvisorBinding {
        return FragmentAdvisorBinding.inflate(inflater)
    }


    override fun initView() {

        viewBinding.apply {
//            imvTest.setImageBitmap(bitmapResult)
        }


    }

    }
