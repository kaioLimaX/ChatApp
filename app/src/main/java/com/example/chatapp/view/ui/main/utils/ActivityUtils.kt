package com.example.chatapp.view.ui.main.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

class ActivityUtils {

    companion object {
        // Function to go to Specific Activity
        fun goToActivity(context: Context, activityClass: Class<*>) {
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
        }
        fun goToActivity(context: Context, activityClass: Class<*>,condition: Boolean) {
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }

        }
        fun goToActivity(context: Context,activityClass: Class<*>, extras: Bundle? = null) {
            val intent = Intent(context, activityClass)

            // Adiciona os extras à intent, se fornecidos
            if (extras != null) {
                intent.putExtras(extras)
            }

            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }
        }
    }
}