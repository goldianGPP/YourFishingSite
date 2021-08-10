package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TambahLokasiActivity extends AppCompatActivity {

    ImageView imgMap;
    TextView txtLatitude, txtLongitude;
    EditText txtNama, txtDeskripsi;
    PreferencesModel pref;
    Button btnMap, btnImg;
    Uri uri;
    ProgressDialogModel dialogModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lokasi);
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
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    imgMap.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                    imgMap.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        getSupportActionBar().setTitle("Tambah Lokasi");
        pref = new PreferencesModel(this,"login");
        dialogModel = new ProgressDialogModel(this);
        imgMap = findViewById(R.id.imgMap);
        btnImg = findViewById(R.id.btnImg);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtNama = findViewById(R.id.txtNama);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        btnMap = findViewById(R.id.btnMap);
    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));
    }

    private void setListener(){
        btnMap.setOnClickListener(onClick);
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
        dialogModel.show();
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody nama = imageModel.requestBody(txtNama.getText().toString());
        RequestBody deskripsi = imageModel.requestBody(txtDeskripsi.getText().toString());
        RequestBody status = imageModel.requestBody(txtDeskripsi.getText().toString());
        RequestBody latitude = imageModel.requestBody(txtLatitude.getText().toString());
        RequestBody longitude = imageModel.requestBody(txtLongitude.getText().toString());
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");

        new LokasiController(this)
                .postLokasi(
                        id_pengguna,
                        nama,
                        deskripsi,
                        status,
                        latitude,
                        longitude,
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

    //listener
    //---------------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnMap){
            if (isFieldEmpty(txtNama) && isFieldEmpty(txtDeskripsi) && isFieldEmpty(txtDeskripsi)) {
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
