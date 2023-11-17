package com.example.gizisight.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("article")
	val article: List<ArticleItem>
)

data class ArticleItem(

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("gambar")
	val gambar: String
)
