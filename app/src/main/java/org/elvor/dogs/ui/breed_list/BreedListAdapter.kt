package org.elvor.dogs.ui.breed_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.dogs.R
import org.elvor.dogs.databinding.ItemBreedBinding
class BreedListAdapter(
    private val withSubbreedsClickListener: (String) -> Unit,
    private val withoutSubbreedsClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<BreedListAdapter.ViewHolder>() {

    private var breedsData: List<Pair<String, Int>> = emptyList()

    fun setData(breedCollection: List<Pair<String, Int>>) {
        this.breedsData = breedCollection
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemBreedBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(breedData: Pair<String, Int>) {
            binding.name.text = breedData.first
            if (breedData.second > 0) {
                binding.root.setOnClickListener {
                    this@BreedListAdapter.withSubbreedsClickListener(
                        breedData.first
                    )
                }
                binding.subbreeds.text = context.getString(
                    R.string.subbreeds,
                    breedData.second.toString()
                )
            } else {
                binding.root.setOnClickListener {
                    this@BreedListAdapter.withoutSubbreedsClickListener(
                        breedData.first
                    )
                }
                binding.subbreeds.text = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = breedsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(breedsData[position])
    }
}