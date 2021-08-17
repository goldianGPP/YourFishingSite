package com.goldian.yourfishsingsite.Model;

import android.content.res.Resources;

import com.goldian.yourfishsingsite.R;

public class ItemModel {
    String id_item, id_pengguna, nama, jenis, deskripsi, harga, img, web, img_key;
    float rating;
    float rank;

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + "item/" + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }
}
