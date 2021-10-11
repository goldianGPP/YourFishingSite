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
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TambahLokasiActivity extends AppCompatActivity {

    Uri uri;

    PreferencesModel pref;
    ProgressDialogModel dialogModel;
    FilterModel filterModel;

    ImageView imgMap;
    TextView txtLatitude, txtLongitude, txtAlamat;
    EditText txtNama, txtDeskripsi, txtIkan;
    Button btnMap, btnImg;


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
        filterModel = new FilterModel();

        imgMap = findViewById(R.id.imgMap);
        btnImg = findViewById(R.id.btnImg);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtNama = findViewById(R.id.txtNama);
        txtIkan = findViewById(R.id.txtIkan);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        btnMap = findViewById(R.id.btnMap);
    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));
        txtAlamat.setText(bundle.getString("alamat"));
    }

    private void setListener(){
        btnMap.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
    }

    private void request(){
        dialogModel.show();
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody nama = imageModel.requestBody(txtNama.getText().toString());
        RequestBody ikan = imageModel.requestBody(txtIkan.getText().toString());
        RequestBody alamat = imageModel.requestBody(txtAlamat.getText().toString());
        RequestBody deskripsi = imageModel.requestBody(txtDeskripsi.getText().toString());
        RequestBody status = imageModel.requestBody(txtDeskripsi.getText().toString());
        RequestBody latitude = imageModel.requestBody(txtLatitude.getText().toString());
        RequestBody longitude = imageModel.requestBody(txtLongitude.getText().toString());
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");

        new LokasiController(this)
                .postLokasi(
                        id_pengguna,
                        nama,
                        ikan,
                        alamat,
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

    private boolean validate(){
        return new Validation()
                .isFieldOk(txtNama,1,20)
                .validate();
    }

    //listener
    //---------------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnMap){
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
    };
}
