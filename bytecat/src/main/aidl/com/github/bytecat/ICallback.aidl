// ICallback.aidl
package com.github.bytecat;

import com.github.bytecat.CivetCat;

interface ICallback {
    void onReady(inout CivetCat cat);
    void onCatAdd(inout CivetCat cat);
    void onCatRemove(inout CivetCat cat);
    void onCatUpdate(inout CivetCat cat);
}