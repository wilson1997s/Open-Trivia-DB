package com.example.opentriviadbapp.retrofit

import com.example.opentriviadbapp.Constant
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
            .baseUrl(Constant.BASE_URL)
            .build()

        this.apiService = retrofit.create(ApiService::class.java)
    }

    fun getCategoryList(): Observable<CategoryListResponse> {
        return apiService.getCategoryList()
    }

    fun getQuestionCountList(id: Int): Observable<CategoryCountResponse> {
        return apiService.getQuestionCountList(id)
    }

    fun getQuestion(token: String, category: String, difficulty: String, type: String): Observable<QuestionResponse> {
        var url = "${Constant.BASE_URL}${Constant.API_GET_1_QUESTION}&${Constant.JSON_TOKEN}=$token"
        if (!category.equals(Constant.DEFAULT)) {
            url = "$url&${Constant.JSON_CATEGORY}=$category"
        }
        if (!difficulty.equals(Constant.DEFAULT)) {
            url = "$url&${Constant.JSON_DIFFICULTY}=$difficulty"
        }
        if (!type.equals(Constant.DEFAULT)) {
            url = "$url&${Constant.JSON_TYPE}=$type"
        }
        return apiService.getQuestion(url)
    }

    fun getToken(): Observable<TokenResponse> {
        return apiService.getToken()
    }

    fun resetToken(token: String): Observable<TokenResponse> {
        return apiService.resetToken(token)
    }


}