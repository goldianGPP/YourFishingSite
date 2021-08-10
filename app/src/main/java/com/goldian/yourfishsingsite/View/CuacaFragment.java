package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goldian.yourfishsingsite.Model.WeatherModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.WeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CuacaFragment extends Fragment {
    final String url = "http://api.openweathermap.org/data/2.5/forecast?q=Yogyakarta&cnt=";
    int cnt;
    DecimalFormat df = new DecimalFormat("#,##");

    List<WeatherModel> weatherModelList;
    RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;

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
        cnt = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (cnt%3!=0) cnt = cnt/3+1;
        else cnt = cnt/3;

        weatherModelList = new ArrayList<>();
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
    public void setValue(int templateID, int lighterId, int weatherId, WeatherModel weatherModel){
        recyclerView.setBackground(getResources().getDrawable(templateID));
        template.setBackground(getResources().getDrawable(templateID));
        imgLighter.setImageResource(lighterId);
        imgWeather.setImageResource(weatherId);
        txtTemp.setText(weatherModel.getTemp());
        txtWeather.setText(weatherModel.getMain());
        txtWeatherDescription.setText(weatherModel.getDescription());
    }

    @SuppressLint("LogNotTimber")
    private void request(){
        StringRequest request = new StringRequest(Request.Method.POST, url + (26 - cnt) + getResources().getString(R.string.wather_token), response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray list = jsonObject.getJSONArray("list");
                for (int i=2; i<=list.length();i++){
                    WeatherModel weatherModel = new WeatherModel();
                    JSONObject object = list.getJSONObject(i);
                    JSONArray Weathers = object.getJSONArray("weather");
                    JSONObject main = object.getJSONObject("main");
                    JSONObject Weather = Weathers.getJSONObject(0);

                    weatherModel.setTemp(df.format(main.getDouble("temp") - 273.15) +"°");
                    weatherModel.setTemp_min(df.format(main.getDouble("temp_min") - 273.15) +"°");
                    weatherModel.setTemp_max(df.format(main.getDouble("temp_max") - 273.15) +"°");
                    weatherModel.setHumidity(Integer.toString(main.getInt("humidity")));
                    weatherModel.setMain(Weather.getString("main"));
                    weatherModel.setDescription(Weather.getString("description"));
                    weatherModel.setDt_txt(object.getString("dt_txt"));

                    weatherModelList.add(weatherModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            result();
        }, error -> {
            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            Log.i(TAG, "nilai: " + error.getMessage());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }

    public void result(){
        weatherAdapter = new WeatherAdapter(getContext(), weatherModelList, this);
        recyclerView.setAdapter(weatherAdapter);
    }

}
