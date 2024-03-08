package com.github.bytecat;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.bytecat.contact.Cat;

import java.util.Objects;

public class CivetCat implements Parcelable {

    private static final String TAG = "CivetCat";

    public String name;
    public String ip;
    public int broadcastPort;
    public int messagePort;

    public Platform platform;

    public CivetCat() {}

    public CivetCat(Cat cat) {
        this.name = cat.getName();
        this.ip = cat.getIpAddress();
        this.broadcastPort = cat.getBroadcastPort();
        this.messagePort = cat.getMessagePort();
        this.platform = cat.getPlatform();
    }

    protected CivetCat(Parcel in) {
        name = in.readString();
        ip = in.readString();
        broadcastPort = in.readInt();
        messagePort = in.readInt();
        platform = Platform.valueOf(in.readString());
    }

    public static final Creator<CivetCat> CREATOR = new Creator<CivetCat>() {
        @Override
        public CivetCat createFromParcel(Parcel in) {
            return new CivetCat(in);
        }

        @Override
        public CivetCat[] newArray(int size) {
            return new CivetCat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ip);
        dest.writeInt(broadcastPort);
        dest.writeInt(messagePort);
        dest.writeString(platform.name());
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        ip = in.readString();
        broadcastPort = in.readInt();
        messagePort = in.readInt();
        platform = Platform.valueOf(in.readString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CivetCat civetCat = (CivetCat) o;
        return Objects.equals(ip, civetCat.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
