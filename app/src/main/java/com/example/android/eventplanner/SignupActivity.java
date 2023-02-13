package com.example.android.eventplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.model.User;
import com.example.android.eventplanner.network.NetworkUtil;
import com.example.android.eventplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import static com.example.android.eventplanner.utils.Validation.validateEmail;
import static com.example.android.eventplanner.utils.Validation.validateFields;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SignupActivity extends AppCompatActivity {

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtRegister;
    private TextView mTvLogin;
    private TextInputLayout mTiName;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private ProgressBar mProgressbar;

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSubscriptions = new CompositeSubscription();

        mEtName = (EditText) findViewById(R.id.input_name);
        mEtEmail = (EditText) findViewById(R.id.input_email);
        mEtPassword = (EditText) findViewById(R.id.input_password);
        mBtRegister = (Button) findViewById(R.id.btn_register);

        mTiName = (TextInputLayout) findViewById(R.id.ti_name);
        mTiEmail = (TextInputLayout) findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) findViewById(R.id.ti_password);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }


    private void register() {

        setError();

        String name = mEtName.getText().toString();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            mTiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            /*mProgressbar.setVisibility(View.VISIBLE);*/
            registerProcess(user);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {

        mTiName.setError(null);
        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void registerProcess(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        /*mProgressbar.setVisibility(View.GONE);*/

        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        if (findViewById(android.R.id.content) != null) {

            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}