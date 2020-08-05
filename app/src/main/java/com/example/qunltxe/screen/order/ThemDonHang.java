package com.example.qunltxe.screen.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.adapter.SpinnerAdapter;
import com.example.qunltxe.data_models.DonHang;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBDonDatHang;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.home.TrangChu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThemDonHang extends AppCompatActivity {
    String maXe;
    EditText txtMaddh, txtNgayDat, txtTenXe, txtSoLuongDatHang, txtDonGia, txtThanhTien, txtsoLuongHienTai;
    Spinner spinnerMaXe;
    Button btnAdd, btnClear;
    RecyclerView recyclerViewDonHang;
    DBXe dbXe;
    DBDonDatHang dbDonDatHang;
    DonHang donHang;
    SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_don_hang);
        setControl();
        setEvent();
        KhoiTaoData();
        loadSpinnerData();
    }

    private void KhoiTaoData() {
        dbDonDatHang = new DBDonDatHang(ThemDonHang.this);
        donHang = new DonHang();
    }

    public void setEvent() {
        ngayNhapDonHang();
        thietLapTongTien();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputAddDonHang();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput();
            }
        });
    }

    private void clearInput() {
        txtMaddh.setText("");
        txtSoLuongDatHang.setText("");
    }

    public void setControl() {
        txtsoLuongHienTai = findViewById(R.id.txtsoLuongHienTai);
        txtMaddh = findViewById(R.id.txtMaddh);
        txtNgayDat = findViewById(R.id.txtNgayDat);
        spinnerMaXe = findViewById(R.id.spinnerMaXe);
        txtTenXe = findViewById(R.id.txtTenXe);
        txtSoLuongDatHang = findViewById(R.id.txtSoLuong);
        txtDonGia = findViewById(R.id.txtDonGia);
        txtThanhTien = findViewById(R.id.txtThanhTien);
        btnAdd = findViewById(R.id.btnThem);
        btnClear = findViewById(R.id.btnClear);
        recyclerViewDonHang = findViewById(R.id.recyclerViewDonHang);
    }

    private void thietLapTongTien() {
        txtThanhTien.setText("0");
        txtSoLuongDatHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String soLuongDat = txtSoLuongDatHang.getText().toString();
                if (soLuongDat.isEmpty()) {
                    txtThanhTien.setText("0");
                } else {
                    txtThanhTien.setText(tinhTongTien() + "");
                }

            }
        });

    }

    private void ngayNhapDonHang() {
        String date = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        txtNgayDat.setText(date);
    }

    public void loadSpinnerData() {
        dbXe = new DBXe(getApplicationContext());
        List<Xe> data = dbXe.layThongTinXe();
        spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinnerMaXe.setAdapter(spinnerAdapter);
        spinnerMaXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Xe xe = spinnerAdapter.getItem(i);
                txtTenXe.setText(xe.getTenXe());
                txtDonGia.setText(String.valueOf(xe.getDonGia()));
                txtsoLuongHienTai.setText(String.valueOf(xe.getSoLuong()));
                maXe = xe.getMaXe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int tinhTongTien() {
        int tongTien = 0;
        int soLuongDat = Integer.parseInt(txtSoLuongDatHang.getText().toString().trim());
        int donGiaXe = Integer.parseInt(txtDonGia.getText().toString().trim());
        tongTien = soLuongDat * donGiaXe;
        return tongTien;
    }

    public int tinhSoLuongXeConLai() {
        int soLuongConLai = 0;
        int soLuongDatHang = Integer.parseInt(txtSoLuongDatHang.getText().toString());
        int soLuongXeHienCo = Integer.parseInt(txtsoLuongHienTai.getText().toString());
        soLuongConLai = soLuongXeHienCo - soLuongDatHang;
        return soLuongConLai;
    }

    public Xe getXe() {
        int soLuongXeConLai = tinhSoLuongXeConLai();
        Xe xe = new Xe();
        xe.setMaXe(maXe);
        xe.setSoLuong(soLuongXeConLai);
        return xe;
    }

    private void updateSoLuongXe() {
        DBXe dbXe = new DBXe(getApplicationContext());
        Xe xe = getXe();
        dbXe.laySoLuongXe(xe);
    }

    private void themDonHang() {
        donHang.setMaDonHang(txtMaddh.getText().toString().trim());
        donHang.setNgayDat(txtNgayDat.getText().toString().trim());
        donHang.setMaXe(maXe);
        donHang.setTenXe(txtTenXe.getText().toString().trim());
        donHang.setSoLuongDat(Integer.parseInt(txtSoLuongDatHang.getText().toString().trim()));
        donHang.setGiaXe(Integer.parseInt(txtDonGia.getText().toString().trim()));
        donHang.setTongTien(Integer.parseInt(txtThanhTien.getText().toString().trim()));
        dbDonDatHang.themDonHang(donHang);
    }

    private void checkInputAddDonHang() {
        int SLHienTai = Integer.parseInt(txtsoLuongHienTai.getText().toString());
        int SLDatHang = Integer.parseInt(txtSoLuongDatHang.getText().toString());
        if (txtMaddh.getText().toString().isEmpty()) {
            txtMaddh.setError("Bạn chưa nhập mã đơn hàng");
        } else if (txtSoLuongDatHang.getText().toString().isEmpty()) {
            txtSoLuongDatHang.setError("Bạn chưa nhập số lượng đặt hàng");
        } else if (SLDatHang > SLHienTai) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ThemDonHang.this);
            alert.setTitle("Lỗi");
            alert.setMessage("Số lượng xe hiện tại không đủ");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else if (!dbDonDatHang.kiemTraMaDonHang(txtMaddh.getText().toString().trim())) {
            updateSoLuongXe();
            themDonHang();
            AlertDialog.Builder alert = new AlertDialog.Builder(ThemDonHang.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Thêm đơn hàng thành công");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ThemDonHang.this);
            alert.setTitle("Lỗi");
            alert.setMessage("Mã đơn hàng đã tồn tại");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
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
                AlertDialog.Builder alert = new AlertDialog.Builder(ThemDonHang.this);
                alert.setTitle("Lỗi");
                alert.setMessage("Có lỗi xảy ra !");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backHomePage() {
        new AlertDialog.Builder(this)
                .setMessage("Về trang chính ?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ThemDonHang.this, TrangChu.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void danhSachDonHang() {
        Intent intent = new Intent(this, DanhSachDonHang.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}