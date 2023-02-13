package com.example.android.eventplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;

import com.example.android.eventplanner.model.Manageevent;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.adapter.rxjava.HttpException;
import rx.subscriptions.CompositeSubscription;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView txtDay, txtHour, txtMinute, txtSecond, tvEventStart, tv, venue, type, date, time;

    private Handler handler;
    private Runnable runnable;
    private ProgressBar mProgressbar;

    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mSubscriptions = new CompositeSubscription();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv = (TextView) findViewById(R.id.dashboard_uname);
        tv.setText(getIntent().getStringExtra("EventName"));

        venue = (TextView) findViewById(R.id.dashboard_venue);
        venue.setText(getIntent().getStringExtra("Venue"));

        type = (TextView) findViewById(R.id.dashboard_type);
        type.setText(getIntent().getStringExtra("SpinnerValue"));

        time = (TextView) findViewById(R.id.dashboard_times);
        time.setText(getIntent().getStringExtra("Time"));




        date = (TextView) findViewById(R.id.dashboard_date);
        date.setText(getIntent().getStringExtra("Date"));

        txtDay = (TextView) findViewById(R.id.txtTimerDay);
        txtHour = (TextView) findViewById(R.id.txtTimerHour);
        txtMinute = (TextView) findViewById(R.id.txtTimerMinute);
        txtSecond = (TextView) findViewById(R.id.txtTimerSecond);
        /*tvEventStart = (TextView) findViewById(R.id.tveventStart);*/
        countDownStart();

        NavigationView navigationViews = (NavigationView) findViewById(R.id.nav_view);
        navigationViews.setNavigationItemSelectedListener(this);
        View header=navigationViews.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/


        TextView den = (TextView)header.findViewById(R.id.dEvent_name);
        den.setText(getIntent().getStringExtra("EventName"));

        TextView dvenue = (TextView)header.findViewById(R.id.dVenue);
        dvenue.setText(getIntent().getStringExtra("Venue"));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ImageView i = (ImageView) findViewById(R.id.dashboard_icon);
        ImageView im = (ImageView)header.findViewById(R.id.dimage);
        if(type.getText().toString().equals("Wedding"))
        {
            String uri = "@drawable/wedding";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            i.setImageDrawable(res);
            im.setImageDrawable(res);

        }
        else if(type.getText().toString().equals("Birthday"))
        {
            String uri = "@drawable/cake1";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            i.setImageDrawable(res);
            im.setImageDrawable(res);

        }
        else if(type.getText().toString().equals("Anniversary"))
        {
            String uri = "@drawable/anniversary";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            i.setImageDrawable(res);
            im.setImageDrawable(res);

        }
        else if(type.getText().toString().equals("Engagement"))
        {
            String uri = "@drawable/engagement";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            i.setImageDrawable(res);
            im.setImageDrawable(res);

        }
        else if(type.getText().toString().equals("Corporate"))
        {
            String uri = "@drawable/corporate";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            i.setImageDrawable(res);
            im.setImageDrawable(res);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initSharedPreferences();
        /*dashboard();*/

    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

   /* private void dashboard() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getDashboard(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }*/
    private void handleResponse(Manageevent manageevent) {

        mProgressbar.setVisibility(View.GONE);
        tv.setText(manageevent.geteName());
        venue.setText(manageevent.geteVenue());
        type.setText(manageevent.geteType());
        date.setText(manageevent.geteDate());
        time.setText(manageevent.geteTime());
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse("2018-04-29");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtDay.setText("" + String.format("%02d", days));
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText(""
                                + String.format("%02d", minutes));
                        txtSecond.setText(""
                                + String.format("%02d", seconds));
                    } else {


                        tvEventStart.setText("The event started!");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {
        findViewById(R.id.LinearLayout10).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout11).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout12).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout13).setVisibility(View.GONE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_add_event) {
            Context context = getApplicationContext();
            CharSequence text = "Add Manageevent";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent numbersIntent = new Intent(DashboardActivity.this, ManageEventActivity.class);
            startActivity(numbersIntent);
        }
        else if (id == R.id.nav_dashboard ) {
            // Handle the camera action
            Context context = getApplicationContext();
            CharSequence text = "Opening Dashboard";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            /*Intent numbersIntent = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(numbersIntent);*/
        } else if (id == R.id.nav_manage_tasks) {
            Context context = getApplicationContext();
            CharSequence text = "Opening Manage Activity";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent numbersIntent = new Intent(DashboardActivity.this, AddTasksActivity.class);
            startActivity(numbersIntent);
        } else if (id == R.id.nav_invite_guests) {
            Context context = getApplicationContext();
            CharSequence text = "Opening Invite Guests List";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent numbersIntent = new Intent(DashboardActivity.this, InviteGuestsActivity.class);
            startActivity(numbersIntent);

        }
        else if (id == R.id.nav_logout) {
            Context context = getApplicationContext();
            CharSequence text = "Logging out";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent numbersIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(numbersIntent);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
