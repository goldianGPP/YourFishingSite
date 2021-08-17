package com.goldian.yourfishsingsite.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.CariBarangFragment;

import java.util.List;

public class JenisAdapter extends RecyclerView.Adapter<JenisAdapter.JenisHolder>{
    Context context;
    CariBarangFragment fragment;
    List<String> list;

    public JenisAdapter(Context context, CariBarangFragment fragment, List<String> list) {
        this.context = context;
        this.fragment = fragment;
        this.list = list;
    }

    @NonNull
    @Override
    public JenisHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_jenis_horizontal,viewGroup,false);
        return new JenisHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JenisHolder holder, int position) {
        final String jenis = list.get(position);
        holder.txtJenis.setText(jenis);
        holder.btnCard.setOnClickListener(view -> {
            Toast.makeText(context, jenis, Toast.LENGTH_SHORT).show();
            fragment.request(jenis,false);
        });
    }

    public static class JenisHolder extends RecyclerView.ViewHolder {
        protected TextView txtJenis;
        protected CardView btnCard;

        public JenisHolder(@NonNull View itemView) {
            super(itemView);
            txtJenis = itemView.findViewById(R.id.txtJenis);
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
