package com.goldian.yourfishsingsite.Model;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.applandeo.materialcalendarview.EventDay;
import com.goldian.yourfishsingsite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class EventModel {
    String id_event, id_pengguna, title, link, deskripsi, img, created_at, edited_at , img_key;
    String tanggal;
    EventDay eventDay;
    Calendar calendar;

    public String getId_event() {
        return id_event;
    }

    public EventModel setId_event(String id_event) {
        this.id_event = id_event;
        return this;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public EventModel setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EventModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLink() {
        return link;
    }

    public EventModel setLink(String link) {
        this.link = link;
        return this;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public EventModel setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
        return this;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + img;
    }

    public EventModel setImg(String img) {
        this.img = img;
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public EventModel setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    public String getEdited_at() {
        return edited_at;
    }

    public EventModel setEdited_at(String edited_at) {
        this.edited_at = edited_at;
        return this;
    }

    public String getImg_key() {
        return img_key;
    }

    public EventModel setImg_key(String img_key) {
        this.img_key = img_key;
        return this;
    }

    public String getTanggal() {
        return tanggal;
    }

    public EventModel setTanggal(Calendar calendar, int h, int m){
        this.tanggal =  setDateString(calendar) + " " + h + "." + m + ".00";
        return this;
    }

    public EventModel setTanggal(String tanggal, int h, int m) {
        this.tanggal = tanggal  + " " + h + "." + m + ".00";
        return this;
    }

    public EventDay getEventDay() {
        return eventDay;
    }

    public EventModel setEventDay(EventDay eventDay) {
        this.eventDay = eventDay;
        return this;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String setDateString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
    }

    public String setDateTimeString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    @SuppressLint("SimpleDateFormat")
    public void setCalendar() {
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            calendar.setTime(format.parse(getTanggal()));
            calendar.add(Calendar.HOUR_OF_DAY, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public Calendar setCalendar(String date) {
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            calendar.setTime(format.parse(date));
            calendar.add(Calendar.HOUR_OF_DAY, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
