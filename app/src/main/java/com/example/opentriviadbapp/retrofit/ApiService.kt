package com.example.opentriviadbapp.retrofit

import com.example.opentriviadbapp.model.CategoryCountResponse
import com.example.opentriviadbapp.model.CategoryListResponse
import com.example.opentriviadbapp.model.QuestionResponse
import com.example.opentriviadbapp.model.TokenResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {

    @GET("api_category.php")
    fun getCategoryList(): Observable<CategoryListResponse>

    @GET("api_count.php")
    fun getQuestionCountList(@Query("category") id: Int): Observable<CategoryCountResponse>

    //maybe not used
    @GET("api.php?amount=1")
    fun getQuestionDefault(): Observable<QuestionResponse>

    @GET
    fun getQuestion(@Url url: String): Observable<QuestionResponse>

    @GET("api_token.php?command=request")
    fun getToken(): Observable<TokenResponse>

    @GET("api_token.php?command=reset")
    fun resetToken(@Query("token") token: String): Observable<TokenResponse>

}