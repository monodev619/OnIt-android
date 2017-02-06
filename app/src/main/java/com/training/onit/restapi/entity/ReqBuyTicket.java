package com.training.onit.restapi.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Snowleopard on 9/12/2016.
 */
public class ReqBuyTicket extends BaseReq {

    public String email;

    @SerializedName("route_name")
    @Expose
    public String routeName;

    public String type;

    public float amount;

    public int quantity;

    public String cardname;

    public String cardnum;

    public String exp_mn;

    public String exp_yr;

    public String cvv;

    public ReqBuyTicket(String szEmail, String szRouteName, boolean isPeriod, int nQuantity, float amount,
                        String szNameOnCard, String szCardNum, String szCVV, String szExpMonth, String szExpYear) {
        super();

        this.email = szEmail;
        this.routeName = szRouteName;
        this.type = isPeriod ? "pass" : "fair";
        this.quantity = nQuantity;
        this.amount = amount;
        this.cardname = szNameOnCard;
        this.cardnum = szCardNum;
        this.exp_mn = szExpMonth;
        this.exp_yr = szExpYear;
        this.cvv = szCVV;
    }
}
