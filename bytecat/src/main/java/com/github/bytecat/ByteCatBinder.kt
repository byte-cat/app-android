package com.github.bytecat

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.file.DefaultFile
import com.github.bytecat.file.TransferCallback
import com.github.bytecat.file.UriFile
import com.github.bytecat.message.FileReqDataParcel
import com.github.bytecat.message.FileResDataParcel
import com.github.bytecat.message.Message
import com.github.bytecat.message.MessageBox
import com.github.bytecat.message.TextDataParcel
import com.github.bytecat.message.MessageParcel
import com.github.bytecat.protocol.data.Data
import com.github.bytecat.protocol.data.FileRequestData
import com.github.bytecat.protocol.data.FileResponseData
import com.github.bytecat.protocol.data.TextData
import java.util.LinkedList

class ByteCatBinder(private val context: Context) : IByteCatService.Stub() {

    companion object {
        private const val TAG = "ByteCatBinder"
    }

    private lateinit var myCat: CatParcel

    private val catCallback by lazy {
        object : ByteCat.Callback {
            override fun onReady(myCat: Cat) {
                this@ByteCatBinder.myCat = CatParcel(myCat)
                if (callback != null) {
                    callback?.onReady(this@ByteCatBinder.myCat)
                }
            }
        }
    }

    private val byteCat by lazy { AndroidByteCat(context).apply { setCallback(catCallback) } }

    private val catParcels = HashMap<String, CatParcel>()

    private val messageCallback = object : MessageBox.Callback {
        override fun onMessageReceived(cat: Cat, message: Message<*>) {
            val catParcel = catParcels[cat.ip] ?: return
            val msgParcel = MessageParcel.fromReceive<Parcelable>(message, object : MessageParcel.Converter {
                override fun <T : Parcelable> convert(data: Data): T {
                    return when(data) {
                        is TextData -> {
                            TextDataParcel(data)
                        }
                        is FileRequestData -> {
                            FileReqDataParcel(data)
                        }
                        is FileResponseData -> {
                            FileResDataParcel(data)
                        }
                        else -> throw IllegalArgumentException("Unrecognized type: ${data::class.java.name}")
                    } as T
                }
            })

            callback?.onCatMessage(catParcel, msgParcel)
        }
    }

    private val catBookCallback by lazy {
        object : CatBook.Callback {
            override fun onContactAdd(cat: Cat) {
                val catParcel = CatParcel(cat)
                catParcels[cat.ip] = catParcel

                MessageBox.obtain(cat).registerCallback(messageCallback)

                callback?.onCatAdd(catParcel)
            }

            override fun onContactRemove(cat: Cat) {
                val catParcel = catParcels.remove(cat.ip) ?: return

                MessageBox.obtain(cat).unregisterCallback(messageCallback)

                callback?.onCatRemove(catParcel)
            }

            override fun onContactUpdate(cat: Cat) {
                val catParcel = CatParcel(cat)
                catParcels[cat.ip] = catParcel
                callback?.onCatUpdate(catParcel)
            }
        }
    }
    private var callback: ICallback? = null

    private val fileSendCallback = object : TransferCallback {

        var outSendCallback: ITransferCallback? = null

        override fun onError(owner: Cat, transferId: String, e: Throwable) {
            val catParcel = catParcels[owner.ip] ?: return
            outSendCallback?.onSuccess(catParcel, transferId)
        }

        override fun onStart(owner: Cat, transferId: String, totalSize: Long) {
            val catParcel = catParcels[owner.ip]
            outSendCallback?.onStart(catParcel, transferId, totalSize)
            Log.d(TAG, "onStart totalSize=$totalSize")
        }

        override fun onSuccess(owner: Cat, transferId: String, md5: String) {
            val catParcel = catParcels[owner.ip] ?: return
            outSendCallback?.onSuccess(catParcel, transferId)
        }

        override fun onTransfer(
            owner: Cat,
            transferId: String,
            transferSize: Long,
            totalSize: Long
        ) {
            val catParcel = catParcels[owner.ip] ?: return
            outSendCallback?.onTransfer(catParcel, transferId, transferSize, totalSize)
        }
    }

    fun startup() {
        byteCat.catBook.registerCallback(catBookCallback)
        byteCat.fileSendManager.registerCallback(fileSendCallback)
        byteCat.startup()
    }

    fun shutdown() {
        callback = null
        byteCat.catBook.unregisterCallback(catBookCallback)
        byteCat.fileSendManager.unregisterCallback(fileSendCallback)
        byteCat.shutdown()
    }

    override fun getCats(): List<CatParcel> {
        return byteCat.catBook.cats.map {
            CatParcel(it)
        }
    }

    override fun sendText(toCat: CatParcel?, text: String?) {
        toCat ?: return
        if (text.isNullOrEmpty()) {
            return
        }
        byteCat.sendText(toCat, text)
    }

    override fun sendFileRequestByPath(toCat: CatParcel?, file: String?) {
        toCat ?: return
        if (file.isNullOrEmpty()) {
            return
        }
        byteCat.sendFileRequest(toCat, DefaultFile(file))
    }

    override fun sendFileRequestByUri(toCat: CatParcel?, uri: Uri?) {
        toCat ?: return
        uri ?: return
        byteCat.sendFileRequest(toCat, UriFile(context, uri))
    }

    override fun rejectFileRequest(toCat: CatParcel?, fileReq: FileReqDataParcel?) {
        toCat ?: return
        fileReq ?: return
        byteCat.rejectFileRequest(toCat, fileReq)
    }
    override fun acceptFileRequest(toCat: CatParcel?, fileReq: FileReqDataParcel?) {
        toCat ?: return
        fileReq ?: return
        byteCat.acceptFileRequest(toCat, fileReq)
    }

    override fun setCallback(callback: ICallback?) {
        this.callback = callback
        if (::myCat.isInitialized) {
            callback?.onReady(myCat)
        }
    }

    override fun setFileSendCallback(callback: ITransferCallback?) {
        this.fileSendCallback.outSendCallback = callback
    }

    override fun setFileReceiveCallback(callback: ITransferCallback?) {
    }

}