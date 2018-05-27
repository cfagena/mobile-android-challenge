package com.test.amaro.amarotest.model;

/**
 * Created by cfage on 23/05/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size implements Parcelable {

    @SerializedName("available")
    @Expose
    public Boolean available;

    @SerializedName("size")
    @Expose
    public String size;

    @SerializedName("sku")
    @Expose
    public String sku;


    protected Size(Parcel in) {
        byte availableVal = in.readByte();
        available = availableVal == 0x02 ? null : availableVal != 0x00;
        size = in.readString();
        sku = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (available == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (available ? 0x01 : 0x00));
        }
        dest.writeString(size);
        dest.writeString(sku);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() {
        @Override
        public Size createFromParcel(Parcel in) {
            return new Size(in);
        }

        @Override
        public Size[] newArray(int size) {
            return new Size[size];
        }
    };
}