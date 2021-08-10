package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.DAO.ApiInterface;
import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.Holder.CommentHolder;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.CommentAdapter;
import com.goldian.yourfishsingsite.View.CommentFragment;

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

    public void postComment(String id_pengguna, String id_lokasi, String comment){
        api.postComment(
                id_pengguna,
                id_lokasi,
                comment
        )
                .enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        result(response.body());
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });
    }

    public void getComments(String id_pengguna){
        api.getComments(id_pengguna)
                .enqueue(new Callback<List<CommentModel>>() {
                    @Override
                    public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                        if (response.isSuccessful()){
                            result(response.body());
                        }
                        else
                            Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                        Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });
    }

    public void postReply(String from_id_pengguna, final CommentModel currentData, final CommentHolder holder){
        api.postReply(
                currentData.getId_comment(),
                from_id_pengguna,
                currentData.getId_pengguna(),
                holder.reply.getText().toString()
        )
                .enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        result(response.body(),holder,currentData);
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });
    }

    public void getReply(final CommentModel currentData, final CommentHolder holder){
        api.getReply(currentData.getId_comment())
                .enqueue(new Callback<List<CommentModel>>() {
                    @Override
                    public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                        if (response.isSuccessful()){
                            result(response.body(),holder,currentData);
                        }
                        else
                            Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });
    }
}
