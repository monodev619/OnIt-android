package com.training.onit.restapi.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Snowleopard on 9/12/2016.
 */
public class ReqLogin {
    @Expose
    public String email;

    @Expose
    public String password;

    @Expose
    public boolean isSocial;

    public ReqLogin(String szEmail, String szPwd, boolean isSocial) {
        this.email = szEmail;
        this.password = szPwd;
        this.isSocial = isSocial;
    }
}
