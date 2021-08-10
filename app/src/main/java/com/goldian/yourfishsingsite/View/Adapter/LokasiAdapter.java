package com.goldian.yourfishsingsite.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.CariBarangFragment;
import com.goldian.yourfishsingsite.View.UbahLokasiActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.LokasiHolder>{
    Context context;
    List<LokasiModel> list;

    public LokasiAdapter(Context context, List<LokasiModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LokasiAdapter.LokasiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_lokasi_vertical, viewGroup,false);
        return new LokasiAdapter.LokasiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LokasiAdapter.LokasiHolder holder, int position) {
        final LokasiModel currentData = list.get(position);
        holder.txtNama.setText(currentData.getNama());
        holder.txtIkan.setText(currentData.getIkan());
        holder.txtDeskripsi.setText(currentData.getDeskripsi());
        String url = context.getResources().getString(R.string.img_url_lokasi) + currentData.getImg();
        Picasso.get().load(url).into(holder.imgLokasi);

        holder.btnCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, UbahLokasiActivity.class);
            context.startActivity(setExtras(intent,currentData,url));
        });
    }

    private Intent setExtras(Intent intent,LokasiModel currentData, String url){
        intent.putExtra("id_lokasi", currentData.getId_lokasi());
        intent.putExtra("nama", currentData.getNama());
        intent.putExtra("ikan", currentData.getIkan());
        intent.putExtra("deskripsi", currentData.getDeskripsi());
        intent.putExtra("img", url);

        return intent;
    }

    public static class LokasiHolder extends RecyclerView.ViewHolder {
        protected TextView txtNama, txtIkan, txtDeskripsi;
        protected ImageView imgLokasi;
        protected CardView btnCard;

        public LokasiHolder(@NonNull View itemView) {
            super(itemView);
            imgLokasi = itemView.findViewById(R.id.imgLokasi);
            txtNama = itemView.findViewById(R.id.txtNama);
            txtIkan = itemView.findViewById(R.id.txtIkan);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsi);
            btnCard = itemView.findViewById(R.id.btnCard);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }
}
