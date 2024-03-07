package com.github.bytecat

import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.github.bytecat.contact.Contact
import com.github.bytecat.handler.IHandler
import com.github.bytecat.platform.IPlatform

class AndroidByteCat : ByteCat() {

    companion object {
        private const val TAG = "AndroidByteHole"
    }

    override val debugger: IDebugger by lazy { AndroidDebugger() }

    override val handler: IHandler by lazy { AndroidHandler() }

    override val platform: IPlatform by lazy {
        object : IPlatform {
            override val system: String
                get() = "Android ${Build.VERSION.RELEASE}"
            override val systemUserName: String
                get() = if (catName?.isNotEmpty() == true) {
                    catName!!
                } else {
                    "${Build.BRAND} ${Build.MODEL}"
                }

        }
    }

    private var catName: String? = null

    fun setCatName(catName: String) {
        this.catName = catName
    }

    class AndroidHandler : IHandler {

        private val thread by lazy { HandlerThread("ByteHoleThread") }

        private val handler by lazy { Handler(thread.looper) }

        init {
            thread.start()
        }

        override fun post(task: Runnable) {
            handler.post(task)
        }

    }

    class AndroidDebugger : IDebugger {
        override fun onBroadcastReady() {
            Log.d(TAG, "onBroadcastReady")
        }

        override fun onBroadcastReceived(fromIp: String, data: ByteArray) {
            Log.d(TAG, "onBroadcastReceived fromIp=$fromIp data=${String(data)}")
        }

        override fun onContactAdd(contact: Contact) {
            Log.d(TAG, "onContactAdd ${contact.name}")
        }

        override fun onContactRemove(contact: Contact) {
            Log.d(TAG, "onContactRemove ${contact.name}")
        }

        override fun onMessageReady() {
            Log.d(TAG, "onMessageReady")
        }

        override fun onMessageReceived(fromIp: String, data: ByteArray) {
            Log.d(TAG, "onMessageReceived fromIp=$fromIp data=${String(data)}")
        }
    }

}