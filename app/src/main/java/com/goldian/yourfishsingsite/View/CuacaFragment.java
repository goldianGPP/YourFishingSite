package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Controller.CuacaController;
import com.goldian.yourfishsingsite.Model.CuacaModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.CuacaAdapter;

import java.util.ArrayList;
import java.util.List;

public class CuacaFragment extends Fragment {
    List<CuacaModel> cuacaModelList;
    RecyclerView recyclerView;
    CuacaAdapter cuacaAdapter;

    RelativeLayout template;
    ImageView imgLighter, imgWeather;
    TextView txtTemp, txtWeather, txtWeatherDescription;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_weather, container, false);
        init(v);
        request();
        return v;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(View v) {
        cuacaModelList = new ArrayList<>();

        recyclerView = v.findViewById(R.id.recyclerView);
        template = v.findViewById(R.id.template);
        imgLighter = v.findViewById(R.id.imgLighter);
        imgWeather = v.findViewById(R.id.imgWeather);
        txtTemp = v.findViewById(R.id.txtTemp);
        txtWeather = v.findViewById(R.id.txtWeather);
        txtWeatherDescription = v.findViewById(R.id.txtWeatherDescription);

        recyclerView = v.findViewById(R.id.recyclerView);
//        recyclerView.setBackground(getResources().getDrawable(R.color.faded_black));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setValue(int templateID, int lighterId, int weatherId, CuacaModel cuacaModel){
        recyclerView.setBackground(getResources().getDrawable(templateID));
        template.setBackground(getResources().getDrawable(templateID));
        imgLighter.setImageResource(lighterId);
        imgWeather.setImageResource(weatherId);
        txtTemp.setText(cuacaModel.getTemp());
        txtWeather.setText(cuacaModel.getMain());
        txtWeatherDescription.setText(cuacaModel.getDescription());
    }

    private void request(){
        new CuacaController(getContext(),this)
                .getRamalanCuaca();
    }

    public void result(List<CuacaModel> cuacaModelList){
        cuacaAdapter = new CuacaAdapter(getContext(), cuacaModelList, this);
        recyclerView.setAdapter(cuacaAdapter);
    }

}
