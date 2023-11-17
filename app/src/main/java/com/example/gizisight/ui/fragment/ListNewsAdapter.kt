package com.example.gizisight.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.data.News
import com.example.gizisight.data.remote.response.ArticleItem
import com.example.gizisight.databinding.ItemNewsBinding
import com.example.gizisight.util.RvClickListener

class ListNewsAdapter(private val listNews: List<ArticleItem>) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {
    var listener: RvClickListener? = null
    class ListViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imageNews)
        val tvName: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDesc)

        fun bind(data: ArticleItem, listener: RvClickListener?) {
            binding.apply {
                Glide.with(itemView.context).load(data.gambar).centerCrop().into(imgPhoto)
                tvName.text = data.judul
                tvDescription.text = data.deskripsi
                imgPhoto.setOnClickListener{
                    listener?.onItemClicked(it, data)
                }
                btnBaca.setOnClickListener{
                    listener?.onItemClicked(it, data)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listNews.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val (name, description, photo) = listNews[position]
//        holder.tvName.text = name
//        holder.tvDescription.text = description
//        Glide.with(holder.itemView.context).load(photo).into(holder.imgPhoto)
        val dataItem = listNews[position]
        holder.bind(dataItem, listener)


    }
}