package com.goldian.yourfishsingsite.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.Picasso;

public class DetailLokasiFragment extends Fragment {

    ImageView imgMap;
    TextView txtLatitude, txtLongitude, txtNama, txtDeskripsi;
    String id_lokasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_show, container, false);
        init(v);
        setValue();
        showComment();
        return  v;
    }

    private void init(View v) {
        imgMap = v.findViewById(R.id.imgMap);
        txtLatitude = v.findViewById(R.id.txtLatitude);
        txtLongitude = v.findViewById(R.id.txtLongitude);
        txtNama = v.findViewById(R.id.txtNama);
        txtDeskripsi = v.findViewById(R.id.txtDeskripsi);
    }

    private void setValue(){
        Bundle bundle = getArguments();
        Picasso.get().load(bundle.getString("img")).into(imgMap);
        txtLatitude.setText(bundle.getString("latitude"));
        txtLongitude.setText(bundle.getString("longitude"));
        txtNama.setText(bundle.getString("nama"));
        txtDeskripsi.setText(bundle.getString("deskripsi"));
        id_lokasi = bundle.getString("id_lokasi");
        String url = getParentFragment().getContext().getResources().getString(R.string.img_url_lokasi) + bundle.getString("img");
        Picasso.get().load(url).into(imgMap);
    }

    private void showComment(){
        Fragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", "lokasi"+id_lokasi);
        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_comment, fragment)
                .commit();
    }

    private void setListener(){
        imgMap.setOnClickListener(view -> {
            if (imgMap.getScaleType() == ImageView.ScaleType.CENTER_CROP)
                imgMap.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else
                imgMap.setScaleType(ImageView.ScaleType.CENTER_CROP);
        });
    }
}
