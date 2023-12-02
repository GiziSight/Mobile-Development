package com.example.gizisight.data

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.gizisight.data.remote.response.*
import com.example.gizisight.data.remote.retrofit.ApiService
import com.example.gizisight.errorJson
import com.example.gizisight.ui.HomeActivity
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    fun registerUser(
        email: String,
        username: String,
        password: String,
        gender: String,
        age: String,
        height: String,
        weight: String,
        activity: Activity,
        loadingDialog: LoadingDialog,
        userPref: SharedPrefManager
    ) {
        Log.d("fasdzz", age)
        val client = apiService.registerUser(email, username, password, gender, age, height, weight)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    loadingDialog.dismiss()
                    val responseBody = response.body()
                    Log.d("REfefee1", responseBody.toString())
                    if (responseBody != null) {
                        userPref.setEmail(email)
                        Toast.makeText(
                            activity,
                            responseBody.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(activity, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity.finish()
                        activity.startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(activity, errorBody, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    loadingDialog.dismiss()
                    if (response.code() == 422) {
                        if (errorBodyString != null) {
                            try {
                                val errorJson = JSONObject(errorBodyString)
                                val errorsArray = errorJson.getJSONArray("errors")
                                if (errorsArray.length() > 0) {
                                    val firstError = errorsArray.getJSONObject(0)
                                    val errorMsg = firstError.getString("msg")
                                    // Now you can use errorMsg as the error message
                                    Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        Log.d("REfefee2", response.message())
                    } else {
                        Toast.makeText(activity, errorBodyString, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
                Log.d("REfefee", t.toString())
            }


        })
    }

    fun getUser(
        email: String,
        userPref: SharedPrefManager
    ) : LiveData<Result<GetUserResponse>> {
        val user = MediatorLiveData<Result<GetUserResponse>>()


        user.postValue(Result.Loading)
        val client = apiService.getUser(email)
        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("REfefee1", responseBody.toString())
                    if (responseBody != null) {
                        userPref.setUser(responseBody.user)
                        user.value = Result.Success(responseBody)
                        Log.d("URED1231", responseBody.user.toString())
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("URED1231", errorBody.toString())
                        user.postValue(Result.Error("Error ${response.code()}"))
                    }
                }
            }


            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.d("REfefee", t.toString())
                user.postValue(Result.Error(t.message.toString()))
            }
        })

        return user;
    }

    fun getArticle(
        query: String,
    ) : LiveData<Result<ArticleResponse>> {
        val article = MediatorLiveData<Result<ArticleResponse>>()

        article.postValue(Result.Loading)
        val client = apiService.getArticle(query)
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("REf2efee2", responseBody.toString())
                    if (responseBody != null) {
                        article.value = Result.Success(responseBody)
                        Log.d("REf2efee2", responseBody.toString())
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("REf2efee2", errorBody.toString())
                        article.postValue(Result.Error("Error ${response.code()}"))
                    }
                }
            }


            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Log.d("REf2efee2", t.toString())
                article.postValue(Result.Error(t.message.toString()))
            }
        })

        return article;
    }

    fun predictImage(
        image: MultipartBody.Part
    ) : LiveData<Result<UploadResponse>> {
        val result = MediatorLiveData<Result<UploadResponse>>()
        result.postValue(Result.Loading)
        val client = apiService.predictImage(image)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("REfefe2e1", responseBody.toString())
                    if (responseBody != null) {
                        result.value = Result.Success(responseBody)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("URE2D1231", errorBody.toString())
                        result.postValue(Result.Error("Error ${response.code()}"))
                    }
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message.toString()))
            }
        })

        return result
    }


    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        userPref: SharedPrefManager,
        loadingDialog: LoadingDialog,
    ) {
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loadingDialog.dismiss()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userPref.setEmail(email)
                        val intent = Intent(activity, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity.finish()
                        activity.startActivity(intent)
                    }
                } else {
                    loadingDialog.dismiss()
                    val errorBody = response.errorBody()?.string()

                   errorJson(errorBody, activity)
                }

    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        loadingDialog.dismiss()
        Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
    }

})
}


companion object {
    @Volatile
    private var instance: UserRepository? = null
    fun getInstance(
        apiService: ApiService
    ): UserRepository =
        instance ?: synchronized(this) {
            instance ?: UserRepository(apiService)
        }.also { instance = it }
}
}