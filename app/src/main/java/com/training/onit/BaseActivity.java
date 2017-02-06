package com.training.onit;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Snowleopard on 9/8/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected void trace(String szMsg) { Log.w(getClass().getName(), szMsg); }
    protected void error(String szMsg) { Log.e(getClass().getName(), szMsg); }

    protected void showToast(String szMsg) { Toast.makeText(this, szMsg, Toast.LENGTH_SHORT).show(); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    private SweetAlertDialog s_pDialog;
    protected void showProgress() {
        if ( s_pDialog == null ) {
            s_pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            s_pDialog.setTitleText("Loading");
            s_pDialog.setCancelable(false);
            s_pDialog.show();
        }
    }

    protected void hideProgress() {
        if ( s_pDialog != null ) {
            s_pDialog.hide();
            s_pDialog = null;
        }
    }

    protected void showError(String szError) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(szError)
                .show();
    }
}
