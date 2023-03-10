package com.example.android.eventplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;/*
import android.app.DialogFragment;*/
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import java.util.Calendar;

import android.support.v4.app.DialogFragment;


/*
*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetDateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetDateFragment#newInstance} factory method to
 * create an instance of this fragment.*/




public class SetDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        int x = view.getMonth()+1;
        Button p1_button = (Button) getActivity().findViewById(R.id.set_date);
        p1_button.setText(view.getDayOfMonth()+"/"+x+"/"+view.getYear());
    }
}
