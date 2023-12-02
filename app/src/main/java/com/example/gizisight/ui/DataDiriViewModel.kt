package com.example.gizisight.ui

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.ui.registrasi.RegistrasiActivity
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager

class DataDiriViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(
        email: String,
        sharedPrefManager: SharedPrefManager
    ) =
        userRepository.getUser(email, sharedPrefManager)

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
        userPref : SharedPrefManager
    ) =
        userRepository.registerUser(email, username, password, gender, age, height, weight, activity, loadingDialog, userPref)
}