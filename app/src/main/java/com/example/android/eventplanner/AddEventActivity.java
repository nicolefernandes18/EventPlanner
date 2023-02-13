package com.example.android.eventplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.Toast;

/*import com.example.android.eventplanner.model.Manageevent;*/
import com.example.android.eventplanner.model.Manageevent;
import com.example.android.eventplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.network.NetworkUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.android.eventplanner.utils.Validation.validateFields;


public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    /*private TextView mEtEmailid;*/
    private EditText edEventname;
    private EditText edVenue;
    private Spinner spin;
    private Button   date;
    private Button   time;
    private Button   send;
    private TextInputLayout mTiEName;
    private TextInputLayout mTiVenue;


    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    String[] event_type={"Wedding","Birthday","Anniversary","Engagement","Corporate"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mSubscriptions = new CompositeSubscription();

        /*mEtEmailid = (TextView) findViewById(R.id.input_email);*/
        edEventname  = (EditText) findViewById(R.id.input_event_name);
        edVenue = (EditText) findViewById(R.id.input_venue);
        time = (Button) findViewById(R.id.set_time);
        date = (Button) findViewById(R.id.set_date);
        send = (Button) findViewById(R.id.butn_login);

        mTiEName = (TextInputLayout) findViewById(R.id.event_name);
        mTiVenue = (TextInputLayout) findViewById(R.id.venue);

        spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,event_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addevent();

            }

        });



        //Getting the instance of Spinner and applying OnItemSelectedListener on it




        /*send.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {

            }
        });*/

    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), event_type[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(getApplicationContext(), event_type[0], Toast.LENGTH_LONG).show();
// TODO Auto-generated method stub

    }
    public void showDatePicker(View v) {
        DialogFragment newFragment = new SetDateFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePicker(View v) {
        DialogFragment newFragment = new SetTimeFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /*public void addEvent(View view){

        Intent numbersIntent = new Intent(AddEventActivity.this, DashboardActivity.class);
        startActivity(numbersIntent);
    }*/

    private void addevent() {

        setError();

        String ename = edEventname.getText().toString();
        String evenue = edVenue.getText().toString();
        /*String eemail = mEtEmailid.getText().toString();*/
        String etype = spin.getSelectedItem().toString();
        String edate = date.getText().toString();
        String etime = time.getText().toString();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uemail= mSharedPreferences.getString(Constants.EMAIL,"");

        System.out.println(uemail);

        int err = 0;

        if (!validateFields(ename)) {

            err++;
            mTiEName.setError("Name should not be empty !");
        }

        if (!validateFields(evenue)) {

            err++;
            mTiVenue.setError("Contact should not be empty !");
        }
        if (err == 0) {



            Manageevent manageevent = new Manageevent();
            manageevent.seteName(ename);
            manageevent.seteVenue(evenue);
            /*manageevent.seteEmail(eemail);*/
            manageevent.seteDate(edate);
            manageevent.seteTime(etime);
            manageevent.seteType(etype);

            manageevent.setuEmail(uemail);


            /*mProgressbar.setVisibility(View.VISIBLE);*/
            addeventProcess(manageevent);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {


        mTiVenue.setError(null);
        mTiEName.setError(null);


    }

    private void addeventProcess(Manageevent manageevent) {

        mSubscriptions.add(NetworkUtil.getRetrofit().addevent(manageevent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        /*mProgressbar.setVisibility(View.GONE);*/
        showSnackBarMessage(response.getMessage());
        /*Intent intent = new Intent(AddEventActivity.this,ManageEventActivity.class);
        intent.putExtra("EventName",edEventname.getText().toString());
        intent.putExtra("Venue",edVenue.getText().toString());
        startActivity(intent);*/
        Intent intents = new Intent(AddEventActivity.this,DashboardActivity.class);
        intents.putExtra("EventName",edEventname.getText().toString());
        intents.putExtra("Venue",edVenue.getText().toString());
        intents.putExtra("Time",time.getText().toString());
        intents.putExtra("Date",date.getText().toString());
        intents.putExtra("SpinnerValue", spin.getSelectedItem().toString());

        startActivity(intents);

    }

    private void handleError(Throwable error) {

        /*mProgressbar.setVisibility(View.GONE);*/

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

        if (findViewById(android.R.id.content) != null) {

            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


}
