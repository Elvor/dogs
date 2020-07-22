package org.elvor.dogs.ui.subbreed_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.dogs.databinding.ItemSubbreedBinding

class SubbreedListAdapter(private val itemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<SubbreedListAdapter.ViewHolder>() {

    private var subbreedsData: List<String> = emptyList()

    fun setData(subbreedData: List<String>) {
        this.subbreedsData = subbreedData
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSubbreedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subbreed: String) {
            binding.name.text = subbreed
            binding.root.setOnClickListener{this@SubbreedListAdapter.itemClickListener(subbreed)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSubbreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = subbreedsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subbreedsData[position])
    }
}