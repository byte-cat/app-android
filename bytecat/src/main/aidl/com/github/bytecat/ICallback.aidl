// ICallback.aidl
package com.github.bytecat;

import com.github.bytecat.CatParcel;
import com.github.bytecat.message.MessageParcel;

interface ICallback {
    void onReady(inout CatParcel cat);
    void onCatAdd(inout CatParcel cat);
    void onCatRemove(inout CatParcel cat);
    void onCatUpdate(inout CatParcel cat);
    void onCatMessage(inout CatParcel cat, inout MessageParcel message);
}