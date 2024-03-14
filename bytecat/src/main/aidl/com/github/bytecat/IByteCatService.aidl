// IByteCatService.aidl
package com.github.bytecat;

import android.net.Uri;
import com.github.bytecat.CatParcel;
import com.github.bytecat.message.FileReqDataParcel;
import com.github.bytecat.message.FileResDataParcel;
import com.github.bytecat.ICallback;

interface IByteCatService {
    List<CatParcel> getCats();
    void sendText(in CatParcel toCat, String text);
    void sendFileRequestByPath(in CatParcel toCat, String file);
    void sendFileRequestByUri(in CatParcel toCat, in Uri uri);
    void rejectFileRequest(in CatParcel toCat, in FileReqDataParcel fileReq);
    void acceptFileRequest(in CatParcel toCat, in FileReqDataParcel fileReq);

    void setCallback(ICallback callback);
}