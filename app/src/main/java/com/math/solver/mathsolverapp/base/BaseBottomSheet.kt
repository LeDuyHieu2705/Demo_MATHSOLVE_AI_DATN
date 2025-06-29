package com.math.solver.mathsolverapp.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.createViewModelLazy
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.di.DIFileComponent
import kotlin.reflect.KClass

abstract class BaseBottomSheet<viewModel : BaseViewModel, viewBinding : ViewBinding>(
    viewModelClass: KClass<viewModel>
) :
    BottomSheetDialogFragment() {
    protected val diComponent by lazy { DIFileComponent() }


    protected val viewModel by createViewModelLazy(viewModelClass, { viewModelStore })
    private var _viewBinding: viewBinding? = null
    protected val viewBinding get() = _viewBinding!! // ktlint-disable
//    protected var loadingProgressDialog: LoadingProgressDialog? = null

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding

    protected abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        onSubscribeObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    /**
     * Fragments outlive their views. Make sure you clean up any references to
     * the binding class instance in the fragment's onDestroyView() method.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun showLoading(isShow: Boolean) {

    }

    open fun onSubscribeObserver() {

    }
}

