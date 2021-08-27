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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.DialogModel;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
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
    String id_item, img;
    Uri uri;
    Bitmap bitmap;
    ItemController itemController;
    PreferencesModel pref;
    ProgressDialogModel dialogModel;
    AlertDialog.Builder builder;
    FilterModel filterModel;
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

    //initialize object and other
    @SuppressLint("SetTextI18n")
    private void init(){
        getSupportActionBar().setTitle("Ubah Barang");

        pref = new PreferencesModel(this, "login");
        builder = new AlertDialog.Builder(this);
        dialogModel = new ProgressDialogModel(this);
        itemController = new ItemController(this);
        filterModel = new FilterModel();

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

    //initialize value
    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        id_item = bundle.getString("id_item");
        img = bundle.getString("img");

        txtItemLink.setText(bundle.getString("url"));
        txtItemName.setText(bundle.getString("nama"));
        txtItemHarga.setText(bundle.getString("harga"));
        txtItemTag.setText(bundle.getString("jenis"));
        txtItemDeskripsi.setText(bundle.getString("deskripsi"));

        Glide.with(this)
                .load(img)
                .signature(new ObjectKey(bundle.getString("key")))
                .into(imgItem);
        imgItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //set listener
    private void setListener(){
        btnPost.setOnClickListener(onCLick);
        btnGallery.setOnClickListener(onCLick);
        btnDelete.setOnClickListener(onCLick);
        imgItem.setOnClickListener(onCLick);
    }

    //set dialog for updating image
    public void dialog(){
        new DialogModel(this, R.layout.view_dialog)
                .defaultView();
    }

    //update data image in server
    public void updateImg(){
        ImageModel imageModel = new ImageModel(this);
        RequestBody id_item = imageModel.requestBody(this.id_item);
        RequestBody cur_img = imageModel.requestBody(img.replace(new ImageModel().getBase_url(),""));
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        itemController.updateItem(
                id_item,
                cur_img,
                file
        );
    }

    //update data in database
    private void update(){
        dialogModel.show();
        isUpdate = true;
        ItemModel itemModel = new ItemModel();
        itemController.updateItem(
            itemModel
                .setId_item(id_item)
                .setNama(txtItemName.getText().toString())
                .setJenis(txtItemTag.getText().toString())
                .setDeskripsi(txtItemDeskripsi.getText().toString())
                .setHarga(txtItemHarga.getText().toString())
                .setWeb(txtItemLink.getText().toString())
        );
    }

    //delete data in database
    private void delete(){
        dialogModel.show();
        isUpdate = true;
        itemController.deleteItem(id_item);
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
                imgItem.setImageBitmap(bitmap);
        }
    }

    private boolean validate(){
        return new Validation()
                .isEmpty(txtItemLink)
                .isFieldOk(txtItemName,1,20)
                .isEmpty(txtItemHarga)
                .isEmpty(txtItemTag)
                .validate();
    }

    //Listener
    //-------------
    @SuppressLint("IntentReset")
    View.OnClickListener onCLick = view -> {
        if (view == btnPost){
            if (validate()) {
                update();
            }
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();;
        }
        else if (view == btnGallery){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if (view == btnDelete){
            delete();
        }
        else if (view == imgItem){
            if (imgItem.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };
}