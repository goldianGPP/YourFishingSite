package com.goldian.yourfishsingsite.Model;

public class PenggunaModel {
    String id_pengguna, nama, username, email, password, auth, sumitemrating, sumitemrated, img_key;

    public String getId_pengguna() {
        return id_pengguna;
    }

    public PenggunaModel setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
        return this;
    }

    public String getNama() {
        return nama;
    }

    public PenggunaModel setNama(String nama) {
        this.nama = nama;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public PenggunaModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PenggunaModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PenggunaModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAuth() {
        return auth;
    }

    public PenggunaModel setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    public String getSumitemrating() {
        return sumitemrating;
    }

    public PenggunaModel setSumitemrating(String sumitemrating) {
        this.sumitemrating = sumitemrating;
        return this;
    }

    public String getSumitemrated() {
        return sumitemrated;
    }

    public PenggunaModel setSumitemrated(String sumitemrated) {
        this.sumitemrated = sumitemrated;
        return this;
    }

    public String getImg_key() {
        return img_key;
    }

    public PenggunaModel setImg_key(String img_key) {
        this.img_key = img_key;
        return this;
    }
}
