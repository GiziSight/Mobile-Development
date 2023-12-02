package com.example.gizisight.ui.fragment.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gizisight.data.UserRepository
import com.example.gizisight.util.SharedPrefManager
import okhttp3.MultipartBody

class CameraViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun predictImage(
        image: MultipartBody.Part
    ) =
        userRepository.predictImage(image)
}