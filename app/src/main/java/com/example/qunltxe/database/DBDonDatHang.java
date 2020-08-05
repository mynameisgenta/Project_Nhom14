package com.example.qunltxe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qunltxe.data_models.DonHang;

import java.util.ArrayList;

public class DBDonDatHang extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuanLyDonHang";
    private static final String TABLE_DON_HANG = "DonHang";

    // Table don dat hang
    private static final String COLUMN_MA_DON_HANG = "madonhang";
    private static final String COLUMN_NGAY_DAT = "ngaydat";
    private static final String COLUMN_SOLUONG_DAT = "soluongdat";
    private static final String COLUMN_MA_XE = "maxe";
    private static final String COLUMN_TEN_XE = "tenxe";
    private static final String COLUMN_DON_GIA = "dongia";
    private static final String COLUMN_TONG_TIEN = "tongtien";

    public static final String CREATE_DONHANG_TABLE = "CREATE TABLE "
            + TABLE_DON_HANG + "("
            + COLUMN_MA_DON_HANG + " TEXT PRIMARY KEY NOT NULL,"
            + COLUMN_NGAY_DAT + " TEXT,"
            + COLUMN_MA_XE + " TEXT,"
            + COLUMN_TEN_XE + " TEXT,"
            + COLUMN_DON_GIA + " INTEGER,"
            + COLUMN_SOLUONG_DAT + " INTEGER,"
            + COLUMN_TONG_TIEN + " INTEGER" + ")";

    public DBDonDatHang(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DONHANG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREATE_DONHANG_TABLE");
        onCreate(db);
    }

    public void themDonHang(DonHang donHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_DON_HANG, donHang.getMaDonHang());
        values.put(COLUMN_NGAY_DAT, donHang.getNgayDat());
        values.put(COLUMN_MA_XE, donHang.getMaXe());
        values.put(COLUMN_TEN_XE, donHang.getTenXe());
        values.put(COLUMN_DON_GIA, donHang.getGiaXe());
        values.put(COLUMN_SOLUONG_DAT, donHang.getSoLuongDat());
        values.put(COLUMN_TONG_TIEN, donHang.getTongTien());
        db.insert(TABLE_DON_HANG, null, values);
        db.close();
    }

    public void xoaDonHang(DonHang donHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DON_HANG, COLUMN_MA_DON_HANG + "= ?", new String[]{String.valueOf(donHang.getMaDonHang())});
        db.close();
    }

    public ArrayList<DonHang> layDuLieuDonHang() {
        ArrayList<DonHang> data = new ArrayList<>();
        String sql = "SELECT * FROM DonHang";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        try {
            cursor.moveToFirst();
            do {
                DonHang donHang = new DonHang();
                donHang.setMaDonHang(cursor.getString(0));
                donHang.setNgayDat(cursor.getString(1));
                donHang.setMaXe(cursor.getString(2));
                donHang.setTenXe(cursor.getString(3));
                donHang.setGiaXe(cursor.getInt(4));
                donHang.setSoLuongDat(cursor.getInt(5));
                donHang.setTongTien(cursor.getInt(6));
                data.add(donHang);
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {
        }
        return data;
    }

    public DonHang layDuLieuDonHang(String madonhang) {
        DonHang donHang = new DonHang();
        String sql = "SELECT * FROM DonHang WHERE madonhang = '" + madonhang + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        try {
            cursor.moveToFirst();
            do {
                donHang.setMaDonHang(cursor.getString(0));
                donHang.setNgayDat(cursor.getString(1));
                donHang.setMaXe(cursor.getString(2));
                donHang.setTenXe(cursor.getString(3));
                donHang.setGiaXe(cursor.getInt(4));
                donHang.setSoLuongDat(cursor.getInt(5));
                donHang.setTongTien(cursor.getInt(6));
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {
        }
        return donHang;
    }

    public boolean kiemTraMaDonHang(String madonhang) {

        String[] columns = {
                COLUMN_MA_DON_HANG
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_MA_DON_HANG + " = ?";
        String[] selectionArgs = {madonhang};
        Cursor cursor = db.query(TABLE_DON_HANG,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

}
