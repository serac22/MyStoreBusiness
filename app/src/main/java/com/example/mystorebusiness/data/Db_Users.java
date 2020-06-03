package com.example.mystorebusiness.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


public class Db_Users extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DataBase.db";

    // User table name
    private static final String TABLE_USER = "tb_users";



    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_PHONE = "user_phone";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_BIRTH = "user_birth";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_IMAGE = "user_image";


    public Db_Users(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table sql query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_USERNAME + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_BIRTH + " TEXT,"
                + COLUMN_USER_ADDRESS + " TEXT,"
                + COLUMN_USER_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        // drop table sql query
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(DROP_USER_TABLE);


        // Create tables again
        onCreate(db);

    }


    public String addUser(UserAccount user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getMail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_PHONE, "none");
        values.put(COLUMN_USER_ADDRESS, "none");
        values.put(COLUMN_USER_BIRTH, "none");
        values.put(COLUMN_USER_IMAGE,"none");

        // Inserting Row
        long rez=db.insert(TABLE_USER, null, values);
        if(rez==-1) {
            db.close();
            return "Failed";
        }
        else {
            db.close();
            return "Successfully";
        }


    }



    public void updateUser(UserAccount user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getMail());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_BIRTH, user.getBirth());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void updateImageProfile(int id,String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_IMAGE,image);

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }






    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }


    public Cursor getUserID(String username){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select "+COLUMN_USER_ID+" from "+TABLE_USER+" where "+COLUMN_USER_USERNAME+"="+"'"+username+"'",null);
    }

    public Cursor getUsers(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_USER+" where "+COLUMN_USER_ID+"="+id,null);
    }

    public boolean checkUser(String username, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // delete user record by id
        long result=db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
        return result != 0;


    }

}
