package com.example.appplaymusic;

public class Song {
    private int idBai;
    private String tenBai;

    public Song( String tenBai,int idBai) {
        this.idBai = idBai;
        this.tenBai = tenBai;
    }

    public int getIdBai() {
        return idBai;
    }

    public void setIdBai(int idBai) {
        this.idBai = idBai;
    }

    public String getTenBai() {
        return tenBai;
    }

    public void setTenBai(String tenBai) {
        this.tenBai = tenBai;
    }
}
