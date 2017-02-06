package com.training.onit.restapi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snowleopard on 9/12/2016.
 */
public class Ticket implements Parcelable {
    public String _id;
    public String type;
    public String route_name;
    public String org_stop;
    public String dst_stop;
    public int quantity;

    protected Ticket(Parcel in) {
        _id = in.readString();
        type = in.readString();
        route_name = in.readString();
        org_stop = in.readString();
        dst_stop = in.readString();
        quantity = in.readInt();
    }

    public boolean isPeriod() {
        return "pass".equals(type);
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(type);
        dest.writeString(route_name);
        dest.writeString(org_stop);
        dest.writeString(dst_stop);
        dest.writeInt(quantity);
    }
}
