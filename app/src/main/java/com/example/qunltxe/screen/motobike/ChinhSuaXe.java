package com.example.qunltxe.screen.motobike;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.home.TrangChu;

import java.util.ArrayList;

public class ChinhSuaXe extends AppCompatActivity {
    EditText txtMaxe, txtTenXe, txtDungTich, txtSoLuong, txtMaLoai, txtDonGia;
    Button btnClear, btnEdit;
    ArrayList<Xe> dataXE = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_xe);
        setControl();
        setEvent();
    }

    private void clearInput() {
        txtMaxe.setText("");
        txtTenXe.setText("");
        txtDungTich.setText("");
        txtDonGia.setText("");
    }

    private void setEvent() {
        String maxe = getIntent().getExtras().getString("maxe");
        DBXe dbXe = new DBXe(getApplicationContext());
        dataXE = dbXe.getAllXe(maxe);
        txtMaxe.setText(dataXE.get(0).getMaXe());
        txtMaLoai.setText(dataXE.get(0).getMaLoai());
        txtTenXe.setText(dataXE.get(0).getTenXe());
        txtDungTich.setText(String.valueOf(dataXE.get(0).getDungTich()));
        txtSoLuong.setText(String.valueOf(dataXE.get(0).getSoLuong()));
        txtDonGia.setText(String.valueOf(dataXE.get(0).getDonGia()));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputUpdate();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Clear thông tin xe thành công");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

    private void setControl() {
        txtMaxe = findViewById(R.id.txtMaXe);
        txtTenXe = findViewById(R.id.txtTenXe);
        txtDungTich = findViewById(R.id.txtDungTich);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtMaLoai = findViewById(R.id.txtMaLoai);
        txtDonGia = findViewById(R.id.txtDonGia);
        btnEdit = findViewById(R.id.btnSua);
        btnClear = findViewById(R.id.btnClear);
    }

    public Xe getXe() {
        Xe xe = new Xe();
        xe.setMaXe(txtMaxe.getText().toString());
        xe.setMaLoai(txtMaLoai.getText().toString());
        xe.setTenXe(txtTenXe.getText().toString());
        xe.setDungTich(Integer.parseInt(txtDungTich.getText().toString()));
        xe.setSoLuong(Integer.parseInt(txtSoLuong.getText().toString()));
        xe.setDonGia(Integer.parseInt(txtDonGia.getText().toString()));
        return xe;
    }

    public void InputUpdate() {
        if (txtMaxe.getText().toString().isEmpty()) {
            txtMaxe.setError("Bạn chưa nhập mã xe");
        } else if (txtMaLoai.getText().toString().isEmpty()) {
            txtMaLoai.setError("Bạn chưa nhập mã loại");
        } else if (txtTenXe.getText().toString().isEmpty()) {
            txtTenXe.setError("Bạn chưa nhập tên xe");
        } else if (txtDungTich.getText().toString().isEmpty()) {
            txtDungTich.setError("Bạn chưa nhập dung tích");
        } else if (txtSoLuong.getText().toString().isEmpty()) {
            txtSoLuong.setError("Bạn chưa nhập số lượng");
        } else if (txtDonGia.getText().toString().isEmpty()) {
            txtDonGia.setError("Bạn chưa nhập đơn giá");
        } else {
            DBXe dbXe = new DBXe(getApplicationContext());
            Xe xe = getXe();
            dbXe.updateMoto(xe);
            AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Cập nhật thông tin xe thành công");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.xe_actionbar, menu);
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
                checkListMoto();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
                alert.setTitle("Thông báo");
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
                        Intent intent = new Intent(ChinhSuaXe.this, TrangChu.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform Your Task Here--When No is pressed
                        dialog.cancel();
                    }
                }).show();
    }

    public void checkListMoto() {
        Intent intent = new Intent(this, DanhSachXe.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }

}