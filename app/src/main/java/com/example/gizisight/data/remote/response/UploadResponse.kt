package com.example.gizisight.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UploadResponse(

	@field:SerializedName("gizi")
	val gizi: Gizi,

	@field:SerializedName("manfaat")
	val manfaat: Manfaat,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("accuracy")
	val accuracy: Double,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class KandunganItem(

	@field:SerializedName("jumlah")
	val jumlah: String,

	@field:SerializedName("nutrisi")
	val nutrisi: String
) : Parcelable

@Parcelize
data class Manfaat(

	@field:SerializedName("manfaat")
	val manfaat: List<ManfaatItem>,

	@field:SerializedName("nama")
	val nama: String
) : Parcelable

@Parcelize
data class Gizi(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("kandungan")
	val kandungan: List<KandunganItem>
) : Parcelable

@Parcelize
data class ManfaatItem(

	@field:SerializedName("khasiat")
	val khasiat: String
) : Parcelable
