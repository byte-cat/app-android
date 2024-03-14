package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.FileRequestData;

import java.util.Objects;

public class FileReqDataParcel extends FileRequestData implements Parcelable {

    public FileReqDataParcel(@NonNull String requestId, @NonNull String name, long size, @NonNull String md5) {
        super(requestId, name, size, md5);
    }

    public FileReqDataParcel(){
        this("", "", 0L, "");
    }

    public FileReqDataParcel(FileRequestData fileReq) {
        this(fileReq.getRequestId(), fileReq.getName(), fileReq.getSize(), fileReq.getMd5());
    }

    protected FileReqDataParcel(Parcel in) {
        this(Objects.requireNonNull(in.readString()), Objects.requireNonNull(in.readString()), in.readLong(), Objects.requireNonNull(in.readString()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getRequestId());
        dest.writeString(getName());
        dest.writeLong(getSize());
        dest.writeString(getMd5());
    }

    public void readFromParcel(Parcel in) {
        setRequestId(in.readString());
        setName(in.readString());
        setSize(in.readLong());
        setMd5(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FileReqDataParcel> CREATOR = new Creator<FileReqDataParcel>() {
        @Override
        public FileReqDataParcel createFromParcel(Parcel in) {
            return new FileReqDataParcel(in);
        }

        @Override
        public FileReqDataParcel[] newArray(int size) {
            return new FileReqDataParcel[size];
        }
    };
}
