package com.myapp.dreamystory.data.remote

import com.myapp.dreamystory.data.remote.response.DetailStoryResponse
import com.myapp.dreamystory.data.remote.response.ListStoryResponse
import com.myapp.dreamystory.data.remote.response.LoginResponse
import com.myapp.dreamystory.data.remote.response.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun dataRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun dataLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListStoryResponse

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?
    ): Call<DefaultResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): Call<ListStoryResponse>
}