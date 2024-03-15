package com.github.bytecat

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.github.bytecat.contact.Cat
import com.github.bytecat.handler.IHandler
import com.github.bytecat.platform.ISystemInfo
import com.github.bytecat.utils.IDebugger
import java.io.File

class AndroidByteCat(private val context: Context) : ByteCat() {

    companion object {
        private const val TAG = "AndroidByteHole"
    }

    override val debugger: IDebugger by lazy { AndroidDebugger() }

    override val handler: IHandler by lazy { AndroidHandler() }

    override val systemInfo: ISystemInfo by lazy {
        object : ISystemInfo {
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

    override var outputDir: File = context.getExternalFilesDir("download")!!
        get() {
            if (!field.exists()) {
                field.mkdirs()
            }
            return field
        }
        set(value) {
            field = value
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

        override fun cancel(task: Runnable) {
            handler.removeCallbacks(task)
        }

        override fun post(task: Runnable) {
            handler.post(task)
        }

        override fun post(delay: Long, task: Runnable) {
            handler.postDelayed(task, delay)
        }

        override fun shutdown() {
            thread.looper.quit()
        }

    }

    class AndroidDebugger : IDebugger {
        override fun onBroadcastReady() {
            Log.d(TAG, "onBroadcastReady")
        }

        override fun onBroadcastReceived(fromIp: String, data: ByteArray) {
            Log.d(TAG, "onBroadcastReceived fromIp=$fromIp data=${String(data)}")
        }

        override fun onContactAdd(cat: Cat) {
            Log.d(TAG, "onContactAdd ${cat.name}")
        }

        override fun onContactRemove(cat: Cat) {
            Log.d(TAG, "onContactRemove ${cat.name}")
        }

        override fun onMessageReady() {
            Log.d(TAG, "onMessageReady")
        }

        override fun onMessageReceived(fromIp: String, data: ByteArray) {
            Log.d(TAG, "onMessageReceived fromIp=$fromIp data=${String(data)}")
        }
    }

}