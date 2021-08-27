package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.goldian.yourfishsingsite.R;

public class DetailLokasiFragment extends Fragment {

    ImageView imgLokasi;
    TextView txtLatitude, txtLongitude, txtNama, txtDeskripsi, txtIkan;
    String id_lokasi, key, url, ikan;
    ImageButton btnCommentDown, btnCommentUp;
    LinearLayout frame_container;
    Button btnDestination;

    boolean isShown, flag;
    float longitude, latitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_show, container, false);
        init(v);
        setValue();
        setListener();
        return  v;
    }

    private void init(View v) {
        frame_container = v.findViewById(R.id.frame_container);
        imgLokasi = v.findViewById(R.id.imgMap);
        txtLatitude = v.findViewById(R.id.txtLatitude);
        txtLongitude = v.findViewById(R.id.txtLongitude);
        txtNama = v.findViewById(R.id.txtNama);
        txtIkan = v.findViewById(R.id.txtIkan);
        txtDeskripsi = v.findViewById(R.id.txtDeskripsi);
        btnCommentUp = v.findViewById(R.id.btnCommentUp);
        btnCommentDown = v.findViewById(R.id.btnCommentDown);
        btnDestination = v.findViewById(R.id.btnDestination);
    }

    @SuppressLint("SetTextI18n")
    private void setValue(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_lokasi = bundle.getString("id_lokasi");
            url = bundle.getString("img");
            key = bundle.getString("key");
            longitude = Float.parseFloat(bundle.getString("longitude"));
            latitude = Float.parseFloat(bundle.getString("latitude"));
            ikan = bundle.getString("ikan");

            txtLatitude.setText("lat    : " + bundle.getString("latitude"));
            txtLongitude.setText("long : " + bundle.getString("longitude"));
            txtNama.setText(bundle.getString("nama"));
            if (ikan != null){
                txtIkan.setText("ikan : " + ikan);
                txtIkan.setVisibility(View.VISIBLE);
            }
            txtDeskripsi.setText("deskripsi : \n\n" + bundle.getString("deskripsi"));
        }

        Glide.with(this)
                .load(url)
                .signature(new ObjectKey(key))
                .into(imgLokasi);
        imgLokasi.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void setFragment(){
        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", "lokasi"+id_lokasi);
        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_comment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    private void setListener(){
        imgLokasi.setOnClickListener(onClick);
        btnCommentUp.setOnClickListener(onClick);
        btnCommentDown.setOnClickListener(onClick);
        btnDestination.setOnClickListener(onClick);
    }

    private void setContainer(boolean visibility){
        isShown = visibility;
        if (visibility)
            frame_container.animate().translationY(0);
        else
            frame_container.animate().translationY(2000);
    }

    View.OnClickListener onClick = view -> {
        if (view == imgLokasi){
            if (imgLokasi.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgLokasi.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgLokasi.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else if (view == btnDestination){
            ((MapLokasiFragment)getParentFragment()).requestRoute(longitude,latitude);
        }
        else if (view == btnCommentUp || view == btnCommentDown){
            if (!flag) {
                flag = true;
                setFragment();
                setContainer(false);
            }

            if (isShown)
                setContainer(false);
            else
                setContainer(true);
        }
    };
}
