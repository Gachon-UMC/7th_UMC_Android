package com.example.flo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/join")
    fun signUp(@Body user : User) : Call<AuthResponse>

    @POST("/login")
    fun logIn(@Body loginRequest: LoginRequest) : Call<AuthResponse>
}