package com.goldian.yourfishsingsite.Model;

import android.content.res.Resources;

import com.applandeo.materialcalendarview.EventDay;
import com.goldian.yourfishsingsite.R;

import java.util.Calendar;

public class EventModel {
    String id_event, id_pengguna, title, link, deskripsi, img, created_at, edited_at , img_key;
    String day;
    String month;
    String year;
    EventDay eventDay;
    Calendar calendar;

    public String getId_event() {
        return id_event;
    }

    public void setId_event(String id_event) {
        this.id_event = id_event;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + "event/" + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEdited_at() {
        return edited_at;
    }

    public void setEdited_at(String edited_at) {
        this.edited_at = edited_at;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }

    public EventDay getEventDay() {
        return eventDay;
    }

    public void setEventDay(EventDay eventDay) {
        this.eventDay = eventDay;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(EventModel eventModel) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MONTH, Integer.parseInt(eventModel.getMonth())-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(eventModel.getDay()));
        calendar.set(Calendar.YEAR, Integer.parseInt(eventModel.getYear()));
    }
}
