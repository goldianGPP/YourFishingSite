package com.goldian.yourfishsingsite.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.CommentController;
import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.CommentAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentFragment extends Fragment {

    CircleImageView imgProfile;
    EditText txtComment;
    ImageButton btnSend;
    CommentAdapter commentAdapter;
    CommentController commentController;
    List<CommentModel> commentModelList;
    RecyclerView recyclerView;
    PreferencesModel pref;
    Context context;
    SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_comment, container, false);
        init(v);
        setListener();
        setValue();
        request();
        return v;
    }

    private void init(View v) {
        imgProfile = v.findViewById(R.id.imgProfile);
        txtComment = v.findViewById(R.id.txtComment);
        btnSend = v.findViewById(R.id.btnSend);
        recyclerView = v.findViewById(R.id.recyclerView);
        swipeRefresh = v.findViewById(R.id.swipeRefresh);

        pref = new PreferencesModel(getActivity(),"login");
        commentController = new CommentController(getContext(),this);
        commentModelList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setValue(){
        String url = getContext().getResources().getString(R.string.img_url_user)+pref.read("id_pengguna")+".jpg";
        Glide.with(getContext())
                .load(url)
                .signature(new ObjectKey(pref.read("key")))
                .into(imgProfile);

        if (getArguments().getString("id").contains("lokasi")) {
            context = getParentFragment().getParentFragment().getContext();
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        else
            context = getContext();
    }

    private void setListener(){
        btnSend.setOnClickListener(onClick);
        swipeRefresh.setOnRefreshListener(this::request);
    }

    //request post comment
    private void request(String id_pengguna, String id, String comment){
        swipeRefresh.setRefreshing(true);
        CommentModel commentModel = new CommentModel();
        commentController.postComment(
            commentModel
                .setId_pengguna(id_pengguna)
                .setIds(id)
                .setComment(comment)
        );
    }

    //request get comments
    public void request(){
        swipeRefresh.setRefreshing(true);
        commentController.getComments(getArguments().getString("id"));
    }

    //result post comment
    public void result(CommentModel commentModel){
        if (commentModel != null) {
            txtComment.setText("");
            commentModelList.add(commentModel);
            commentAdapter = new CommentAdapter(getContext(), commentModelList, pref.read("username"));
            recyclerView.setAdapter(commentAdapter);
        }
        swipeRefresh.setRefreshing(false);
    }

    //result get comments
    public void result(List<CommentModel> commentModels){
        if (commentModels != null) {
            this.commentModelList = commentModels;
            commentAdapter = new CommentAdapter(context, commentModels, pref.read("username"));
            recyclerView.setAdapter(commentAdapter);
        }
        swipeRefresh.setRefreshing(false);
    }

    //Listener
    //-------------
    View.OnClickListener onClick = view -> {
        if (view == btnSend){
            request(
                pref.read("id_pengguna"),
                getArguments().getString("id"),
                txtComment.getText().toString()
            );
        }
    };
}
