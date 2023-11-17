package com.example.gizisight.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.SharedPrefManager

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser(
        email: String,
        sharedPrefManager: SharedPrefManager
    ) =
        userRepository.getUser(email, sharedPrefManager)

    fun getArticle(
        query: String,
    ) =
        userRepository.getArticle(query)
}