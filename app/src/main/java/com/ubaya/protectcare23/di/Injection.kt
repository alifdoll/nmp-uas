package com.ubaya.protectcare23.di

import android.content.Context
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.api.VolleyService


//Karena class ApiService dependent dengan class volleyservice
//dan class volleyservice dependent dengan context
//Injection ini digunakan untuk menyediakan apiService

object Injection {
    fun provideService(context: Context): ApiService {
        val volleyService = VolleyService.getInstance(context)

        return ApiService.getInstance(volleyService)
    }
}