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

import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.ImageModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UbahEventActivity extends AppCompatActivity {
    Button btnImg, btnAddEvent, btnDelete;
    ImageView imgEvent;
    EditText txtTitle, txtDeskripsi, txtLink;
    ImageModel imageModel;
    ProgressDialogModel dialogModel;
    AlertDialog.Builder builder;
    EventController eventController;

    Uri uri;
    Bitmap bitmap;
    PreferencesModel pref;

    String id_event;
    boolean isUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);
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
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    imgEvent.setImageBitmap(bitmap);
                    dialog();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        getSupportActionBar().setTitle("Ubah Event");
        pref = new PreferencesModel(this,"login");
        imageModel = new ImageModel(this);
        dialogModel = new ProgressDialogModel(this);
        builder = new AlertDialog.Builder(this);
        eventController = new EventController(this);

        btnImg = findViewById(R.id.btnImg);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnDelete = findViewById(R.id.btnDelete);
        imgEvent = findViewById(R.id.imgEvent);
        txtTitle = findViewById(R.id.txtTitle);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtLink = findViewById(R.id.txtLink);

        btnImg.setText("ubah gambar");
        btnAddEvent.setText("ubah");
        imgEvent.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        id_event = bundle.getString("id_event");
        txtTitle.setText(bundle.getString("title"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        txtLink.setText(bundle.getString("link"));
        Picasso.get().load(bundle.getString("img")).into(imgEvent);
    }

    private void setListener(){
        btnAddEvent.setOnClickListener(onClick);
        btnImg.setOnClickListener(onClick);
        btnDelete.setOnClickListener(onClick);
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
        RequestBody id_event = imageModel.requestBody(this.id_event);
        MultipartBody.Part file = imageModel.multipartBody(uri,"image");
        dialogModel.show();
        isUpdate = false;
        eventController.updateEvent(
                id_event,
                file
        );
    }

    private void request(){
        dialogModel.show();
        isUpdate = true;
        eventController.updateEvent(
                id_event,
                txtTitle.getText().toString(),
                txtDeskripsi.getText().toString(),
                txtLink.getText().toString()
        );
    }

    public void result(boolean bool){
        if (bool){
            if (isUpdate)
                finish();
            else
                imgEvent.setImageBitmap(bitmap);
        }
        dialogModel.dismiss();
    }

    //Listener
    //-----------
    @SuppressLint("IntentReset")
    View.OnClickListener onClick = view -> {
        if (view == btnAddEvent){
            if (isFieldEmpty(txtLink) && isFieldEmpty(txtTitle) && isFieldEmpty(txtDeskripsi))
                request();
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
