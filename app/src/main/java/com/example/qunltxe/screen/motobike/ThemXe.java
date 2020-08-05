package com.example.qunltxe.screen.motobike;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBCongTy;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.home.TrangChu;

import java.util.List;

public class ThemXe extends AppCompatActivity {
    EditText txtMaxe, txtTenXe, txtDungTich, txtSoLuong, txtDonGia;
    Spinner txtMaLoai;
    Button btnAdd, btnClear;
    RecyclerView recyclerViewXe;
    DBXe dbXe;
    DBCongTy dbCongTy;
    Xe xe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_xe);
        setControl();
        setEvent();
        loadSpinnerData();
        KhoiTaoData();
    }

    private void KhoiTaoData() {
        dbXe = new DBXe(ThemXe.this);
        xe = new Xe();
    }

    public void setEvent() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputAddXe();
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
        txtMaxe.setText("");
        txtTenXe.setText("");
        txtDungTich.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
    }

    public void setControl() {
        txtMaxe = findViewById(R.id.txtMaXe);
        txtTenXe = findViewById(R.id.txtTenXe);
        txtDungTich = findViewById(R.id.txtDungTich);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtMaLoai = findViewById(R.id.txtMaLoai);
        txtDonGia = findViewById(R.id.txtDonGia);
        btnAdd = findViewById(R.id.btnThem);
        btnClear = findViewById(R.id.btnClear);
        recyclerViewXe = findViewById(R.id.recyclerViewXe);
    }

    private void loadSpinnerData() {
        dbCongTy = new DBCongTy(getApplicationContext());
        List<String> data = dbCongTy.getALLMaLoai();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtMaLoai.setAdapter(dataAdapter);
    }

    private void checkInputAddXe() {
        String text_MaLoai = txtMaLoai.getSelectedItem().toString().trim();
        if (txtMaxe.getText().toString().isEmpty()) {
            txtMaxe.setError("Bạn chưa nhập mã xe");
        } else if (txtTenXe.getText().toString().isEmpty()) {
            txtTenXe.setError("Bạn chưa nhập tên xe");
        } else if (txtDungTich.getText().toString().isEmpty()) {
            txtDungTich.setError("Bạn chưa nhập dung tích");
        } else if (txtSoLuong.getText().toString().isEmpty()) {
            txtSoLuong.setError("Bạn chưa nhập số lượng");
        } else if (txtDonGia.getText().toString().isEmpty()) {
            txtDonGia.setError("Bạn chưa nhập đơn giá");
        } else if (!dbXe.checkCodeMoto(txtMaxe.getText().toString().trim())) {
            xe.setMaLoai(text_MaLoai);
            xe.setMaXe(txtMaxe.getText().toString().trim());
            xe.setTenXe(txtTenXe.getText().toString().trim());
            xe.setDungTich(Integer.parseInt(txtDungTich.getText().toString().trim()));
            xe.setSoLuong(Integer.parseInt(txtSoLuong.getText().toString().trim()));
            xe.setDonGia(Integer.parseInt(txtDonGia.getText().toString().trim()));
            dbXe.addMoto(xe);

            AlertDialog.Builder alert = new AlertDialog.Builder(ThemXe.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Thêm xe thành công");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ThemXe.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Mã xe đã tồn tại");
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
                home();
                break;

            case R.id.menuItemUpdate:
                checkList();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ThemXe.this);
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
                        Intent intent = new Intent(ThemXe.this, TrangChu.class);
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
        Intent intent = new Intent(this, DanhSachXe.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }

}