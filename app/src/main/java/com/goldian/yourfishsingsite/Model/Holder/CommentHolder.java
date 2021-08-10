package com.goldian.yourfishsingsite.Model.Holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.R;

public class CommentHolder extends RecyclerView.ViewHolder{
    public TextView replyto, comment, username, btnReply, reply, showReplies;
    public ImageButton upVote, downVote, send, close;
    public ImageView imgProfile;
    public RelativeLayout relativeLayout;
    public RecyclerView recyclerView;


    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        replyto = itemView.findViewById(R.id.txtReplies);
        comment = itemView.findViewById(R.id.txtComment);
        username = itemView.findViewById(R.id.txtUsername);
        btnReply = itemView.findViewById(R.id.btnReply);
        upVote = itemView.findViewById(R.id.btnUp);
        downVote = itemView.findViewById(R.id.btnDown);
        imgProfile = itemView.findViewById(R.id.imgProfile);
        relativeLayout = itemView.findViewById(R.id.replyContainer);
        reply = itemView.findViewById(R.id.txtReply);
        send = itemView.findViewById(R.id.btnSend);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        showReplies = itemView.findViewById(R.id.btnShowReplies);
        close = itemView.findViewById(R.id.btnClose);
    }
}

