package com.goldian.yourfishsingsite.Model.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.R;

public class EventHolder extends RecyclerView.ViewHolder{
    public CardView card;
    public ImageView imgEvent;
    public TextView txtTitle, txtDeskripsi;

    public EventHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.btnAddEvent);
        imgEvent = itemView.findViewById(R.id.imgEvent);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtDeskripsi = itemView.findViewById(R.id.txtDeskripsi);
    }
}
