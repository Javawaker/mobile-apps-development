package com.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DB_NAME = "Teachers.db";
    private static final int DB_VERSION = 1;
    private static final String TBL_NAME = "tblTeachers";
    private static final String C_ID = "id";
    private static final String C_NAME = "name";
    private static final String C_JOB_TITLE = "jobTitle";
    private static final String C_DEGREE = "degree";
    private static final String C_RANK = "rank";
    private static final String C_PHOTO = "photo";
    public DBHelper(@Nullable Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //создание новой таблицы в БД
        String Query =  "CREATE TABLE " + TBL_NAME + "( " +
                C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_NAME + " TEXT, " +
                C_JOB_TITLE + " TEXT, " +
                C_DEGREE + " TEXT, " +
                C_RANK + " TEXT, " +
                C_PHOTO + " TEXT)";
        db.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //удаление таблицы в БД, если она есть
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME + ";");
        onCreate(db);
    }

    void addTeacher(String name, String job_title, String degree, String rank, String photo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_NAME, name);
        cv.put(C_JOB_TITLE, job_title);
        cv.put(C_DEGREE, degree);
        cv.put(C_RANK, rank);
        cv.put(C_PHOTO, photo);
        long res = db.insert(TBL_NAME, null,cv);
        if(res == -1){
            Toast.makeText(context, R.string.error_add, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.success_add, Toast.LENGTH_SHORT).show();
        }
    }

    Cursor loadData(){
        String Query = "SELECT " + C_ID + "," + C_NAME + "," + C_JOB_TITLE + "," + C_DEGREE + "," + C_RANK + "," + C_PHOTO +
                        " FROM " + TBL_NAME +
                        " ORDER BY " + C_RANK + ", " + C_NAME + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(Query,null);
        }
        return cursor;
    }

    void editData(String id, String name, String job_title, String rank, String degree, String photo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_ID, id);
        cv.put(C_NAME, name);
        cv.put(C_JOB_TITLE, job_title);
        cv.put(C_DEGREE, degree);
        cv.put(C_RANK, rank);
        cv.put(C_PHOTO, photo);
        long result = db.update(TBL_NAME, cv, C_ID+"=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, R.string.error_edit, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.success_edit, Toast.LENGTH_SHORT).show();
        }
    }

    void delOneTeacher(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_NAME, C_ID+"=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, R.string.error_delete, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.success_delete, Toast.LENGTH_SHORT).show();
        }
    }

    void delAllTeachers(){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_NAME,"",new String[]{});
        if(result == -1){
            Toast.makeText(context, R.string.error_delete, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.success_delete_all, Toast.LENGTH_SHORT).show();
        }
    }
}