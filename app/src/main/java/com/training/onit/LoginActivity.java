package com.training.onit;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.training.onit.restapi.entity.ReqLogin;
import com.training.onit.restapi.entity.RespUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;

    private Context mContext;
    private EditText m_edtEmail, m_edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        m_edtEmail = (EditText) findViewById(R.id.edtEmail);
        m_edtPassword = (EditText) findViewById(R.id.edtPassword);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.btnLoginWithGoogle).setOnClickListener(this);
        findViewById(R.id.btnLoginWithFacebook).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        error("onConnectionFailed:" + connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnLogin:
            {
                String szEmail = m_edtEmail.getText().toString();
                String szPwd = m_edtPassword.getText().toString();
                if (TextUtils.isEmpty(szEmail) ) {
                    showToast("Enter your email address.");
                    return;
                }

                if (TextUtils.isEmpty(szPwd)) {
                    showToast("Password can not be empty.");
                    return;
                }

                login(szEmail, szPwd, false);
            }
            break;

            case R.id.btnLoginWithGoogle:
                signInWithGoogle();
                break;

            case R.id.btnLoginWithFacebook:
                showToast("Not Implemented.");
                break;

            case R.id.btnSignUp:
                startActivity(new Intent(mContext, RegisterActivity.class));
                finish();
                break;
        }
    }

    private void onSuccessLogin() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private void login(String szEmail, String szPassword, boolean isSocial) {
        Call<RespUser> call = AppGlobal.apiService.signin(new ReqLogin(szEmail, szPassword, isSocial));
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
                    onSuccessLogin();
                }
                else {
                    showError("failed to login");
                }
            }

            @Override
            public void onFailure(Call<RespUser> call, Throwable t) {
                // Log error here since request failed
                Log.e("###", "login", t);

                showError("exception on login");
                hideProgress();
            }
        });
        showProgress();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    private void signOutFromGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    private void revokeAccessGoogle() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            trace("Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        trace("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            trace("Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            login(email, "google", true);
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

}
