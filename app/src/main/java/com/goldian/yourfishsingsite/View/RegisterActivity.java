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
import com.goldian.yourfishsingsite.Model.ProgressDialogModel;
import com.goldian.yourfishsingsite.R;

public class RegisterActivity extends AppCompatActivity {

    EditText txtNama, txtEmail, txtUsername, txtPassword;
    TextView txtLogin;
    Button btnRegister;

    ProgressDialogModel dialogModel;

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

    private boolean isFieldEmpty(EditText editText){
        if (editText.getText().toString().equals("")) {
            editText.setError("isi");
            return false;
        }
        else
            return true;
    }

    @SuppressWarnings("rawtypes")
    public void setIntent(Class intent){
        startActivity(new Intent(this, intent));
    }

    private void request(){
        dialogModel.show();
        new PenggunaController(this)
            .register(
                    txtEmail.getText().toString(),
                    txtNama.getText().toString(),
                    txtUsername.getText().toString(),
                    txtPassword.getText().toString()
            );
    }

    public void result(boolean bool){
        if (bool){
            setIntent(LoginActivity.class);
            finish();
        }
        dialogModel.dismiss();
    }

    //Listener
    //---------------
    View.OnClickListener onClick = view -> {
        if (view == btnRegister){
            if (isFieldEmpty(txtNama) && isFieldEmpty(txtEmail) && isFieldEmpty(txtUsername) && isFieldEmpty(txtPassword))
                request();
            else
                Toast.makeText(this, "penuhi kebutuhan field", Toast.LENGTH_LONG).show();
        }
        if (view == txtLogin){
            setIntent(LoginActivity.class);
        }
    };
}
