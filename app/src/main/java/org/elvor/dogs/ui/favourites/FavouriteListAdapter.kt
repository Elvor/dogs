package org.elvor.dogs.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.dogs.R
import org.elvor.dogs.databinding.ItemWithInfoBinding
import org.elvor.dogs.ui.ListAdapter

class FavouriteListAdapter(private val itemClickListener: (String, String?) -> Unit) :
    RecyclerView.Adapter<FavouriteListAdapter.ViewHolder>(), ListAdapter<FavouriteInfo> {

    override var items: List<FavouriteInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemWithInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favouriteInfo: FavouriteInfo) {
            binding.name.text =
                (if (favouriteInfo.subbreed != null) "${favouriteInfo.subbreed} ${favouriteInfo.breed}" else favouriteInfo.breed).capitalize()
            binding.info.text = binding.info.context.resources.getQuantityString(
                R.plurals.photos,
                favouriteInfo.photoCount,
                favouriteInfo.photoCount
            )
            binding.root.setOnClickListener {
                this@FavouriteListAdapter.itemClickListener(
                    favouriteInfo.breed,
                    favouriteInfo.subbreed
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWithInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

data class FavouriteInfo(val breed: String, val subbreed: String?, val photoCount: Int)