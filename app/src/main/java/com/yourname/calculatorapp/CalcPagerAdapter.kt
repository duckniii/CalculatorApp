package com.yourname.calculatorapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalcPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> NormalCalcFragment()
        1 -> FractionFragment()
        else -> CurrencyFragment()
    }
}