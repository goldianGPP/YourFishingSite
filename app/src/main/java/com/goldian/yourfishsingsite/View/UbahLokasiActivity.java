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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahLokasiActivity extends AppCompatActivity {

    ImageView imgMap;
    TextView txtLatitude, txtLongitude;
    EditText txtNama, txtDeskripsi, txtIkan;
    PreferencesModel pref;
    Button btnMap, btnImg, btnDelete;
    AlertDialog.Builder builder;
    Uri uri;
    Bitmap bitmap;
    ProgressDialogModel dialogModel;
    LokasiController lokasiController;

    boolean isUpdate;
    String id_lokasi ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lokasi);
        init();
        setListener();
        setValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
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

    private void init(){
        getSupportActionBar().setTitle("Ubah Lokasi");
        pref = new PreferencesModel(this,"login");
        builder = new AlertDialog.Builder(this);
        dialogModel = new ProgressDialogModel(this);
        lokasiController = new LokasiController(this);

        btnImg = findViewById(R.id.btnImg);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtNama = findViewById(R.id.txtNama);
        txtIkan = findViewById(R.id.txtIkan);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        btnMap = findViewById(R.id.btnMap);
        imgMap = findViewById(R.id.imgMap);
        btnDelete = findViewById(R.id.btnDelete);

        imgMap.setVisibility(View.VISIBLE);
        btnImg.setText("ubah gambar");
        btnMap.setText("ubah");
        btnDelete.setVisibility(View.VISIBLE);
    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));

        id_lokasi = bundle.getString("id_lokasi");
        txtNama.setText(bundle.getString("nama"));
        txtIkan.setText(bundle.getString("ikan"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        Picasso.get().load(bundle.getString("img")).into(imgMap);
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

    public void dialog(){
        builder.setMessage("Gambar Akan Diubah").setTitle("PERINGATAN");
        builder.setPositiveButton("ok", (dialog, id) -> {
            updateImg();
            dialog.dismiss();
        });
        builder.setNegativeButton("batal", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateImg(){
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_lokasi = imageModel.requestBody(this.id_lokasi);
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        lokasiController.updateLokasi(
                id_lokasi,
                file
        );
    }

    private void request(){
        dialogModel.show();
        isUpdate = true;
        lokasiController.updateLokasi(
                id_lokasi,
                txtNama.getText().toString(),
                txtIkan.getText().toString(),
                txtDeskripsi.getText().toString()
        );
    }

    //listener
    //---------------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnMap){
            if (isFieldEmpty(txtNama) && isFieldEmpty(txtIkan) && isFieldEmpty(txtDeskripsi)) {
                request();
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();
        }
        else if (view == btnImg){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    };

}
