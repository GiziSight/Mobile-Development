package com.example.gizisight.data.remote.retrofit

import com.example.gizisight.data.remote.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //    endpoint Register
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(
        @Field("email") name: String,
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("birthdate") birthdate: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Call<MessageResponse>

    //    endpoint login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("getUser")
    fun getUser(
        @Query("email") email: String,
    ): Call<GetUserResponse>

    @GET("api/search")
    fun getArticle(
        @Query("query") query: String,
    ): Call<ArticleResponse>

    @Multipart
    @POST("upload")
    fun predictImage(
        @Part image: MultipartBody.Part,
    ): Call<UploadResponse>


}