package com.example.chatapp.view.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.example.chatapp.view.ui.main.utils.ActivityUtils
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            ActivityUtils.goToActivity(this,AuthActivity::class.java)
        }


    }
}