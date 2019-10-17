package com.androsgames.vksdktest

import android.app.Application
import com.androsgames.vksdktest.ui.SearchActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            SearchActivity.startFrom(this@BaseApplication)
        }
    }

}