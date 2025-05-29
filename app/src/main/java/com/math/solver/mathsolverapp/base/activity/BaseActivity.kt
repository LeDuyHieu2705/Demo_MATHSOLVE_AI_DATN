package com.math.solver.mathsolverapp.base.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelLazy
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.di.DIFileComponent
import kotlin.reflect.KClass

abstract class BaseActivity<viewModel : BaseViewModel, viewBinding : ViewBinding>(viewModelClass: KClass<viewModel>) :
    AppCompatActivity() {


    protected val diFileComponent by lazy { DIFileComponent() }

    protected val viewModel by ViewModelLazy(
     viewModelClass,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )


    lateinit var gson: Gson

    // Create a Handler associated with the current Looper
    val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())


//    protected lateinit var progressDialog: DialogLoadingApp

    protected lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        setContentView(viewBinding.root)
//        progressDialog = DialogLoadingApp.newInstance()
//        transparentStatusBar()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        gson = Gson()
        preLoadData()
        loadAds()
        initView()
        clickView()
        observerData()
    }

    open fun preLoadData() {}
    open fun loadAds() {}
    protected abstract fun initView()
    open fun clickView() {}
    open fun observerData() {}


    override fun onResume() {
        super.onResume()
//        hideNavigationBar()

    }

    override fun attachBaseContext(mContext: Context) {
//        val resources = mContext.resources
//        val locale = Locale(currentLanguage)
//        locale.let {
//            Locale.setDefault(it)
//        }
//        val config = resources.configuration
//        config.setLocale(locale)
//        val newContext = mContext.createConfigurationContext(config)
        super.attachBaseContext(mContext)
    }
    fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }





    fun showLoading() {
//        try {
//            val tag = DialogLoadingApp::class.java.simpleName
//            val existingFragment = supportFragmentManager.findFragmentByTag(tag)
//            if (existingFragment == null || !existingFragment.isAdded) {
//                progressDialog = DialogLoadingApp.newInstance()
//                progressDialog.show(supportFragmentManager, tag)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    // Ẩn DialogLoading nếu đang hiển thị
    fun hideLoading() {
//        try {
//            val tag = DialogLoadingApp::class.java.simpleName
//            val existingFragment = supportFragmentManager.findFragmentByTag(tag)
//            if (existingFragment != null && existingFragment is DialogFragment) {
//                existingFragment.dismiss()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

}