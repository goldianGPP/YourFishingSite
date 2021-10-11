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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Model.DialogModel;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahLokasiActivity extends AppCompatActivity {

    ImageView imgLokasi;
    TextView txtLatitude, txtLongitude, txtAlamat;
    EditText txtNama, txtDeskripsi, txtIkan;
    PreferencesModel pref;
    Button btnMap, btnImg, btnDelete;
    AlertDialog.Builder builder;
    Uri uri;
    Bitmap bitmap;
    ProgressDialogModel dialogModel;
    LokasiController lokasiController;
    FilterModel filterModel;

    boolean isUpdate;
    String id_lokasi, img;

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

    //initialize object and other
    private void init(){
        getSupportActionBar().setTitle("Ubah Lokasi");

        pref = new PreferencesModel(this,"login");
        builder = new AlertDialog.Builder(this);
        dialogModel = new ProgressDialogModel(this);
        lokasiController = new LokasiController(this);
        filterModel = new FilterModel();

        btnImg = findViewById(R.id.btnImg);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtNama = findViewById(R.id.txtNama);
        txtIkan = findViewById(R.id.txtIkan);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        btnMap = findViewById(R.id.btnMap);
        imgLokasi = findViewById(R.id.imgMap);
        btnDelete = findViewById(R.id.btnDelete);

        imgLokasi.setVisibility(View.VISIBLE);
        btnImg.setText("ubah gambar");
        btnMap.setText("ubah");
        btnDelete.setVisibility(View.VISIBLE);
    }

    //initialize value
    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        id_lokasi = bundle.getString("id_lokasi");
        img = bundle.getString("img");

        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));
        txtNama.setText(bundle.getString("nama"));
        txtIkan.setText(bundle.getString("ikan"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        txtAlamat.setText(bundle.getString("alamat"));

        Glide.with(this)
                .load(img)
                .signature(new ObjectKey(bundle.getString("key")))
                .into(imgLokasi);
        imgLokasi.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //set listener
    private void setListener(){
        btnMap.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
        btnDelete.setOnClickListener(onClick);
        imgLokasi.setOnClickListener(onClick);
    }

    //set dialog for updating image
    public void dialog(){
        new DialogModel(this, R.layout.view_dialog)
                .defaultView();
    }

    //update data image in server
    public void updateImg(){
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_lokasi = imageModel.requestBody(this.id_lokasi);
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody cur_img = imageModel.requestBody(img.replace(new ImageModel().getBase_url(),""));
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        lokasiController.updateLokasi(
                id_lokasi,
                id_pengguna,
                cur_img,
                file
        );
    }

    //update data in database
    private void update(){
        dialogModel.show();
        isUpdate = true;
        LokasiModel lokasiModel = new LokasiModel();
        lokasiController.updateLokasi(
            lokasiModel
                .setId_lokasi(id_lokasi)
                .setNama(txtNama.getText().toString())
                .setIkan(txtIkan.getText().toString())
                .setDeskripsi(txtDeskripsi.getText().toString())
        );
    }

    //delete data in database
    private void delete(){
        dialogModel.show();
        isUpdate = true;
        lokasiController.deleteLokasi(
                id_lokasi,
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
            else
                imgLokasi.setImageBitmap(bitmap);
        }
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
                update();
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();
        }
        else if (view == btnImg){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if (view == btnDelete){
            delete();
        }
        else if (view == imgLokasi){
            if (imgLokasi.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgLokasi.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgLokasi.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };

}
