package com.example.qunltxe.View_Models.HomePage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.QuanLyCongTy.DanhSachCongTy;
import com.example.qunltxe.View_Models.QuanLyXe.DanhSachXe;
import com.example.qunltxe.View_Models.TaiKhoan.DanhSachTaiKhoan;

public class TrangChu extends AppCompatActivity {
    Button btnQuanLyCty, btnQuanLyXe, btnDonDatHang, btnExit, btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        setControl();
        setEvent();
    }


    private void setEvent() {

        btnQuanLyCty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, DanhSachCongTy.class);
                startActivity(intent);

            }
        });
        btnQuanLyXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this, DanhSachXe.class);
                startActivity(intent);

            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, DanhSachTaiKhoan.class);
                startActivity(intent);

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrangChu.this)
                        .setMessage("Bạn muốn thoát ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform Your Task Here--When No is pressed
                                dialog.cancel();
                            }
                        }).show();
            }
        });

    }

    public void setControl() {
        btnQuanLyCty = findViewById(R.id.btnQuanLyCty);
        btnQuanLyXe = findViewById(R.id.btnQuanLyXe);
        btnDonDatHang = findViewById(R.id.btnDonDatHang);
        btnUser = findViewById(R.id.btnUser);
        btnExit = findViewById(R.id.btnExit);
    }
}