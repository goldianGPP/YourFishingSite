package com.goldian.yourfishsingsite.Model;

import android.content.res.Resources;

import com.goldian.yourfishsingsite.R;

public class ItemModel {
    String id_item, id_pengguna, nama, jenis, deskripsi, harga, img, web, created_at, img_key;
    float rating;
    float rank;

    public String getId_item() {
        return id_item;
    }

    public ItemModel setId_item(String id_item) {
        this.id_item = id_item;
        return this;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public ItemModel setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
        return this;
    }

    public String getNama() {
        return nama;
    }

    public ItemModel setNama(String nama) {
        this.nama = nama;
        return this;
    }

    public String getJenis() {
        return jenis;
    }

    public ItemModel setJenis(String jenis) {
        this.jenis = jenis;
        return this;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public ItemModel setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
        return this;
    }

    public String getHarga() {
        return harga;
    }

    public ItemModel setHarga(String harga) {
        this.harga = harga;
        return this;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + "images/item/" + img;
    }

    public ItemModel setImg(String img) {
        this.img = img;
        return this;
    }

    public String getWeb() {
        return web;
    }

    public ItemModel setWeb(String web) {
        this.web = web;
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public ItemModel setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    public String getImg_key() {
        return img_key;
    }

    public ItemModel setImg_key(String img_key) {
        this.img_key = img_key;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public ItemModel setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public float getRank() {
        return rank;
    }

    public ItemModel setRank(float rank) {
        this.rank = rank;
        return this;
    }

    //get string to indonesian rupisah's format
    public String getHargaInRupiah(){
        int length = harga.length(), count = 0;
        String output = "";

        for (char c : harga.toCharArray()) {
            if(((length - count) % 3) == 0 && count != 0)
                output += ",";
            output += c;
            count+=1;
        }
        return "Rp. " + output;
    }

    //set string to indonesian rupisah's format
    public String setHargaInRupiah(String harga){
        int length = harga.length(), count = 0;
        String output = "";

        for (char c : harga.toCharArray()) {
            if(((length - count) % 3) == 0 && count != 0)
                output += ",";
            output += c;
            count+=1;
        }
        return "Rp. " + output;
    }
}
