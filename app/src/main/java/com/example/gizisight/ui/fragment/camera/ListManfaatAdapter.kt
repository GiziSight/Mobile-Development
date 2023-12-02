package com.example.gizisight.ui.fragment.camera

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gizisight.data.remote.response.KandunganItem
import com.example.gizisight.data.remote.response.ManfaatItem
import com.example.gizisight.databinding.ItemInformasiGiziBinding
import com.example.gizisight.databinding.ItemManfaatKhasiatBinding

class ListManfaatAdapter(private val listManfaat: List<ManfaatItem>) : RecyclerView.Adapter<ListManfaatAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ItemManfaatKhasiatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ManfaatItem, position: Int) {
            binding.apply {
                tvTipeInformasi.text = data.khasiat
                binding.tvNo.text = (position +1).toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemManfaatKhasiatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listManfaat.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = listManfaat[position]
        holder.bind(dataItem, position)


    }
}