package com.github.bytecat.vm

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.github.bytecat.ByteCatManager
import com.github.bytecat.CatParcel

class FileSendVM : ViewModel(), ByteCatManager.FileTransferCallback {

    companion object {
        private const val TAG = "FileSendVM"
    }

    private val progressMap = mutableStateMapOf<String, Float>()

    override fun onTransfer(
        owner: CatParcel,
        transferId: String,
        transferSize: Long,
        totalSize: Long
    ) {

        Log.d(TAG, "onTransfer !!! ${transferSize.toFloat() / totalSize}")
        progressMap[transferId] = transferSize.toFloat() / totalSize
    }

    fun getPosition(transferId: String): Float = progressMap[transferId] ?: 0F

}