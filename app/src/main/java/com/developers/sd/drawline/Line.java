package com.developers.sd.drawline;

import android.os.Parcel;
import android.os.Parcelable;

public class Line implements Parcelable{

    private String title;
    private int status;

    public Line(String title, int status) {
        this.title = title;
        this.status = status;
    }

    public Line(Parcel in) {
        title = in.readString();
        status = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeInt(status);
    }

    public static final Parcelable.Creator<Line> CREATOR = new Parcelable.Creator<Line>() {
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
