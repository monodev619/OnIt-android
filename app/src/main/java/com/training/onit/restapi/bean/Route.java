package com.training.onit.restapi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snowleopard on 9/11/2016.
 */
public class Route implements Parcelable{
    public String routeId;
    public String routeName;
    public String startPos;
    public String endPos;
    public float price;
    public float monthPrice;

    public Route(){}
    public Route(String routeId, String routeName, String startPos, String endPos, float price, float monthPrice) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.startPos = startPos;
        this.endPos = endPos;
        this.price = price;
        this.monthPrice = monthPrice;
    }


    protected Route(Parcel in) {
        routeId = in.readString();
        routeName = in.readString();
        startPos = in.readString();
        endPos = in.readString();
        price = in.readFloat();
        monthPrice = in.readFloat();
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(routeId);
        dest.writeString(routeName);
        dest.writeString(startPos);
        dest.writeString(endPos);
        dest.writeFloat(price);
        dest.writeFloat(monthPrice);
    }
}

