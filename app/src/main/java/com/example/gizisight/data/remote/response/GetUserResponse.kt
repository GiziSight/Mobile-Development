package com.example.gizisight.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("personalData")
	val personalData: PersonalData,

	@field:SerializedName("akgData")
	val akgData: AkgData,

	@field:SerializedName("user")
	val user: User
)

data class UserInfoItem(

	@field:SerializedName("nilai")
	val nilai: String,

	@field:SerializedName("name")
	val name: String
)

data class AkgData(

	@field:SerializedName("bagian3")
	val bagian3: List<Bagian3Item>,

	@field:SerializedName("bagian2")
	val bagian2: List<Bagian2Item>,

	@field:SerializedName("bagian1")
	val bagian1: List<Bagian1Item>
)

data class User(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("weight")
	val weight: Float,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("height")
	val height: Int
)

data class PersonalData(

	@field:SerializedName("userInfo")
	val userInfo: List<UserInfoItem>
)

data class Bagian3Item(

	@field:SerializedName("nilai")
	val nilai: String,

	@field:SerializedName("name")
	val name: String
)

data class Bagian2Item(

	@field:SerializedName("nilai")
	val nilai: String,

	@field:SerializedName("name")
	val name: String
)

data class Bagian1Item(

	@field:SerializedName("nilai")
	val nilai: String,

	@field:SerializedName("name")
	val name: String
)
