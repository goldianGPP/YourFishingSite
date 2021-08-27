package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.CalendarFragment;
import com.goldian.yourfishsingsite.View.TambahEventActivity;
import com.goldian.yourfishsingsite.View.TampilEventActivity;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.UbahEventActivity;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventController {
    Context context;
    Fragment fragment = null;
    ApiInterface api;

    public EventController(Context context) {
        this.context = context;
        api = ApiHelper.apiInterface();
    }

    public EventController(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        api = ApiHelper.apiInterface();
    }

    //Result
    //-------------------------------------------------------------------------------
    private void result(boolean bool){
        if (context instanceof TambahEventActivity)
            ((TambahEventActivity) context).result(bool);
        else if (context instanceof UbahEventActivity)
            ((UbahEventActivity) context).result(bool);
    }

    private void result(List<EventModel> eventModels){
        if (context instanceof FragmentActivity){
            if (fragment instanceof CalendarFragment)
                ((CalendarFragment) fragment).result(eventModels);
        }
        if (context instanceof TampilEventActivity)
            ((TampilEventActivity)context).result(eventModels);
    }

    //Get
    //-------------------------------------------------------------------------------
    //get event
    public void getEvent(){
        api.getEvent()
                .enqueue(resListObject);
    }

    //get event by id
    public void getEvent(String id_pengguna){
        api.getEvent(id_pengguna)
                .enqueue(resListObject);
    }

    //Post
    //-------------------------------------------------------------------------------
    //post event
    public void postEvent(RequestBody id_pengguna, RequestBody title, RequestBody day, RequestBody month, RequestBody year, RequestBody link, RequestBody deskripsi, MultipartBody.Part file){
        api.postEvent(
                id_pengguna,
                title,
                day,
                month,
                year,
                link,
                deskripsi,
                file
        ).enqueue(resBoolean);
    }

    //Update
    //-------------------------------------------------------------------------------
    //update event
    public void updateEvent(EventModel eventModel){
        api.updateEvent(eventModel).enqueue(resBoolean);
    }

    //update image event
    public void updateEvent(RequestBody id_event, RequestBody id_pengguna, RequestBody cur_img, MultipartBody.Part file){
        api.updateEvent(
                id_event,
                id_pengguna,
                cur_img,
                file
        ).enqueue(resBoolean);
    }

    //Delete
    //-------------------------------------------------------------------------------
    //delete event
    public void deleteEvent(String id_event, String img){
        api.deleteEvent(id_event,img).enqueue(resBoolean);
    }


    //---------------------------------------------------------------------------------------------------------------------------------------
    /*
     * call back result variable for retrofit
     */
    //BOOLEAN RESULTS
    Callback resBoolean = new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            if (response.isSuccessful()) {
                TastyToast.makeText(context, context.getResources().getString(R.string.success), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                result(response.body());
            }
            else {
                TastyToast.makeText(context, context.getResources().getString(R.string.failed), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                result(false);
            }
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            TastyToast.makeText(context, context.getResources().getString(R.string.error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            result(false);
            call.cancel();
        }
    };
    //LIST OF OBJECT RESULTS
    Callback resListObject = new Callback<List<EventModel>>() {
        @Override
        public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
            if (response.isSuccessful()) {
                result(response.body());
            }
            else {
                Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                result(null);
            }
        }

        @Override
        public void onFailure(Call<List<EventModel>> call, Throwable t) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
            result(null);
            call.cancel();
        }
    };
}
