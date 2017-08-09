package com.retartsoft.lemon.wordmemorizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 06.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_CARDS                  = "cards";
    private static final String COLUMN_CARD_ID               = "id";
    private static final String COLUMN_CREATE_DATE           = "create_date";
    private static final String COLUMN_CARDNAME              = "cardname";
    private static final String COLUMN_SUCCESSFUL_ATTEMPTS   = "successful_attempts";
    private static final String COLUMN_FAILED_ATTEMPTS       = "failed_attempts";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static class CardCursor extends CursorWrapper {

        public CardCursor(Cursor c) {
            super(c);
        }

        public Card getCard() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Card c = new Card();
            c.setCreateDate(getLong(getColumnIndex(COLUMN_CREATE_DATE)));
            c.setTitle(getString(getColumnIndex(COLUMN_CARDNAME)));
            c.setId(UUID.fromString(getString(getColumnIndex(COLUMN_CARD_ID))));
            super.moveToNext();
            return c;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cards ( " +
                " id VARCHAR(36) PRIMARY KEY," +
                " create_date INTEGER," +
                " cardname VARCHAR(100)," +
                " successful_attempts INTEGER," +
                " failed_attempts INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertCard(Card c) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CARD_ID, c.getId().toString());
        cv.put(COLUMN_CARDNAME, c.getTitle());
        cv.put(COLUMN_CREATE_DATE, c.getCreateDate());
        cv.put(COLUMN_SUCCESSFUL_ATTEMPTS, 0);
        cv.put(COLUMN_FAILED_ATTEMPTS, 0);
        getWritableDatabase().insert(TABLE_CARDS, null, cv);
    }

    public void updateCard(Card mCard) {
        //TODO: implement method
    }

    public void removeCard(Card card) {
        String where = COLUMN_CARD_ID + "=" + "'" + card.getId().toString() + "'";
        getWritableDatabase().delete(TABLE_CARDS, where, null);
    }

    public ArrayList<Card> queryCards() {
        String sql = "select * from cards";
        Cursor c = getReadableDatabase().rawQuery(sql, null);   //.query(TABLE_CARDS, null, null, null, null, null, COLUMN_CREATE_DATE + " asc");
        c.moveToFirst();
        CardCursor cc = new CardCursor(c);
        ArrayList<Card> cardList = new ArrayList<Card>();
        Card card = cc.getCard();
        while (card != null) {
            ArrayList<Word> words = new ArrayList<Word>();
            for (int j = 0; j < 30; ++j) {
                Word w = new Word();
                w.setEng("English word #" + j);
                w.setRus("Русский слово #" + j);
                words.add(w);
            }
            card.setWords(words);
            cardList.add(card);
            card = cc.getCard();
        }
        cc.close();
        c.close();
        return cardList;
    }
}
