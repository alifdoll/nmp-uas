package com.ubaya.proteccare23.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ubaya.protectcare23.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}