package com.example.opentriviadbapp.retrofit

import com.example.opentriviadbapp.Constant
import com.example.opentriviadbapp.model.CategoryCountResponse
import com.example.opentriviadbapp.model.CategoryListResponse
import com.example.opentriviadbapp.model.QuestionResponse
import com.example.opentriviadbapp.model.TokenResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {

    @GET(Constant.API_CATEGORY)
    fun getCategoryList(): Observable<CategoryListResponse>

    @GET
    fun getQuestion(@Url url: String): Observable<QuestionResponse>

    @GET(Constant.API_COUNT)
    fun getQuestionCountList(@Query(Constant.JSON_CATEGORY) id: Int): Observable<CategoryCountResponse>

    @GET(Constant.API_TOKEN + Constant.API_REQUEST)
    fun getToken(): Observable<TokenResponse>

    @GET(Constant.API_TOKEN + Constant.API_RESET)
    fun resetToken(@Query(Constant.JSON_TOKEN) token: String): Observable<TokenResponse>

}