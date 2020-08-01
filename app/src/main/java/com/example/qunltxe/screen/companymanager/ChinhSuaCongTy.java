package com.example.qunltxe.screen.companymanager;

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
import com.example.qunltxe.data_models.CongTy;
import com.example.qunltxe.database.DBCongTy;
import com.example.qunltxe.screen.home.TrangChu;

import java.util.ArrayList;

public class ChinhSuaCongTy extends AppCompatActivity {
    EditText ed_maloai, ed_tenloai, ed_xuatxu;
    Button btn_sua, btn_clear;
    ArrayList<CongTy> dataCTY = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_cty);
        setControl();
        setEvent();
    }

    private CongTy getCongTy() {
        CongTy congTy = new CongTy();
        congTy.setMaLoai(ed_maloai.getText().toString());
        congTy.setTenLoai(ed_tenloai.getText().toString());
        congTy.setXuatXu(ed_xuatxu.getText().toString());
        return congTy;
    }

    private void setEvent() {
        String maloai = getIntent().getExtras().getString("maloai");
        DBCongTy dbCongTy = new DBCongTy(this);
        dataCTY = dbCongTy.getAllCty(maloai);
        ed_maloai.setText(dataCTY.get(0).getMaLoai());
        ed_tenloai.setText(dataCTY.get(0).getTenLoai());
        ed_xuatxu.setText(dataCTY.get(0).getXuatXu());
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputUpdate();
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_tenloai.setText("");
                ed_xuatxu.setText("");
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
            alert.setMessage("Cập nhật thông tin thành công");
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
                backHomePage();
                break;

            case R.id.menuItemUpdate:
                checkListCongTy();
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

    public void checkListCongTy() {
        Intent intent = new Intent(this, DanhSachCty.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }

    private void setControl() {
        ed_maloai = findViewById(R.id.ed_maloai);
        ed_tenloai = findViewById(R.id.ed_tenloai);
        ed_xuatxu = findViewById(R.id.ed_xuatxu);
        btn_sua = findViewById(R.id.btn_sua);
        btn_clear = findViewById(R.id.btn_clear);
    }
}
