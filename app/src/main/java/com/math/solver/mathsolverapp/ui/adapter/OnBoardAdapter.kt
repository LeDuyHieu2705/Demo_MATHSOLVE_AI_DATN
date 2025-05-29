package com.math.solver.mathsolverapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.math.solver.mathsolverapp.R

class OnBoardAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment()
            1 -> SecondFragment()
            2 -> ThirdFragment()
            else -> FirstFragment()
        }
    }

    class FirstFragment : Fragment(R.layout.fragment_onboarding1)
    class SecondFragment : Fragment(R.layout.fragment_onboarding2)
    class ThirdFragment : Fragment(R.layout.fragment_onboarding3)

}