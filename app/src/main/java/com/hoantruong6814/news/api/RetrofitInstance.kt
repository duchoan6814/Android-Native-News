package com.hoantruong6814.news.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.hoantruong6814.news.util.Constants.Companion.DOMAIN
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            val client = OkHttpClient.Builder().addInterceptor(logging).build();


            Retrofit.Builder().baseUrl(DOMAIN).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        }

        val api by lazy {
            retrofit.create(NewAPI::class.java);
        }
    }

}