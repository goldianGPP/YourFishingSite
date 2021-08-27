package com.goldian.yourfishsingsite.View.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.CommentController;
import com.goldian.yourfishsingsite.Interface.CommentInterface;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.Holder.CommentHolder;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> implements CommentInterface {
    Context context;
    List<CommentModel> list;
    CommentController commentController;
    String username;

    ReplyAdapter replyAdapter;
    PreferencesModel pref;

    public CommentAdapter(Context context, List<CommentModel> list, String username) {
        this.context = context;
        this.list = list;
        this.username = username;
        commentController = new CommentController(context,this);
        pref = new PreferencesModel(context, "login");
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(context).inflate(R.layout.view_comment_vertical,parent,false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CommentModel currentData = list.get(position);
        String url;
        init(holder,currentData);
        holder.replyto.setText("to : POST");
        holder.comment.setText(currentData.getComment());
        holder.send.setOnClickListener(onClick(currentData,holder));
        holder.close.setOnClickListener(onClick(currentData,holder));
        holder.reply.addTextChangedListener(onChange(holder));
        holder.btnReply.setOnClickListener(onClick(currentData,holder));
        holder.showReplies.setOnClickListener(onClick(currentData,holder));
        if (Integer.parseInt(currentData.getReplies()) > 0){
            holder.showReplies.setVisibility(View.VISIBLE);
        }
        if (currentData.getUsername() != null) {
            holder.username.setText(currentData.getUsername());
            url = context.getResources().getString(R.string.img_url_user) + currentData.getId_pengguna() + ".jpg";
        }
        else {
            currentData.setUsername(pref.read("username"));
            holder.username.setText(currentData.getUsername());
            url = context.getResources().getString(R.string.img_url_user) + pref.read("id_pengguna") + ".jpg";
        }
        Glide.with(context)
                .load(url)
                .signature(new ObjectKey(pref.read("key")))
                .into(holder.imgProfile);


    }

    private void init(CommentHolder holder, CommentModel currentData) {
        currentData.setCommentModels();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void result(CommentModel commentModel, CommentHolder holder, CommentModel currentData){
        holder.reply.setText("");
        currentData.addCommentModels(commentModel);
        replyAdapter = new ReplyAdapter(context, currentData.getCommentModels(), currentData);
        holder.recyclerView.setAdapter(replyAdapter);
        holder.recyclerView.setVisibility(View.VISIBLE);
        holder.relativeLayout.setVisibility(View.GONE);
        if (!holder.showReplies.getText().equals("no")){
            holder.showReplies.setVisibility(View.VISIBLE);
            holder.showReplies.setText("hide replies");
        }
    }

    public void result(List<CommentModel> commentModels, CommentHolder holder, CommentModel currentData){
        currentData.addAllCommentModels(commentModels);
        replyAdapter = new ReplyAdapter(context, currentData.getCommentModels(), currentData);
        holder.recyclerView.setAdapter(replyAdapter);
        holder.recyclerView.setVisibility(View.VISIBLE);
        holder.showReplies.setText("hide replies");
    }

    public void postReply(final CommentModel currentData, final CommentHolder holder){
        pref = new PreferencesModel(context, "login");
        CommentModel commentModel = new CommentModel();
        commentController.postReply(
                commentModel
                        .setId_pengguna(pref.read("id_pengguna"))
                        .setId_comment(currentData.getId_comment())
                        .setReply_to(currentData.getId_pengguna())
                        .setReply(holder.reply.getText().toString()),
                currentData,
                holder
        );
    }

    void getReply(final CommentModel currentData, final CommentHolder holder){
        commentController.getReply(currentData,holder);
    }

    View.OnClickListener onClick(final CommentModel currentData, final CommentHolder holder){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == holder.btnReply){
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }
                else if (view == holder.send){
                    postReply(currentData, holder);
                }
                else if (view == holder.showReplies){
                    if (holder.showReplies.getText().toString().equals("show replies")){
                        if (currentData.getCommentModels().size() > 0) {
                            holder.recyclerView.setVisibility(View.VISIBLE);
                            holder.showReplies.setText("hide replies");
                        }
                        else
                            getReply(currentData,holder);
                    }
                    else {
                        holder.showReplies.setText("show replies");
                        holder.recyclerView.setVisibility(View.GONE);
                    }
                }
                else if (view == holder.close){
                    holder.relativeLayout.setVisibility(View.GONE);
                }
            }
        };
    }

    TextWatcher onChange(final CommentHolder holder){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0)
                    holder.send.setVisibility(View.GONE);
                else
                    holder.send.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        };
    }
}

