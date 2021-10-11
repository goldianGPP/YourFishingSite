package com.goldian.yourfishsingsite.Model;

public class LokasiModel {
    String id_lokasi, id_pengguna, nama, alamat, deskripsi, ikan, img, img_key;
    Float latitude, longitude;

    public String getId_lokasi() {
        return id_lokasi;
    }

    public LokasiModel setId_lokasi(String id_lokasi) {
        this.id_lokasi = id_lokasi;
        return this;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public LokasiModel setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
        return this;
    }

    public String getNama() {
        return nama;
    }

    public LokasiModel setNama(String nama) {
        this.nama = nama;
        return this;
    }

    public String getAlamat() {
        return alamat;
    }

    public LokasiModel setAlamat(String alamat) {
        this.alamat = alamat;
        return this;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public LokasiModel setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
        return this;
    }

    public String getIkan() {
        return ikan;
    }

    public LokasiModel setIkan(String ikan) {
        this.ikan = ikan;
        return this;
    }

    public String getImg() {
        return new ImageModel().getBase_url() + img;
    }

    public LokasiModel setImg(String img) {
        this.img = img;
        return this;
    }

    public String getImg_key() {
        return img_key;
    }

    public LokasiModel setImg_key(String img_key) {
        this.img_key = img_key;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public LokasiModel setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public LokasiModel setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }
}
