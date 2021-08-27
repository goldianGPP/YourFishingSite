package com.goldian.yourfishsingsite.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.ItemAdapter;

import java.util.List;

public class TampilBarangActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    PreferencesModel pref;
    ItemController itemController;
    SwipeRefreshLayout swipeRefresh;

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
        getSupportActionBar().setTitle("Tampil Barang");
        pref = new PreferencesModel(this,"login");
        itemController = new ItemController(this);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::request);
    }

    private void request(){
        swipeRefresh.setRefreshing(true);
        if (getIntent().getExtras().getBoolean("flag")){
            itemController
                .getMyItems(
                        pref.read("id_pengguna")
                );
        }
        else {
            itemController
                .getRatedItems(
                        pref.read("id_pengguna")
                );
        }
    }

    public void result(List<ItemModel> itemModels){
        if (itemModels!=null){
            itemAdapter = new ItemAdapter(this, itemModels, itemModels.size(), "vertical");
            recyclerView.setAdapter(itemAdapter);
        }
        swipeRefresh.setRefreshing(false);
    }


}
