package com.example.qunltxe.View_Models.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.Database.DBCongTy;
import com.example.qunltxe.Database.DBUser;
import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.DangKy.DangKy;
import com.example.qunltxe.View_Models.HomePage.TrangChu;

public class DangNhap extends AppCompatActivity {
    EditText login_username;
    EditText login_password;
    Button btn_login;
    ImageButton ib_finger;
    CheckBox cb_rememberme;
    TextView register;
    DBUser dbUser;
    DBCongTy dbCongTy;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setControl();
        setEvent();
        CreateData();
    }

    private void CreateData() {
        dbUser = new DBUser(DangNhap.this);
    }

    public void setEvent() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        ib_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, VanTay.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String name = sharedPreferences.getString("name", "");
        String passwords = sharedPreferences.getString("passowrd", "");

        login_password.setText(passwords);
        login_username.setText(name);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputLogin();
                if (cb_rememberme.isChecked()) {
                    editor.putString("name", login_username.getText().toString());
                    editor.putString("passowrd", login_password.getText().toString());
                    editor.commit();
                } else {
                    editor.putString("name", "");
                    editor.putString("passowrd", "");
                    editor.commit();
                }
            }
        });
    }

    public void setControl() {
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.btn_login);
        ib_finger = findViewById(R.id.ib_finger);
        cb_rememberme = findViewById(R.id.cb_rememberme);
        register = findViewById(R.id.go_register);
    }

    private void checkInputLogin() {
        if (login_username.getText().toString().isEmpty()) {
            login_username.setError("Bạn chưa nhập tài khoản");
        } else if (login_password.getText().toString().isEmpty()) {
            login_password.setError("Bạn chưa nhập mật khẩu");
        } else if (dbUser.checkUserLogin(login_username.getText().toString().trim()
                , login_password.getText().toString().trim())) {
            putPref("username", login_username.getText().toString().trim(), getApplicationContext());
            Intent accountsIntent = new Intent(DangNhap.this, TrangChu.class);
            startActivity(accountsIntent);
            accountsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(DangNhap.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Sai tài khoản hoặc mật khẩu");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
    }

}