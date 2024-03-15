// ICallback.aidl
package com.github.bytecat;

import com.github.bytecat.CatParcel;
import com.github.bytecat.message.MessageParcel;

interface ITransferCallback {
    void onStart(inout CatParcel owner, String transferId, long totalSize);
    void onTransfer(inout CatParcel owner, String transferId, long transferSize, long totalSize);
    void onSuccess(inout CatParcel owner, String transferId);
    void onError(inout CatParcel owner, String transferId);
}