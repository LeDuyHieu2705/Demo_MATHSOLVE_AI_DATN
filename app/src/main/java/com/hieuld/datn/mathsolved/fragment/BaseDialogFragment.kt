package com.hieuld.datn.mathsolved.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.createViewModelLazy
import androidx.viewbinding.ViewBinding
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.di.DIFileComponent
import kotlin.reflect.KClass

abstract class BaseDialogFragment<viewModel : BaseViewModel, viewBinding : ViewBinding>(
    viewModelClass: KClass<viewModel>
) :
    DialogFragment() {


    protected val diFileComponent by lazy { DIFileComponent() }

    protected val viewModel by createViewModelLazy(viewModelClass, { viewModelStore })
    private var _viewBinding: viewBinding? = null
    protected val viewBinding get() = _viewBinding!! // ktlint-disable

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding

    protected abstract fun initView()

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
        preLoadData()
        loadAds()
        initView()
        onSubscribeObserver()
        clickView()
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


    open fun onSubscribeObserver() {}
    open fun preLoadData() {}
    open fun loadAds() {}
    open fun clickView() {}


    protected fun showLoading() {
        (activity as? BaseActivity<*, *>)?.showLoading()
    }

    protected fun hideLoading() {
        (activity as? BaseActivity<*, *>)?.hideLoading()
    }
}
