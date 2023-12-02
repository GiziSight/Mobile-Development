package com.example.gizisight.ui.registrasi

import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager

class RegistrasiViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registerUser(
        email: String,
        username: String,
        password: String,
        gender: String,
        age: String,
        height: String,
        weight: String,
        activity: RegistrasiActivity,
        loadingDialog: LoadingDialog,
        userPref: SharedPrefManager
    ) =
        userRepository.registerUser(email, username, password, gender, age, height, weight, activity, loadingDialog, userPref)
}