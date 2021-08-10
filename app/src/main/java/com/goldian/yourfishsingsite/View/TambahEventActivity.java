package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TambahEventActivity extends AppCompatActivity {
    Button btnImg, btnAddEvent;
    ImageView imgEvent;
    EditText txtTitle, txtDeskripsi, txtLink;
    ImageModel imageModel;
    ProgressDialogModel dialogModel;

    Uri uri;
    Bitmap bitmap;
    PreferencesModel pref;

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

        btnImg = findViewById(R.id.btnImg);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        imgEvent = findViewById(R.id.imgEvent);
        txtTitle = findViewById(R.id.txtTitle);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);
    }

    private void setListener(){
        btnAddEvent.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
    }

    private boolean isFieldEmpty(EditText editText){
        if (editText.getText().toString().equals("")) {
            editText.setError("isi");
            return false;
        }
        else
            return true;
    }

    private void request(){
        Bundle bundle = getIntent().getExtras();
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody title = imageModel.requestBody(txtTitle.getText().toString());
        RequestBody day = imageModel.requestBody(bundle.getString("day"));
        RequestBody month = imageModel.requestBody(bundle.getString("month"));
        RequestBody year = imageModel.requestBody(bundle.getString("year"));
        RequestBody link = imageModel.requestBody(txtLink.getText().toString());
        RequestBody deskripsi = imageModel.requestBody(txtDeskripsi.getText().toString());
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");

        dialogModel.show();
        new EventController(this)
                .postEvent(
                        id_pengguna,
                        title,
                        day,
                        month,
                        year,
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

    //Listener
    //-----------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnAddEvent){
            if (isFieldEmpty(txtLink) && isFieldEmpty(txtTitle) && isFieldEmpty(txtDeskripsi)) {
                if (!uri.equals(null))
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
    };
}
