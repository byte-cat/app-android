package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.FileResponseData;

public class FileResDataParcel implements Parcelable {

    public String responseId;
    public int responseCode;
    public int streamPort;
    public String acceptCode;

    public FileResDataParcel() {}

    public FileResDataParcel(FileResponseData fileRes) {
        this.responseId = fileRes.getResponseId();
        this.responseCode = fileRes.getResponseCode();
        this.streamPort = fileRes.getStreamPort();
        this.acceptCode = fileRes.getAcceptCode();
    }

    protected FileResDataParcel(Parcel in) {
        this.responseId = in.readString();
        this.responseCode = in.readInt();
        this.streamPort = in.readInt();
        this.acceptCode = in.readString();
    }

    public static final Creator<FileResDataParcel> CREATOR = new Creator<FileResDataParcel>() {
        @Override
        public FileResDataParcel createFromParcel(Parcel in) {
            return new FileResDataParcel(in);
        }

        @Override
        public FileResDataParcel[] newArray(int size) {
            return new FileResDataParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(responseId);
        dest.writeInt(responseCode);
        dest.writeInt(streamPort);
        dest.writeString(acceptCode);
    }

    public void readFromParcel(@NonNull Parcel in) {
        this.responseId = in.readString();
        this.responseCode = in.readInt();
        this.streamPort = in.readInt();
        this.acceptCode = in.readString();
    }

}
