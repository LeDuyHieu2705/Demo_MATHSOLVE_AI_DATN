package com.math.solver.mathsolverapp.ui.activity

import android.view.LayoutInflater
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.databinding.ActivityLoginBinding
import com.math.solver.mathsolverapp.utils.clickOnce

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