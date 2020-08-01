package com.example.qunltxe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.qunltxe.data_models.User;

import java.util.ArrayList;


public class DBUser extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuanLyUser";
    private static final String TABLE_USER = "user";

    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_IMAGE = "image";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_NAME + " TEXT PRIMARY KEY,"
            + COLUMN_FULL_NAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_IMAGE + " BLOB" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DBUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);
        onCreate(db);

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_FULL_NAME, user.getFullname());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_FULL_NAME, user.getFullname());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_IMAGE, user.getImage());
        db.update(TABLE_USER, values, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getUsername())});
        db.close();
    }


    // get ảnh từ database
    public Bitmap getImageAvatar(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String qu = "SELECT image  FROM user where feedid=" + i;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()) {
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null;
    }

    public ArrayList<User> getAllUser() {
        ArrayList<User> data = new ArrayList<>();
        String sql = "SELECT * FROM user";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        do {
            User user = new User();
            user.setUsername(cursor.getString(0));
            user.setFullname(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setImage(cursor.getBlob(3));
            data.add(user);
        }
        while (cursor.moveToNext());
        return data;
    }

    public User getUserByUserName(String user_name) {
        User user = new User();
        String sql = "SELECT * FROM user WHERE user_name ='" + user_name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        do {
            user.setUsername(cursor.getString(0));
            user.setFullname(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setImage(cursor.getBlob(3));
        }
        while (cursor.moveToNext());
        return user;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getUsername())});
        db.close();
    }

    public boolean checkUserRegister(String username) {

        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USER,
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

    public boolean checkUserLogin(String username, String password) {

        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USER,
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
