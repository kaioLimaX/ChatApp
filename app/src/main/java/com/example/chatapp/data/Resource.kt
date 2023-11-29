package com.example.chatapp.data

import java.lang.Exception

sealed class Resource<out T> {
    data class Sucess<out T>(val result: T): Resource<T>()
    data class Failure(val e: Exception): Resource<Nothing>()

}