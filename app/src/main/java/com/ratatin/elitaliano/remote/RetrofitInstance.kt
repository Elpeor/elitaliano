package com.ratatin.elitaliano.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://ratatinprogramin-production.up.railway.app")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService:: class.java)
    }
}