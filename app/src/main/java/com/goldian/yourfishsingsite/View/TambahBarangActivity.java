package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TambahBarangActivity extends AppCompatActivity {

    ImageView imgItem;
    ListView listBarang;
    EditText txtItemLink, txtItemName, txtItemHarga, txtItemTag, txtItemDeskripsi;
    Button btnPost, btnGallery;
    PreferencesModel pref;
    Uri uri;
    ProgressDialogModel dialogModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        init();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            try {
                uri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgItem.setImageBitmap(selectedImage);
                imgItem.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(){
        getSupportActionBar().setTitle("Tambah Barang");
        pref = new PreferencesModel(this, "login");
        dialogModel = new ProgressDialogModel(this);
        imgItem = findViewById(R.id.imgItem);
        txtItemLink = findViewById(R.id.txtItemLink);
        txtItemName = findViewById(R.id.txtItemName);
        txtItemHarga = findViewById(R.id.txtItemHarga);
        txtItemTag = findViewById(R.id.txtItemTag);
        txtItemDeskripsi = findViewById(R.id.txtItemDeskripsi);
        btnPost = findViewById(R.id.btnPost);
        btnGallery = findViewById(R.id.btnGallery);
    }

    private void setListener(){
        btnPost.setOnClickListener(onCLick);
        btnGallery.setOnClickListener(onCLick);
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
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        RequestBody nama = imageModel.requestBody(txtItemName.getText().toString());
        RequestBody jenis = imageModel.requestBody(txtItemTag.getText().toString().trim());
        RequestBody deskripsi = imageModel.requestBody(txtItemDeskripsi.getText().toString());
        RequestBody harga = imageModel.requestBody(txtItemHarga.getText().toString());
        RequestBody web = imageModel.requestBody(txtItemLink.getText().toString());
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        new ItemController(this).postItem(
                id_pengguna,
                nama,
                jenis,
                deskripsi,
                harga,
                web,
                file
        );
    }

    public void result(boolean bool){
        if (bool){
            finish();
        }
        dialogModel.dismiss();
    }

    //Listener
    //-------------
    @SuppressLint("IntentReset")
    View.OnClickListener onCLick = view -> {
        if (view == btnPost){
            if (isFieldEmpty(txtItemLink) && isFieldEmpty(txtItemName) && isFieldEmpty(txtItemHarga) && isFieldEmpty(txtItemTag) && isFieldEmpty(txtItemDeskripsi)) {
                if (uri != null)
                    request();
                else
                    Toast.makeText(this, "masukkan gambar", Toast.LENGTH_LONG).show();;
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();;
        }
        else if (view == btnGallery){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    };
}
