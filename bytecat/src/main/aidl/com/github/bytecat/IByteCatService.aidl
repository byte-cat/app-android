// IByteCatService.aidl
package com.github.bytecat;

import android.net.Uri;
import com.github.bytecat.CatParcel;
import com.github.bytecat.ICallback;

interface IByteCatService {
    List<CatParcel> getCats();
    void sendMessage(in CatParcel toCat, String text);
    void sendFileRequestByPath(in CatParcel toCat, String file);
    void sendFileRequestByUri(in CatParcel toCat, in Uri uri);
    void setCallback(ICallback callback);
}