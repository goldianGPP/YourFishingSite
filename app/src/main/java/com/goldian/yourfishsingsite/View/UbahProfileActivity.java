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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.PenggunaController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahProfileActivity extends AppCompatActivity {

//    CircleImageView imgProfile;
    Button btnImg, btnUpdate;
    EditText txtUsername, txtNama;
    ImageModel imageModel;
    Uri uri;
    Bitmap bitmap;
    String BODYNAME = "image";
    PreferencesModel preferences;
    ProgressDialogModel dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        init();
        setValue();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
                CropImage.activity(data.getData())
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                try {
                    uri = result.getUri();
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    postImage();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(UbahProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void init(){
        getSupportActionBar().setTitle("Ubah Profile");
        preferences = new PreferencesModel(this,"login");
        dialog = new ProgressDialogModel(this);
        imageModel = new ImageModel(this);
        btnImg = findViewById(R.id.btnImg);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtUsername = findViewById(R.id.txtUsername);
        txtNama = findViewById(R.id.txtNama);
    }

    private void setValue(){
        Bundle b = getIntent().getExtras();
        txtUsername.setText(b.getString("username"));
        txtNama.setText(b.getString("nama"));

        CircleImageView imgProfile = findViewById(R.id.imgProfile);
        Picasso.get().load(b.getString("img"))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(imgProfile);
    }

    private void setListener(){
        btnImg.setOnClickListener(onClick);
        btnUpdate.setOnClickListener(onClick);
    }

    private boolean isFieldEmpty(EditText editText){
        if (editText.getText().toString().equals("")) {
            editText.setError("isi");
            return false;
        }
        else
            return true;
    }

    private void postImage(){
        RequestBody id_pengguna = imageModel.requestBody(preferences.read("id_pengguna"));
        MultipartBody.Part file = imageModel.multipartBody(getContentResolver(),bitmap,BODYNAME);
        dialog.show();
        new PenggunaController(this).updateUserImg(
                id_pengguna,
                file
        );
    }

    private void updateUser(){
        new PenggunaController(this).updateUser(
                preferences.read("id_pengguna"),
                txtNama.getText().toString(),
                txtUsername.getText().toString()
        );
    }

    public void result(boolean bool){
        if (bool){
            setResult(RESULT_OK);
            finish();
        }
        dialog.dismiss();
    }

    //listener
    //-------------
    @SuppressLint({"IntentReset", "ShowToast"})
    View.OnClickListener onClick = view -> {
        if (view == btnImg){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
        else if (view == btnUpdate){
            if (isFieldEmpty(txtNama) && isFieldEmpty(txtUsername))
                updateUser();
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG);
        }
    };
}
