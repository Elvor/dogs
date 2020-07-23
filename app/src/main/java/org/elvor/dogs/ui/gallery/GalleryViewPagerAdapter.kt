package org.elvor.dogs.ui.gallery

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter

class GalleryViewPagerAdapter(fragment: BaseGalleryFragment) : FragmentStateAdapter(fragment) {
    private val backingItems = mutableListOf<String>()
    private val pageIds = mutableListOf<Long>()

    var items: List<String>
        set(value) {
            val result = DiffUtil.calculateDiff(PagerDiffUtilCallback(backingItems, value), true)
            backingItems.clear()
            pageIds.clear()
            backingItems.addAll(value)
            pageIds.addAll(value.map { it.hashCode().toLong() })
            result.dispatchUpdatesTo(this)
        }
        get() = backingItems

    override fun getItemCount(): Int = backingItems.size

    override fun createFragment(position: Int): Fragment =
        ImageFragment.newInstance(backingItems[position])


    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong() // make sure notifyDataSetChanged() works
    }

    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
    }

    private class PagerDiffUtilCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)

    }
}