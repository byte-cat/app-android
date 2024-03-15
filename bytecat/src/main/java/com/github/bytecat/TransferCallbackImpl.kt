package com.github.bytecat

open class TransferCallbackImpl : ITransferCallback.Stub() {

    override fun onStart(owner: CatParcel?, transferId: String?, totalSize: Long) {
    }

    override fun onTransfer(
        owner: CatParcel?,
        transferId: String?,
        transferSize: Long,
        totalSize: Long
    ) {
    }

    override fun onSuccess(owner: CatParcel?, transferId: String?) {
    }

    override fun onError(owner: CatParcel?, transferId: String?) {
    }
}