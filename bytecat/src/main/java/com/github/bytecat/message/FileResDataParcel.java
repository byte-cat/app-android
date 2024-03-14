package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.FileResponseData;

import java.util.Objects;

public class FileResDataParcel extends FileResponseData implements Parcelable {

    public FileResDataParcel(@NonNull String responseId, int responseCode, int streamPort, @NonNull String acceptCode) {
        super(responseId, responseCode, streamPort, acceptCode);
    }

    public FileResDataParcel() {
        this("", 0, 0, "");
    }

    public FileResDataParcel(FileResponseData fileRes) {
        this(fileRes.getResponseId(), fileRes.getResponseCode(), fileRes.getStreamPort(), fileRes.getAcceptCode());
    }

    protected FileResDataParcel(Parcel in) {
        this(Objects.requireNonNull(in.readString()), in.readInt(), in.readInt(), Objects.requireNonNull(in.readString()));
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
        dest.writeString(getResponseId());
        dest.writeInt(getResponseCode());
        dest.writeInt(getStreamPort());
        dest.writeString(getAcceptCode());
    }

    public void readFromParcel(@NonNull Parcel in) {
        setResponseId(Objects.requireNonNull(in.readString()));
        setResponseCode(in.readInt());
        setStreamPort(in.readInt());
        setAcceptCode(Objects.requireNonNull(in.readString()));
    }

}
