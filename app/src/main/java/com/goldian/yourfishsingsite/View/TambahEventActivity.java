package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TambahEventActivity extends AppCompatActivity {
    Button btnImg, btnAddEvent;
    ImageView imgEvent;
    ImageButton btnDate;
    EditText txtTitle, txtDeskripsi, txtLink;
    TextView txtDate;
    ImageModel imageModel;
    ProgressDialogModel dialogModel;
    FilterModel filterModel;
    CalendarView datePicker;
    NumberPicker pcrJam, pcrMenit;

    Uri uri;
    Bitmap bitmap;
    PreferencesModel pref;
    Integer month, year, day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);
        init();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
                try {
                    uri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    imgEvent.setImageBitmap(bitmap);
                    imgEvent.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        getSupportActionBar().setTitle("Tambah Event");

        imageModel = new ImageModel(this);
        dialogModel = new ProgressDialogModel(this);
        pref = new PreferencesModel(this,"login");
        filterModel = new FilterModel();

        btnImg = findViewById(R.id.btnImg);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnDate = findViewById(R.id.btnDate);
        imgEvent = findViewById(R.id.imgEvent);
        txtDate = findViewById(R.id.txtDate);
        txtTitle = findViewById(R.id.txtTitle);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);
        datePicker = findViewById(R.id.datePicker);
        pcrJam = findViewById(R.id.pcrJam);
        pcrJam.setMaxValue(23);
        pcrJam.setMinValue(0);
        pcrMenit = findViewById(R.id.pcrMenit);
        pcrMenit.setMaxValue(59);
        pcrMenit.setMinValue(0);
    }

    private void setListener(){
        btnAddEvent.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
        btnDate.setOnClickListener(onClick);
        datePicker.setOnDayClickListener(onDate);
    }

    private void request(){
        Bundle bundle = getIntent().getExtras();
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody title = imageModel.requestBody(txtTitle.getText().toString());
        RequestBody tanggal = imageModel.requestBody(new EventModel().setTanggal(txtDate.getText().toString(), pcrJam.getValue(), pcrMenit.getValue()).getTanggal());
        RequestBody link = imageModel.requestBody(txtLink.getText().toString());
        RequestBody deskripsi = imageModel.requestBody(txtDeskripsi.getText().toString());
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");

        dialogModel.show();
        new EventController(this)
                .postEvent(
                        id_pengguna,
                        title,
                        tanggal,
                        link,
                        deskripsi,
                        file
                );
    }

    public void result(boolean bool){
        if (bool){
            setResult(RESULT_OK);
            finish();
        }
        dialogModel.dismiss();
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
                if (uri != null)
                    request();
                else
                    Toast.makeText(this, "masukkan gambar", Toast.LENGTH_LONG).show();;
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();;
        }
        else if (view == btnImg){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if (view == btnDate){
            if (datePicker.getVisibility() == View.VISIBLE)
                datePicker.setVisibility(View.GONE);
            else
                datePicker.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("SetTextI18n")
    OnDayClickListener onDate = eventDay -> {
        Calendar calendar = eventDay.getCalendar();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        txtDate.setText(day+"-"+month+"-"+year);
        datePicker.setVisibility(View.GONE);
    };
}
