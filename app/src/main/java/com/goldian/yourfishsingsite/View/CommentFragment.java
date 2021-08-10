package com.goldian.yourfishsingsite.View;

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
        Picasso.get().load(url).into(imgProfile);
    }

    private void setListener(){
        btnSend.setOnClickListener(onClick);
    }

    //request post comment
    private void request(String id_pengguna, String id, String comment){
        commentController.postComment(
                id_pengguna,
                id,
                comment
        );
    }

    //request get comments
    public void request(){
        commentController.getComments(getArguments().getString("id"));
    }

    //result post comment
    public void result(CommentModel commentModel){
        txtComment.setText("");
        commentModelList.add(commentModel);
        commentAdapter = new CommentAdapter(getContext(), commentModelList, pref.read("username"));
        recyclerView.setAdapter(commentAdapter);
    }

    //result get comments
    public void result(List<CommentModel> commentModels){
        this.commentModelList = commentModels;
        if (getArguments().getString("id").contains("lokasi"))
            commentAdapter = new CommentAdapter(getParentFragment().getParentFragment().getContext(), commentModels, pref.read("username"));
        else
            commentAdapter = new CommentAdapter(getContext(), commentModels, pref.read("username"));
        recyclerView.setAdapter(commentAdapter);
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
