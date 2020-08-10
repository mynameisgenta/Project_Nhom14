package com.example.qunltxe.screen.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.User;
import com.example.qunltxe.database.DBUser;
import com.example.qunltxe.screen.home.TrangChu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChinhSuaTaiKhoan extends AppCompatActivity {
    Bitmap selectedBitmap;
    ImageView imgPicture;
    ImageButton uploadPicture, capturePicture;
    EditText txtUsername, txtFullname, txtPassword;
    Button btnClear, btnEdit;
    User currentUser = new User();
    byte[] byteArr;
    DBUser dbUser;

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        return byteArray.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_user);
        getSupportActionBar().setTitle("Chỉnh sửa tài khoản");
        dbUser = new DBUser(this);
        setControl();
        layDuLieuTaiKhoan();
        setEvent();
    }

    private void setEvent() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatTaiKhoan();
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

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhThuVien();
            }
        });

        capturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chupHinhCamera();
            }
        });
    }

    private void setControl() {
        txtUsername = findViewById(R.id.txtUsername);
        txtFullname = findViewById(R.id.txtFullname);
        txtPassword = findViewById(R.id.txtPassword);
        btnEdit = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
        imgPicture = findViewById(R.id.imgUserPicture);
        uploadPicture = findViewById(R.id.uploadPicture);
        capturePicture = findViewById(R.id.capturePicture);
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private void layDuLieuTaiKhoan() {
        String username = getIntent().getExtras().getString("user_name");
        DBUser dbUser = new DBUser(getApplicationContext());
        currentUser = dbUser.kiemTraTaiKhoan(username);
        txtUsername.setText(currentUser.getUsername());
        txtFullname.setText(currentUser.getFullname());
        txtPassword.setText(currentUser.getPassword());
        byteArr = currentUser.getImage();
        if (byteArr != null) {
            imgPicture.setImageBitmap(convertByteArrayToBitmap(byteArr));
        }
    }

    public void thietLapTaiKhoan() {
        currentUser.setUsername(txtUsername.getText().toString());
        currentUser.setFullname(txtFullname.getText().toString());
        currentUser.setPassword(txtPassword.getText().toString());
        currentUser.setImage(byteArr);
    }

    protected void yeuCauQuyenTruyCap() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.INTERNET}, 1);
    }

    protected void layQuyenTruyCap() {
        int permission_camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permission_internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permission_write_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_camera != PackageManager.PERMISSION_GRANTED || permission_internet != PackageManager.PERMISSION_GRANTED
                || permission_write_storage != PackageManager.PERMISSION_GRANTED || permission_read_storage != PackageManager.PERMISSION_GRANTED) {
            yeuCauQuyenTruyCap();
        }
    }

    private void chupHinhCamera() {
        layQuyenTruyCap();
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt, 100);
    }

    private void chonAnhThuVien() {
        layQuyenTruyCap();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 200);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            byteArr = getBitmapAsByteArray(selectedBitmap);
            currentUser.setImage(byteArr);
            imgPicture.setImageBitmap(selectedBitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                byteArr = getBitmapAsByteArray(selectedBitmap);
                currentUser.setImage(byteArr);
                imgPicture.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void capNhatTaiKhoan() {
        if (txtFullname.getText().toString().isEmpty()) {
            txtFullname.setError("Bạn chưa nhập họ tên");
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Bạn chưa nhập mật khẩu");
        } else if (txtPassword.getText().toString().length() < 6) {
            txtPassword.setError("Mật khẩu phải có ít nhất 6 kí tự");
        } else {
            thietLapTaiKhoan();
            dbUser.capnhatTaiKhoan(currentUser);
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
                danhSachTaiKhoan();
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
        Intent intent = new Intent(ChinhSuaTaiKhoan.this, TrangChu.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public void danhSachTaiKhoan() {
        Intent intent = new Intent(this, DanhSachTaiKhoan.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}