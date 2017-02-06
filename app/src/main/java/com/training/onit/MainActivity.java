package com.training.onit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.training.onit.restapi.bean.OwnTicket;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnUseTicket).setOnClickListener(this);
        findViewById(R.id.btnBuyTicket).setOnClickListener(this);
        findViewById(R.id.btnInfo).setOnClickListener(this);
        findViewById(R.id.btnSetting).setOnClickListener(this);

        AppGlobal.s_ownTickets.add(new OwnTicket(AppGlobal.s_routes[2], 10));
        AppGlobal.s_ownTickets.add(new OwnTicket(AppGlobal.s_routes[3], -1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUseTicket:
                startActivity(new Intent(mContext, TicketListActivity.class));
                break;

            case R.id.btnBuyTicket:
                startActivity(new Intent(mContext, BuyTicketActivity.class));
                break;

            case R.id.btnInfo:
                startActivity(new Intent(mContext, InfoActivity.class));
                break;

            case R.id.btnSetting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }
}
