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
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.Data_Models.CongTy;
import com.example.qunltxe.Database.DBCongTy;
import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.HomePage.TrangChu;

public class ThemCongTy extends AppCompatActivity {
    EditText ed_maloai, ed_tenloai, ed_xuatxu;
    Button btn_them, btn_clear;
    RecyclerView recyclerViewCty;
    DBCongTy dbCongTy;
    CongTy congTy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_cty);
        setControl();
        setEvent();
        KhoiTaoData();
    }

    private void KhoiTaoData() {
        dbCongTy = new DBCongTy(ThemCongTy.this);
        congTy = new CongTy();
    }

    private void setEvent() {
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputAddCty();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_maloai.setText("");
                ed_tenloai.setText("");
                ed_xuatxu.setText("");
            }
        });
    }

    private void checkInputAddCty() {
        if (ed_maloai.getText().toString().isEmpty()) {
            ed_maloai.setError("Bạn chưa nhập mã loại");
        } else if (ed_tenloai.getText().toString().isEmpty()) {
            ed_tenloai.setError("Bạn chưa nhập tên loại");
        } else if (ed_xuatxu.getText().toString().isEmpty()) {
            ed_xuatxu.setError("Bạn chưa nhập xuất xứ");
        } else if (!dbCongTy.checkCodeCongTy(ed_maloai.getText().toString().trim())) {
            congTy.setMaLoai(ed_maloai.getText().toString().trim());
            congTy.setTenLoai(ed_tenloai.getText().toString().trim());
            congTy.setXuatXu(ed_xuatxu.getText().toString().trim());
            dbCongTy.addCongty(congTy);

            AlertDialog.Builder alert = new AlertDialog.Builder(ThemCongTy.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Thêm công ty thành công");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ThemCongTy.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Mã loại đã tồn tại");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.congty_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID) {
            case R.id.menuItemHome:
                home();
                break;

            case R.id.menuItemUpdate:
                checkList();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ThemCongTy.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Có lỗi xảy ra !");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void home() {
        new AlertDialog.Builder(this)
                .setMessage("Về trang chính ?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ThemCongTy.this, TrangChu.class);
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

    public void checkList() {
        Intent intent = new Intent(this, DanhSachCty.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }

    private void setControl() {
        ed_maloai = findViewById(R.id.ed_maloai);
        ed_tenloai = findViewById(R.id.ed_tenloai);
        ed_xuatxu = findViewById(R.id.ed_xuatxu);
        btn_them = findViewById(R.id.btn_them);
        btn_clear = findViewById(R.id.btn_clear);
        recyclerViewCty = findViewById(R.id.recyclerViewCty);
    }
}
