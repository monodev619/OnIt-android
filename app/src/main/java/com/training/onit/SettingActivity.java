package com.training.onit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.btnAccount).setOnClickListener(this);
        findViewById(R.id.btnTos).setOnClickListener(this);
        findViewById(R.id.btnAppInfo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAccount:
                startActivity(new Intent(mContext, AccountSettingActivity.class));
                break;

            case R.id.btnTos:
                startActivity(new Intent(mContext, TosActivity.class));
                break;

            case R.id.btnAppInfo:
                startActivity(new Intent(mContext, AppInfoActivity.class));
                break;
        }
    }
}
