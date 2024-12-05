package com.example.flo.data.remote

import android.util.Log
import com.example.flo.login.LogInView
import com.example.flo.login.LoginRequest
import com.example.flo.signup.SignUpView
import com.example.flo.data.entities.User
import com.example.flo.getRetrofit
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var logInView: LogInView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLogInView(logInView: LogInView) {
        this.logInView = logInView
    }

    fun signUp(user : User) {
        val signUpService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/RESPONSE", response.toString())
                Log.d("SIGNUP/RESPONSE_CODE", response.code().toString())
                Log.d("SIGNUP/RESPONSE_BODY", response.body()?.toString() ?: "Response body is null")

                val resp: AuthResponse = response.body()!!

                if (resp == null || !resp.isSuccess) {
                    signUpView.onSignUpFailure()
                    return
                }

                signUpView.onSignUpSuccess()
            }


            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
                signUpView.onSignUpFailure()
            }
        })

        Log.d("SignUpActivity", "All Finished")
    }

    fun logIn(user : User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        val loginRequest = LoginRequest(
            email = user.email,
            password = user.password
        )

        authService.logIn(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                Log.d("LOGIN/RESPONSE_CODE", response.code().toString())
                Log.d("LOGIN/RESPONSE_BODY", response.body()?.toString() ?: "Response body is null")
                Log.d("LOGIN/REQUEST_BODY", Gson().toJson(user))

                val resp: AuthResponse = response.body()!!

                if (resp == null || !resp.isSuccess) {
                    logInView.onLogInFailure()
                    return
                }

                logInView.onLogInSuccess(resp.isSuccess)
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
                logInView.onLogInFailure()
            }
        })

        Log.d("LoginActivity", "All Finished")
    }
}