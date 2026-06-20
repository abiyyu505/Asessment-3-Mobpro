package com.abiyyu0003.asessment2mobpro.network

import com.abiyyu0003.asessment2mobpro.model.Catatan
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://raw.githubusercontent.com/abiyyu505/studynotes-api/main/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface CatatanApiService {

    @GET("catatan.json")
    suspend fun getCatatan(): List<Catatan>
}

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        MoshiConverterFactory.create(moshi)
    )
    .build()

object CatatanApi {

    val service: CatatanApiService by lazy {
        retrofit.create(CatatanApiService::class.java)
    }
}