package com.example.mystorebusiness.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mystorebusiness.account.ui.employees.Employee;
import java.util.ArrayList;

public class Db_Employee extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DataBaseEmployee.db";

    // User table name
    private static final String TABLE_EMPLOYEE = "tb_employees";



    // User Table Columns names
    private static final String COLUMN_EMPLOYEE_ID = "employee_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_EMPLOYEE_SALARY = "employee_salary";
    private static final String COLUMN_EMPLOYEE_EMAIL = "employee_email";
    private static final String COLUMN_EMPLOYEE_CNP = "employee_cnp";
    private static final String COLUMN_EMPLOYEE_PHONE = "employee_phone";
    private static final String COLUMN_EMPLOYEE_NAME = "employee_name";
    private static final String COLUMN_EMPLOYEE_BIRTH = "employee_birth";
    private static final String COLUMN_EMPLOYEE_ADDRESS = "employee_address";
    private static final String COLUMN_EMPLOYEE_SERIES = "employee_series";
    private static final String COLUMN_EMPLOYEE_IMAGE = "employee_image";
    private static final String COLUMN_EMPLOYEE_DESCRIPTION = "employee_description";


    public Db_Employee(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table sql query
        String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
                + COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_EMPLOYEE_NAME + " TEXT," + COLUMN_EMPLOYEE_ADDRESS + " TEXT, " + COLUMN_EMPLOYEE_PHONE + " TEXT," + COLUMN_EMPLOYEE_EMAIL + " TEXT," + COLUMN_EMPLOYEE_SALARY + " TEXT," + COLUMN_EMPLOYEE_CNP + " TEXT," + COLUMN_EMPLOYEE_SERIES + " TEXT," + COLUMN_EMPLOYEE_BIRTH + " TEXT," + COLUMN_EMPLOYEE_IMAGE + " TEXT," + COLUMN_EMPLOYEE_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_EMPLOYEE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        // drop table sql query
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_EMPLOYEE;
        db.execSQL(DROP_USER_TABLE);


        // Create tables again
        onCreate(db);

    }


    public String addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, employee.getId_user());
        values.put(COLUMN_EMPLOYEE_NAME, employee.getName());
        values.put(COLUMN_EMPLOYEE_ADDRESS, employee.getAddress());
        values.put(COLUMN_EMPLOYEE_PHONE, employee.getPhone());
        values.put(COLUMN_EMPLOYEE_EMAIL, employee.getMail());
        values.put(COLUMN_EMPLOYEE_SALARY, employee.getSalary());
        values.put(COLUMN_EMPLOYEE_CNP,employee.getCNP());
        values.put(COLUMN_EMPLOYEE_SERIES,employee.getSeries());
        values.put(COLUMN_EMPLOYEE_BIRTH,employee.getBirth());
        values.put(COLUMN_EMPLOYEE_IMAGE,employee.getImage());
        values.put(COLUMN_EMPLOYEE_DESCRIPTION,employee.getDescription());

        // Inserting Row
        long rez=db.insert(TABLE_EMPLOYEE, null, values);
        if(rez==-1) {
            db.close();
            return "Failed";
        }
        else {
            db.close();
            return "Successfully";
        }


    }


    public ArrayList<Employee> getAllEmployees(String sort,int id_user) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_EMPLOYEE_ID,
                COLUMN_USER_ID,
                COLUMN_EMPLOYEE_NAME,
                COLUMN_EMPLOYEE_ADDRESS,
                COLUMN_EMPLOYEE_PHONE,
                COLUMN_EMPLOYEE_EMAIL,
                COLUMN_EMPLOYEE_SALARY,
                COLUMN_EMPLOYEE_CNP,
                COLUMN_EMPLOYEE_SERIES,
                COLUMN_EMPLOYEE_BIRTH,
                COLUMN_EMPLOYEE_IMAGE,
                COLUMN_EMPLOYEE_DESCRIPTION
        };

        String sortOrder;
        switch (sort) {
            case "name":
                sortOrder = COLUMN_EMPLOYEE_NAME + " ASC";break;
            case "salary":
                sortOrder = COLUMN_EMPLOYEE_SALARY + " ASC";break;

            default:
                sortOrder = COLUMN_EMPLOYEE_ID + " ASC";break;
        }



        ArrayList<Employee> employeesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the stocks table

        Cursor cursor = db.query(TABLE_EMPLOYEE, //Table to query
                columns,    //columns to return
                COLUMN_USER_ID + " = ?",        //columns for the WHERE clause
                new String[]{String.valueOf(id_user)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Employee item= new Employee();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_ID))));
                item.setId_user(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_NAME)));
                item.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_ADDRESS)));
                item.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_PHONE)));
                item.setMail(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_EMAIL)));
                item.setSalary(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_SALARY))));
                item.setCNP(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_CNP)));
                item.setSeries(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_SERIES)));
                item.setBirth(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_BIRTH)));
                item.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_IMAGE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_DESCRIPTION)));
                // Adding employees record to list
                employeesList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return employees list
        return employeesList;
    }

    public void updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMPLOYEE_NAME, employee.getName());
        values.put(COLUMN_EMPLOYEE_ADDRESS, employee.getAddress());
        values.put(COLUMN_EMPLOYEE_PHONE, employee.getPhone());
        values.put(COLUMN_EMPLOYEE_EMAIL, employee.getMail());
        values.put(COLUMN_EMPLOYEE_SALARY, employee.getSalary());
        values.put(COLUMN_EMPLOYEE_CNP,employee.getCNP());
        values.put(COLUMN_EMPLOYEE_SERIES,employee.getSeries());
        values.put(COLUMN_EMPLOYEE_BIRTH,employee.getBirth());
        values.put(COLUMN_EMPLOYEE_IMAGE,employee.getImage());
        values.put(COLUMN_EMPLOYEE_DESCRIPTION,employee.getDescription());

        // updating row
        db.update(TABLE_EMPLOYEE, values, COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(employee.getId())});
        db.close();
    }


    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_EMPLOYEE, COLUMN_EMPLOYEE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }




    public Cursor getEmployeeID(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select "+COLUMN_EMPLOYEE_ID+" from "+TABLE_EMPLOYEE+" where "+COLUMN_EMPLOYEE_NAME+"="+"'"+name+"'",null);
    }

    public Cursor getEmployee(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_EMPLOYEE+" where "+COLUMN_EMPLOYEE_ID+"="+id,null);
    }


    public void deleteEmployees(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


}