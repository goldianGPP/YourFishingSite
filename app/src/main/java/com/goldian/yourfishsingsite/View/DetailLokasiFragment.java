package com.goldian.yourfishsingsite.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView txtLatitude, txtLongitude, txtNama, txtDeskripsi;
    String id_lokasi, key, url;
    ImageButton btnCommentDown, btnCommentUp;
    LinearLayout frame_container;

    boolean isShown, flag;

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
        txtDeskripsi = v.findViewById(R.id.txtDeskripsi);
        btnCommentUp = v.findViewById(R.id.btnCommentUp);
        btnCommentDown = v.findViewById(R.id.btnCommentDown);
    }

    private void setValue(){
        Bundle bundle = getArguments();
        id_lokasi = bundle.getString("id_lokasi");
        url = bundle.getString("img");
        key = bundle.getString("key");

        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));
        txtNama.setText(bundle.getString("nama"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));

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
    }

    private boolean setContainer(boolean visibility){
        if (visibility) {
            frame_container.animate().translationY(0);
            return true;
        }
        else {
            frame_container.animate().translationY(2000);
            return false;
        }
    }

    View.OnClickListener onClick = view -> {
        if (view == imgLokasi){
            if (imgLokasi.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgLokasi.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgLokasi.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else if (view == btnCommentUp || view == btnCommentDown){
            if (!flag) {
                flag = true;
                setFragment();
            }

            if (isShown)
                isShown = setContainer(false);
            else
                isShown = setContainer(true);
        }
    };
}
