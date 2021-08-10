package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.PenggunaModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.LoginActivity;
import com.goldian.yourfishsingsite.View.UbahProfileActivity;
import com.goldian.yourfishsingsite.View.RegisterActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenggunaController {
    Context context;
    ApiInterface api;

    public PenggunaController(Context context) {
        this.context = context;
        api = ApiHelper.apiInterface();
    }

    private void result(PenggunaModel penggunaModel){
        if (context instanceof LoginActivity)
            ((LoginActivity)context).result(penggunaModel);
    }

    private void result(boolean bool){
        if (context instanceof UbahProfileActivity)
            ((UbahProfileActivity)context).result(bool);
        if (context instanceof RegisterActivity)
            ((RegisterActivity)context).result(bool);
    }

    public void login(String username, String password){
        api.loginUser(username,password)
            .enqueue(new Callback<PenggunaModel>() {
                @Override
                public void onResponse(@NonNull Call<PenggunaModel> call, @NonNull Response<PenggunaModel> response) {
                    if (response.isSuccessful()){
                        result(response.body());
                    }
                    else {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                        result((PenggunaModel) null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PenggunaModel> call, @NonNull Throwable t) {
                    result((PenggunaModel) null);
                    Toast.makeText(context, R.string.error,Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
    }

    public void register(String email, String nama, String username, String password){
        api.registerUser(email, nama, username, password)
            .enqueue(new Callback<PenggunaModel>() {
                @Override
                public void onResponse(Call<PenggunaModel> call, Response<PenggunaModel> response) {
                    if (response.isSuccessful()){
                        result(true);
                    }
                    else {
                        result(false);
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PenggunaModel> call, Throwable t) {
                    Toast.makeText(context, R.string.error,Toast.LENGTH_SHORT).show();
                    result(false);
                    call.cancel();
                }
            });
    }

    public void updateUser(String id_pengguna, String nama, String username){
        api.updateUser(
                id_pengguna,
                nama,
                username
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, R.string.error,Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void updateUserImg(RequestBody id_pengguna, MultipartBody.Part file){
        api.updateUserImg(id_pengguna,file)
            .enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()){
                        result(true);
                    }
                    else {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                        result(false);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(context, R.string.error + " = " + t.getMessage(),Toast.LENGTH_SHORT).show();
                    result(false);
                    call.cancel();
                }
            });
    }


}
