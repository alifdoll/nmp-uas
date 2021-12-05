package com.ubaya.protectcare23.api

import com.ubaya.protectcare23.data.Checks
import com.ubaya.protectcare23.data.Location
import com.ubaya.protectcare23.data.User

// Class untuk memanggil API
class ApiService(
    private val httpService: VolleyService
): ApiServiceInterface {

    private val BASE_API_AUTH = "https://ubaya.fun/native/160419122/auth/"
    private val BASE_API = "https://ubaya.fun/native/160419122/api/"

    companion object {
        @Volatile
        private var instance: ApiService? = null

        fun getInstance(volleyService: VolleyService): ApiService =
            instance ?: synchronized(this) {
                instance ?: ApiService(volleyService).apply {
                    instance = this
                }
            }
    }

    override suspend fun logIn(username: String, password: String): String {
        val response = httpService.post(
            BASE_API_AUTH + "login.php",
            body = mapOf(
                "user_name" to username,
                "password" to password
            )
        )

        val result = response.data.getString("result")
        if(result == "success") {
            return response.data.getString("token")
        }else throw UnexpectedResponseException("Success was expected but $result was given")
    }

    override suspend fun getLocation(): MutableList<Location> {
        val response = httpService.get(
            BASE_API + "location/location.php"
        )

        val addresses = mutableListOf<Location>()
        val result = response.data.getString("result")
        if(result == "success") {
            val locations = response.data.getJSONArray("data")
            for (i in 0 until locations.length()) {
                val addr = Location(
                    locations.getJSONObject(i).getString("id"),
                    locations.getJSONObject(i).getString("name")
                )
                addresses.add(addr)
            }
            return addresses
        }else throw UnexpectedResponseException("Success was expected but $result was given")
    }

    override suspend fun getUser(token: String): User {
        val response = httpService.post(
            BASE_API_AUTH + "user.php",
            body = mapOf(
                "token" to token
            )
        )

        val result = response.data.getString("result")
        if (result == "success") {
            val data = response.data.getJSONObject("data")
            return User(
                data.getString("name"),
                data.getInt("vaccinated")
            )
        }else throw UnexpectedResponseException("Success was expected but $result was given")
    }

    override suspend fun getCheckHistory(token: String): MutableList<Checks> {
        val response = httpService.post(
            BASE_API + "check/checks.php",
            body = mapOf("token" to token)
        )

        val checks = mutableListOf<Checks>()

        val result = response.data.getString("result")

        if (result == "success") {
            val data = response.data.getJSONArray("data")
            for (i in 0 until data.length()) {
                val obj = data.getJSONObject(i)
               val check = Checks(
                   obj.getString("loc_id"),
                   obj.getString("name"),
                   obj.getString("check_in"),
                   if(obj.getString("check_out").isNullOrEmpty()) ""
                   else obj.getString("check_out")
               )
                checks.add(check)
            }
            return checks
        } else throw UnexpectedResponseException("Success was expected but $result was given")
    }

    override suspend fun checkIn(token: String, location_id: String): Boolean {
        val response = httpService.post(
            BASE_API + "check/in.php",
            body = mapOf(
                "token" to token,
                "location_id" to location_id
            )
        )

        val result = response.data.getString("result")

        return result == "success"
    }


    override suspend fun checkOut(token: String, location_id: String): Boolean {
        val response = httpService.post(
            BASE_API + "check/out.php",
            body = mapOf(
                "token" to token,
                "location_id" to location_id
            )
        )

        val result = response.data.getString("result")
//        throw UnexpectedResponseException("success was expected but $result was given")

        return result == "success"
    }

    override suspend fun checkStatus(token: String): Checks {
        val response = httpService.post(
            BASE_API + "check/checkin.php",
            body = mapOf(
                "token" to token
            )
        )

        val result = response.data.getString("result")
        if (result == "success") {
            val data = response.data.getJSONObject("data")
            val cheks = Checks(
                loc_id = data.getString("loc_id"),
                loc_name = data.getString("name"),
                checkIn = data.getString("check_in"),
                checkOut = data.getString("check_out")
            )
            return cheks
        } else if(result == "success belum") {
            return Checks(
                loc_id = "",
                loc_name = "",
                checkIn =  "",
                checkOut =  ""
            )
        } else throw UnexpectedResponseException("Success was expected but $result was given")
    }
}