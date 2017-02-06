package com.training.onit.restapi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snowleopard on 9/11/2016.
 */
public class OwnTicket implements Parcelable {
    public Route    route;
    public int      nCount;     // -1 : unlimited on this month

    public OwnTicket() {}
    public OwnTicket(Route route, int nCount) {
        this.route = route;
        this.nCount = nCount;
    }

    protected OwnTicket(Parcel in) {
        route = in.readParcelable(Route.class.getClassLoader());
        nCount = in.readInt();
    }

    public static final Creator<OwnTicket> CREATOR = new Creator<OwnTicket>() {
        @Override
        public OwnTicket createFromParcel(Parcel in) {
            return new OwnTicket(in);
        }

        @Override
        public OwnTicket[] newArray(int size) {
            return new OwnTicket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(route, flags);
        dest.writeInt(nCount);
    }
}
