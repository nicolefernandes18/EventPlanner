package com.example.android.eventplanner.network;


import com.example.android.eventplanner.model.Guest;
import com.example.android.eventplanner.model.Manageevent;
import com.example.android.eventplanner.model.Response;
import com.example.android.eventplanner.model.Task;
import com.example.android.eventplanner.model.User;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {
    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("authenticate")
    Observable<Response> login();


    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    @POST("guests")
    Observable<Response> addguest(@Body Guest guest);

    @POST("tasks")
    Observable<Response> addtask(@Body Task task);

    @POST("manageevents")
    Observable<Response> addevent(@Body Manageevent manageevent);


}
