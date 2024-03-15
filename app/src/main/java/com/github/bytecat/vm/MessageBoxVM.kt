package com.github.bytecat.vm

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.ByteCatManager
import com.github.bytecat.CatParcel
import com.github.bytecat.message.FileReqDataParcel
import com.github.bytecat.message.FileResDataParcel
import com.github.bytecat.message.TextDataParcel
import com.github.bytecat.message.MessageParcel

class MessageBoxVM : ViewModel(), ByteCatManager.CatCallback, ByteCatManager.MessageCallback, ByteCatManager.FileTransferCallback {

    companion object {
        private const val TAG = "MessageBoxVM"
    }

    private val recentMessages = mutableStateMapOf<CatParcel, MessageParcel<*>>()

    private fun newMessage(catParcel: CatParcel, messageParcel: MessageParcel<*>) {
        when (messageParcel.data) {
            is TextDataParcel, is FileReqDataParcel -> {
                recentMessages[catParcel] = messageParcel
            }
            is FileResDataParcel -> {
                val fileRes = messageParcel.data as FileResDataParcel
                Log.d(TAG, "newMessage ${fileRes.isAccepted}")
                if (fileRes.isAccepted) {
                    recentMessages[catParcel] = messageParcel
                }
            }
        }
    }

    fun hasTextMessage(catParcel: CatParcel): Boolean {
        return recentMessages[catParcel]?.data?.let {
            it is TextDataParcel
        } ?: false
    }

    fun getMessageDataOrNull(catParcel: CatParcel): Parcelable? {
        return recentMessages[catParcel]?.data
    }

    fun markAsRead(catParcel: CatParcel) {
        recentMessages.remove(catParcel)
    }

    override fun onMyCatReady(cat: CatParcel) {}

    override fun onCatAdd(cat: CatParcel) {
    }

    override fun onCatRemove(cat: CatParcel) {
        markAsRead(cat)
    }

    override fun onCatUpdate(cat: CatParcel) {
    }

    override fun onMessageReceived(cat: CatParcel, message: MessageParcel<*>) {
        newMessage(cat, message)
    }

    override fun onSuccess(owner: CatParcel, transferId: String) {
        markAsRead(owner)
    }

}