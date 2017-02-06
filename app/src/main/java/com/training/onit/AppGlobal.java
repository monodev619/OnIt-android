package com.training.onit;

import android.content.Context;

import com.training.onit.restapi.RestApiService;
import com.training.onit.restapi.bean.OwnTicket;
import com.training.onit.restapi.bean.Route;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Snowleopard on 9/8/2016.
 */
public class AppGlobal {

    public static String BASE_URL = "https://on-it-sandbox.herokuapp.com/api/";
    public static RestApiService apiService;
    static
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(RestApiService.class);

        s_routes = new Route[] {
            new Route("1", "Route 501", "Chochrane", "Airdree", 2.3f, 99.0f),
            new Route("2", "Route 502", "Airdree", "Calgary", 2.35f, 99.0f),
            new Route("3", "Route 507", "Black Diamond", "Calgary", 2.45f, 99.0f),
            new Route("4", "Route 509", "Chochrane", "Calgary", 1.35f, 99.0f)
        };
        s_ownTickets = new ArrayList<OwnTicket>();
    }

    public static String user_email;
    public static String authId;
    public static String authToken;

    public static Route[] s_routes;
    public static ArrayList<OwnTicket> s_ownTickets;

    private static SweetAlertDialog s_pDialog;
    public static void showProgress(Context context) {
        if ( s_pDialog == null ) {
            s_pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            s_pDialog.setTitleText("Loading");
            s_pDialog.setCancelable(false);
            s_pDialog.show();
        }
    }

    public static void hideProgress() {
        if ( s_pDialog != null ) {
            s_pDialog.hide();
            s_pDialog = null;
        }
    }
}
