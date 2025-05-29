package com.hieuld.datn.mathsolved.ui.activity

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.ActivityLoginBinding
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity

class LoginActivity : BaseActivity<BaseViewModel, ActivityLoginBinding>(BaseViewModel::class){
    override fun inflateViewBinding(inflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(inflater)
    }

    override fun initView() {
        viewBinding.apply {
            btnClose.clickOnce {
                finish()
            }
            btnLoginGoogle.clickOnce {
                actGoogleLogin()
            }
            btnLoginEmail.clickOnce {
                actEmailLogin()
            }
        }
    }
    private fun actGoogleLogin() {


    }
    private fun actEmailLogin() {


    }


}