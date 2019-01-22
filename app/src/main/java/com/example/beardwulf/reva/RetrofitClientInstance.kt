package com.example.beardwulf.reva

import com.example.beardwulf.reva.domain.Coordinate
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.Gson

/**
 * Connectie met de databank -> link.
 * Via retrofit
 */
class RetrofitClientInstance {
    private var retrofit: Retrofit? = null
   // private val BASE_URL = "http://projecten3studserver08.westeurope.cloudapp.azure.com"
   private val BASE_URL = "http://10.0.2.2:3000"
    fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            val gson = GsonBuilder()
                    .create()
            retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
        return retrofit
    }
}