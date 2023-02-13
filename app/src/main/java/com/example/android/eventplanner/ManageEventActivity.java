package com.example.android.eventplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.eventplanner.utils.Constants;

public class ManageEventActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ename= mSharedPreferences.getString(Constants.EVENT,"");

       /* TextView tv = (TextView) findViewById(R.id.toDoETextDetails);
        tv.setText(getIntent().getStringExtra(ename));

        TextView venue = (TextView) findViewById(R.id.toDoETextNotes);
        venue.setText(getIntent().getStringExtra("Venue"));*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(ManageEventActivity.this, AddEventActivity.class);
                startActivity(numbersIntent);
            }
        });
    }

}
