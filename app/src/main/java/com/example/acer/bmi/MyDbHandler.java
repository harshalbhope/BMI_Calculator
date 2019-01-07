package com.example.acer.bmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ACER on 15-12-2018.
 */

public class MyDbHandler extends SQLiteOpenHelper {


    Context context;
    SQLiteDatabase db;

    MyDbHandler(Context context){
        super(context, "bmidb", null, 1);
        this.context = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table BD (id Integer primary key,bmi decimal(4,2), name TEXT, date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql);
        Log.d("DB456","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addRecord(String bmi, String name){

        ContentValues cv = new ContentValues();

        cv.put("bmi", Float.parseFloat(bmi));
        cv.put("name", name);

        long rid = db.insert("BD", null, cv);

        if (rid < 0){
            Toast.makeText(context, "This name already exists", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Record has been Added", Toast.LENGTH_SHORT).show();

        }
    }

    public String viewRecord(){
        StringBuffer sb = new StringBuffer();
        Cursor c = db.query("BD",null, null, null, null, null, null);


        if(c.getCount() > 0){
            c.moveToFirst();
            do {
               String bmi = c.getString(c.getColumnIndex("bmi"));
                String date = c.getString(c.getColumnIndex("date"));
                String name = c.getString(c.getColumnIndex("name"));
                sb.append("name: "+name+" bmi: "+bmi+ " date: "+date+ "\n");

            }while (c.moveToNext());
        }
        return sb.toString();
    }

}
