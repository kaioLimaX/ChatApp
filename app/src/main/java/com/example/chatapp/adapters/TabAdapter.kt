package com.example.chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapp.view.ui.auth.fragments.LoginInFragment
import com.example.chatapp.view.ui.auth.fragments.SignInFragment

class TabAdapter(
    val abas : List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return abas.size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> return SignInFragment()

        }
        return LoginInFragment()
    }
}