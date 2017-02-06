package com.training.onit.restapi.entity;

import com.google.gson.annotations.Expose;
import com.training.onit.AppGlobal;

/**
 * Created by Snowleopard on 9/12/2016.
 */
public class BaseReq {
    public String token;
    public BaseReq() {
        this.token = AppGlobal.authToken;
    }
}
