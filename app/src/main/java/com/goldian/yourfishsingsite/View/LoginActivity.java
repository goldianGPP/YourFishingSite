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
import com.goldian.yourfishsingsite.Model.PenggunaModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    TextView txtRegister;
    Button btnLogin;
    PreferencesModel preferences;
    ProgressDialogModel dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setListener();
    }

    //inisialisasi variable
    private void init(){
        getSupportActionBar().hide();
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        preferences = new PreferencesModel(this,getResources().getString(R.string.login));
        txtUsername.setText(preferences.read("username"));
        txtPassword.setText(preferences.read("password"));
        dialog = new ProgressDialogModel(this);
    }

    private void setListener(){
        btnLogin.setOnClickListener(onClick);
        txtRegister.setOnClickListener(onClick);
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
        dialog.show();
        new PenggunaController(this)
            .login(
                    txtUsername.getText().toString(),
                    txtPassword.getText().toString()
            );
    }

    public void result(PenggunaModel penggunaModel){
        if (penggunaModel!=null){
            preferences.write("username", txtUsername.getText().toString());
            preferences.write("password", txtPassword.getText().toString());
            preferences.write("id_pengguna", penggunaModel.getId_pengguna());
            preferences.write("nama", penggunaModel.getNama());
            preferences.write("email", penggunaModel.getEmail());
            setIntent(MainActivity.class);
            finish();
        }
        dialog.dismiss();
    }

    @SuppressWarnings("rawtypes")
    public void setIntent(Class intent){
        startActivity(new Intent(this, intent));
    }

    //listener
    //---------------------------------
    View.OnClickListener onClick = view -> {
        if (view == btnLogin){
            if (isFieldEmpty(txtUsername) && isFieldEmpty(txtPassword))
                request();
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();;
        }
        else {
            setIntent(RegisterActivity.class);
        }
    };


}
