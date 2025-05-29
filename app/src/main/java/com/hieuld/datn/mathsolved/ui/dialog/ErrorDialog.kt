package com.hieuld.datn.mathsolved.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.DialogErrorBinding
import com.hieuld.datn.mathsolved.fragment.BaseDialogFragment
import com.hieuld.datn.mathsolved.utils.dismissDialogSafely

class ErrorDialog : BaseDialogFragment<BaseViewModel, DialogErrorBinding>(BaseViewModel::class) {

    private var message: String = "This isn’t Physics! Please scan the correct subject area or try different text"

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogErrorBinding {
        return DialogErrorBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        viewBinding.apply {
//            tvMessage.text = message
//
//            btnClose.setOnClickListener {
//                dismissDialogSafely(requireActivity().supportFragmentManager, "ErrorDialog")
//            }
        }
    }

    fun setMessage(msg: String): ErrorDialog {
        this.message = msg
        return this
    }

    companion object {
        fun newInstance(): ErrorDialog {
            return ErrorDialog()
        }
    }
}
