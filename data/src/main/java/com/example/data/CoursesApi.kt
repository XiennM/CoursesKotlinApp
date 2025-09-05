package com.example.data



import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CoursesApi {
    @GET
    suspend fun getCoursesRaw(@Url url: String): Response<ResponseBody>
}