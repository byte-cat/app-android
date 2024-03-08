package com.github.bytecat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

object ByteCatManager {

    private var iCatService: IByteCatService? = null

    private var connectCallback: ConnectCallback? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val catService = IByteCatService.Stub.asInterface(service)
            connectCallback?.onConnected(catService)
            iCatService = catService
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iCatService = null
            connectCallback?.onDisconnected()
        }
    }

    fun connect(context: Context, callback: ConnectCallback) {
        if (iCatService != null) {
            return
        }
        this.connectCallback = callback
        val intent = Intent(context, ByteCatService::class.java)
//        context.startForegroundService(intent)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    interface ConnectCallback {
        fun onConnected(catService: IByteCatService)
        fun onDisconnected()
    }

}