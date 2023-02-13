package com.example.android.eventplanner.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by joefe on 14/03/2018.
 */

public class Validation {
    public static boolean validateFields(String name){

        if (TextUtils.isEmpty(name)) {

            return false;

        } else {

            return true;
        }
    }

    public static boolean validateEmail(String string) {

        if (TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches()) {

            return false;

        } else {

            return  true;
        }
    }
}
