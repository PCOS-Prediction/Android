package com.pc.pcosprediction

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    companion object {
        val BASE_URL = "<YOUR-BASE-URL>"

        fun getClient(): Retrofit? {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
