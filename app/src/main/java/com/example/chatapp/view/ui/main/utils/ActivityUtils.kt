package com.example.chatapp.view.ui.main.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class ActivityUtils {

    companion object {
        // Function to go to Specific Activity
        fun goToActivity(context: Context, activityClass: Class<*>) {
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
        }

        fun goToActivity(context: Context,activityClass: Class<*>, extras: Bundle? = null) {
            val intent = Intent(context, activityClass)

            // extras intent
            if (extras != null) {
                intent.putExtras(extras)
            }


        }

        }

    }
