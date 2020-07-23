package org.elvor.dogs.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.dogs.R
import org.elvor.dogs.databinding.ItemBreedBinding
import org.elvor.dogs.databinding.ItemSubbreedBinding
import org.elvor.dogs.ui.ListAdapter

class FavouriteListAdapter(private val itemClickListener: (String, String?) -> Unit) :
    RecyclerView.Adapter<FavouriteListAdapter.ViewHolder>(), ListAdapter<BreedInfo> {

    override var items: List<BreedInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemBreedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(breedInfo: BreedInfo) {
            binding.name.text =
                (if (breedInfo.subbreed != null) "${breedInfo.subbreed} ${breedInfo.breed}" else breedInfo.breed).capitalize()
            binding.info.text = binding.info.context.resources.getQuantityString(R.plurals.photos, breedInfo.photoCount, breedInfo.photoCount)
            binding.root.setOnClickListener {
                this@FavouriteListAdapter.itemClickListener(
                    breedInfo.breed,
                    breedInfo.subbreed
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

data class BreedInfo(val breed: String, val subbreed: String?, val photoCount: Int)