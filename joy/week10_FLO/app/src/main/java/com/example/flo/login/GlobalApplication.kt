package com.example.flo.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.example.flo.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}