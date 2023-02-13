package com.example.android.eventplanner;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eventplanner.adapters.ToDoListAdapters;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.model.Task;
import com.example.android.eventplanner.model.ToDoData;
import com.example.android.eventplanner.network.NetworkUtil;
import com.example.android.eventplanner.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.android.eventplanner.utils.Validation.validateFields;

public  class AddTasksActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ToDoData> tdd = new ArrayList<>();
    /*SqliteHelper mysqlite;*/
    SwipeRefreshLayout swipeRefreshLayout;

    private EditText mTName;
    private EditText mTNotes;
    private EditText mTPriority;
    private Button mBtAddTask;
    private TextInputLayout mTiTName;

    private TextInputLayout mTiTNotes;

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        mSubscriptions = new CompositeSubscription();



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new ToDoListAdapters(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.divider));

      swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
              /*  updateCardView();*/
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AddTasksActivity.this);
                dialog.setContentView(R.layout.custom_cardlayout);
                dialog.show();
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                TextView tvstatus = (TextView) dialog.findViewById(R.id.status);
                cb.setVisibility(View.GONE);
                tvstatus.setVisibility(View.GONE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTName = (EditText) dialog.findViewById(R.id.input_task_desc);
                        mTNotes  = (EditText) dialog.findViewById(R.id.input_task_notes);
                        mTiTName = (TextInputLayout) dialog.findViewById(R.id.toDoText);
                        mTiTNotes = (TextInputLayout) dialog.findViewById(R.id.toDoTextNotes);

                        if (mTName.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btn.getText();
                            }
                            Spinner getTime = (Spinner) dialog.findViewById(R.id.spinner);
                            EditText timeInNumb = (EditText) dialog.findViewById(R.id.input_task_time);
                            if(getTime.getSelectedItem().toString().matches("Days") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Days in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 24 * 60 * 60 * 1000 ;
                                scheduleNotification(miliTime, mTName.getText().toString(),RadioSelection);
                            } else if (getTime.getSelectedItem().toString().matches("Minutes") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Minutes in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 60 * 1000 ;
                               scheduleNotification(miliTime, mTName.getText().toString(),RadioSelection);
                            } else if (getTime.getSelectedItem().toString().matches("Hours") && !(timeInNumb.getText().toString().matches(""))) {
                                // Convert timeInNumb to Hours in Miliseconds
                                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                                long miliTime = longtime * 60 * 60 * 1000 ;
                               scheduleNotification(miliTime, mTName.getText().toString(),RadioSelection);
                            }
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ToDoTaskDetails", mTName.getText().toString());
                            contentValues.put("ToDoTaskPrority", RadioSelection);
                            contentValues.put("ToDoTaskStatus", "Incomplete");
                            contentValues.put("ToDoNotes", mTNotes.getText().toString());

                            addentryOftask();
                            dialog.hide();

                           /* mysqlite = new SqliteHelper(getApplicationContext());
                            Boolean b = mysqlite.insertInto(contentValues);
                            if (b) {
                                dialog.hide();
                                updateCardView();
                            } else {
                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }*/

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });
    }


    private void addentryOftask() {

        setError();

        String ttasks = mTName.getText().toString();
        String tnotes = mTNotes.getText().toString();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uemail= mSharedPreferences.getString(Constants.EMAIL,"");

        System.out.println(uemail);

        int err = 0;

        if (!validateFields(ttasks)) {

            err++;
            mTiTName.setError("Name should not be empty !");
        }

        if (!validateFields(tnotes)) {

            err++;
            mTiTNotes.setError("Contact should not be empty !");
        }


        if (err == 0) {

            Task task = new Task();
            task.setTname(ttasks);
            task.setTnotes(tnotes);

            task.setuEmail(uemail);


            /*mProgressbar.setVisibility(View.VISIBLE);*/
            addTaskProcess(task);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }


    private void setError() {

        mTiTName.setError(null);
        mTiTNotes.setError(null);
    }

    private void addTaskProcess(Task task){

        mSubscriptions.add(NetworkUtil.getRetrofit().addtask(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        /*mProgressbar.setVisibility(View.GONE);*/
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
        editor.putString(Constants.TASK,response.getMessage());
        editor.apply();

        updateCardView();



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

    /*@Override
    public void onRefresh() {

    }*/
   public void scheduleNotification(long time, String TaskTitle, String TaskPrority) {

        Calendar Calendar_Object = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final int _id = (int) System.currentTimeMillis();
        Intent myIntent = new Intent(AddTasksActivity.this, AlarmRecever.class);
        myIntent.putExtra("TaskTitle", TaskTitle);
        myIntent.putExtra("TaskPrority",TaskPrority);
        myIntent.putExtra("id",_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTasksActivity.this,
                _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis() + time,
                pendingIntent);

       System.out.println("ScheduleNotification Service is working");

    }

    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);


            tdd.clear();
            adapter.notifyDataSetChanged();

                ToDoData tddObj = new ToDoData();


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tname= mSharedPreferences.getString(Constants.TASK,"");

                tddObj.setToDoTaskDetails(tname);

                tdd.add(tddObj);

            adapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateCardView();
    }
}
