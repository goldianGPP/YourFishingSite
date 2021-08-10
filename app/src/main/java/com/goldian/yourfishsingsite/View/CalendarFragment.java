package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.goldian.yourfishsingsite.Controller.EventController;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.EventAdapter;

import java.text.DateFormatSymbols;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment {

    CalendarView calemdarView;
    RecyclerView recyclerView;
    List<EventDay> events = new ArrayList<>();
    List<EventModel> eventModels = new ArrayList<>();
    DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    CardView cardView;
    Button btnAddEvent;
    Calendar calendar;
    TextView txtDate;
    Integer month, year, day;

    EventAdapter eventAdapter;
    PreferencesModel pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        init(v);
        setListener();
        request();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            request();
        }
    }

    private void init(View v){
        pref = new PreferencesModel(getContext(), "login");
        calemdarView = v.findViewById(R.id.calemdarView);
        txtDate = v.findViewById(R.id.txtDate);
        cardView = v.findViewById(R.id.cardView);
        btnAddEvent = v.findViewById(R.id.btnAddEvent);
        recyclerView = v.findViewById(R.id.recyclerView);

        calendar = Calendar.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("SetTextI18n")
    public void setDateText(Calendar calendar){
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        txtDate.setText(day + " " + dateFormatSymbols.getMonths()[month] + " " + year);
    }

    private void setListener(){
        calemdarView.setOnDayClickListener(onChange);
        calemdarView.setOnPreviousPageChangeListener(onPage);
        calemdarView.setOnForwardPageChangeListener(onPage);
        btnAddEvent.setOnClickListener(onClick);
    }

    private void setEvents(){
        Calendar temp = Calendar.getInstance();
        for (EventModel eventModel: eventModels) {
            eventModel.setCalendar(eventModel);
            if (eventModel.getCalendar().before(temp))
                eventModel.setEventDay(new EventDay(eventModel.getCalendar(),R.drawable.img_baloon_uncolored));
            else
                eventModel.setEventDay(new EventDay(eventModel.getCalendar(),R.drawable.img_baloon_colored));
            events.add(eventModel.getEventDay());
        }
        calemdarView.setEvents(events);
    }

    private void request(){
        new EventController(getContext(),this)
                .getEvent();
    }

    public void result(List<EventModel> eventModels){
        if (eventModels!=null){
            this.eventModels = eventModels;
            setEvents();
        }

    }

    //Listener
    //--------------
    View.OnClickListener onClick = view -> {
        if (view == btnAddEvent){
            Intent intent = new Intent(getContext(), TambahEventActivity.class);
            intent.putExtra("day",day.toString());
            intent.putExtra("month",String.valueOf((month+1)));
            intent.putExtra("year",year.toString());
            startActivityForResult(intent,1);
        }
    };

    OnCalendarPageChangeListener onPage = () -> setDateText(calendar);

    OnDayClickListener onChange = eventDay -> {
        cardView.setVisibility(View.VISIBLE);
        calendar = (eventDay.getCalendar());
        setDateText(calendar);
        List<EventModel> temp = new ArrayList<>();
        if (eventModels != null){
            for (EventModel eventModel: eventModels) {
                if (eventModel.getEventDay().getCalendar().equals(calendar)) {
                    temp.add(eventModel);
                }
            }
            if (temp!=null) {
                eventAdapter = new EventAdapter(getContext(), temp, pref.read("id_pengguna"));
                recyclerView.setAdapter(eventAdapter);
            }
            else
                Toast.makeText(getContext(),"tidak ada event",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(),"tidak ada event",Toast.LENGTH_SHORT).show();
    };
}
