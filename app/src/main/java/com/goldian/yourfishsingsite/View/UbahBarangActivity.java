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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahBarangActivity extends AppCompatActivity {

    ImageView imgItem;
    EditText txtItemLink, txtItemName, txtItemHarga, txtItemTag, txtItemDeskripsi;
    Button btnPost, btnGallery, btnDelete;
    String id_item;
    Uri uri;
    Bitmap bitmap;
    ItemController itemController;
    PreferencesModel pref;
    ProgressDialogModel dialogModel;
    AlertDialog.Builder builder;
    boolean isUpdate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
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

    @SuppressLint("SetTextI18n")
    private void init(){
        getSupportActionBar().setTitle("Ubah Barang");
        pref = new PreferencesModel(this, "login");
        builder = new AlertDialog.Builder(this);
        dialogModel = new ProgressDialogModel(this);
        itemController = new ItemController(this);

        imgItem = findViewById(R.id.imgItem);
        txtItemLink = findViewById(R.id.txtItemLink);
        txtItemName = findViewById(R.id.txtItemName);
        txtItemHarga = findViewById(R.id.txtItemHarga);
        txtItemTag = findViewById(R.id.txtItemTag);
        txtItemDeskripsi = findViewById(R.id.txtItemDeskripsi);
        btnPost = findViewById(R.id.btnPost);
        btnGallery = findViewById(R.id.btnGallery);
        btnDelete = findViewById(R.id.btnDelete);

        btnGallery.setText("ubah gambar");
        btnPost.setText("ubah");
        imgItem.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        txtItemLink.setText(bundle.getString("url"));
        txtItemName.setText(bundle.getString("nama"));
        txtItemHarga.setText(bundle.getString("harga"));
        txtItemTag.setText(bundle.getString("jenis"));
        txtItemDeskripsi.setText(bundle.getString("deskripsi"));
        id_item = bundle.getString("id_item");
        Picasso.get().load(bundle.getString("img")).into(imgItem);
    }

    private void setListener(){
        btnPost.setOnClickListener(onCLick);
        btnGallery.setOnClickListener(onCLick);
        btnDelete.setOnClickListener(onCLick);
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
        RequestBody id_item = imageModel.requestBody(this.id_item);
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        itemController.updateItem(
                id_item,
                file
        );
    }

    private void request(){
        dialogModel.show();
        isUpdate = true;
        itemController.updateItem(
                id_item,
                txtItemName.getText().toString(),
                txtItemTag.getText().toString(),
                txtItemDeskripsi.getText().toString(),
                txtItemHarga.getText().toString(),
                txtItemLink.getText().toString()
        );
    }

    public void result(boolean bool){
        if (bool){
            if (isUpdate)
                finish();
            else
                imgItem.setImageBitmap(bitmap);
        }
        dialogModel.dismiss();
    }

    //Listener
    //-------------
    @SuppressLint("IntentReset")
    View.OnClickListener onCLick = view -> {
        if (view == btnPost){
            if (isFieldEmpty(txtItemLink) && isFieldEmpty(txtItemName) && isFieldEmpty(txtItemHarga) && isFieldEmpty(txtItemTag) && isFieldEmpty(txtItemDeskripsi))
                request();
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