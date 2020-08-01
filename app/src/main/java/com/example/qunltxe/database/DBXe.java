package com.example.qunltxe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qunltxe.data_models.Xe;

import java.util.ArrayList;

public class DBXe extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuanLyXe";
    private static final String TABLE_XE = "Xe";

    // Table Xe
    private static final String COLUMN_MA_XE = "maxe";
    private static final String COLUMN_TEN_XE = "tenxe";
    private static final String COLUMN_DUNGTICH_XE = "dungtich";
    private static final String COLUMN_SOLUONG_XE = "soluong";
    private static final String COLUMN_MALOAI_XE = "maloai";
    private static final String COLUMN_DON_GIA = "dongia";

    public static final String CREATE_XE_TABLE = "CREATE TABLE "
            + TABLE_XE + "("
            + COLUMN_MA_XE + " TEXT PRIMARY KEY NOT NULL,"
            + COLUMN_MALOAI_XE + " TEXT,"
            + COLUMN_TEN_XE + " TEXT,"
            + COLUMN_DUNGTICH_XE + " INTEGER,"
            + COLUMN_SOLUONG_XE + " INTEGER,"
            + COLUMN_DON_GIA + " INTEGER" + ")";

    public DBXe(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_XE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREATE_XE_TABLE");
        onCreate(db);
    }


    public void addMoto(Xe xe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_XE, xe.getMaXe());
        values.put(COLUMN_MALOAI_XE, xe.getMaLoai());
        values.put(COLUMN_TEN_XE, xe.getTenXe());
        values.put(COLUMN_DUNGTICH_XE, xe.getDungTich());
        values.put(COLUMN_SOLUONG_XE, xe.getSoLuong());
        values.put(COLUMN_DON_GIA, xe.getDonGia());
        db.insert("Xe", null, values);
        db.close();
    }

    public void updateMoto(Xe xe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_XE, xe.getMaXe());
        values.put(COLUMN_MALOAI_XE, xe.getMaLoai());
        values.put(COLUMN_TEN_XE, xe.getTenXe());
        values.put(COLUMN_DUNGTICH_XE, xe.getDungTich());
        values.put(COLUMN_SOLUONG_XE, xe.getSoLuong());
        values.put(COLUMN_DON_GIA, xe.getDonGia());
        db.update(TABLE_XE, values, COLUMN_MA_XE + " = ?",
                new String[]{String.valueOf(xe.getMaXe())});
        db.close();
    }

    public void deleteMoto(Xe xe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_XE, COLUMN_MA_XE + " = ?",
                new String[]{String.valueOf(xe.getMaXe())});
        db.close();
    }

    public ArrayList<Xe> getAllXe() {
        ArrayList<Xe> data = new ArrayList<>();
        String sql = "SELECT * FROM Xe";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        try {
            cursor.moveToFirst();
            do {
                Xe xe = new Xe();
                xe.setMaXe(cursor.getString(0));
                xe.setMaLoai(cursor.getString(1));
                xe.setTenXe(cursor.getString(2));
                xe.setDungTich(cursor.getInt(3));
                xe.setSoLuong(cursor.getInt(4));
                xe.setDonGia(cursor.getString(5));
                data.add(xe);
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {
        }
        return data;
    }

    public ArrayList<Xe> getAllXe(String maxe) {
        ArrayList<Xe> data = new ArrayList<>();
        String sql = "SELECT * FROM Xe WHERE maxe ='" + maxe + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        try {
            cursor.moveToFirst();
            do {
                Xe xe = new Xe();
                xe.setMaXe(cursor.getString(0));
                xe.setMaLoai(cursor.getString(1));
                xe.setTenXe(cursor.getString(2));
                xe.setDungTich(cursor.getInt(3));
                xe.setSoLuong(cursor.getInt(4));
                xe.setDonGia(cursor.getString(5));
                data.add(xe);
            }
            while (cursor.moveToNext());
        } catch (Exception ex) {
        }

        return data;
    }

    public boolean checkCodeMoto(String username) {

        String[] columns = {
                COLUMN_MA_XE
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_MA_XE + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_XE,
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
