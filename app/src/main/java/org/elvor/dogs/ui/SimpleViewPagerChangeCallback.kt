package org.elvor.dogs.ui

import androidx.viewpager2.widget.ViewPager2

class SimpleViewPagerChangeCallback(private val lambda: (Int) -> Unit) :
    ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        lambda(position)
    }
}