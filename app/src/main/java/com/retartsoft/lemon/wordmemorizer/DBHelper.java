package com.retartsoft.lemon.wordmemorizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lemon on 06.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_CARDS = "cards";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_CARDNAME = "cardname";
    private static final String COLUMN_SUCCESSFUL_ATTEMPTS = "successful_attempts";
    private static final String COLUMN_FAILED_ATTEMPTS = "failed_attempts";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE cards ( " +
                        " id INTEGER PRIMARY AUTOINCREMENT," +
                        " create_date INTEGER," +
                        " cardname VARCHAR(100)," +
                        " successful_attempts INTEGER DEFAULT 0," +
                        " failed_attempts INTEGER DEFAULT 0 )"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertCard(Card c) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARDNAME, c.getTitle());
        cv.put(COLUMN_CREATE_DATE, c.getCreateDate());
        return getWritableDatabase().insert(TABLE_CARDS, null, cv);
    }
}
