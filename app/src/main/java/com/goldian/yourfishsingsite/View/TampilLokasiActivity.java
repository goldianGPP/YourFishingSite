package com.goldian.yourfishsingsite.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.EventAdapter;
import com.goldian.yourfishsingsite.View.Adapter.LokasiAdapter;

import java.util.List;

public class TampilLokasiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LokasiAdapter lokasiAdapter;
    PreferencesModel pref;
    SwipeRefreshLayout swipeRefresh;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        request();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            request();
        }
    }

    private void init(){
        getSupportActionBar().setTitle("Tampil Lokasi");
        pref = new PreferencesModel(this,"login");

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::request);

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(TampilLokasiActivity.this, TambahEventActivity.class);
            intent.putExtra("identity",4);
            startActivityForResult(intent, 1);
        });
    }

    private void request(){
        swipeRefresh.setRefreshing(true);
        new LokasiController(this)
                .getLokasi(
                        pref.read("id_pengguna")
                );
    }

    public void result(List<LokasiModel> lokasiModels){
        if (lokasiModels!=null){
            lokasiAdapter = new LokasiAdapter(this, lokasiModels);
            recyclerView.setAdapter(lokasiAdapter);
        }
        swipeRefresh.setRefreshing(false);
    }

}
