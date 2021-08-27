package com.goldian.yourfishsingsite.View.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.Holder.ItemHolder;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.DetailBarangActivity;
import com.goldian.yourfishsingsite.View.TampilBarangActivity;
import com.goldian.yourfishsingsite.View.TampilEventActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> implements Filterable {
    private final Context context;
    List<ItemModel> list;
    List<ItemModel> listFull;
    View view;
    String type;
    int size;

    public ItemAdapter(Context context, List<ItemModel> list, int size, String type) {
        this.list = list;
        listFull = new ArrayList<>(list);
        this.context = context;
        this.size = size;
        this.type = type;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if(type.equals("horizontal"))
            view = LayoutInflater.from(context).inflate(R.layout.view_item_horizontal,viewGroup,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.view_item_vertical,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 5) ? 6 : position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final ItemModel currentData = list.get(position);
        init (holder, currentData);
    }

    @Override
    public int getItemCount() {
        if(!type.equals("horizontal"))
            return list == null ? 0 : list.size();
        return size;
    }

    //----------------------------------------CODE---------------------------------------------------------------------------------------------

    private String is20Character(String text){
        if (text.length() > 20){
            return text.toLowerCase().substring(0,17) + "...";
        }
        return text;
    }

    @SuppressLint("SetTextI18n")
    void init (ItemHolder holder, final ItemModel currentData){
        String nama, harga , tags;

        nama = is20Character(currentData.getNama());
        tags = is20Character(currentData.getJenis());

        if (type.equals("horizontal")){
            holder.strRate.setRating(currentData.getRating());
        }
        else {
            holder.txtDeskripsi.setText(currentData.getDeskripsi());
        }

        harga = currentData.getHargaInRupiah();
        holder.txtNama.setText(nama);

        holder.txtHarga.setText(harga);
        holder.txtJenis.setText(tags);
        holder.btnCard.setOnClickListener(onClick(currentData, holder));

        Glide.with(context)
            .load(currentData.getImg())
            .signature(new ObjectKey(currentData.getImg_key()))
            .into(holder.imgItem);
    }

    private Intent setExtras(Intent intent,final ItemModel currentData){
        intent.putExtra("id_item",currentData.getId_item());
        intent.putExtra("id_pengguna",currentData.getId_pengguna());
        intent.putExtra("img",currentData.getImg());
        intent.putExtra("nama",currentData.getNama());
        intent.putExtra("harga",currentData.getHarga());
        intent.putExtra("rating",currentData.getRating());
        intent.putExtra("jenis",currentData.getJenis());
        intent.putExtra("deskripsi",currentData.getDeskripsi());
        intent.putExtra("url",currentData.getWeb());
        intent.putExtra("key",currentData.getImg_key());

        return intent;
    }

    //listener
    //------------
    View.OnClickListener onClick(final ItemModel currentData, final ItemHolder holder){
        return view -> {
            Intent intent = new Intent(context, DetailBarangActivity.class);
            intent = setExtras(intent, currentData);
            if (context instanceof  TampilBarangActivity)
                ((TampilBarangActivity)context).startActivityForResult(intent,1);
            else
                context.startActivity(intent);
        };
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ItemModel> filterResults = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filterResults.addAll(listFull);
            }
            else {
                String filterPatern = charSequence.toString().toLowerCase();
                for (ItemModel itemModel : listFull){
                    if((itemModel.getNama() + itemModel.getJenis()).toLowerCase().contains(filterPatern)){
                        filterResults.add(itemModel);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterResults;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
