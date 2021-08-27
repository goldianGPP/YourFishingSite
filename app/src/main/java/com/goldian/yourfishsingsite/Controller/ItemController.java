package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.DetailBarangActivity;
import com.goldian.yourfishsingsite.View.TambahBarangActivity;
import com.goldian.yourfishsingsite.View.TampilBarangActivity;
import com.goldian.yourfishsingsite.View.UbahBarangActivity;
import com.goldian.yourfishsingsite.View.CariBarangFragment;
import com.sdsmdg.tastytoast.TastyToast;

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
        else if (context instanceof DetailBarangActivity){
            ((DetailBarangActivity)context).result(bool);
        }
    }

    //Result
    //-------------------------------------------------------------------------------
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


    //Get
    //-------------------------------------------------------------------------------
    //get recomendation itembased
    public void getRecomendation(String id_pengguna, String jenis, String id_item){
        api.getRecomendation(
                id_pengguna,
                id_item,
                jenis
        ).enqueue(resListObject);
    }

    //get recomendation userbased
    public void getRecomendation(String id_pengguna, String jenis){
        api.getRecomendation(
                id_pengguna,
                jenis
        ).enqueue(resListObject);
    }

    //get recomendation userbased by search
    public void getSearchRecomendation(String id_pengguna, String search){
        api.getSearchRecomendation(
                id_pengguna,
                search
        ).enqueue(resListObject);
    }

    //get item
    public void getMyItems(String id_pengguna) {
        api.getMyItems(
                id_pengguna
        ).enqueue(resListObject);
    }

    //get item
    public void getItems(String jenis) {
        api.getItems(
                jenis
        ).enqueue(resListObject);
    }

    public void getSearchItems(String search) {
        api.getSearchItems(
                search
        ).enqueue(resListObject);
    }

    //get rated item
    public void getRatedItems(String id_pengguna) {
        api.getRatedItems(
                id_pengguna
        ).enqueue(resListObject);
    }

    //Post
    //-------------------------------------------------------------------------------
    //post item
    public void postItem(RequestBody id_pengguna, RequestBody nama, RequestBody jenis, RequestBody deskripsi, RequestBody harga, RequestBody web, MultipartBody.Part file){
        api.postItem(
                id_pengguna,
                nama,
                jenis,
                deskripsi,
                harga,
                web,
                file
        ).enqueue(resBoolean);
    }

    public void setRating(ItemModel itemModel){
        api.setRating(itemModel).enqueue(resBoolean);
    }

    //Update
    //-------------------------------------------------------------------------------
    //update item
    public void updateItem(ItemModel itemModel){
        api.updateItem(itemModel).enqueue(resBoolean);
    }

    //update image item
    public void updateItem(RequestBody id_item, RequestBody cur_img, MultipartBody.Part file){
        api.updateItem(
                id_item,
                cur_img,
                file
        ).enqueue(resBoolean);
    }

    //Delete
    //-------------------------------------------------------------------------------
    //delete item
    public void deleteItem(String id_item){
        api.deleteItem(id_item).enqueue(resBoolean);
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
                result( false);
                TastyToast.makeText(context, context.getResources().getString(R.string.failed), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            result( false);
            TastyToast.makeText(context, context.getResources().getString(R.string.error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            call.cancel();
        }
    };
    //LIST OF OBJECT RESULTS
    Callback resListObject = new Callback<List<ItemModel>>() {
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
    };
}
