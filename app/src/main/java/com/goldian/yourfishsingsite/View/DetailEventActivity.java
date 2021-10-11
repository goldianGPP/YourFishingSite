package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class DetailEventActivity extends AppCompatActivity {

    ImageView imgEvent;
    TextView txtTitle, txtDeskripsi, txtLink, txtDate;
    LinearLayout container, frame_container;
    ImageButton btnCommentDown, btnCommentUp;
    Button btnUpdate;
    String id_event, img;
    PreferencesModel pref;

    String key, tanggal, deskripsi;
    boolean isShown, flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        init();
        setValue();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (isShown)
            isShown = setContainer(false);
        else
            super.onBackPressed();
    }

    private void init(){
        getSupportActionBar().setTitle("Detail Event");
        pref = new PreferencesModel(this, "login");

        frame_container = findViewById(R.id.frame_container);
        imgEvent = findViewById(R.id.imgEvent);
        txtTitle = findViewById(R.id.txtTitle);
        txtDate = findViewById(R.id.txtDate);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);
        container = findViewById(R.id.container);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCommentUp = findViewById(R.id.btnCommentUp);
        btnCommentDown = findViewById(R.id.btnCommentDown);
    }

    @SuppressLint("SetTextI18n")
    private void setValue(){
        EventModel eventModel = new EventModel();
        Bundle bundle = getIntent().getExtras();
        id_event = bundle.getString("id_event");
        img = bundle.getString("img");
        tanggal = bundle.getString("tanggal");
        key = bundle.getString("key");
        deskripsi = bundle.getString("deskripsi");

        txtTitle.setText(bundle.getString("title"));
        txtDeskripsi.setText("deskripsi : \n\n" + deskripsi);
        txtLink.setText(bundle.getString("link"));

        txtDate.setText("tanggal : " + eventModel.setDateTimeString(eventModel.setCalendar(tanggal)));

        Glide.with(this)
                .load(img)
                .signature(new ObjectKey(key))
                .into(imgEvent);
        imgEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (!txtLink.getText().toString().equals(""))
            container.setVisibility(View.VISIBLE);

//        if (bundle.getString("id_pengguna").equals(pref.read("id_pengguna")))
//            btnUpdate.setVisibility(View.VISIBLE);

        if (bundle.getBoolean("update"))
            btnUpdate.setVisibility(View.VISIBLE);
    }

    private Intent setExtras(Intent intent){
        intent.putExtra("id_event",id_event);
        intent.putExtra("img",img);
        intent.putExtra("title",txtTitle.getText().toString());
        intent.putExtra("deskripsi",deskripsi);
        intent.putExtra("link",txtLink.getText().toString());
        intent.putExtra("tanggal",tanggal);
        intent.putExtra("key",key);

        return intent;
    }

    private void setListener(){
        btnUpdate.setOnClickListener(onClick);
        imgEvent.setOnClickListener(onClick);
        btnCommentUp.setOnClickListener(onClick);
        btnCommentDown.setOnClickListener(onClick);
    }

    private void setFragment(){
        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", "item"+id_event);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_comment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    private boolean setContainer(boolean visibility){
        if (visibility) {
            frame_container.animate().translationY(0);
            return true;
        }
        else {
            frame_container.animate().translationY(2000);
            return false;
        }
    }

    //listener--------------
    //----------------------
    View.OnClickListener onClick = view -> {
        if (view == btnUpdate){
            Intent intent = new Intent(this, UbahEventActivity.class);
            startActivityForResult(setExtras(intent),1);
        }
        else if (view == btnCommentUp || view == btnCommentDown){
            if (!flag) {
                flag = true;
                setFragment();
            }

            if (isShown)
                isShown = setContainer(false);
            else
                isShown = setContainer(true);
        }
        else if (view == imgEvent){
            if (imgEvent.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgEvent.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };
}
