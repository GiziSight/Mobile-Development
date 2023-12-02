package com.example.gizisight.ui.akg

import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.SharedPrefManager

class AKGViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(
        email: String,
        sharedPrefManager: SharedPrefManager
    ) =
        userRepository.getUser(email, sharedPrefManager)
}