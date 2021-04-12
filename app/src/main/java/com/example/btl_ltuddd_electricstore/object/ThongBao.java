package com.example.btl_ltuddd_electricstore.object;

public class ThongBao {
    private String ten;
    private String id;
    private String thongTin;
    private String trangThai;
    private String time;

    public ThongBao() {
    }

    public ThongBao(String ten, String id, String thongTin, String trangThai, String time) {
        this.ten = ten;
        this.id = id;
        this.thongTin = thongTin;
        this.trangThai = trangThai;
        this.time = time;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThongTin() {
        return thongTin;
    }

    public void setThongTin(String thongTin) {
        this.thongTin = thongTin;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
