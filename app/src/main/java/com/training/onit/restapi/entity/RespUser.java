package com.training.onit.restapi.entity;

import com.google.gson.annotations.Expose;
import com.training.onit.restapi.entity.BaseResp;

/**
 * Created by Snowleopard on 9/10/2016.
 */
public class RespUser extends BaseResp {

    public static class UserInfo {

        @Expose
        public String id;

        @Expose
        public String token;

        @Expose
        public String email;
    }

    @Expose
    public UserInfo user;
}
