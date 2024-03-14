package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.TextData;

public class TextDataParcel implements Parcelable {
    public String text;

    public TextDataParcel() {
    }

    public TextDataParcel(TextData textData) {
        this(textData.getText());
    }

    public TextDataParcel(String text) {
        this.text = text;
    }

    protected TextDataParcel(Parcel in) {
        text = in.readString();
    }

    public static final Creator<TextDataParcel> CREATOR = new Creator<TextDataParcel>() {
        @Override
        public TextDataParcel createFromParcel(Parcel in) {
            return new TextDataParcel(in);
        }

        @Override
        public TextDataParcel[] newArray(int size) {
            return new TextDataParcel[size];
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
