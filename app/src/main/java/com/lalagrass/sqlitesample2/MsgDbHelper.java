package com.lalagrass.sqlitesample2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ASUS on 9/8/2015.
 */
public class MsgDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_OVERVIEW_ENTRIES =
            "CREATE TABLE " + MsgOverviewEntry.TABLE_NAME + " (" +
                    MsgOverviewEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    MsgOverviewEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    MsgOverviewEntry.COLUMN_NAME_MSG + TEXT_TYPE + COMMA_SEP +
                    MsgOverviewEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                    MsgOverviewEntry.COLUMN_NAME_ISSENDER + INTEGER_TYPE +
                    " )";
    private static final String SQL_CREATE_DETAIL_ENTRIES =
            "CREATE TABLE " + MsgDetailEntry.TABLE_NAME + " (" +
                    MsgDetailEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    MsgDetailEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    MsgDetailEntry.COLUMN_NAME_MSG + TEXT_TYPE + COMMA_SEP +
                    MsgDetailEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                    MsgDetailEntry.COLUMN_NAME_ISSENDER + INTEGER_TYPE +
                    " )";
    private static final String SQL_DELETE_OVERVIEW_ENTRIES =
            "DROP TABLE IF EXISTS " + MsgOverviewEntry.TABLE_NAME;
    private static final String SQL_DELETE_DETAIL_ENTRIES =
            "DROP TABLE IF EXISTS " + MsgDetailEntry.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class MsgOverviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "overview";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MSG = "msg";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ISSENDER = "isSender";
    }

    public static abstract class MsgDetailEntry implements BaseColumns {
        public static final String TABLE_NAME = "detail";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MSG = "msg";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ISSENDER = "isSender";
    }

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "msg.db";

    public MsgDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_OVERVIEW_ENTRIES);
        db.execSQL(SQL_CREATE_DETAIL_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_OVERVIEW_ENTRIES);
        db.execSQL(SQL_DELETE_DETAIL_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<MsgData> listDetail(String name) {
        List<MsgData> tags = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MsgDetailEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        String filter = MsgDetailEntry.COLUMN_NAME_NAME + "='" + name +"'";
        String orderBy =  MsgDetailEntry.COLUMN_NAME_DATE + " ASC";
        Cursor c = db.query(
                MsgDetailEntry.TABLE_NAME,
                new String[]{
                        MsgDetailEntry.COLUMN_NAME_ID,
                        MsgDetailEntry.COLUMN_NAME_NAME,
                        MsgDetailEntry.COLUMN_NAME_MSG,
                        MsgDetailEntry.COLUMN_NAME_DATE,
                        MsgDetailEntry.COLUMN_NAME_ISSENDER},
                filter,
                null,
                null,
                null,
                orderBy);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MsgData t = new MsgData();
                t.Id = c.getLong(c.getColumnIndex(MsgDetailEntry.COLUMN_NAME_ID));
                t.Name = c.getString(c.getColumnIndex(MsgDetailEntry.COLUMN_NAME_NAME));
                t.Msg = c.getString(c.getColumnIndex(MsgDetailEntry.COLUMN_NAME_MSG));
                t.Date = c.getLong(c.getColumnIndex(MsgDetailEntry.COLUMN_NAME_DATE));
                t.IsSender = c.getInt(c.getColumnIndex(MsgDetailEntry.COLUMN_NAME_ISSENDER));
                tags.add(t);
            } while (c.moveToNext());
        }
        db.close();
        return tags;
    }

    public List<MsgData> listOverview() {
        List<MsgData> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String orderBy =  MsgOverviewEntry.COLUMN_NAME_DATE + " DESC";
        Cursor c = db.query(
                MsgOverviewEntry.TABLE_NAME,
                new String[]{
                        MsgOverviewEntry.COLUMN_NAME_ID,
                        MsgOverviewEntry.COLUMN_NAME_NAME,
                        MsgOverviewEntry.COLUMN_NAME_MSG,
                        MsgOverviewEntry.COLUMN_NAME_DATE,
                        MsgOverviewEntry.COLUMN_NAME_ISSENDER},
                null,
                null,
                null,
                null,
                orderBy);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MsgData t = new MsgData();
                t.Id = c.getLong(c.getColumnIndex(MsgOverviewEntry.COLUMN_NAME_ID));
                t.Name = c.getString(c.getColumnIndex(MsgOverviewEntry.COLUMN_NAME_NAME));
                t.Msg = c.getString(c.getColumnIndex(MsgOverviewEntry.COLUMN_NAME_MSG));
                t.Date = c.getLong(c.getColumnIndex(MsgOverviewEntry.COLUMN_NAME_DATE));
                t.IsSender = c.getInt(c.getColumnIndex(MsgOverviewEntry.COLUMN_NAME_ISSENDER));
                tags.add(t);
            } while (c.moveToNext());
        }
        db.close();
        return tags;
    }

    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            //deleteOverview(name, db);
            deleteDetail(name, db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private void deleteOverview(String name, SQLiteDatabase db) {
        db.delete(MsgOverviewEntry.TABLE_NAME, MsgOverviewEntry.COLUMN_NAME_NAME + "='" +name + "'", null);
    }

    private void deleteDetail(String name, SQLiteDatabase db) {
        db.delete(MsgDetailEntry.TABLE_NAME, MsgDetailEntry.COLUMN_NAME_NAME + "='" +name + "'", null);
    }

    public void insert(MsgData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            insertOverview(data, db);
            insertDetail(data, db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private long insertOverview(MsgData data, SQLiteDatabase db) {
        long newRowId;
        ContentValues values = new ContentValues();
        values.put(MsgOverviewEntry.COLUMN_NAME_NAME, data.Name);
        values.put(MsgOverviewEntry.COLUMN_NAME_MSG, data.Msg);
        values.put(MsgOverviewEntry.COLUMN_NAME_DATE, data.Date);
        values.put(MsgOverviewEntry.COLUMN_NAME_ISSENDER, data.IsSender);

        String selectQuery = "SELECT  * FROM " + MsgOverviewEntry.TABLE_NAME + " WHERE "
                + MsgOverviewEntry.COLUMN_NAME_NAME + " = '" + data.Name + "'";
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            newRowId = db.update(
                    MsgOverviewEntry.TABLE_NAME,
                    values,
                    MsgOverviewEntry.COLUMN_NAME_NAME + " = ?",
                    new String[]{data.Name});
        } else {
            newRowId = db.insert(
                    MsgOverviewEntry.TABLE_NAME,
                    null,
                    values);
        }
        return newRowId;
    }

    private long insertDetail(MsgData data, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(MsgDetailEntry.COLUMN_NAME_NAME, data.Name);
        values.put(MsgDetailEntry.COLUMN_NAME_MSG, data.Msg);
        values.put(MsgDetailEntry.COLUMN_NAME_DATE, data.Date);
        values.put(MsgDetailEntry.COLUMN_NAME_ISSENDER, data.IsSender);
        long newRowId;
        newRowId = db.insert(
                MsgDetailEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public void InitDefaultDb() {
        if (listOverview().size() == 0) {
            MsgData user1 = new MsgData();
            user1.Name = "user1";
            user1.Msg = "How are you";
            user1.IsSender = 0;
            user1.Date = new Date().getTime();
            insert(user1);
            user1.Date++;
            user1.IsSender = 1;
            user1.Msg = "Fine, and you?";
            insert(user1);
            user1.Date++;
            user1.IsSender = 0;
            user1.Msg = "Fine, thank you.";
            insert(user1);
        }
    }
}
