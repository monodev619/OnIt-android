package com.training.onit.restapi;

import com.training.onit.restapi.entity.BaseReq;
import com.training.onit.restapi.entity.BaseResp;
import com.training.onit.restapi.entity.ReqBuyTicket;
import com.training.onit.restapi.entity.ReqConsumeTicket;
import com.training.onit.restapi.entity.ReqLogin;
import com.training.onit.restapi.entity.ReqRegister;
import com.training.onit.restapi.entity.ReqSearchRoute;
import com.training.onit.restapi.entity.RespBuyTicket;
import com.training.onit.restapi.entity.RespMyTickets;
import com.training.onit.restapi.entity.RespRouteList;
import com.training.onit.restapi.entity.RespUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Snowleopard on 9/8/2016.
 */
public interface RestApiService {

    @POST("user/signup")
    Call<RespUser> signup(@Body ReqRegister userInfo);

    @POST("user/signin")
    Call<RespUser> signin(@Body ReqLogin userInfo);

    @POST("route/getRouteSuggestions")
    Call<RespRouteList> searchRoute(@Body ReqSearchRoute searchRoute);

    @POST("trans/buyticket")
    Call<RespBuyTicket> buyTicket(@Body ReqBuyTicket buyTicket);

    @POST("trans/validateticket")
    Call<BaseResp> consumeTicket(@Body ReqConsumeTicket consumeTicket);

    @GET("trans/tickets")
    Call<RespMyTickets> getMyTickets(@Query("token") String token);
}
