package com.goldian.yourfishsingsite.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.Holder.EventHolder;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.DetailEventActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
    Context context;
    List<EventModel> list;
    String id_pengguna;
    ImageModel imageModel;

    public EventAdapter(Context context, List<EventModel> list, String id_pengguna) {
        this.context = context;
        this.list = list;
        this.id_pengguna = id_pengguna;
        imageModel = new ImageModel();
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventHolder(LayoutInflater.from(context).inflate(R.layout.view_event_vertical,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        final EventModel currentData = list.get(position);
        String url = context.getResources().getString(R.string.img_url_event)+currentData.getImg();
        Picasso.get().load(url).into(holder.imgEvent);
        holder.txtTitle.setText(currentData.getTitle());
        holder.txtDeskripsi.setText(currentData.getDeskripsi());

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailEventActivity.class);
            intent.putExtra("id_pengguna", currentData.getId_pengguna());
            intent.putExtra("id_event", currentData.getId_event());
            intent.putExtra("title", currentData.getTitle());
            intent.putExtra("deskripsi", currentData.getDeskripsi());
            intent.putExtra("link", currentData.getLink());
            intent.putExtra("img", url);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
