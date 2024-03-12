package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.MessageData;

public class MessageDataParcel implements Parcelable {
    public String text;

    public MessageDataParcel() {
    }

    public MessageDataParcel(MessageData messageData) {
        this(messageData.getText());
    }

    public MessageDataParcel(String text) {
        this.text = text;
    }

    protected MessageDataParcel(Parcel in) {
        text = in.readString();
    }

    public static final Creator<MessageDataParcel> CREATOR = new Creator<MessageDataParcel>() {
        @Override
        public MessageDataParcel createFromParcel(Parcel in) {
            return new MessageDataParcel(in);
        }

        @Override
        public MessageDataParcel[] newArray(int size) {
            return new MessageDataParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(text);
    }

    public void readFromParcel(@NonNull Parcel in) {
        text = in.readString();
    }

}
