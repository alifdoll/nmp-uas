package com.ubaya.protectcare23.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.ActivityAuthBinding
import com.ubaya.protectcare23.di.Injection
import com.ubaya.protectcare23.ui.home.MainActivity
import com.ubaya.protectcare23.utils.Global
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = applicationContext.getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE)
        val jwt = sharedPref.getString("token", "")
        lifecycleScope.launch {
            if (!jwt.isNullOrEmpty()) {
                val act = MainActivity::class.java
                startActivity(Intent(this@AuthActivity, act))
                this@AuthActivity.finish()
            }
        }

        apiService = Injection.provideService(this@AuthActivity)
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val username = binding.username.editText?.text.toString()
                val password = binding.password.editText?.text.toString()

                when {
                    binding.username.editText?.text!!.isEmpty() -> binding.username.editText!!.error = "Tidak Boleh Kosong"
                    binding.password.editText?.text!!.isEmpty() -> binding.password.editText!!.error = "Tidak Boleh Kosong"
                    else -> {
                        try {
                            val token = apiService.logIn(username, password)
                            sharedPref.edit().putString("token", token).apply()

                        } catch (e: Exception) {
                            Toast.makeText(this@AuthActivity, "Log In Error", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}