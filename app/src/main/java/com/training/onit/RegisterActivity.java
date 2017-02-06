package com.training.onit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.training.onit.restapi.entity.ReqRegister;
import com.training.onit.restapi.entity.RespUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private EditText m_edtEmail, m_edtPassword, m_edtPassword2;
    private View m_btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        m_edtEmail = (EditText) findViewById(R.id.edtEmail);
        m_edtPassword = (EditText) findViewById(R.id.edtPassword);
        m_edtPassword2 = (EditText) findViewById(R.id.edtPassword2);
        m_btnRegister = findViewById(R.id.btnRegister);
        m_btnRegister.setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);

        ((CheckBox) findViewById(R.id.chkAgreeTOS)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m_btnRegister.setEnabled(isChecked);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRegister:
            {
                String szEmail = m_edtEmail.getText().toString();
                String szPwd = m_edtPassword.getText().toString();
                String szPwd2 = m_edtPassword2.getText().toString();
                if (TextUtils.isEmpty(szEmail)) {
                    showToast("Enter your email address.");
                    return;
                }

                if ( TextUtils.isEmpty(szPwd)) {
                    showToast("Password can not be empty.");
                    return;
                }

                if ( !szPwd.equals(szPwd2) ) {
                    showToast("Password is not identical.");
                    return;
                }

                register(szEmail, szPwd);
            }
            break;

            case R.id.btnSignIn:
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
        }
    }

    private void register(String szEmail, String szPassword) {
        Call<RespUser> call = AppGlobal.apiService.signup(new ReqRegister(szEmail, szPassword));
        call.enqueue(new Callback<RespUser>() {
            @Override
            public void onResponse(Call<RespUser> call, Response<RespUser> response) {
                hideProgress();
                int statusCode = response.code();
                RespUser respUser = response.body();
                if ( respUser != null && !respUser.hasError ) {
                    AppGlobal.authId = respUser.user.id;
                    AppGlobal.authToken = respUser.user.token;
                    AppGlobal.user_email = respUser.user.email;
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                else {
                    showError("failed to signup");
                }
            }

            @Override
            public void onFailure(Call<RespUser> call, Throwable t) {
                // Log error here since request failed
                showError("exception on signup");
                hideProgress();
            }
        });
        showProgress();
    }
}
