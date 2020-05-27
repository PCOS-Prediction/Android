package com.pc.pcosprediction

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {
    @POST("predict_api")
    fun predict(@Body mlinput: MLModel): Call<PredictionResponse>
}