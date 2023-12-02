package com.example.gizisight.ui.fragment.camera

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gizisight.data.remote.response.KandunganItem
import com.example.gizisight.databinding.ItemInformasiGiziBinding

class ListKandunganAdapter(private val listKandungan: List<KandunganItem>) : RecyclerView.Adapter<ListKandunganAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ItemInformasiGiziBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: KandunganItem, position: Int) {
            binding.apply {
                tvValue.text = data.jumlah
                tvTipeInformasi.text = data.nutrisi
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

    override fun getItemCount(): Int = listKandungan.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataItem = listKandungan[position]
        holder.bind(dataItem, position)


    }
    // Helper function to convert dp to pixels
    private fun convertDpToPixel(dp: Float, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return dp * density
    }
}