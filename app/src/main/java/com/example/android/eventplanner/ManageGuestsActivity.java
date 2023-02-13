package com.example.android.eventplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.eventplanner.adapters.GuestsAdapters;
import com.example.android.eventplanner.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fascel on 16-04-2018.
 */



public class ManageGuestsActivity extends AppCompatActivity  {



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String  mGname;
    public static int i;
    List<String> input = new ArrayList<>();

  /*  public static  int i;
    static ManageGuestsActivity A;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*A = this;*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_guests);

       /* Guest guest=new Guest();*/

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String gname= mSharedPreferences.getString(Constants.GUEST,"");

  /*      System.out.println(gname + "pLEASE DN SHOW NULL");*/

        recyclerView = (RecyclerView) findViewById(R.id.grecycler_view_s);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        input.add(gname + i);
            i++;
        // define an adapter
        mAdapter = new GuestsAdapters(input);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.gfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(ManageGuestsActivity.this, AddNewGuest.class);
                startActivity(numbersIntent);
            }
        });
    }

    public void mailguest(View view)
    {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String gname= mSharedPreferences.getString(Constants.GUEST,"");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation ");
        intent.putExtra(Intent.EXTRA_TEXT, "Dear " + gname +",");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


   /* public static final String TAG = ManageGuestsActivity.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;

    private ProgressBar mProgressbar;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String  mGname;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreferences();
        loadProfile();
    }

    private void initViews() {

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);

    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mGname = mSharedPreferences.getString(Constants.GUEST,"");
    }

  *//*  private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        finish();
    }*//*



    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mGname)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(User user) {

       *//* mProgressbar.setVisibility(View.GONE);*//*
        mTvName.setText(mGname);


    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

*/
}


