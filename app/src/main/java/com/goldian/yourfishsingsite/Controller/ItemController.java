package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.DetailBarangActivity;
import com.goldian.yourfishsingsite.View.TambahBarangActivity;
import com.goldian.yourfishsingsite.View.TampilBarangActivity;
import com.goldian.yourfishsingsite.View.UbahBarangActivity;
import com.goldian.yourfishsingsite.View.CariBarangFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemController {
    Context context;
    Fragment fragment = null;
    ApiInterface api;

    public ItemController(Context context) {
        this.context = context;
        api = ApiHelper.apiInterface();
    }

    public ItemController(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        api = ApiHelper.apiInterface();
    }

    private void result(boolean bool){
        if (context instanceof TambahBarangActivity){
            ((TambahBarangActivity)context).result(bool);
        }
        else if (context instanceof UbahBarangActivity){
            ((UbahBarangActivity)context).result(bool);
        }
    }

    public void result(List<ItemModel> itemModelList){
        if (context instanceof FragmentActivity){
            if (fragment instanceof CariBarangFragment)
                ((CariBarangFragment)fragment).result(itemModelList);
        }
        else if (context instanceof DetailBarangActivity)
            ((DetailBarangActivity)context).result(itemModelList);
        else if (context instanceof TampilBarangActivity)
            ((TampilBarangActivity)context).result(itemModelList);
    }

    //responses
    //--------------------
    public void getRecomendation(String id_pengguna, String jenis, String id_item){
        api.getRecomendation(
                id_pengguna,
                id_item,
                jenis
        ).enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    result(response.body());
                    Log.i(TAG, "onSuccess: " + response.body());
                }
                else {
                    result( null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                result( null);
//                Toast.makeText(context, R.string.error,Toast.LENGTH_SHORT).show();
                Toast.makeText(context, t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: " + t.getMessage());
                call.cancel();

            }
        });
    }

    public void getRecomendation(String id_pengguna, String jenis){
        api.getRecomendation(
                id_pengguna,
                jenis
        ).enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    result(response.body());
                }
                else {
                    result( null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                result( null);
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void getSearchRecomendation(String id_pengguna, String search){
        api.getSearchRecomendation(
                id_pengguna,
                search
        ).enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    result(response.body());
                }
                else {
                    result(new ArrayList<>());
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                result(new ArrayList<>());
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void postItem(RequestBody id_pengguna, RequestBody nama, RequestBody jenis, RequestBody deskripsi, RequestBody harga, RequestBody web, MultipartBody.Part file){
        api.postItem(
                id_pengguna,
                nama,
                jenis,
                deskripsi,
                harga,
                web,
                file
        ).enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                if (response.isSuccessful()) {
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void updateItem(String id_item, String nama, String jenis, String deskripsi, String harga, String web){
        api.updateItem(
                id_item,
                nama,
                jenis,
                deskripsi,
                harga,
                web
        ).enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                if (response.isSuccessful()) {
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void updateItem(RequestBody id_item, MultipartBody.Part file){
        api.updateItem(
                id_item,
                file
        ).enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                if (response.isSuccessful()) {
                }
                else {
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void getItems(String id_pengguna) {
        api.getItems(
                id_pengguna
        ).enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    result(response.body());
                }
                else {
                    result( null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                result( null);
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void getRatedItems(String id_pengguna) {
        api.getRatedItems(
                id_pengguna
        ).enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                if (response.isSuccessful()) {
                    result(response.body());
                }
                else {
                    result( null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                result( null);
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
