package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.Data;

import java.util.Objects;

public class MessageParcel<T extends Parcelable> implements Parcelable {

    public static <T extends Parcelable> MessageParcel<T> fromReceive(Message<? extends Data> message, Converter converter) {
        Parcelable parcelable = converter.convert(message.getData());
        return new MessageParcel(message.getType(), message.getTimestamp(), parcelable);
    }

    public interface Converter {
        <T extends Parcelable> T convert(@NonNull Data data);
    }

    public int type;
    public long timestamp;
    public T data;

    public MessageParcel() {
    }

    public MessageParcel(int type, long timestamp, T data) {
        this.type = type;
        this.timestamp = timestamp;
        this.data = data;
    }

    protected MessageParcel(Parcel in) {
        type = in.readInt();
        timestamp = in.readLong();
        String dataClzName = in.readString();
        try {
            data = in.readParcelable(Class.forName(Objects.requireNonNull(dataClzName)).getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeLong(timestamp);
        if (data != null) {
            dest.writeString(data.getClass().getName());
            dest.writeParcelable(data, flags);
        }
    }

    public void readFromParcel(Parcel in) {
        this.type = in.readInt();
        this.timestamp = in.readLong();
        String dataClzName = in.readString();
        if (dataClzName != null) {
            try {
                in.readParcelable(Class.forName(dataClzName).getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageParcel> CREATOR = new Creator<MessageParcel>() {
        @Override
        public MessageParcel createFromParcel(Parcel in) {
            return new MessageParcel(in);
        }

        @Override
        public MessageParcel[] newArray(int size) {
            return new MessageParcel[size];
        }
    };
}
