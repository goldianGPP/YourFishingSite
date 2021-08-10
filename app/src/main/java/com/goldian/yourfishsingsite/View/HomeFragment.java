package com.goldian.yourfishsingsite.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.R;

public class HomeFragment extends Fragment {

    RelativeLayout btnCuaca, btnTempat, btnItem, btnLokasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        setListener();
        return v;
    }

    private void init(View v) {
        btnCuaca = v.findViewById(R.id.btnCuaca);
        btnTempat = v.findViewById(R.id.btnTempat);
        btnItem = v.findViewById(R.id.btnItem);
        btnLokasi = v.findViewById(R.id.btnLokasi);
    }

    private void setListener(){
        btnCuaca.setOnClickListener(onClick);
        btnTempat.setOnClickListener(onClick);
        btnItem.setOnClickListener(onClick);
        btnLokasi.setOnClickListener(onClick);
    }

    private void setIntent(int identity){
        Intent intent = new Intent(getContext(), FragmentActivity.class);
        intent.putExtra("identity",identity);
        startActivity(intent);
    }

    //listener
    View.OnClickListener onClick = view -> {
        if (view == btnCuaca)
            setIntent(1);
        else if (view == btnTempat)
            setIntent(2);
        else if (view == btnItem)
            setIntent(3);
        else if (view == btnLokasi)
            setIntent(4);
    };
}
