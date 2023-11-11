package com.example.gizisight.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.SharedPrefManager

class ProfileViewModel(private val  userRepository : UserRepository) : ViewModel() {

    fun getUser(
        token: String,
        sharedPrefManager: SharedPrefManager
    ) =
        userRepository.getUser(token, sharedPrefManager)
}