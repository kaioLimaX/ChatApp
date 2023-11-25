package com.example.chatapp.data

import java.lang.Exception

sealed class Resource<out R> {
    data class Sucess<out R>(val result: R): Resource<R>()
    data class Failure(val e: Exception): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}