package com.example.chatapp.view.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.adapters.TabAdapter
import com.example.chatapp.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarNavegacaoLogin()
    }

    private fun inicializarNavegacaoLogin() {
        val tabLayout = binding.tabAuth
        val viewPager = binding.viewPagerMenuAuth

        //adapter
        val abas = listOf("Login In", "Sign Up")
        viewPager.adapter = TabAdapter(
            abas,
            supportFragmentManager,
            lifecycle
        )
        tabLayout.isTabIndicatorFullWidth = true

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = abas[position]

        }.attach()

    }
}