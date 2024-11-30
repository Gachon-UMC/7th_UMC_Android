package com.example.flo

interface LoginView {
    fun onLogInSuccess(isSuccess: Boolean)
    fun onLoginFailure()
}