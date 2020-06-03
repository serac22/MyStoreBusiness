package com.example.mystorebusiness.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mystorebusiness.account.ui.sales.SaleItem;
import com.example.mystorebusiness.account.ui.stocks.StockItem;

import java.util.ArrayList;

public class Db_Sales extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DataBaseSales.db";

    // Stock table name
    private static final String TABLE_NAME = "tb_sales";

    // Stock Table Columns names
    public static final String COLUMN_SALE_ID = "id_sale";
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_COD = "cod";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_DATA = "data";


    public Db_Sales(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table sql query
        String CREATE_STOCKS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COD + " TEXT NOT NULL,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_TOTAL + " INTEGER NOT NULL DEFAULT 0,"
                + COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_DATA + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_STOCKS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop stocks Table if exist
        // drop table sql query
        String DROP_STOCKS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_STOCKS_TABLE);

        // Create tables again
        onCreate(db);

    }


    public String addItem(SaleItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID,item.getId_user());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_COD,item.getCod());
        values.put(COLUMN_TOTAL,item.getTotal());
        values.put(COLUMN_DATA,item.getData());


        // Inserting Row
        long rez=db.insert(TABLE_NAME, null, values);
        if(rez==-1) {
            db.close();
            return "Failed";
        }
        else {
            db.close();
            return "Successfully";
        }


    }



    public ArrayList<SaleItem> getAllItems(String sort,int id_user) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_SALE_ID,
                COLUMN_USER_ID,
                COLUMN_COD,
                COLUMN_NAME,
                COLUMN_TOTAL,
                COLUMN_QUANTITY,
                COLUMN_DATA
        };

        String sortOrder = COLUMN_SALE_ID + " ASC";

        ArrayList<SaleItem> saleList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the stocks table
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                COLUMN_USER_ID + " = ?",        //columns for the WHERE clause
                new String[]{String.valueOf(id_user)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SaleItem item= new SaleItem();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ID))));
                item.setId_user(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                item.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY))));
                item.setCod(cursor.getString(cursor.getColumnIndex(COLUMN_COD)));;
                item.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL))));
                item.setData(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));

                // Adding stocks record to list
                saleList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return stocks list
        return saleList;
    }


    public void deleteSale(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


}
