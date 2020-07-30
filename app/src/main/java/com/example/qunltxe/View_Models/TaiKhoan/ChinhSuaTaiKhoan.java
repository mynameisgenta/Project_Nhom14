package com.example.qunltxe.View_Models.TaiKhoan;

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

import com.example.qunltxe.Data_Models.User;
import com.example.qunltxe.Database.DBUser;
import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.HomePage.TrangChu;

import java.util.ArrayList;

public class ChinhSuaTaiKhoan extends AppCompatActivity {

    EditText txtUsername, txtFullname, txtPassword;
    Button btnClear, btnEdit;
    ArrayList<User> dataUSER = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_user);
        setControl();
        setEvent();
    }

    private void setControl() {
        txtUsername = findViewById(R.id.txtUsername);
        txtFullname = findViewById(R.id.txtFullname);
        txtPassword = findViewById(R.id.txtPassword);
        btnEdit = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
    }

    public User getUser() {
        User user = new User();
        user.setUsername(txtUsername.getText().toString());
        user.setFullname(txtFullname.getText().toString());
        user.setPassword(txtPassword.getText().toString());
        return user;
    }

    private void setEvent() {
        String username = getIntent().getExtras().getString("user_name");
        DBUser dbUser = new DBUser(getApplicationContext());
        dataUSER = dbUser.getAllUser(username);
        txtUsername.setText(dataUSER.get(0).getUsername());
        txtFullname.setText(dataUSER.get(0).getFullname());
        txtPassword.setText(dataUSER.get(0).getPassword());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputUpdate();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFullname.setText("");
                txtPassword.setText("");
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaTaiKhoan.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Clear thông tin xe thành công");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

    public void InputUpdate() {
        if (txtFullname.getText().toString().isEmpty()) {
            txtFullname.setError("Bạn chưa nhập họ tên");
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Bạn chưa nhập mật khẩu");
        } else {
            DBUser dbUser = new DBUser(getApplicationContext());
            User user = getUser();
            dbUser.updateUser(user);
            AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaTaiKhoan.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Cập nhật thông tin tài khoản thành công");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaTaiKhoan.this);
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
                        Intent intent = new Intent(ChinhSuaTaiKhoan.this, TrangChu.class);
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
        Intent intent = new Intent(this, DanhSachTaiKhoan.class);
        startActivity(intent);

        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}