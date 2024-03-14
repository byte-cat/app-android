package com.github.bytecat.vm

import android.os.Parcelable
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.ByteCatManager
import com.github.bytecat.CatParcel
import com.github.bytecat.message.TextDataParcel
import com.github.bytecat.message.MessageParcel

class MessageBoxVM : ViewModel(), ByteCatManager.CatCallback, ByteCatManager.MessageCallback {

    private val recentMessages = mutableStateMapOf<CatParcel, MessageParcel<*>>()

    private fun newMessage(catParcel: CatParcel, messageParcel: MessageParcel<*>) {
        when (messageParcel.data) {
            is TextDataParcel -> {
                recentMessages[catParcel] = messageParcel
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

}