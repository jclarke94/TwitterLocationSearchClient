package com.servedonline.bellandroidassignment.activities.Entity;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;


public class Coordinates implements Parcelable {
    private String type;
    private double[] coordinates;

    protected Coordinates(Parcel in) {
        type = in.readString();
        coordinates = in.createDoubleArray();
    }

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeDoubleArray(coordinates);
    }
}
