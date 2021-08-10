package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.CalendarFragment;
import com.goldian.yourfishsingsite.View.TambahEventActivity;
import com.goldian.yourfishsingsite.View.TampilEventActivity;
import com.goldian.yourfishsingsite.View.FragmentActivity;

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

    private void result(boolean bool){
        if (context instanceof TambahEventActivity)
            ((TambahEventActivity) context).result(bool);
    }

    private void result(List<EventModel> eventModels){
        if (context instanceof FragmentActivity){
            if (fragment instanceof CalendarFragment)
                ((CalendarFragment) fragment).result(eventModels);
        }
        if (context instanceof TampilEventActivity)
            ((TampilEventActivity)context).result(eventModels);
    }

    public void getEvent(){
        api.getEvent()
                .enqueue(new Callback<List<EventModel>>() {
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
                });
    }

    public void getEvent(String id_pengguna){
        api.getEvent(id_pengguna)
                .enqueue(new Callback<List<EventModel>>() {
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
                });
    }

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
        ).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.isSuccessful()) {
                    result(true);
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                    result(false);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                result(false);
                call.cancel();
            }
        });
    }

    public void updateEvent(String id_event, String title, String deskripsi, String link){
        api.updateEvent(
                id_event,
                title,
                deskripsi,
                link
        ).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.isSuccessful()) {
                    result(true);
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                    result(false);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                result(false);
                call.cancel();
            }
        });
    }

    public void updateEvent(RequestBody id_event, MultipartBody.Part file){
        api.updateEvent(
                id_event,
                file
        ).enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.isSuccessful()) {
                    result(true);
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                    result(false);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                result(false);
                call.cancel();
            }
        });
    }
}
