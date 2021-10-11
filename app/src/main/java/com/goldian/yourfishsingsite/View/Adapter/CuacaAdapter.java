package com.goldian.yourfishsingsite.View.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Model.CuacaModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.CuacaFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CuacaAdapter extends RecyclerView.Adapter<CuacaAdapter.WeatherHolder>{
    final Context context;
    List<CuacaModel> list;
    CuacaFragment fragment;
    Calendar calendar;
    int hari_ini, besok, lusa;

    public CuacaAdapter(Context context, List<CuacaModel> list, CuacaFragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        calendar = Calendar.getInstance();
        hari_ini =  calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        besok = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        lusa = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @NonNull
    @Override
    public CuacaAdapter.WeatherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_weather_horizontal,viewGroup,false);
        return new WeatherHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CuacaAdapter.WeatherHolder holder, int position) {
        final CuacaModel currentData = list.get(position);
        String temp = currentData.getDt_txt().substring(8,10), time = currentData.getDt_txt().substring(11);

        if (hari_ini == Integer.parseInt(temp))
            holder.txtDay.setText("HARI INI");
        else if (besok == Integer.parseInt(temp))
            holder.txtDay.setText("BESOK");
        else if (lusa == Integer.parseInt(temp))
            holder.txtDay.setText("LUSA");

        holder.background.setBackground(context.getResources().getDrawable(setButton(Integer.parseInt(currentData.getDt_txt().substring(11,13)))));

        holder.imgWeather.setImageResource(setWeather(currentData.getMain()));
        holder.txtTime.setText(time);
        holder.txtWeather.setText(currentData.getMain());
        holder.btnCard.setOnClickListener(view -> setView(currentData));

        if (position == 0){
            setView(currentData);
        }
    }

    private int setTemplate(int time){
        if (time >= 6 && time < 12) return R.drawable.gradient_moring;
        else if (time >= 12 && time < 18) return R.drawable.gradient_evening;
        else return R.drawable.gradient_night;
    }

    private int setButton(int time){
        if (time >= 6 && time < 12) return R.drawable.button_primary;
        else if (time >= 12 && time < 18) return R.drawable.button_warning;
        else return R.drawable.button_secondary;
    }

    private int setLighter(int time){
        if (time >= 6 && time < 12) return R.drawable.img_sun;
        else if (time >= 12 && time < 18) return R.drawable.img_sun;
        else return R.drawable.img_night;
    }

    private int setWeather(String weather){
        if (weather.equals("Rain")) return R.drawable.img_rainy;
        else if (weather.equals("Clouds")) return R.drawable.img_cloudy;
        else return R.drawable.img_sunny;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setView(CuacaModel currentData){
        int time = Integer.parseInt(currentData.getDt_txt().substring(11,13));
        int template, lighter, weather;
        template = setTemplate(time);
        lighter = setLighter(time);
        weather = setWeather(currentData.getMain().trim());


        Objects.requireNonNull(((FragmentActivity) context).getSupportActionBar()).setBackgroundDrawable(context.getResources().getDrawable(template));

        fragment.setValue(
                template,
                lighter,
                weather,
                currentData
        );
    }

    public static class WeatherHolder extends RecyclerView.ViewHolder {
        protected CardView btnCard;
        protected ImageView imgWeather;
        protected TextView txtDay, txtTime, txtWeather;
        protected LinearLayout background;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            btnCard = itemView.findViewById(R.id.btnCard);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtWeather = itemView.findViewById(R.id.txtWeather);
            background = itemView.findViewById(R.id.background);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }
}
