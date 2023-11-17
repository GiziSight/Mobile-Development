package com.example.gizisight.ui

import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager

class IntroViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(
        email: String,
        sharedPrefManager: SharedPrefManager
    ) =
        userRepository.getUser(email, sharedPrefManager)

    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        userPref: SharedPrefManager,
        loadingDialog: LoadingDialog,

        ) =

        userRepository.loginUser(email, password, activity, userPref, loadingDialog)
}