package com.jn.glint

import android.app.Application

/**
 * Application class to initialize the [AppContainer].
 */
class GlintApplication : Application() {
    
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
