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
import com.sdsmdg.tastytoast.TastyToast;

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

    public void login(PenggunaModel penggunaModel){
        api.loginUser(penggunaModel).enqueue(resObject);
    }

    public void register(PenggunaModel penggunaModel){
        api.registerUser(penggunaModel)
            .enqueue(resBoolean);
    }

    public void updateUser(PenggunaModel penggunaModel){
        api.updateUser(penggunaModel).enqueue(resBoolean);
    }

    public void updateUserImg(RequestBody id_pengguna, MultipartBody.Part file){
        api.updateUserImg(id_pengguna,file)
            .enqueue(resBoolean);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------
    /*
    * call back result variable for retrofit
     */
    //BOOLEAN RESULTS
    Callback resBoolean = new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            if (response.isSuccessful()){
                TastyToast.makeText(context, context.getResources().getString(R.string.success), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                result(true);
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
    //OBJECT RESULTS
    Callback resObject = new Callback<PenggunaModel>() {
        @Override
        public void onResponse(@NonNull Call<PenggunaModel> call, @NonNull Response<PenggunaModel> response) {
            if (response.isSuccessful()){
                TastyToast.makeText(context, context.getResources().getString(R.string.success), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                result(response.body());
            }
            else {
                TastyToast.makeText(context, context.getResources().getString(R.string.failed), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                result((PenggunaModel) null);
            }
        }

        @Override
        public void onFailure(@NonNull Call<PenggunaModel> call, @NonNull Throwable t) {
            result((PenggunaModel) null);
            TastyToast.makeText(context, context.getResources().getString(R.string.error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            call.cancel();
        }
    };


}
