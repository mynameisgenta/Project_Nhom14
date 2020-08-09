package com.example.qunltxe.screen.motobike;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.home.TrangChu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChinhSuaXe extends AppCompatActivity {

    Bitmap selectedBitmap;
    byte[] byteArr;
    ImageView imgPicture;
    EditText txtMaxe, txtTenXe, txtDungTich, txtSoLuong, txtMaLoai, txtDonGia;
    Button btnClear, btnEdit;
    ArrayList<Xe> dataXE = new ArrayList<>();

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        return byteArray.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_xe);
        getSupportActionBar().setTitle("Chỉnh sửa xe");
        setControl();
        setEvent();
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private void clearInput() {
        txtMaxe.setText("");
        txtTenXe.setText("");
        txtDungTich.setText("");
        txtDonGia.setText("");
    }

    private void layDuLieuXe() {
        String maxe = getIntent().getExtras().getString("maxe");
        DBXe dbXe = new DBXe(getApplicationContext());
        dataXE = dbXe.layDuLieuXe(maxe);
        txtMaxe.setText(dataXE.get(0).getMaXe());
        txtMaLoai.setText(dataXE.get(0).getMaLoai());
        txtTenXe.setText(dataXE.get(0).getTenXe());
        txtDungTich.setText(String.valueOf(dataXE.get(0).getDungTich()));
        txtSoLuong.setText(String.valueOf(dataXE.get(0).getSoLuong()));
        txtDonGia.setText(String.valueOf(dataXE.get(0).getDonGia()));
        byteArr = dataXE.get(0).getImage();
        if (byteArr != null) {
            imgPicture.setImageBitmap(convertByteArrayToBitmap(byteArr));
        }
    }

    private void setEvent() {
        layDuLieuXe();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatDuLieu();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Clear thông tin xe thành công");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });

        imgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhThuVien();
            }
        });

    }

    protected void yeuCauQuyenTruyCap() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.INTERNET}, 1);
    }

    protected void layQuyenTruyCap() {
        int permission_internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permission_write_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_internet != PackageManager.PERMISSION_GRANTED
                || permission_write_storage != PackageManager.PERMISSION_GRANTED || permission_read_storage != PackageManager.PERMISSION_GRANTED) {
            yeuCauQuyenTruyCap();
        }
    }

    private void chonAnhThuVien() {
        layQuyenTruyCap();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 200);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                byteArr = getBitmapAsByteArray(selectedBitmap);
                imgPicture.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setControl() {
        txtMaxe = findViewById(R.id.txtMaXe);
        txtTenXe = findViewById(R.id.txtTenXe);
        txtDungTich = findViewById(R.id.txtDungTich);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtMaLoai = findViewById(R.id.txtMaLoai);
        txtDonGia = findViewById(R.id.txtDonGia);
        btnEdit = findViewById(R.id.btnSua);
        btnClear = findViewById(R.id.btnClear);
        imgPicture = findViewById(R.id.uploadPictureXe);
    }

    public Xe getXe() {
        Xe xe = new Xe();
        xe.setMaXe(txtMaxe.getText().toString());
        xe.setMaLoai(txtMaLoai.getText().toString());
        xe.setTenXe(txtTenXe.getText().toString());
        xe.setDungTich(Integer.parseInt(txtDungTich.getText().toString()));
        xe.setSoLuong(Integer.parseInt(txtSoLuong.getText().toString()));
        xe.setDonGia(Integer.parseInt(txtDonGia.getText().toString()));
        xe.setImage(byteArr);
        return xe;
    }

    private void capNhatXe() {
        DBXe dbXe = new DBXe(getApplicationContext());
        Xe xe = getXe();
        dbXe.capnhatXe(xe);
    }

    public void capNhatDuLieu() {
        if (txtMaxe.getText().toString().isEmpty()) {
            txtMaxe.setError("Bạn chưa nhập mã xe");
        } else if (txtMaLoai.getText().toString().isEmpty()) {
            txtMaLoai.setError("Bạn chưa nhập mã loại");
        } else if (txtTenXe.getText().toString().isEmpty()) {
            txtTenXe.setError("Bạn chưa nhập tên xe");
        } else if (txtDungTich.getText().toString().isEmpty()) {
            txtDungTich.setError("Bạn chưa nhập dung tích");
        } else if (txtSoLuong.getText().toString().isEmpty()) {
            txtSoLuong.setError("Bạn chưa nhập số lượng");
        } else if (txtDonGia.getText().toString().isEmpty()) {
            txtDonGia.setError("Bạn chưa nhập đơn giá");
        } else {
            capNhatXe();
            AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Cập nhật thông tin xe thành công");
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
                backHomePage();
                break;

            case R.id.menuItemUpdate:
                danhSachXe();
                break;


            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ChinhSuaXe.this);
                alert.setTitle("Thông báo");
                alert.setMessage("Có lỗi xảy ra !");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backHomePage() {
        Intent intent = new Intent(ChinhSuaXe.this, TrangChu.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public void danhSachXe() {
        Intent intent = new Intent(this, DanhSachXe.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
    }

}