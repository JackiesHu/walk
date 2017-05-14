package com.buxingzhe.pedestrian.walk;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by chinaso on 2017/5/14.
 */

public class HourStep implements Serializable, Parcelable {

    private int stepCount;
    private String hour;

    public HourStep(){

    }
    protected HourStep(Parcel in) {
        stepCount = in.readInt();
        hour = in.readString();
    }

    public static final Creator<HourStep> CREATOR = new Creator<HourStep>() {
        @Override
        public HourStep createFromParcel(Parcel in) {
            return new HourStep(in);
        }

        @Override
        public HourStep[] newArray(int size) {
            return new HourStep[size];
        }
    };

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stepCount);
        dest.writeString(this.hour);
    }
}
