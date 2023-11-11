package com.example.gizisight.ui.login

import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        userPref: SharedPrefManager,
        loadingDialog: LoadingDialog,

        ) =

        userRepository.loginUser(email, password, activity, userPref, loadingDialog)
}