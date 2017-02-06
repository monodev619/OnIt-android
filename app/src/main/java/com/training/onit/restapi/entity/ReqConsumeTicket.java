package com.training.onit.restapi.entity;

/**
 * Created by Snowleopard on 9/12/2016.
 */
public class ReqConsumeTicket extends BaseReq {
    public String email;
    public String route_name;
    public int quantity;

    public ReqConsumeTicket(String szEmail, String szRouteName, int nCount) {
        super();

        this.email = szEmail;
        this.route_name = szRouteName;
        this.quantity = nCount;
    }
}
