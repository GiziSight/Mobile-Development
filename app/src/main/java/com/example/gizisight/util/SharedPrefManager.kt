package com.example.gizisight.util

import android.content.Context
import com.example.gizisight.data.remote.response.LoginResponse
import com.example.gizisight.data.remote.response.User

class SharedPrefManager(private val context: Context) {
    private val preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    companion object {
        const val TOKEN_KEY = "token"
        const val NAME = "NAME"
        const val EMAIL = "EMAIL"
        const val AGE = "AGE"
        const val HEIGHT = "HEIGHT"
        const val WEIGHT = "WEIGHT"
        const val GENDER = "GENDER"
    }

    fun setToken(value: LoginResponse) {
        val editor = preferences.edit()
        editor.putString(TOKEN_KEY, value.token)
        editor.apply()
    }


    fun setUser(value: User?) {
        val editor = preferences.edit()
        editor.putString(NAME, value?.username)
        editor.putString(EMAIL, value?.email)
        editor.putInt(AGE, value?.age!!)
        editor.putInt(WEIGHT, value?.weight!!)
        editor.putInt(HEIGHT, value?.height!!)
        editor.putString(GENDER, value.gender)
        editor.apply()
    }

    fun setEmail(value: String) {
        val editor = preferences.edit()
        editor.putString(EMAIL, value)
        editor.apply()
    }


    fun getUser(): String? {
        return preferences.getString(TOKEN_KEY, "")
    }

    fun getName(): String? {
        return preferences.getString(NAME, "")
    }

    fun getEmail(): String? {
        return preferences.getString(EMAIL, "")
    }

    fun getAge(): Int? {
        return preferences.getInt(AGE, 0)
    }

    fun getHeight(): Int? {
        return preferences.getInt(HEIGHT, 0)
    }

    fun getWeight(): Int? {
        return preferences.getInt(WEIGHT, 0)
    }

    fun getGender(): String? {
        return preferences.getString(GENDER, "")
    }

    fun removeToken() {
        val editor = preferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }

    fun removeEmail() {
        val editor = preferences.edit()
        editor.remove(EMAIL)
        editor.apply()
    }

}