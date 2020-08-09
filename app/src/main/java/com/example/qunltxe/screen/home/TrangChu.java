package com.example.qunltxe.screen.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.User;
import com.example.qunltxe.database.DBUser;
import com.example.qunltxe.screen.company.DanhSachCty;
import com.example.qunltxe.screen.info_app.Information;
import com.example.qunltxe.screen.login.DangNhap;
import com.example.qunltxe.screen.motobike.DanhSachXe;
import com.example.qunltxe.screen.order.DanhSachDonHang;
import com.example.qunltxe.screen.register.DanhSachTaiKhoan;
import com.example.qunltxe.screen.statistical.ThongKe;

import java.io.ByteArrayInputStream;

import static com.example.qunltxe.screen.login.DangNhap.getPref;

public class TrangChu extends AppCompatActivity {
    ImageView btnExit, btnQuanLyCty, btnQuanLyXe, btnDonDatHang, btnUser, user_photo, btn_infoapp, btn_thongke;
    TextView fullname;
    User currentUser = new User();
    byte[] byteArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        setControl();
        setEvent();
    }

    private void layDuLieuTaiKhoan() {
        String current_username = getPref("username", this);
        DBUser dbUser = new DBUser(getApplicationContext());
        currentUser = dbUser.kiemTraTaiKhoan(current_username);
        fullname.setText(currentUser.getFullname());
        byteArr = currentUser.getImage();
        if (byteArr != null) {
            user_photo.setImageBitmap(convertByteArrayToBitmap(byteArr));
        }
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private void setEvent() {

        layDuLieuTaiKhoan();

        btnDonDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, DanhSachDonHang.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btn_thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, ThongKe.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btn_infoapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, Information.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btnQuanLyCty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this, DanhSachCty.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btnQuanLyXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this, DanhSachXe.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChu.this, DanhSachTaiKhoan.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrangChu.this)
                        .setMessage("Bạn muốn đăng xuất ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(TrangChu.this, DangNhap.class);
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
        });

    }

    public void setControl() {
        btnQuanLyCty = findViewById(R.id.btnQuanLyCty);
        btnQuanLyXe = findViewById(R.id.btnQuanLyXe);
        btnDonDatHang = findViewById(R.id.btnDonDatHang);
        btnUser = findViewById(R.id.btnUser);
        btnExit = findViewById(R.id.btnExit);
        user_photo = findViewById(R.id.user_photo);
        fullname = findViewById(R.id.full_name);
        btn_infoapp = findViewById(R.id.btn_infoapp);
        btn_thongke = findViewById(R.id.btn_thongke);
    }
}