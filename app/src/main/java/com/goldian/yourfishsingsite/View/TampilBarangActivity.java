package com.goldian.yourfishsingsite.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.ItemAdapter;

import java.util.List;

public class TampilBarangActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    PreferencesModel pref;
    ProgressDialogModel dialogModel;
    ItemController itemController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        request();
    }

    private void init(){
        getSupportActionBar().setTitle("Tampil Barang");
        pref = new PreferencesModel(this,"login");
        itemController = new ItemController(this);
        dialogModel = new ProgressDialogModel(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void request(){
        dialogModel.show();
        if (getIntent().getExtras().getBoolean("flag")){
            itemController
                .getItems(
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
        dialogModel.dismiss();
    }


}
