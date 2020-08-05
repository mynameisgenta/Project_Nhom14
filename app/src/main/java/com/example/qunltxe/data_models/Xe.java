package com.example.qunltxe.data_models;

public class Xe {
    private String MaLoai;
    private String MaXe;
    private String TenXe;
    private int DungTich;
    private int SoLuong;
    private int DonGia;

    public Xe(String maXe, String tenXe, int soLuong, int donGia) {
        MaXe = maXe;
        TenXe = tenXe;
        SoLuong = soLuong;
        DonGia = donGia;
    }

    public Xe() {

    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String maLoai) {
        MaLoai = maLoai;
    }

    public String getMaXe() {
        return MaXe;
    }

    public void setMaXe(String maXe) {
        MaXe = maXe;
    }

    public String getTenXe() {
        return TenXe;
    }

    public void setTenXe(String tenXe) {
        TenXe = tenXe;
    }

    public int getDungTich() {
        return DungTich;
    }

    public void setDungTich(int dungTich) {
        DungTich = dungTich;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public int getDonGia() {
        return DonGia;
    }

    public void setDonGia(int donGia) {
        DonGia = donGia;
    }
}
