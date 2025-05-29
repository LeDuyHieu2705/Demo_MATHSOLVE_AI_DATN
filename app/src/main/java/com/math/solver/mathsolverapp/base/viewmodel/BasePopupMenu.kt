package com.math.solver.mathsolverapp.base.viewmodel

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow

class BasePopupMenu(
    private val context: Context,
    private val rLayoutId: Int,
    private val onClickListener: PopupMenuCustomOnClickListener
) {
    private val popupWindow: PopupWindow
    private val popupView: View
    fun setAnimationStyle(animationStyle: Int) {
        popupWindow.animationStyle = animationStyle
    }

    fun show() {
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    fun show(anchorView: View) {
        popupWindow.showAsDropDown(anchorView, anchorView.width - popupWindow.width, 0)
    }

    fun showLeft(anchorView: View) {
        popupWindow.showAsDropDown(
            anchorView,
            anchorView.width - popupWindow.width,
            0,
            Gravity.TOP or Gravity.LEFT
        )
    }

    fun showRight(anchorView: View) {
        popupWindow.showAsDropDown(
            anchorView,
            anchorView.width - popupWindow.width,
            0,
            Gravity.TOP or Gravity.RIGHT
        )
    }


    interface PopupMenuCustomOnClickListener {
        fun initView(v: View)
        fun onClick(menuItemId: Int, v: View)
    }

    init {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(rLayoutId, null)
        onClickListener.initView(popupView)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 10f
        val linearLayout = popupView as LinearLayout
        for (i in 0 until linearLayout.childCount) {
            val v: View = linearLayout.getChildAt(i)
            v.setOnClickListener { v1 ->
                onClickListener.onClick(v1.id, popupView)
                popupWindow.dismiss()
            }
        }
    }
}