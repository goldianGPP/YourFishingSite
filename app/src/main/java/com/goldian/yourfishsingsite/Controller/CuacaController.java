package com.goldian.yourfishsingsite.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goldian.yourfishsingsite.DAO.ApiHelper;
import com.goldian.yourfishsingsite.Model.CuacaModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.CuacaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CuacaController {
    Context context;
    Fragment fragment = null;
    int cnt;

    String api = "http://api.openweathermap.org/data/2.5/forecast?q=Yogyakarta&cnt=";
    DecimalFormat df = new DecimalFormat("#,##");
    List<CuacaModel> cuacaModelList;

    public CuacaController(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;

        cnt = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (cnt%3!=0) cnt = cnt/3+1;
        else cnt = cnt/3;

        cuacaModelList = new ArrayList<>();
    }

    public void result(List<CuacaModel> cuacaModelList){
        if (fragment instanceof CuacaFragment)
            ((CuacaFragment)fragment).result(cuacaModelList);
    }

    public void getRamalanCuaca(){
        StringRequest request = new StringRequest(Request.Method.POST, api + (26 - cnt) + context.getResources().getString(R.string.wather_token), response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray list = jsonObject.getJSONArray("list");
                for (int i=2; i<=list.length();i++){
                    CuacaModel cuacaModel = new CuacaModel();
                    JSONObject object = list.getJSONObject(i);
                    JSONArray Weathers = object.getJSONArray("weather");
                    JSONObject main = object.getJSONObject("main");
                    JSONObject Weather = Weathers.getJSONObject(0);

                    cuacaModel.setTemp(df.format(main.getDouble("temp") - 273.15) +"°");
                    cuacaModel.setTemp_min(df.format(main.getDouble("temp_min") - 273.15) +"°");
                    cuacaModel.setTemp_max(df.format(main.getDouble("temp_max") - 273.15) +"°");
                    cuacaModel.setHumidity(Integer.toString(main.getInt("humidity")));
                    cuacaModel.setMain(Weather.getString("main"));
                    cuacaModel.setDescription(Weather.getString("description"));
                    cuacaModel.setDt_txt(object.getString("dt_txt"));

                    cuacaModelList.add(cuacaModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            result(cuacaModelList);
        }, error -> {
            Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(request);
    }
}
