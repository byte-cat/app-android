// IByteCatService.aidl
package com.github.bytecat;

import com.github.bytecat.CatParcel;
import com.github.bytecat.ICallback;

interface IByteCatService {
    List<CatParcel> getCats();
    void sendMessage(in CatParcel toCat, String text);
    void setCallback(ICallback callback);
}