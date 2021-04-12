package com.example.btl_ltuddd_electricstore.object;

import java.util.Date;

public class ThongTinCaNhan {
    private String email;
    private String hoTen;
    private String gioiTinh;
    private String ngaySinh;
    private String chungMinhNhanDan;
    private String chucVu;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getChungMinhNhanDan() {
        return chungMinhNhanDan;
    }

    public void setChungMinhNhanDan(String chungMinhNhanDan) {
        this.chungMinhNhanDan = chungMinhNhanDan;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public ThongTinCaNhan() {
    }

    public ThongTinCaNhan(String email, String hoTen, String gioiTinh, String ngaySinh, String chungMinhNhanDan, String chucVu) {
        this.email = email;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.chungMinhNhanDan = chungMinhNhanDan;
        this.chucVu = chucVu;
    }
}
