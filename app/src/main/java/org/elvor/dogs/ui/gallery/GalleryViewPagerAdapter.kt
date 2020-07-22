package org.elvor.dogs.ui.gallery

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GalleryViewPagerAdapter(fragment: GalleryFragment) : FragmentStateAdapter(fragment) {
    private var items: List<String> = emptyList()

    fun setItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = ImageFragment.newInstance(items[position])
}