package com.goldian.yourfishsingsite.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    Button btnUpdateProfile;
    PreferencesModel pref;
    TextView txtUsername, txtNama, txtEmail, txtBio, txtLink;
    String filename;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        init(v);
        setListener();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                initImageObject(v);
            }
        }
    }

    private void initImageObject(View v){
        CircleImageView imgProfile = v.findViewById(R.id.imgProfile);
        filename = getResources().getString(R.string.img_url_user)+pref.read("id_pengguna")+".jpg";
        Picasso.get().load(filename)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(imgProfile);
    }

    private void init(View v) {
        pref = new PreferencesModel(getContext(),getResources().getString(R.string.login));
        initImageObject(v);
        btnUpdateProfile = v.findViewById(R.id.btnUpdateProfile);
        txtEmail = v.findViewById(R.id.txtEmail);
        txtUsername = v.findViewById(R.id.txtUsername);
        txtNama = v.findViewById(R.id.txtNama);
        txtBio = v.findViewById(R.id.txtBio);
        txtLink = v.findViewById(R.id.txtLink);
        txtUsername.setText(pref.read("username"));
        txtNama.setText(pref.read("nama"));
        txtEmail.setText(pref.read("email"));
    }

    private void setListener(){
        btnUpdateProfile.setOnClickListener(onClick);
    }

    //listener
    View.OnClickListener onClick = view -> {
        if (view == btnUpdateProfile){
            Intent intent = new Intent(getContext(), UbahProfileActivity.class);
            intent.putExtra("username",txtUsername.getText().toString());
            intent.putExtra("nama",txtNama.getText().toString());
            intent.putExtra("bio",txtBio.getText().toString());
            intent.putExtra("link",txtLink.getText().toString());
            intent.putExtra("img",filename);
            startActivityForResult(intent,1);
        }
    };
}
