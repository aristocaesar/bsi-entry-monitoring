package com.bsi.entrymonitoring.api

import com.bsi.entrymonitoring.model.User
import com.bsi.entrymonitoring.model.Employee
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("sibsi/api_login/getsearch/{username}/{password}")
    fun login(
        @Path("username") username: String,
        @Path("password") password: String
    ): Call<List<User>>

    @GET("sibsi/api_employee/getsearchwithdoor/{door}")
    fun getDoor(
        @Path("door") doorID: String,
    ): Call<List<Employee>>
}