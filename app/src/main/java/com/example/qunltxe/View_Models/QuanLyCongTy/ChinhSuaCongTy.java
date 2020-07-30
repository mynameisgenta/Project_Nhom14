package com.example.qunltxe.View_Models.QuanLyCongTy;

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

import com.example.qunltxe.Data_Models.CongTy;
import com.example.qunltxe.Data_Models.User;
import com.example.qunltxe.Data_Models.Xe;
import com.example.qunltxe.Database.DBCongTy;
import com.example.qunltxe.Database.DBUser;
import com.example.qunltxe.Database.DBXe;
import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.HomePage.TrangChu;
import com.example.qunltxe.View_Models.TaiKhoan.ChinhSuaTaiKhoan;
import com.example.qunltxe.View_Models.TaiKhoan.DanhSachTaiKhoan;

import java.util.ArrayList;

public class ChinhSuaCongTy extends AppCompatActivity {
    
    EditText ed_maloai, ed_tenloai, ed_xuatxu;
    Button btnClear, btnEdit;
    ArrayList<CongTy> dataCONGTY = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_user);
        setControl();
        setEvent();
    }

    private void setControl() {
        ed_maloai = findViewById(R.id.ed_maloai);
        ed_tenloai = findViewById(R.id.txtFullname);
        ed_xuatxu = findViewById(R.id.ed_xuatxu);
        btnEdit = findViewById(R.id.btn_sua);
        btnClear = findViewById(R.id.btn_clear);
    }

    public CongTy getCongTy() {
        CongTy congTy = new CongTy();
        congTy.setMaLoai(ed_maloai.getText().toString());
        congTy.setTenLoai(ed_tenloai.getText().toString());
        congTy.setXuatXu(ed_xuatxu.getText().toString());
        return congTy;
    }

    private void setEvent() {
        String maloai = getIntent().getExtras().getString("maloai");
        DBCongTy dbCongTy = new DBCongTy(getApplicationContext());
        dataCONGTY = dbCongTy.getAllCty(maloai);
        ed_maloai.setText(dataCONGTY.get(0).getMaLoai());
        ed_tenloai.setText(dataCONGTY.get(0).getTenLoai());
        ed_xuatxu.setText(dataCONGTY.get(0).getXuatXu());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputUpdate();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_maloai.setText("");
                ed_tenloai.setText("");
                ed_xuatxu.setText("");
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaCongTy.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Clear thông tin công ty thành công");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

    public void InputUpdate() {
        if (ed_maloai.getText().toString().isEmpty()) {
            ed_maloai.setError("Bạn chưa nhập mã loại");
        } else if (ed_tenloai.getText().toString().isEmpty()) {
            ed_tenloai.setError("Bạn chưa nhập tên loại");
        } else if (ed_xuatxu.getText().toString().isEmpty()) {
            ed_xuatxu.setError("Bạn chưa nhập xuất xứ");
        } else {
            DBCongTy dbCongTy = new DBCongTy(getApplicationContext());
            CongTy congTy = getCongTy();
            dbCongTy.updateCongty(congTy);
            AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaCongTy.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Cập nhật thông tin công ty thành công");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_actionbar, menu);
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
                checkListUser();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaCongTy.this);
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
                        Intent intent = new Intent(ChinhSuaCongTy.this, TrangChu.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform Your Task Here--When No is pressed
                        dialog.cancel();
                    }
                }).show();
    }

    public void checkListUser() {
        Intent intent = new Intent(this, DanhSachCongTy.class);
        startActivity(intent);

        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}