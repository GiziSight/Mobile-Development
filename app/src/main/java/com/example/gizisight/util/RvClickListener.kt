package com.example.gizisight.util

import android.view.View
import com.example.gizisight.data.remote.response.ArticleItem

interface RvClickListener {
    fun onItemClicked(view: View, data: ArticleItem)
}