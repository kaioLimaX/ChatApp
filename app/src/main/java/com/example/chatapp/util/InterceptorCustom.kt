package com.example.chatapp.util

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class InterceptorCustom : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // request -> Requisição
        //response -> Resposta
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader(
            "Authorization",
            "Bearer ${Constants.API_KEY}"
        )

        try {
            // Prossiga com a execução da solicitação
            val result = chain.proceed(requestBuilder.build())
            return result
        } catch (e: IOException) {
            // Lidar com exceções, se necessário
            e.printStackTrace()
            throw e
        }
    }

}