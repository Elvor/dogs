package org.elvor.dogs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.Exception

class MainViewPagerAdapter(fragment: MainFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BreedListFragment()
            1 -> FavoriteListFragment()
            else -> throw Exception("no fragment for $position position")
        }
    }
}