package com.github.bytecat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.github.bytecat.message.FileReqDataParcel
import com.github.bytecat.message.MessageParcel
import com.github.bytecat.utils.Registry
import java.lang.ref.WeakReference

object ByteCatManager {

    private const val TAG = "ByteCatManager"

    private var iCatService: IByteCatService? = null

    private val catServiceCallback = object : CallbackImpl() {
        override fun onReady(cat: CatParcel?) {
            cat ?: return
            catCallbackRegistry.forEach {
                it.onMyCatReady(cat)
            }
        }

        override fun onCatAdd(cat: CatParcel?) {
            cat ?: return
            catCallbackRegistry.forEach {
                it.onCatAdd(cat)
            }
        }

        override fun onCatRemove(cat: CatParcel?) {
            cat ?: return
            catCallbackRegistry.forEach {
                it.onCatRemove(cat)
            }
        }

        override fun onCatUpdate(cat: CatParcel?) {
            cat ?: return
            catCallbackRegistry.forEach {
                it.onCatUpdate(cat)
            }
        }

        override fun onCatMessage(cat: CatParcel?, message: MessageParcel<*>?) {
            cat ?: return
            message ?: return
            messageCallbackRegistry.forEach {
                it.onMessageReceived(cat, message)
            }
        }
    }
    val catCallbackRegistry = Registry<CatCallback>()
    val messageCallbackRegistry = Registry<MessageCallback>()

    private val fileSendCallback = object : TransferCallbackImpl() {
        override fun onStart(owner: CatParcel?, transferId: String?, totalSize: Long) {
            owner ?: return
            transferId ?: return
            sendCallbackRegistry.forEach {
                it.onStart(owner, transferId, totalSize)
            }
        }

        override fun onTransfer(
            owner: CatParcel?,
            transferId: String?,
            transferSize: Long,
            totalSize: Long
        ) {
            owner ?: return
            transferId ?: return
            sendCallbackRegistry.forEach {
                it.onTransfer(owner, transferId, transferSize, totalSize)
            }
        }

        override fun onSuccess(owner: CatParcel?, transferId: String?) {
            owner ?: return
            transferId ?: return
            sendCallbackRegistry.forEach {
                it.onSuccess(owner, transferId)
            }
        }

        override fun onError(owner: CatParcel?, transferId: String?) {
            owner ?: return
            transferId ?: return
            sendCallbackRegistry.forEach {
                onError(owner, transferId)
            }
        }

    }
    val sendCallbackRegistry = Registry<FileTransferCallback>()

    private var connectCallback: ConnectCallback? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val catService = IByteCatService.Stub.asInterface(service)
            catService.setCallback(catServiceCallback)
            catService.setFileSendCallback(fileSendCallback)

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

    fun sendText(cat: CatParcel, text: String) {
        iCatService?.sendText(cat, text)
    }

    fun sendFileRequest(cat: CatParcel, uri: Uri) {
        iCatService?.sendFileRequestByUri(cat, uri)
    }

    fun rejectFileRequest(cat: CatParcel, fileReq: FileReqDataParcel) {
        iCatService?.rejectFileRequest(cat, fileReq)
    }

    fun acceptFileRequest(cat: CatParcel, fileReq: FileReqDataParcel) {
        iCatService?.acceptFileRequest(cat, fileReq)
    }

    interface ConnectCallback {
        fun onConnected(catService: IByteCatService)
        fun onDisconnected()
    }

    interface CatCallback {
        fun onMyCatReady(cat: CatParcel)
        fun onCatAdd(cat: CatParcel)
        fun onCatRemove(cat: CatParcel)
        fun onCatUpdate(cat: CatParcel)
    }

    interface MessageCallback {
        fun onMessageReceived(cat: CatParcel, message: MessageParcel<*>)
    }

    interface FileTransferCallback {
        fun onStart(owner: CatParcel, transferId: String, totalSize: Long) {}

        fun onTransfer(
            owner: CatParcel,
            transferId: String,
            transferSize: Long,
            totalSize: Long
        ) {}

        fun onSuccess(owner: CatParcel, transferId: String) {}

        fun onError(owner: CatParcel, transferId: String) {}
    }

}