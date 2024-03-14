package com.github.bytecat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.bytecat.protocol.data.TextData;

import java.util.Objects;

public class TextDataParcel extends TextData implements Parcelable {

    public TextDataParcel(@NonNull String text) {
        super(text);
    }

    public TextDataParcel(TextData textData) {
        this(textData.getText());
    }

    public TextDataParcel() {
        this("");
    }

    protected TextDataParcel(Parcel in) {
        this(Objects.requireNonNull(in.readString()));
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
        dest.writeString(getText());
    }

    public void readFromParcel(@NonNull Parcel in) {
        setText(Objects.requireNonNull(in.readString()));
    }

}
