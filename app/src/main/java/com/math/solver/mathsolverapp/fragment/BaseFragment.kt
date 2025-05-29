package com.math.solver.mathsolverapp.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.di.DIFileComponent
import kotlin.reflect.KClass


abstract class BaseFragment<viewModel : BaseViewModel, viewBinding : ViewBinding>(viewModelClass: KClass<viewModel>) :
    Fragment() {


    protected val diFileComponent by lazy { DIFileComponent() }


    lateinit var gson: Gson

    // Create a Handler associated with the current Looper
    val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

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
        gson = Gson()
        preLoadData()
        loadAds()
        initView()
        observerData()
        clickView()
    }

    /**
     * Fragments outlive their views. Make sure you clean up any references to
     * the binding class instance in the fragment's onDestroyView() method.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    open fun observerData() {}
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
