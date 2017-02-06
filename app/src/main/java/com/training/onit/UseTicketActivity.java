package com.training.onit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.training.onit.restapi.bean.Ticket;
import com.training.onit.restapi.entity.BaseResp;
import com.training.onit.restapi.entity.ReqConsumeTicket;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UseTicketActivity extends BaseActivity implements View.OnClickListener {

    TextView tvRouteName, tvStartPos, tvEndPos, tvTicketCount;
    int m_nTicketCount;
    Ticket m_ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_ticket);

        tvRouteName = (TextView) findViewById(R.id.tvRouteName);
        tvTicketCount = (TextView) findViewById(R.id.tvTicketCount);
        tvStartPos = (TextView) findViewById(R.id.tvStartPos);
        tvEndPos = (TextView) findViewById(R.id.tvEndPos);

        findViewById(R.id.btnIncrease).setOnClickListener(this);
        findViewById(R.id.btnDecrease).setOnClickListener(this);
        findViewById(R.id.btnConfirmUse).setOnClickListener(this);

        m_ticket = getIntent().getParcelableExtra("ticket");
        if ( m_ticket == null) {
            finish();
            return;
        }

        tvRouteName.setText(m_ticket.route_name);
        tvStartPos.setText(m_ticket.org_stop);
        tvEndPos.setText(m_ticket.dst_stop);

        m_nTicketCount = 1;
        tvTicketCount.setText(m_nTicketCount + "");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnIncrease:
                if ( m_ticket.isPeriod() || m_ticket.quantity > m_nTicketCount ) {
                    m_nTicketCount++;
                    tvTicketCount.setText(m_nTicketCount + "");
                }
                break;

            case R.id.btnDecrease:
                if ( m_nTicketCount > 1 ) {
                    m_nTicketCount--;
                    tvTicketCount.setText(m_nTicketCount + "");
                }
                break;

            case R.id.btnConfirmUse:
                if ( !m_ticket.isPeriod() ) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to recover used ticket!")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    consumeTicket(m_nTicketCount);
                                }
                            })
                            .setCancelText("Cancel")
                            .show();

                }
                else
                    finish();
                break;
        }
    }

    private void consumeTicket(int nTicketCount) {
        Call<BaseResp> call = AppGlobal.apiService.consumeTicket(new ReqConsumeTicket(AppGlobal.user_email, m_ticket.route_name, m_nTicketCount));
        call.enqueue(new Callback<BaseResp>() {

            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                hideProgress();
                int statusCode = response.code();
                BaseResp resp = response.body();
                if ( resp != null && !resp.hasError ) {
                    showToast(m_nTicketCount + " tickets has been consumed.");
                    finish();
                }
                else {
                    showError("Failed to use tickets");
                }
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                showError("Exception to use tickets");
                hideProgress();
            }
        });
        showProgress();
    }
}
