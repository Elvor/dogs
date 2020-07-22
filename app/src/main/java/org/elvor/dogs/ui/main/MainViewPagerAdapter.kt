package org.elvor.dogs.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.elvor.dogs.ui.breed_list.BreedListFragment
import org.elvor.dogs.ui.FavoriteListFragment
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