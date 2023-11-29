package com.example.chatapp.view.ui.main.utils

import android.content.Context
import android.content.Intent

class ActivityUtils {

    companion object {
        // Function to go to Specific Activity
        fun goToActivity(context: Context, activityClass: Class<*>) {
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
        }
    }
}