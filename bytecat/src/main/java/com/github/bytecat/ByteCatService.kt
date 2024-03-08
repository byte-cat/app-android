package com.github.bytecat

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ByteCatService : Service() {

    private val byteCatBinder: ByteCatBinder by lazy {
        ByteCatBinder()
    }

    override fun onCreate() {
        super.onCreate()
        byteCatBinder.startup()
    }

    override fun onBind(intent: Intent): IBinder {
        return byteCatBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        byteCatBinder.shutdown()
    }

}