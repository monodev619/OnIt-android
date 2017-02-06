package com.training.onit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.training.onit.restapi.bean.Route;
import com.training.onit.restapi.entity.BaseResp;
import com.training.onit.restapi.entity.ReqBuyTicket;
import com.training.onit.restapi.entity.RespBuyTicket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyTicketActivity extends BaseActivity implements View.OnClickListener{

    View llStep1, llStep2, llStep3;

    boolean m_buyMonthTicket;
    int     m_nTicketCount;
    Route   m_selectedRoute;
    int m_nYear, m_nMonth;

    TextView tvRoute;
    AutoCompleteTextView tvAutoCompleteRoute;
    TextView tvPrice, tvTicketCount;

    EditText edtNameOnCard, edtCardNumber, edtCVV;
    TextView selMonth, selYear;
    CheckBox chkStep2;

    TextView tvBillInfo;

    final String[] MONTH_NAMES = {"JAN", "FEB", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        llStep1 = findViewById(R.id.llStep1);
        llStep2 = findViewById(R.id.llStep2);
        llStep3 = findViewById(R.id.llStep3);
        llStep1.setVisibility(View.VISIBLE);
        llStep2.setVisibility(View.GONE);
        llStep3.setVisibility(View.GONE);

        findViewById(R.id.btnStep1).setOnClickListener(this);
        findViewById(R.id.btnIncrease).setOnClickListener(this);
        findViewById(R.id.btnDecrease).setOnClickListener(this);
        tvAutoCompleteRoute = (AutoCompleteTextView) findViewById(R.id.tvAutoCompleteRoute);
        tvAutoCompleteRoute.setAdapter(new RouteAutoCompleteAdapter());
        tvAutoCompleteRoute.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_selectedRoute = (Route) parent.getItemAtPosition(position);
                tvAutoCompleteRoute.setText(m_selectedRoute.routeName);

                tvPrice.setText(String.format("$%.2f", m_selectedRoute.price));
                tvTicketCount.setText(m_nTicketCount + "");
            }
        });
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvTicketCount  = (TextView) findViewById(R.id.tvTicketCount);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        findViewById(R.id.llSelectRoute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> values = new ArrayList<Map<String, String>>();
                Map<String, String> item;
                for ( int i = 0; i < AppGlobal.s_routes.length; i++) {
                    item = new HashMap<String, String>();
                    Route route = AppGlobal.s_routes[i];
                    item.put("name", route.routeName);
                    item.put("route", route.startPos + " < - > " + route.endPos);
                    values.add(item);
                }
                SimpleAdapter adapter = new SimpleAdapter(mContext, values,
                        android.R.layout.simple_list_item_2, new String[]{"name","route"}, new int[]{android.R.id.text1,android.R.id.text2});

               new AlertDialog.Builder(mContext)
                       .setAdapter(adapter, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               m_selectedRoute = AppGlobal.s_routes[which];
                               tvRoute.setText(m_selectedRoute.routeName);

                               tvPrice.setText(String.format("$%.2f", m_selectedRoute.price));
                               tvTicketCount.setText(m_nTicketCount + "");
                           }
                       }).create().show();
            }
        });

        ((CheckBox) llStep1.findViewById(R.id.chkBuyMonth)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m_buyMonthTicket = isChecked;
                tvTicketCount.setText(m_nTicketCount + "");
            }
        });
        m_nTicketCount = 1;
        tvTicketCount.setText(m_nTicketCount + "");

        findViewById(R.id.btnStep2).setOnClickListener(this);
        edtNameOnCard = (EditText) findViewById(R.id.edtNameOnCard);
        edtCardNumber = (EditText) findViewById(R.id.edtCardNumber);
        edtCVV = (EditText) findViewById(R.id.edtCVV);
        selMonth = (TextView) findViewById(R.id.selMonth);
        selYear = (TextView) findViewById(R.id.selYear);
        chkStep2 = (CheckBox) findViewById(R.id.chkStep2);
        selMonth.setOnClickListener(this);
        selYear.setOnClickListener(this);
        m_nYear = Calendar.getInstance().get(Calendar.YEAR);
        selYear.setText(m_nYear+"");
        m_nMonth = Calendar.getInstance().get(Calendar.MONTH);
        selMonth.setText(MONTH_NAMES[m_nMonth]);

        findViewById(R.id.btnStep3).setOnClickListener(this);
        findViewById(R.id.btnTos).setOnClickListener(this);
        tvBillInfo = (TextView) findViewById(R.id.tvBillInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStep1:
                if ( m_selectedRoute == null ) {
                    showToast("Select a route.");
                    tvAutoCompleteRoute.requestFocus();
                    return;
                }
                llStep1.setVisibility(View.GONE);
                llStep2.setVisibility(View.VISIBLE);
                break;

            case R.id.btnIncrease:
                if ( m_selectedRoute == null ) {
                    showToast("Please select a route");
                    return;
                }
                if ( !m_buyMonthTicket ) {
                    m_nTicketCount++;
                    tvPrice.setText(String.format("$%.2f", m_nTicketCount * m_selectedRoute.price));
                    tvTicketCount.setText(m_nTicketCount + "");
                }
                break;

            case R.id.btnDecrease:
                if ( m_selectedRoute == null ) {
                    showToast("Please select a route");
                    return;
                }
                if ( !m_buyMonthTicket ) {
                    if (m_nTicketCount > 1) {
                        m_nTicketCount--;
                        tvPrice.setText(String.format("$%.2f", m_nTicketCount * m_selectedRoute.price));
                        tvTicketCount.setText(m_nTicketCount + "");
                    }
                }
                break;

            case R.id.btnStep2:
                if ( !chkStep2.isChecked() ) {
                    showToast("Please accept Terms & Conditions");
                    return;
                }
                if ( edtNameOnCard.getText().length() == 0 ) {
                    showToast("Input name on card.");
                    return;
                }
                if ( edtCardNumber.getText().length() == 0 ) {
                    showToast("Input card number.");
                    return;
                }
                if ( edtCVV.getText().length() == 0 ) {
                    showToast("Input CVV.");
                    return;
                }

                llStep2.setVisibility(View.GONE);
                llStep3.setVisibility(View.VISIBLE);

                String szMsg = "Route\n"
                        + m_selectedRoute.startPos + " <-> " + m_selectedRoute.endPos + "\n"
                        + (m_buyMonthTicket ? "Type:Monthly\tQty:unlimited":"Type:Single use\tQty:" + m_nTicketCount) + "\n"
                        + String.format("Total:$%.2f",(m_buyMonthTicket?m_selectedRoute.monthPrice:m_selectedRoute.price * m_nTicketCount));
                tvBillInfo.setText(szMsg);
                break;

            case R.id.selMonth:
            {
                new AlertDialog.Builder(mContext)
                        .setItems(MONTH_NAMES, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selMonth.setText(MONTH_NAMES[which]);
                                m_nMonth = which;
                            }
                }).create().show();
            }
            break;

            case R.id.selYear:
            {
                final int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                String[] YEARS = {thisYear + "", (thisYear + 1) + "",  (thisYear + 2) + "",
                        (thisYear + 3) + "", (thisYear + 4) + "", (thisYear + 5) + "",
                        (thisYear + 6) + "", (thisYear + 7) + "", (thisYear + 8) + ""};
                new AlertDialog.Builder(mContext)
                        .setItems(YEARS, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selYear.setText((thisYear + which) + "");
                                m_nYear = thisYear + which;
                            }
                        }).create().show();
            }
            break;

            case R.id.btnStep3:
                buyTicket();
                break;

            case R.id.btnTos:
                startActivity(new Intent(mContext, TosActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ( llStep3.getVisibility() == View.VISIBLE ) {
            llStep2.setVisibility(View.VISIBLE);
            llStep3.setVisibility(View.GONE);
        }
        else if ( llStep2.getVisibility() == View.VISIBLE) {
            llStep1.setVisibility(View.VISIBLE);
            llStep2.setVisibility(View.GONE);
        }
        else
            super.onBackPressed();
    }

    private void buyTicket() {

        float amount = m_buyMonthTicket ? m_selectedRoute.monthPrice : m_selectedRoute.price * m_nTicketCount;
        ReqBuyTicket reqBuyTicket = new ReqBuyTicket(
                AppGlobal.user_email, m_selectedRoute.routeName, m_buyMonthTicket, m_nTicketCount, amount,
                edtNameOnCard.getText().toString(), edtCardNumber.getText().toString(), edtCVV.getText().toString(),
                String.format("%02d", m_nMonth + 1), (m_nYear - 2000) + ""
        );
        Call<RespBuyTicket> call = AppGlobal.apiService.buyTicket(reqBuyTicket);
        call.enqueue(new Callback<RespBuyTicket>() {

            @Override
            public void onResponse(Call<RespBuyTicket> call, Response<RespBuyTicket> response) {
                hideProgress();
                int statusCode = response.code();
                BaseResp resp = response.body();
                if ( resp != null && !resp.hasError ) {

                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Thank you!")
                            //.setContentText("Thank you!")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(mContext, TicketListActivity.class));
                                    finish();
                                }
                            })
                            .show();
                }
                else {
                    showError("failed to buy ticket");
                }
            }

            @Override
            public void onFailure(Call<RespBuyTicket> call, Throwable t) {
                showError("exception on buying ticket");
                hideProgress();
            }
        });
        showProgress();
    }
}
