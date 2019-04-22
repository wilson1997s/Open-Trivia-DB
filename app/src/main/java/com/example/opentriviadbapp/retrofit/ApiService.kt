package com.example.opentriviadbapp.retrofit

import com.example.opentriviadbapp.model.CategoryCountResponse
import com.example.opentriviadbapp.model.CategoryListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("api_category.php")
    fun getCategoryList(): Observable<CategoryListResponse>

    @GET("api_count.php")
    fun getQuestionCountList(@Query("category") id: Int): Observable<CategoryCountResponse>

}