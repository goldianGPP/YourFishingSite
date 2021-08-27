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

    public int getDay() {
        return Integer.parseInt(day);
    }

    public EventModel setDay(String day) {
        this.day = day;
        return this;
    }

    public int getMonth() {
        return Integer.parseInt(month);
    }

    public EventModel setMonth(String month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return Integer.parseInt(year);
    }

    public EventModel setYear(String year) {
        this.year = year;
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

    public void setCalendar(EventModel eventModel) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MONTH, eventModel.getMonth()-1);
        calendar.set(Calendar.DAY_OF_MONTH, eventModel.getDay());
        calendar.set(Calendar.YEAR, eventModel.getYear());
    }
}
