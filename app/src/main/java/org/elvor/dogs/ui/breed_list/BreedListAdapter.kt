package org.elvor.dogs.ui.breed_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.dogs.R
import org.elvor.dogs.databinding.ItemWithInfoBinding
import org.elvor.dogs.ui.ListAdapter

class BreedListAdapter(
    private val withSubbreedsClickListener: (String) -> Unit,
    private val withoutSubbreedsClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<BreedListAdapter.ViewHolder>(), ListAdapter<BreedInfo> {

    override var items: List<BreedInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemWithInfoBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(breedInfo: BreedInfo) {
            binding.name.text = breedInfo.name.capitalize()
            if (breedInfo.subbreedsCount > 0) {
                binding.root.setOnClickListener {
                    this@BreedListAdapter.withSubbreedsClickListener(
                        breedInfo.name
                    )
                }
                binding.info.text = context.resources.getQuantityString(
                    R.plurals.subbreeds,
                    breedInfo.subbreedsCount,
                    breedInfo.subbreedsCount
                )
            } else {
                binding.root.setOnClickListener {
                    this@BreedListAdapter.withoutSubbreedsClickListener(
                        breedInfo.name
                    )
                }
                binding.info.text = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWithInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

data class BreedInfo(val name: String, val subbreedsCount: Int)