package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.Holder.CommentHolder;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.CommentAdapter;
import com.goldian.yourfishsingsite.View.CommentFragment;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentController {
    Context context;
    Fragment fragment = null;
    CommentAdapter adapter = null;
    ApiInterface api;

    public CommentController(Context context) {
        this.context = context;
        api = ApiHelper.apiInterface();
    }

    public CommentController(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        api = ApiHelper.apiInterface();
    }

    public CommentController(Context context, CommentAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        api = ApiHelper.apiInterface();
    }


    //Result
    //-------------------------------------------------------------------------------
    private void result(CommentModel commentModel, CommentHolder holder, CommentModel model){
        adapter.result(commentModel,holder,model);
    }

    public void result(List<CommentModel> commentModelList, CommentHolder holder, CommentModel model){
        adapter.result(commentModelList,holder,model);
    }

    private void result(CommentModel commentModel){
        ((CommentFragment)fragment).result(commentModel);
    }

    public void result(List<CommentModel> commentModelList){
        ((CommentFragment)fragment).result(commentModelList);
    }

    public void result(Boolean bool){
//        ((CommentFragment)fragment).result(bool);
    }

    //Get
    //-------------------------------------------------------------------------------
    //get comment
    public void getComments(String id_pengguna){
        api.getComments(id_pengguna)
                .enqueue(resListObject());
    }

    //get reply
    public void getReply(final CommentModel currentData, final CommentHolder holder){
        api.getReply(currentData.getId_comment())
                .enqueue(resListObject(currentData, holder));
    }

    //Post
    //-------------------------------------------------------------------------------
    //post comment
    public void postComment(CommentModel commentModel){
        api.postComment(commentModel).enqueue(resObject());
    }

    //post reply
    public void postReply(CommentModel commentModel, final CommentModel currentData, final CommentHolder holder){
        api.postReply(commentModel).enqueue(resListObject(currentData, holder));
    }

    //Delete
    //-------------------------------------------------------------------------------
    //delete comment
    public void deleteComment(String id_comment){
        api.deleteComment(id_comment).enqueue(resBoolean);
    }

    //delete reply
    public void deleteReply(String id_reply){
        api.deleteReply(id_reply).enqueue(resBoolean);
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
            }
            else {
                result(false);
                Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
            }
            call.cancel();
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            result(false);
            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
            call.cancel();
        }
    };
    //OBJECT RESULTS
    private Callback resObject(){
        Callback resObject = new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()){
                    result(response.body());
                }
                else {
                    result((CommentModel) null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                result((CommentModel) null);
                Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                call.cancel();
            }
        };

        return resObject;
    }

    //LIST OF OBJECT RESULTS
    private Callback resListObject(){
        Callback resListObject = new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (response.isSuccessful()){
                    result(response.body());
                }
                else {
                    result((List<CommentModel>) null);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                result((List<CommentModel>) null);
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
                call.cancel();
            }
        };

        return resListObject;
    }

    private Callback resListObject(final CommentModel currentData, final CommentHolder holder){
        Callback resListObject = new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()){
                    result(response.body(),holder,currentData);
                }
                else {
                    result((CommentModel) null,holder,currentData);
                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                result((CommentModel) null,holder,currentData);
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
                call.cancel();
            }
        };

        return resListObject;
    }
}
