package com.example.opentriviadbapp.retrofit

import com.example.opentriviadbapp.model.CategoryCountResponse
import com.example.opentriviadbapp.model.CategoryListResponse
import com.example.opentriviadbapp.model.QuestionResponse
import com.example.opentriviadbapp.model.TokenResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiRepo {

    private val apiService: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://opentdb.com/")
            .build()

        this.apiService = retrofit.create(ApiService::class.java)
    }

    fun getCategoryList(): Observable<CategoryListResponse> {
        return apiService.getCategoryList()
    }

    fun getQuestionCountList(id: Int): Observable<CategoryCountResponse> {
        return apiService.getQuestionCountList(id)
    }

    fun getQuestion(url: String): Observable<QuestionResponse> {
        return apiService.getQuestion(url)
    }

    fun getToken(): Observable<TokenResponse> {
        return apiService.getToken()
    }

    fun resetToken(token: String): Observable<TokenResponse> {
        return apiService.resetToken(token)
    }


}