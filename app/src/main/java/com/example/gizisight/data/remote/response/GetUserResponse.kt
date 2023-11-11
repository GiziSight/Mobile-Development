package com.example.gizisight.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("username")
	val username :String,

	@field:SerializedName("height")
	val height : Int
)
