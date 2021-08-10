package com.goldian.yourfishsingsite.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.EventAdapter;

import java.util.List;

public class TampilEventActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
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
        getSupportActionBar().setTitle("Tampil Event");
        pref = new PreferencesModel(this,"login");
        dialogModel = new ProgressDialogModel(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void request(){
        dialogModel.show();
        new EventController(this)
            .getEvent(
                    pref.read("id_pengguna")
            );
    }

    public void result(List<EventModel> eventModels){
        if (eventModels!=null){
            eventAdapter = new EventAdapter(this, eventModels, pref.read("id_pengguna"));
            recyclerView.setAdapter(eventAdapter);
        }
        dialogModel.dismiss();
    }

}
