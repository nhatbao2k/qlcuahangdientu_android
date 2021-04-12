package com.example.btl_ltuddd_electricstore.object;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class SanPham implements Serializable {
    private String maSP;
    private String tenSP;
    private String maLoaiSP;
    private String gia;
    private String moTa;
    private String hinhAnh;

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String maLoaiSP, String gia, String moTa, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoaiSP = maLoaiSP;
        this.gia = gia;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> results = new HashMap<>();
        results.put("maSP",maSP);
        results.put("tenSP",tenSP);
        results.put("maLoaiSP",maLoaiSP);
        results.put("gia",gia);
        results.put("moTa",moTa);
        results.put("hinhAnh",hinhAnh);

        return results;
    }
}
