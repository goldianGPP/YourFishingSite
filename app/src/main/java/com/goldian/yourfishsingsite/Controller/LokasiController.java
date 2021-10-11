package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PenggunaModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.TambahLokasiActivity;
import com.goldian.yourfishsingsite.View.MapLokasiFragment;
import com.goldian.yourfishsingsite.View.TampilLokasiActivity;
import com.goldian.yourfishsingsite.View.UbahLokasiActivity;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LokasiController {
    Context context;
    Fragment fragment = null;
    ApiInterface api;

    public LokasiController(Context context) {
        this.context = context;
        api = ApiHelper.apiInterface();
    }

    public LokasiController(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        api = ApiHelper.apiInterface();
    }

    //Result
    //-------------------------------------------------------------------------------
    private void result(List<LokasiModel> lokasiModels){
        if (context instanceof FragmentActivity){
            if (fragment instanceof MapLokasiFragment)
                ((MapLokasiFragment) fragment).result(lokasiModels);
        }
        if (context instanceof TampilLokasiActivity)
            ((TampilLokasiActivity) context).result(lokasiModels);
    }

    private void result(boolean bool){
        if (context instanceof TambahLokasiActivity){
            ((TambahLokasiActivity) context).result(bool);
        }
        else if (context instanceof UbahLokasiActivity){
            ((UbahLokasiActivity) context).result(bool);
        }
    }

    //Get
    //-------------------------------------------------------------------------------
    //get lokasi by user
    public void getLokasi(String id_pengguna){
        api.getLokasi(id_pengguna).enqueue(resListObject);
    }

    //get lokasi
    public void getLokasi(){
        api.getLokasi()
                .enqueue(resListObject);
    }

    //Post
    //-------------------------------------------------------------------------------
    //post lokasi
    public void postLokasi(RequestBody id_pengguna, RequestBody nama, RequestBody ikan, RequestBody alamat, RequestBody deskripsi, RequestBody status, RequestBody latitude, RequestBody longitude, MultipartBody.Part file){
        api.postLokasi(
                id_pengguna,
                nama,
                ikan,
                alamat,
                deskripsi,
                status,
                latitude,
                longitude,
                file
        ).enqueue(resBoolean);
    }

    //Update
    //-------------------------------------------------------------------------------
    //update lokasi
    public void updateLokasi(LokasiModel lokasiModel){
        api.updateLokasi(lokasiModel).enqueue(resBoolean);
    }

    //update image lokasi
    public void updateLokasi(RequestBody id_lokasi, RequestBody id_pengguna, RequestBody cur_img, MultipartBody.Part file){
        api.updateLokasi(
                id_lokasi,
                id_pengguna,
                cur_img,
                file
        ).enqueue(resBoolean);
    }

    //Delete
    //-------------------------------------------------------------------------------
    //delete lokasi
    public void deleteLokasi(String id_lokasi, String img){
        api.deleteLokasi(id_lokasi, img).enqueue(resBoolean);
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
                result(response.body());
                TastyToast.makeText(context, context.getResources().getString(R.string.success), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
    Callback resListObject = new Callback<List<LokasiModel>>() {
        @Override
        public void onResponse(Call<List<LokasiModel>> call, Response<List<LokasiModel>> response) {
            if (response.isSuccessful()) {
                result(response.body());
            }
            else {
                Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                result(null);
            }
        }

        @Override
        public void onFailure(Call<List<LokasiModel>> call, Throwable t) {
            Toast.makeText(context, R.string.error,Toast.LENGTH_SHORT).show();
            result(null);
            call.cancel();
        }
    };
}
