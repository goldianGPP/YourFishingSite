package com.goldian.yourfishsingsite.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    ProgressDialogModel dialogModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        request();
    }

    private void init(){
        getSupportActionBar().setTitle("Tampil Lokasi");
        pref = new PreferencesModel(this,"login");
        dialogModel = new ProgressDialogModel(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void request(){
        dialogModel.show();
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
        dialogModel.dismiss();
    }

}
