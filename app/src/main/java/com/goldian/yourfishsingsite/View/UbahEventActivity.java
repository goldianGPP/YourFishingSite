package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.DialogModel;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahEventActivity extends AppCompatActivity {
    Button btnImg, btnAddEvent, btnDelete;
    ImageView imgEvent;
    ImageButton btnDate;
    EditText txtTitle, txtDeskripsi, txtLink, txtDate;
    ImageModel imageModel;
    ProgressDialogModel dialogModel;
    AlertDialog.Builder builder;
    EventController eventController;
    FilterModel filterModel;
    CalendarView datePicker;

    Uri uri;
    Bitmap bitmap;
    PreferencesModel pref;

    String id_event, img;
    boolean isUpdate;
    Integer month, year, day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);
        init();
        setValue();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
                try {
                    uri = data.getData();
                    final InputStream imageStream;
                    imageStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    dialog();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //initialize object and other
    @SuppressLint("SetTextI18n")
    private void init(){
        getSupportActionBar().setTitle("Ubah Event");

        pref = new PreferencesModel(this,"login");
        imageModel = new ImageModel(this);
        dialogModel = new ProgressDialogModel(this);
        builder = new AlertDialog.Builder(this);
        eventController = new EventController(this);
        filterModel = new FilterModel();

        btnImg = findViewById(R.id.btnImg);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnDelete = findViewById(R.id.btnDelete);
        btnDate = findViewById(R.id.btnDate);
        txtDate = findViewById(R.id.txtDate);
        imgEvent = findViewById(R.id.imgEvent);
        txtTitle = findViewById(R.id.txtTitle);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);
        datePicker = findViewById(R.id.datePicker);

        btnImg.setText("ubah gambar");
        btnAddEvent.setText("ubah");
        imgEvent.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }

    //initialize value
    @SuppressLint("SetTextI18n")
    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        id_event = bundle.getString("id_event");
        day = bundle.getInt("day");
        month = bundle.getInt("month");
        year = bundle.getInt("year");
        img = bundle.getString("img");

        txtTitle.setText(bundle.getString("title"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        txtLink.setText(bundle.getString("link"));
        txtDate.setText(day+"/"+month+"/"+year);

        Glide.with(this)
                .load(img)
                .signature(new ObjectKey(bundle.getString("key")))
                .into(imgEvent);
        imgEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //set listener
    private void setListener(){
        btnAddEvent.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
        btnDelete.setOnClickListener(onClick);
        datePicker.setOnDayClickListener(onDate);
        btnDate.setOnClickListener(onClick);
        imgEvent.setOnClickListener(onClick);
    }

    //set dialog for updating image
    public void dialog(){
        new DialogModel(this, R.layout.view_dialog)
                .defaultView();
    }

    //update data image in server
    public void updateImg(){
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_event = imageModel.requestBody(this.id_event);
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody cur_img = imageModel.requestBody(img.replace(new ImageModel().getBase_url(),""));
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        eventController.updateEvent(
                id_event,
                id_pengguna,
                cur_img,
                file
        );
    }

    //update data in database
    private void update(){
        dialogModel.show();
        isUpdate = true;
        EventModel eventModel = new EventModel();

        eventController.updateEvent(
            eventModel
                .setId_event(id_event)
                .setDay(day.toString())
                .setMonth(String.valueOf(month))
                .setYear(year.toString())
                .setTitle(txtTitle.getText().toString())
                .setDeskripsi(txtDeskripsi.getText().toString())
                .setLink(txtLink.getText().toString())
        );
    }

    //delete data in database
    private void delete(){
        dialogModel.show();
        isUpdate = true;
        eventController.deleteEvent(
                id_event,
                img.replace(new ImageModel().getBase_url(),"")
        );
    }

    //get result from database into the view
    public void result(boolean bool){
        dialogModel.dismiss();
        if (bool){
            if (isUpdate) {
                setResult(RESULT_OK);
                finish();
            }
            else {
                imgEvent.setImageBitmap(bitmap);
            }
        }
    }

    private boolean validate(){
        return new Validation()
                .isEmpty(txtLink)
                .isFieldOk(txtTitle,1,20)
                .isEmpty(txtDeskripsi)
                .isEmpty(txtDate)
                .validate();
    }

    //Listener
    //-----------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnAddEvent){
            if (validate()) {
                update();
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();;
        }
        else if (view == btnImg){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if (view == btnDelete){
            delete();
        }
        else if (view == btnDate){
            if (datePicker.getVisibility() == View.VISIBLE)
                datePicker.setVisibility(View.GONE);
            else
                datePicker.setVisibility(View.VISIBLE);
        }
        else if (view == imgEvent){
            if (imgEvent.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgEvent.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };

    @SuppressLint("SetTextI18n")
    OnDayClickListener onDate = eventDay -> {
        Calendar calendar = eventDay.getCalendar();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        txtDate.setText(day+"/"+month+"/"+year);
        datePicker.setVisibility(View.GONE);
    };

}
