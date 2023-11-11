package com.example.gizisight.di

import com.example.gizisight.data.UserRepository
import com.example.gizisight.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiServices()
        return UserRepository.getInstance(apiService)
    }
}