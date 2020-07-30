package com.example.qunltxe.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qunltxe.Data_Models.CongTy;

import java.util.ArrayList;

public class DBCongTy extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuanLyDatHangXe";
    private static final String TABLE_CTY = "Cty";

    // Table Cong Ty
    private static final String COLUMN_MA_LOAI = "maloai";
    private static final String COLUMN_TEN_LOAI = "tenloai";
    private static final String COLUMN_XUAT_XU = "xuatxu";

    public static final String CREATE_CTY_TABLE = "CREATE TABLE "
            + TABLE_CTY + "("
            + COLUMN_MA_LOAI + " TEXT PRIMARY KEY NOT NULL,"
            + COLUMN_TEN_LOAI + " TEXT,"
            + COLUMN_XUAT_XU + " TEXT" + ")";

    public DBCongTy(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CTY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREATE_CTY_TABLE");
        onCreate(db);
    }

    public void addCongty(CongTy congTy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_LOAI, congTy.getMaLoai());
        values.put(COLUMN_TEN_LOAI, congTy.getTenLoai());
        values.put(COLUMN_XUAT_XU, congTy.getXuatXu());
        db.insert(TABLE_CTY, null, values);
        db.close();
    }

    public void updateCongty(CongTy congTy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_LOAI, congTy.getMaLoai());
        values.put(COLUMN_TEN_LOAI, congTy.getTenLoai());
        values.put(COLUMN_XUAT_XU, congTy.getXuatXu());
        db.update(TABLE_CTY, values, COLUMN_MA_LOAI + " = ?", new String[]{String.valueOf(congTy.getMaLoai())});
        db.close();
    }

    public void deleteCongty(CongTy congTy) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CTY, COLUMN_MA_LOAI + "= ?", new String[]{String.valueOf(congTy.getMaLoai())});
        db.close();
    }

    public ArrayList<CongTy> getAllCty() {
        ArrayList<CongTy> data = new ArrayList<>();
        String sql = "SELECT * FROM Cty";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        try {
            cursor.moveToFirst();
            do {
                CongTy congTy = new CongTy();
                congTy.setMaLoai(cursor.getString(0));
                congTy.setTenLoai(cursor.getString(1));
                congTy.setXuatXu(cursor.getString(2));
                data.add(congTy);
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {
        }
        return data;
    }

    public ArrayList<CongTy> getAllCty(String maloai) {
        ArrayList<CongTy> data = new ArrayList<>();
        String sql = "SELECT * FROM Cty WHERE maloai = '" + maloai + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        try {
            cursor.moveToFirst();
            do {
                CongTy congTy = new CongTy();
                congTy.setMaLoai(cursor.getString(0));
                congTy.setTenLoai(cursor.getString(1));
                congTy.setXuatXu(cursor.getString(2));
                data.add(congTy);
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {

        }
        return data;
    }

    public boolean checkCodeMoto(String username) {

        String[] columns = {
                COLUMN_MA_LOAI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_MA_LOAI + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_CTY,
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
