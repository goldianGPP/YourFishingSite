package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Controller.PenggunaController;
import com.goldian.yourfishsingsite.Model.DialogModel;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PenggunaModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahProfileActivity extends AppCompatActivity {

//    CircleImageView imgProfile;
    Button btnImg, btnUpdate, btnPassword;
    EditText txtUsername, txtNama, txtPassword;
    ImageModel imageModel;
    Uri uri;
    Bitmap bitmap;
    String BODYNAME = "image";
    PreferencesModel pref;
    ProgressDialogModel dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profile);
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
        pref = new PreferencesModel(this,"login");
        dialog = new ProgressDialogModel(this);
        imageModel = new ImageModel(this);
        btnImg = findViewById(R.id.btnImg);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnPassword = findViewById(R.id.btnPassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtNama = findViewById(R.id.txtNama);
        txtPassword = findViewById(R.id.txtPassword);
    }

    private void setValue(){
        Bundle b = getIntent().getExtras();
        txtUsername.setText(b.getString("username"));
        txtNama.setText(b.getString("nama"));

        CircleImageView imgProfile = findViewById(R.id.imgProfile);
        Glide.with(this)
                .load(b.getString("img"))
                .signature(new ObjectKey(pref.read("key")))
                .into(imgProfile);
    }

    private void setListener(){
        btnImg.setOnClickListener(onClick);
        btnUpdate.setOnClickListener(onClick);
        btnPassword.setOnClickListener(onClick);
        txtPassword.addTextChangedListener(watcher);
    }

    private void postImage(){
        RequestBody id_pengguna = imageModel.requestBody(pref.read("id_pengguna"));
        MultipartBody.Part file = imageModel.multipartBody(getContentResolver(),bitmap,BODYNAME);
        dialog.show();
        new PenggunaController(this).updateUserImg(
                id_pengguna,
                file
        );
    }

    private void updateUser(){
        PenggunaModel penggunaModel = new PenggunaModel();
        new PenggunaController(this).updateUser(
                penggunaModel
                .setId_pengguna(pref.read("id_pengguna"))
                .setNama(txtNama.getText().toString())
                .setUsername(txtUsername.getText().toString())
        );
    }

    public void updatePassword(){

    }

    public void result(boolean bool){
        dialog.dismiss();
        if (bool){
            pref.write("key", Calendar.getInstance().getTime().toString());
            setResult(RESULT_OK);
            finish();
        }
    }

    private boolean validate(){
        return new Validation()
                .isFieldOk(txtUsername,3, 10)
                .isFieldOk(txtNama,1,20)
                .validate();
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
            if (validate())
                updateUser();
        }
        else if (view == btnPassword){
            if (new Validation().isFieldOk(txtPassword,6,20).validate()) {
                new DialogModel(this, R.layout.view_dialog)
                        .defaultViewText();
            }
        }
    };

    TextWatcher watcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length()>0)
                btnPassword.setVisibility(View.VISIBLE);
            else
                btnPassword.setVisibility(View.GONE);
        }

        @Override public void afterTextChanged(Editable editable) { }
    };
}
