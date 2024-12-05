package com.example.flo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(user: User) {

        val signUpService = getRetrofit().create(AuthApi::class.java)

        signUpService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: AuthResponse = response.body()!!

                    Log.d("SIGNUP-RESPONSE", signUpResponse.toString())

                    if (!signUpResponse.isSuccess) {
                        signUpView.onSignUpFailure()
                        return
                    }

                    signUpView.onSignUpSuccess()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
                signUpView.onSignUpFailure()
            }
        })
    }


    fun login(user: User) {
        val loginService = getRetrofit().create(AuthApi::class.java)
        val loginRequest = LoginRequest(
            email = user.email,
            password = user.password
        )

        loginService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("Login-Success", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: AuthResponse = response.body()!!

                    if (loginResponse == null || !loginResponse.isSuccess) {
                        loginView.onLoginFailure()
                        return
                    }

                    loginView.onLogInSuccess(loginResponse.isSuccess)
                }
            }
            override fun onFailure(p0: Call<AuthResponse>, p1: Throwable) {
                Log.d("LOGIN/FAILURE", p1.message.toString())
                loginView.onLoginFailure()
            }
        })
    }
}