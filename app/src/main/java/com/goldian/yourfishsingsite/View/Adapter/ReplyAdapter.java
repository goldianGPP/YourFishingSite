package com.goldian.yourfishsingsite.View.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.Holder.CommentHolder;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<CommentHolder> {
    Context context;
    List<CommentModel> list;
    CommentModel currentCommentData;

    PreferencesModel pref;

    public ReplyAdapter(Context context, List<CommentModel> list, CommentModel currentCommentData) {
        this.context = context;
        this.list = list;
        this.currentCommentData = currentCommentData;
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
//        return 2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        final CommentModel currentData = list.get(position);
        if (currentData.getReplier() != null)
            holder.replyto.setText("to : " + currentData.getReplier());
        else
            holder.replyto.setText("to : " + this.currentCommentData.getUsername());
        if (currentData.getUsername() != null) {
            holder.username.setText(currentData.getUsername());
            String url = context.getResources().getString(R.string.img_url_user) + currentData.getReply_to() + ".jpg";
            Picasso.get().load(url).into(holder.imgProfile);
        }
        else {
            holder.username.setText(pref.read("username"));
            String url = context.getResources().getString(R.string.img_url_user) + pref.read("id_pengguna") + ".jpg";
            Picasso.get().load(url).into(holder.imgProfile);
        }
        holder.comment.setText(currentData.getReply());
        holder.btnReply.setOnClickListener(onClick(currentData,holder));
        holder.send.setOnClickListener(onClick(currentData,holder));
        holder.reply.addTextChangedListener(onChange(holder));

    }

    View.OnClickListener onClick(final CommentModel currentData, final CommentHolder holder){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == holder.btnReply){
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }
                else if (view == holder.send){
                    CommentAdapter commentAdapter = new CommentAdapter(context,currentCommentData.getCommentModels(), currentData.getUsername());
                    currentCommentData.setId_pengguna(currentData.getId_pengguna());
                    currentCommentData.setUsername(currentData.getUsername());
                    holder.showReplies.setText("no");
                    commentAdapter.postReply(currentCommentData,holder);
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
