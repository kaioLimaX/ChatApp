package com.example.chatapp.data.models

import android.media.Image
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Usuario(
    val nome : String,
    val uid : String,
    val email : String,
    val image: String
) : Parcelable
