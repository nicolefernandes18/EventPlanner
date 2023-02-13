package com.example.android.eventplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventplanner.model.Guest;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.model.User;
import com.example.android.eventplanner.network.NetworkUtil;
import com.example.android.eventplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.android.eventplanner.utils.Validation.validateEmail;
import static com.example.android.eventplanner.utils.Validation.validateFields;

/**
 * Created by Fascel on 15-04-2018.
 */

public class AddNewGuest extends AppCompatActivity {

    private EditText mEtgName;
    private EditText mEtgEmail;
    private EditText mEtgContact;
    private Button mBtAddGuest;
    private TextInputLayout mTigName;
    private TextInputLayout mTigContact;
    private TextInputLayout mTigEmail;
    private TextInputLayout mTiEmail;

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guests);
        mSubscriptions = new CompositeSubscription();

        mEtgName = (EditText) findViewById(R.id.gname);
        mEtgContact = (EditText) findViewById(R.id.gcontact);
        mEtgEmail = (EditText) findViewById(R.id.gemail);

        mBtAddGuest = (Button) findViewById(R.id.btn_AddNewGuest);

        mTigName = (TextInputLayout) findViewById(R.id.gtvname);
        mTigContact = (TextInputLayout) findViewById(R.id.gtvcontact);
        mTigEmail = (TextInputLayout) findViewById(R.id.gtvemail);
        mTiEmail = (TextInputLayout) findViewById(R.id.ti_email);


        mBtAddGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addguest();
            }
        });

    }


    private void addguest() {

        setError();

        String gname = mEtgName.getText().toString();
        String gcontact = mEtgContact.getText().toString();
        String gemail = mEtgEmail.getText().toString();



        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uemail= mSharedPreferences.getString(Constants.EMAIL,"");

        System.out.println(uemail);

        int err = 0;

        if (!validateFields(gname)) {

            err++;
            mTigName.setError("Name should not be empty !");
        }

        if (!validateFields(gcontact)) {

            err++;
            mTigContact.setError("Contact should not be empty !");
        }

        if (!validateEmail(gemail)) {

            err++;
            mTigEmail.setError("Email should be valid !");
        }


        if (err == 0) {

            Guest guest = new Guest();
            guest.setgName(gname);
            guest.setgContact(gcontact);
            guest.setgEmail(gemail);
            guest.setuEmail(uemail);


            /*mProgressbar.setVisibility(View.VISIBLE);*/
            addguestProcess(guest);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {

        mTigName.setError(null);
        mTigEmail.setError(null);
        mTigContact.setError(null);
    }

    private void addguestProcess(Guest guest) {

        mSubscriptions.add(NetworkUtil.getRetrofit().addguest(guest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        /*mProgressbar.setVisibility(View.GONE);*/
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
        editor.putString(Constants.GUEST,response.getMessage());
        editor.apply();

        Intent intent = new Intent(AddNewGuest.this, ManageGuestsActivity.class);
        startActivity(intent);

    }

    private void handleError(Throwable error) {



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
