package com.github.bytecat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import java.lang.ref.WeakReference

object ByteCatManager {

    private const val TAG = "ByteCatManager"

    private var iCatService: IByteCatService? = null

    private var connectCallback: ConnectCallback? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val catService = IByteCatService.Stub.asInterface(service)

            Log.d(TAG, "onServiceConnected=$connectCallback")

            connectCallback?.onConnected(catService)
            iCatService = catService
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iCatService = null
            connectCallback?.onDisconnected()
        }
    }

    private var connectedContextRef: WeakReference<Context>? = null

    fun connect(context: Context, callback: ConnectCallback) {
        Log.d(TAG, "connect iCatService=$iCatService")
        this.connectCallback = callback
        if (iCatService != null) {
            callback.onConnected(iCatService!!)
            return
        }

        val intent = Intent(context, ByteCatService::class.java)
//        context.startForegroundService(intent)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        connectedContextRef = WeakReference(context)
    }

    fun disconnect() {
        connectedContextRef?.get()?.unbindService(serviceConnection)
        connectedContextRef?.clear()
        connectedContextRef = null
    }

    interface ConnectCallback {
        fun onConnected(catService: IByteCatService)
        fun onDisconnected()
    }

}