package com.boscatov.stackoverflow

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface StackApi {
    @GET("questions")
    fun get(@Query("pagesize") pageSize: Int, @Query("order") order: String,
            @Query("sort") sort: String, @Query("tagged") tagged: String,
            @Query("site") site: String): Call<StackPage>

    companion object Factory {
        fun create(): StackApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.stackexchange.com/2.2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(StackApi::class.java)
        }
    }
}