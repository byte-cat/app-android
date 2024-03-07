package com.github.bytecat

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.github.bytecat.channel.contact.Contact
import com.github.bytecat.channel.handler.IHandler

class AndroidByteCat : ByteCat() {

    companion object {
        private const val TAG = "com.github.bytecat.AndroidByteHole"
    }

    override val debugger: IDebugger by lazy { AndroidDebugger() }

    override val handler: IHandler by lazy { AndroidHandler() }

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