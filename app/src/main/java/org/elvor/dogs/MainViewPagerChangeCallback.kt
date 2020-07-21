package org.elvor.dogs

import androidx.viewpager2.widget.ViewPager2

class MainViewPagerChangeCallback(private val lambda: (Int) -> Unit) : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        lambda(position)
    }
}