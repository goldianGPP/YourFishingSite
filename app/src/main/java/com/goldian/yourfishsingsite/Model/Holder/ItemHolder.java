package com.goldian.yourfishsingsite.Model.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.R;

public class ItemHolder extends RecyclerView.ViewHolder {
    public TextView txtNama, txtHarga, txtJenis, txtDeskripsi;
    public RatingBar strRate;
    public CardView btnCard;
    public Button button;
    public ImageView imgItem;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        txtNama = itemView.findViewById(R.id.txtNama);
        txtHarga = itemView.findViewById(R.id.txtHarga);
        txtJenis = itemView.findViewById(R.id.txtJenis);
        txtDeskripsi = itemView.findViewById(R.id.txtDeskripsi);
        strRate = itemView.findViewById(R.id.strRate);
        btnCard = itemView.findViewById(R.id.btnCard);
        imgItem = itemView.findViewById(R.id.imgItem);
    }
}
