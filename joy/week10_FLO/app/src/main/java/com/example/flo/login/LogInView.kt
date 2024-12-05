package com.example.flo.login

interface LogInView {
    fun onLogInSuccess(isSuccess: Boolean)
    fun onLogInFailure()
}