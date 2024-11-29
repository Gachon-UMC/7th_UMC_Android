package com.example.flo

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value = "isSuccess") val isSuccess : Boolean,
//    @SerializedName(value = "code") val code : Int,
    @SerializedName(value = "code") val code : String,
    @SerializedName(value = "message") val message : String,
    @SerializedName(value = "result") val result : Result?
)

//data class Result (
//
//    @SerializedName(value = "memberId") val memberId : Int,
//    @SerializedName(value = "createdAt") val createdAt : String,
//    @SerializedName(value = "updatedAt") val updatedAt : String,
//)

data class Result (

    @SerializedName(value = "memberId") var memberId : Int,
    @SerializedName(value = "accessToken") var accessToken : String
)