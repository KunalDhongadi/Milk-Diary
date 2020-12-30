package com.kunaldhongadi.milkydiary;

import android.os.Parcel;
import android.os.Parcelable;

public class Days implements Parcelable {

    public final String date;
    public final String day;
    public final int imageId;

    public Days(String date, String day, int imageId) {
        this.date = date;
        this.day = day;
        this.imageId = imageId;
    }


    protected Days(Parcel in) {
        date = in.readString();
        day = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<Days> CREATOR = new Creator<Days>() {
        @Override
        public Days createFromParcel(Parcel in) {
            return new Days(in);
        }

        @Override
        public Days[] newArray(int size) {
            return new Days[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(day);
        parcel.writeInt(imageId);
    }
}
