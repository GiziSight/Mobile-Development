package com.example.gizisight.ui.akg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gizisight.data.remote.response.Bagian3Item
import com.example.gizisight.databinding.ItemInformasiGiziBinding

class Informasi3Adapter(private val listBagian: List<Bagian3Item>) : RecyclerView.Adapter<Informasi3Adapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ItemInformasiGiziBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Bagian3Item, position: Int) {
            binding.apply {
                tvValue.text = data.nilai
                tvTipeInformasi.text = data.name
                if (position < 9) {
                    textView6.text = "${(position +1)}  " // Display empty space
                } else {
                    textView6.text = (position + 1).toString() // Display position + 1
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemInformasiGiziBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listBagian.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = listBagian[position]
        holder.bind(dataItem, position)


    }
}