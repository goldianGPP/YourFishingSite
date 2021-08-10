package com.goldian.yourfishsingsite.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.R;

public class FragmentActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        drawerLayout = findViewById(R.id.drawerLayout);
        getExtras();
    }

    private void getExtras(){
        int val = getIntent().getIntExtra("identity",1);
        switch (val){
            case 1: setFragment(new CuacaFragment()); getSupportActionBar().setTitle("Cuaca"); break;
            case 2: setFragment(new CalendarFragment()); getSupportActionBar().setTitle("Kalender"); break;
            case 3: setFragment(new CariBarangFragment()); getSupportActionBar().setTitle("Cari Barang"); break;
            case 4: setFragment(new MapLokasiFragment()); getSupportActionBar().setTitle("Map"); break;
        }
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        fragment,
                        fragment.getClass().getSimpleName())
                .setCustomAnimations(
                        R.anim.slide_up,
                        R.anim.slide_down
                )
                .commit();
    }
}
