package com.example.gizisight

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gizisight.data.UserRepository
import com.example.gizisight.di.Injection
import com.example.gizisight.ui.akg.AKGViewModel
import com.example.gizisight.ui.DataDiriViewModel
import com.example.gizisight.ui.IntroViewModel
import com.example.gizisight.ui.fragment.camera.CameraViewModel
import com.example.gizisight.ui.fragment.home.HomeViewModel
import com.example.gizisight.ui.fragment.profile.ProfileViewModel
import com.example.gizisight.ui.login.LoginViewModel
import com.example.gizisight.ui.registrasi.RegistrasiViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrasiViewModel::class.java)) {
            return RegistrasiViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(AKGViewModel::class.java)) {
            return AKGViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DataDiriViewModel::class.java)) {
            return DataDiriViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
            return IntroViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}