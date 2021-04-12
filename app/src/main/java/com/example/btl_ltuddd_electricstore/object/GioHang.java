package com.example.btl_ltuddd_electricstore.object;

public class GioHang {
    private String MaSP;
    private String TenSP;
    private String Gia;
    private String HinhAnh;
    private int soLuong;
    private String giaMacDinh;

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String maSP) {
        MaSP = maSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGiaMacDinh() {
        return giaMacDinh;
    }

    public void setGiaMacDinh(String giaMacDinh) {
        this.giaMacDinh = giaMacDinh;
    }

    public GioHang() {
    }

    public GioHang(String maSP, String tenSP, String gia, String hinhAnh, int soLuong, String giaMacDinh) {
        MaSP = maSP;
        TenSP = tenSP;
        Gia = gia;
        HinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.giaMacDinh = giaMacDinh;
    }
}
