package com.example.btl_ltuddd_electricstore.object;

public class LichSuMuaHang {
    private String CMT;
    private String TrangThai;
    private String TenKH;
    private String sdt;
    private String DiaChi;
    private String TenSP;
    private String HinhAnh;
    private String Gia;
    private int SoLuong;
    private String NgayMua;
    private String NgayXacNhan;

    public String getCMT() {
        return CMT;
    }

    public void setCMT(String CMT) {
        this.CMT = CMT;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String tenKH) {
        this.TenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        this.DiaChi = diaChi;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        this.TenSP = tenSP;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.HinhAnh = hinhAnh;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        this.Gia = gia;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        this.SoLuong = soLuong;
    }

    public String getNgayMua() {
        return NgayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.NgayMua = ngayMua;
    }

    public String getNgayXacNhan() {
        return NgayXacNhan;
    }

    public void setNgayXacNhan(String ngayXacNhan) {
        NgayXacNhan = ngayXacNhan;
    }

    public LichSuMuaHang() {
    }

    public LichSuMuaHang(String CMT, String trangThai, String tenKH, String sdt, String diaChi, String tenSP, String hinhAnh, String gia, int soLuong, String ngayMua, String ngayXacNhan) {
        this.CMT = CMT;
        this.TrangThai = trangThai;
        this.TenKH = tenKH;
        this.sdt = sdt;
        this.DiaChi = diaChi;
        this.TenSP = tenSP;
        this.HinhAnh = hinhAnh;
        this.Gia = gia;
        this.SoLuong = soLuong;
        this.NgayMua = ngayMua;
        this.NgayXacNhan = ngayXacNhan;
    }
}
