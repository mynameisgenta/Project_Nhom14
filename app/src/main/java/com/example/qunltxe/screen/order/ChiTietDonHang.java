package com.example.qunltxe.screen.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.DonHang;
import com.example.qunltxe.database.DBDonDatHang;
import com.example.qunltxe.screen.home.TrangChu;

import java.text.DecimalFormat;

public class ChiTietDonHang extends AppCompatActivity {

    EditText txtMaddh, txtNgayDat, txtMaXe, txtTenXe, txtSoLuong, txtDonGia, txtThanhTien;
    DonHang currentDonHang = new DonHang();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chi_tiet_don_hang);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        setControl();
        setEvent();
    }

    private void layDuLieuDonHang() {
        String madonhang = getIntent().getExtras().getString("madonhang");
        DBDonDatHang dbDonDatHang = new DBDonDatHang(getApplicationContext());
        currentDonHang = dbDonDatHang.layDuLieuDonHang(madonhang);
        txtMaddh.setText(currentDonHang.getMaDonHang());
        txtNgayDat.setText(currentDonHang.getNgayDat());
        txtMaXe.setText(currentDonHang.getMaXe());
        txtTenXe.setText(currentDonHang.getTenXe());
        txtSoLuong.setText(String.valueOf(currentDonHang.getSoLuongDat()));
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtDonGia.setText(formatter.format(currentDonHang.getGiaXe()) + " VNĐ");
        txtThanhTien.setText(formatter.format(currentDonHang.getTongTien()) + " VNĐ");
    }

    private void setEvent() {
        layDuLieuDonHang();
    }

    private void setControl() {
        txtMaddh = findViewById(R.id.txtMaddh);
        txtNgayDat = findViewById(R.id.txtNgayDat);
        txtMaXe = findViewById(R.id.txtMaXe);
        txtTenXe = findViewById(R.id.txtTenXe);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtDonGia = findViewById(R.id.txtDonGia);
        txtThanhTien = findViewById(R.id.txtThanhTien);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donhang_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID) {
            case R.id.menuItemHome:
                backHomePage();
                break;

            case R.id.menuItemUpdate:
                danhSachDonHang();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChiTietDonHang.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Có lỗi xảy ra !");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backHomePage() {
        Intent intent = new Intent(ChiTietDonHang.this, TrangChu.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public void danhSachDonHang() {
        Intent intent = new Intent(this, DanhSachDonHang.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}