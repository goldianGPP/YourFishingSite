package com.goldian.yourfishsingsite.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView imgProfile;
    RelativeLayout btnCuaca, btnTempat, btnItem, btnLokasi;
    ImageButton btnSideMenu;
    NavigationView menuSNav;
    DrawerLayout drawerLayout;
    Button btnUpdateProfile;
    PreferencesModel pref;
    TextView txtUsername, txtNama, txtEmail;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                initImageObject();
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        else {
            finish();
        }
    }

    private void initImageObject(){
        CircleImageView imgProfile = findViewById(R.id.imgProfile);
        filename = getResources().getString(R.string.img_url_user)+pref.read("id_pengguna")+".jpg";
        Glide.with(this)
                .load(filename)
                .signature(new ObjectKey(pref.read("key")))
                .into(imgProfile);
    }

    //inisialisasi variable
    private void init(){
        getSupportActionBar().hide();pref = new PreferencesModel(this,getResources().getString(R.string.login));
        initImageObject();
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtNama = findViewById(R.id.txtNama);
        txtUsername.setText(pref.read("username"));
        txtNama.setText(pref.read("nama"));
        txtEmail.setText(pref.read("email"));
        imgProfile = findViewById(R.id.imgProfile);
        btnSideMenu = findViewById(R.id.btnSideMenu);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuSNav = findViewById(R.id.menuSNav);
        btnCuaca = findViewById(R.id.btnCuaca);
        btnTempat = findViewById(R.id.btnTempat);
        btnItem = findViewById(R.id.btnItem);
        btnLokasi = findViewById(R.id.btnLokasi);
    }

    private void setListener(){
        menuSNav.setNavigationItemSelectedListener(sideNavListener);
        btnSideMenu.setOnClickListener(onClick);
        btnCuaca.setOnClickListener(onClick);
        btnTempat.setOnClickListener(onClick);
        btnItem.setOnClickListener(onClick);
        btnLokasi.setOnClickListener(onClick);
        btnUpdateProfile.setOnClickListener(onClick);
    }

    private void setIntent(int identity){
        Intent intent = new Intent(this, FragmentActivity.class);
        intent.putExtra("identity",identity);
        startActivity(intent);
    }

    //listener
    //---------------------------------
    View.OnClickListener onClick = new View.OnClickListener() {
        @SuppressLint("RtlHardcoded")
        @Override
        public void onClick(View view) {
            if (view == btnSideMenu){
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                else
                    drawerLayout.openDrawer(Gravity.RIGHT);
            }
            else if (view == btnUpdateProfile){
                Intent intent = new Intent(MainActivity.this, UbahProfileActivity.class);
                intent.putExtra("username",txtUsername.getText().toString());
                intent.putExtra("nama",txtNama.getText().toString());
                intent.putExtra("img",filename);
                startActivityForResult(intent,1);
            }
            else if (view == btnCuaca)
                setIntent(1);
            else if (view == btnTempat)
                setIntent(2);
            else if (view == btnItem)
                setIntent(3);
            else if (view == btnLokasi)
                setIntent(4);
        }
    };

    private final NavigationView.OnNavigationItemSelectedListener sideNavListener = item -> {
        if (item.getItemId()==R.id.logout) {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else if (item.getItemId()==R.id.item_add){
            startActivity(new Intent(this, TambahBarangActivity.class));
        }
        else if (item.getItemId()==R.id.item_list){
            Intent intent = new Intent(this, TampilBarangActivity.class);
            intent.putExtra("flag",true);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.item_list_rated){
            Intent intent = new Intent(this, TampilBarangActivity.class);
            intent.putExtra("flag",false);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.event_list){
            startActivity(new Intent(this, TampilEventActivity.class));
        }
        else if(item.getItemId()==R.id.lokasi_list){
            startActivity(new Intent(this, TampilLokasiActivity.class));
        }
        return true;
    };
}