package com.goldian.yourfishsingsite.Model;

import android.content.res.Resources;

import com.goldian.yourfishsingsite.R;

public class LokasiModel {
    String id_lokasi, id_pengguna, nama, deskripsi, ikan, img, img_key;
    Float latitude, longitude;

    public String getId_lokasi() {
        return id_lokasi;
    }

    public void setId_lokasi(String id_lokasi) {
        this.id_lokasi = id_lokasi;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getIkan() {
        return ikan;
    }

    public void setIkan(String ikan) {
        this.ikan = ikan;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + "lokasi/" + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }
}
