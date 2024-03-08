// IByteCatService.aidl
package com.github.bytecat;

import com.github.bytecat.CivetCat;
import com.github.bytecat.ICallback;

interface IByteCatService {
    List<CivetCat> getCats();
    void setCallback(ICallback callback);
}