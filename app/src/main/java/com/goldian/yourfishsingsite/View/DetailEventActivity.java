package com.goldian.yourfishsingsite.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

public class DetailEventActivity extends AppCompatActivity {

    ImageView imgEvent;
    TextView txtTitle, txtDeskripsi, txtLink;
    LinearLayout container;
    Button btnUpdate;
    String id_event, img;
    PreferencesModel pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        init();
        setValue();
        setListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init(){
        getSupportActionBar().setTitle("Detail Event");
        pref = new PreferencesModel(this, "login");
        imgEvent = findViewById(R.id.imgEvent);
        txtTitle = findViewById(R.id.txtTitle);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);
        container = findViewById(R.id.container);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        id_event = bundle.getString("id_event");
        img = bundle.getString("img");
        txtTitle.setText(bundle.getString("title"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        txtLink.setText(bundle.getString("link"));
        Picasso.get().load(img).into(imgEvent);

        if (bundle.getString("id_pengguna").equals(pref.read("id_pengguna")))
            btnUpdate.setVisibility(View.VISIBLE);

        if (!txtLink.getText().toString().equals(""))
            container.setVisibility(View.VISIBLE);
    }

    private Intent setExtras(Intent intent){
        intent.putExtra("id_event",id_event);
        intent.putExtra("img",img);
        intent.putExtra("title",txtTitle.getText().toString());
        intent.putExtra("deskripsi",txtDeskripsi.getText().toString());
        intent.putExtra("link",txtLink.getText().toString());

        return intent;
    }

    private void setListener(){
        btnUpdate.setOnClickListener(onClick);
        imgEvent.setOnClickListener(onClick);
    }

    //listener--------------
    //----------------------
    View.OnClickListener onClick = view -> {
        if (view == btnUpdate){
            Intent intent = new Intent(this, UbahEventActivity.class);
            startActivity(setExtras(intent));
        }
        else if (view == imgEvent){
            if (imgEvent.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgEvent.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };
}
