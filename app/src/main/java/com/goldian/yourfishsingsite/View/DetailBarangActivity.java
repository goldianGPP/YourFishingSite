package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class DetailBarangActivity extends AppCompatActivity {

    LinearLayout container;
    ImageView imgItem;
    RatingBar ratingItem;
    Button btnLink, btnUpdate;
    TextView txtDescription, txtItemName, txtItemHarga, txtJenis;
    ImageButton btnCommentDown, btnCommentUp;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;

    PreferencesModel pref;
    String harga,  url, id_item, jenis, id_pengguna, img;
    boolean isShown = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        init();
        setListener();
    }

    private void init() {
        getSupportActionBar().setTitle("Detail Barang");
        pref = new PreferencesModel(this,getResources().getString(R.string.login));
        container = findViewById(R.id.frame_container);
        imgItem = findViewById(R.id.imgItem);
        ratingItem = findViewById(R.id.ratingItem);
        btnLink = findViewById(R.id.btnLink);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtDescription = findViewById(R.id.txtDeskripsi);
        txtItemHarga = findViewById(R.id.txtItemHarga);
        txtItemName = findViewById(R.id.txtItemName);
        btnCommentUp = findViewById(R.id.btnCommentUp);
        btnCommentDown = findViewById(R.id.btnCommentDown);
        txtJenis = findViewById(R.id.txtJenis);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        setValues();
        setFragment();
        request();
    }

    @Override
    public void onBackPressed() {
        if (isShown)
            isShown = setContainer(false);
        else
            super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void setValues(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jenis = bundle.getString("jenis");
            url = bundle.getString("url");
            id_item = bundle.getString("id_item");
            id_pengguna = bundle.getString("id_pengguna");
            harga = bundle.getString("harga");
            img = bundle.getString("img");

            txtItemName.setText(bundle.getString("nama"));
            txtItemHarga.setText(new FilterModel().setToRupiah(harga));
            ratingItem.setRating(bundle.getFloat("rating",0f));
            txtJenis.setText("tag : " + jenis);
            txtDescription.setText("deskripsi : \n\n" + bundle.getString("deskripsi"));
            Picasso.get().load(img).into(imgItem);

            if (Objects.equals(bundle.getString("id_pengguna"), pref.read("id_pengguna")))
                btnUpdate.setVisibility(View.VISIBLE);
        }
        else
            finish();
    }

    private void setListener(){
        btnLink.setOnClickListener(onClick);
        btnUpdate.setOnClickListener(onClick);
        btnCommentUp.setOnClickListener(onClick);
        btnCommentDown.setOnClickListener(onClick);
        imgItem.setOnClickListener(onClick);
    }

    private void openWeb(){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setFragment(){
        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", "item"+id_item);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_comment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    private boolean setContainer(boolean visibility){
        if (visibility) {
            container.animate().translationY(0);
            return true;
        }
        else {
            container.animate().translationY(2000);
            return false;
        }
    }

    private Intent setExtras(Intent intent){
        intent.putExtra("id_item",id_item);
        intent.putExtra("id_pengguna",id_pengguna);
        intent.putExtra("img",img);
        intent.putExtra("nama",txtItemName.getText().toString());
        intent.putExtra("harga",txtItemHarga.getText().toString());
        intent.putExtra("jenis",jenis);
        intent.putExtra("deskripsi",txtDescription.getText().toString());
        intent.putExtra("url",url);

        return intent;
    }

    private void request(){
        new ItemController(this)
                .getRecomendation(
                        pref.read("id_pengguna"),
                        jenis,
                        id_item
                );
    }

    public void result(List<ItemModel> itemModelList){
        if (itemModelList!=null){
            itemAdapter = new ItemAdapter(this, itemModelList, itemModelList.size(), "horizontal");
            recyclerView.setAdapter(itemAdapter);
        }
    }

    //listener
    //--------------
    private final View.OnClickListener onClick = view -> {
        if (view == btnLink)
            openWeb();
        else if (view == btnUpdate){
            Intent intent = new Intent(this, UbahBarangActivity.class);
            startActivity(setExtras(intent));
        }
        else if (view == btnCommentUp || view == btnCommentDown){
            if (isShown)
                isShown = setContainer(false);
            else
                isShown = setContainer(true);
        }
        else if (view == imgItem){
            if (imgItem.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };
}
