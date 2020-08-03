package com.example.qunltxe.screen.register;

import android.Manifest;
import android.content.DialogInterface;
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        return byteArray.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_user);
        dbUser = new DBUser(this);
        initViews();
        initData();
        initListeners();
    }

    private void initListeners() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUpdate();
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
                choosePicture();
            }
        });

        capturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
    }

    private void initViews() {
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

    private void initData() {
        String username = getIntent().getExtras().getString("user_name");
        DBUser dbUser = new DBUser(getApplicationContext());
        currentUser = dbUser.getUserByUserName(username);
        txtUsername.setText(currentUser.getUsername());
        txtFullname.setText(currentUser.getFullname());
        txtPassword.setText(currentUser.getPassword());
        byteArr = currentUser.getImage();
        if (byteArr != null) {
            imgPicture.setImageBitmap(convertByteArrayToBitmap(byteArr));
        }
    }

    public void updateCurrentUser() {
        currentUser.setUsername(txtUsername.getText().toString());
        currentUser.setFullname(txtFullname.getText().toString());
        currentUser.setPassword(txtPassword.getText().toString());
        currentUser.setImage(byteArr);
    }

    // yêu cầu quyền truy cập
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.INTERNET}, 1);
    }

    protected void getPermission() {
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
            makeRequest();
        }
    }

    // chụp từ camera
    private void capturePicture() {
        getPermission();
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt, 100);
    }

    // chọn từ thư viện
    private void choosePicture() {
        getPermission();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 200);
    }

    // xử lí ảnh
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

    public void inputUpdate() {
        if (txtFullname.getText().toString().isEmpty()) {
            txtFullname.setError("Bạn chưa nhập họ tên");
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Bạn chưa nhập mật khẩu");
        } else {
            updateCurrentUser();
            dbUser.updateUser(currentUser);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }
}