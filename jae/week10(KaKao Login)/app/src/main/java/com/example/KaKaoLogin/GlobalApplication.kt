package com.example.KaKaoLogin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "6105991f00a3351df851b7d5abb61162")
    }

}