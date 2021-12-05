package com.ubaya.protectcare23.api

import com.ubaya.protectcare23.data.Checks
import com.ubaya.protectcare23.data.Location
import com.ubaya.protectcare23.data.User


//Untuk Menambahkan Method Pemanggilan API disini
interface ApiServiceInterface {
    suspend fun logIn(username: String, password: String): String
    suspend fun getLocation(): MutableList<Location>
    suspend fun getUser(token: String): User
    suspend fun getCheckHistory(token: String): MutableList<Checks>
    suspend fun checkIn(token: String, location_id: String): Boolean
    suspend fun checkOut(token: String, location_id: String): Boolean
    suspend fun checkStatus(token: String): Checks
}