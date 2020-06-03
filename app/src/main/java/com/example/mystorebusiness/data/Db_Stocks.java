package com.example.mystorebusiness.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mystorebusiness.account.ui.stocks.StockItem;

import java.util.ArrayList;

public class Db_Stocks extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DataBaseStock.db";

    // Stock table name
    private static final String TABLE_NAME = "tb_stocks";

    // Stock Table Columns names
    public static final String COLUMN_STOCK_ID = "id_stock";
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_COD = "cod";
    public static final String COLUMN_ADDITION = "addition";
    public static final String COLUMN_FINAL_PRICE = "final_price";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_DATA_EXPIRATION = "data_expiration";


    public Db_Stocks(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table sql query
        String CREATE_STOCKS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COD + " TEXT NOT NULL,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + COLUMN_ADDITION + " INTEGER NOT NULL DEFAULT 0,"
                + COLUMN_FINAL_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_DATA + " TEXT NOT NULL,"
                + COLUMN_DATA_EXPIRATION + " TEXT NOT NULL,"
                + COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL" + ")";
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


    public String addItem(StockItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID,item.getId_user());
        values.put(COLUMN_NAME, item.getProductName());
        values.put(COLUMN_PRICE,item.getPrice());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_DESCRIPTION,item.getDescription());
        values.put(COLUMN_IMAGE,item.getImage());
        values.put(COLUMN_COD,item.getCod());
        values.put(COLUMN_ADDITION,item.getAddition());
        values.put(COLUMN_FINAL_PRICE,item.getFinal_price());
        values.put(COLUMN_DATA_EXPIRATION,item.getData_expiration());
        values.put(COLUMN_DATA,item.getData_add());


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



    public ArrayList<StockItem> getAllItems(String sort,int id_user) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_STOCK_ID,
                COLUMN_USER_ID,
                COLUMN_COD,
                COLUMN_NAME,
                COLUMN_PRICE,
                COLUMN_ADDITION,
                COLUMN_FINAL_PRICE,
                COLUMN_QUANTITY,
                COLUMN_DATA,
                COLUMN_DATA_EXPIRATION,
                COLUMN_DESCRIPTION,
                COLUMN_IMAGE
        };

        String sortOrder;
        switch (sort) {
            case "name":
                sortOrder = COLUMN_NAME + " ASC";break;
            case "price":
                sortOrder = COLUMN_PRICE + " ASC";break;
            case "quantity":
                sortOrder = COLUMN_QUANTITY + " ASC";break;

            default:
                sortOrder = COLUMN_STOCK_ID + " ASC";break;
        }



        ArrayList<StockItem> stockList = new ArrayList<>();

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
                StockItem item= new StockItem(0,null,null,null,null,null,null,null,null,null,null);
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_STOCK_ID))));
                item.setId_user(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                item.setProductName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                item.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
                item.setQuantity(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                item.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                item.setCod(cursor.getString(cursor.getColumnIndex(COLUMN_COD)));
                item.setAddition(cursor.getString(cursor.getColumnIndex(COLUMN_ADDITION)));
                item.setFinal_price(cursor.getString(cursor.getColumnIndex(COLUMN_FINAL_PRICE)));
                item.setData_add(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
                item.setData_expiration(cursor.getString(cursor.getColumnIndex(COLUMN_DATA_EXPIRATION)));

                // Adding stocks record to list
                stockList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return stocks list
        return stockList;
    }


    public boolean checkCod(String cod) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_STOCK_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_COD + " = ?";

        // selection arguments
        String[] selectionArgs = {cod};

        // query user table with conditions
        Cursor cursor = db.query(TABLE_NAME, //Table to query
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

    public void updateItem(StockItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getProductName());
        values.put(COLUMN_PRICE,item.getPrice());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_DESCRIPTION,item.getDescription());
        values.put(COLUMN_DATA,item.getData_add());
        values.put(COLUMN_IMAGE,item.getImage());
        values.put(COLUMN_ADDITION,item.getAddition());
        values.put(COLUMN_FINAL_PRICE,item.getFinal_price());

        // updating row
        db.update(TABLE_NAME, values, COLUMN_STOCK_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public void updateQuantity(int quantity,String code,int id_user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quantity);

        // updating row
        db.update(TABLE_NAME, values, COLUMN_COD + " = ?"+" and "+COLUMN_USER_ID + " = ?",
                new String[]{code,String.valueOf(id_user)});
        db.close();
    }


    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_NAME, COLUMN_STOCK_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getProduct(String cod,int id_user){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_COD+"="+cod+" and "+COLUMN_USER_ID+"="+id_user,null);
    }

    public Cursor getProducts(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_STOCK_ID+"="+id,null);
    }

    public void deleteStocks(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getProductID(String cod){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_COD+"="+cod,null);
    }

}
