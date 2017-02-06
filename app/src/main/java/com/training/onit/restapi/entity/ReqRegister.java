package com.training.onit.restapi.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Snowleopard on 9/10/2016.
 */
public class ReqRegister {
    @Expose
    public String email;

    @Expose
    public String password;

    public ReqRegister(String szEmail, String szPwd) {
        this.email = szEmail;
        this.password = szPwd;
    }
}
