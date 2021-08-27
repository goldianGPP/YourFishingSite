package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldian.yourfishsingsite.Controller.PenggunaController;
import com.goldian.yourfishsingsite.Model.FilterModel;
import com.goldian.yourfishsingsite.Model.PenggunaModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.Model.Validation;
import com.goldian.yourfishsingsite.R;

public class RegisterActivity extends AppCompatActivity {

    EditText txtNama, txtEmail, txtUsername, txtPassword;
    TextView txtLogin;
    Button btnRegister;

    ProgressDialogModel dialogModel;
    FilterModel filterModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setListener();
    }

    private void init(){
        getSupportActionBar().hide();
        dialogModel = new ProgressDialogModel(this);
        filterModel = new FilterModel();

        txtNama = findViewById(R.id.txtNama);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtLogin = findViewById(R.id.txtLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setListener(){
        btnRegister.setOnClickListener(onClick);
        txtLogin.setOnClickListener(onClick);
    }

    @SuppressWarnings("rawtypes")
    public void setIntent(Class intent){
        startActivity(new Intent(this, intent));
    }

    private void request(){
        dialogModel.show();
        PenggunaModel penggunaModel = new PenggunaModel();
        new PenggunaController(this)
            .register(
                penggunaModel
                    .setEmail(txtEmail.getText().toString())
                    .setNama(txtNama.getText().toString())
                    .setUsername(txtUsername.getText().toString())
                    .setPassword(txtPassword.getText().toString())
            );
    }

    public void result(Boolean bool){
        if (bool){
            setIntent(LoginActivity.class);
            finish();
        }
        dialogModel.dismiss();
    }

    private boolean validate(){
        return new Validation()
                .isFieldOk(txtNama,1, 20)
                .isEmpty(txtEmail)
                .isFieldOk(txtUsername,3, 10)
                .isFieldOk(txtPassword,6,20)
                .validate();
    }

    //Listener
    //---------------
    View.OnClickListener onClick = view -> {
        if (view == btnRegister){
            if (validate())
                request();
        }
        if (view == txtLogin){
            setIntent(LoginActivity.class);
        }
    };
}
