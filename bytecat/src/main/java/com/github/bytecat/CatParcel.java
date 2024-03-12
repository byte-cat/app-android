package com.github.bytecat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.contact.Cat;

import java.util.Objects;

public class CatParcel extends Cat implements Parcelable {

    public CatParcel() {
        this("", "", "", 0, 0);
    }

    public CatParcel(Cat cat) {
        this(cat.getIp(), cat.getName(), cat.getSystem(), cat.getBroadcastPort(), cat.getMessagePort());
    }

    public CatParcel(@NonNull String ip, @NonNull String name, @NonNull String system, int broadcastPort, int messagePort) {
        super(ip, name, system, broadcastPort, messagePort);
    }

    protected CatParcel(Parcel in) {
        this(Objects.requireNonNull(in.readString()), Objects.requireNonNull(in.readString()),
                Objects.requireNonNull(in.readString()), in.readInt(), in.readInt());
    }

    public static final Creator<CatParcel> CREATOR = new Creator<CatParcel>() {
        @Override
        public CatParcel createFromParcel(Parcel in) {
            return new CatParcel(in);
        }

        @Override
        public CatParcel[] newArray(int size) {
            return new CatParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(getIp());
        dest.writeString(getName());
        dest.writeString(getSystem());
        dest.writeInt(getBroadcastPort());
        dest.writeInt(getMessagePort());
    }

    public void readFromParcel(Parcel in) {
        this.setIp(Objects.requireNonNull(in.readString()));
        this.setName(Objects.requireNonNull(in.readString()));
        this.setSystem(Objects.requireNonNull(in.readString()));
        this.setBroadcastPort(in.readInt());
        this.setMessagePort(in.readInt());
    }
}
