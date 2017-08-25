package com.retartsoft.lemon.wordmemorizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 06.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_WORDS                  = "words";
    private static final String COLUMN_ENG_WORD              = "eng_word";
    private static final String COLUMN_RUS_WORD              = "rus_word";
    private static final String FOREIGN_COLUMN_CARD_ID       = "card_id";
    // also contains id, successful, failed_attempts, comment

    private static final String TABLE_CARDS                  = "cards";
    private static final String COLUMN_ID                    = "id";
    private static final String COLUMN_CREATE_DATE           = "create_date";
    private static final String COLUMN_EDIT_DATE             = "edit_date";
    private static final String COLUMN_CARDNAME              = "cardname";
    private static final String COLUMN_SUCCESSFUL_ATTEMPTS   = "successful_attempts";
    private static final String COLUMN_FAILED_ATTEMPTS       = "failed_attempts";
    private static final String COLUMN_COMMENT               = "comment";

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
            c.setEditDate(getLong(getColumnIndex(COLUMN_EDIT_DATE)));
            c.setTitle(getString(getColumnIndex(COLUMN_CARDNAME)));
            c.setId(UUID.fromString(getString(getColumnIndex(COLUMN_ID))));
            c.setComment(getString(getColumnIndex(COLUMN_COMMENT)));
            super.moveToNext();
            return c;
        }
    }

    public static class WordCursor extends CursorWrapper {

        public WordCursor(Cursor c) {
            super(c);
        }

        public Word getWord() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Word w = new Word();
            w.setId(UUID.fromString(getString(getColumnIndex(COLUMN_ID))));
            w.setComment(getString(getColumnIndex(COLUMN_COMMENT)));
            w.setEng(getString(getColumnIndex(COLUMN_ENG_WORD)));
            w.setRus(getString(getColumnIndex(COLUMN_RUS_WORD)));
            super.moveToNext();
            return w;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cards ( " +
                " id VARCHAR(36) PRIMARY KEY," +
                " create_date INTEGER," +
                " edit_date INTEGER," +
                " cardname VARCHAR(100), " +
                " comment TEXT," +
                " successful_attempts INTEGER," +
                " failed_attempts INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE words ( " +
                " id VARCHAR(36) PRIMARY KEY, " +
                " eng_word VARCHAR(100)," +
                " rus_word VARCHAR(100)," +
                " comment TEXT," +
                " successful_attempts INTEGER," +
                " failed_attempts INTEGER," +
                " card_id INTEGER, " +
                " FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertCard(Card c) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, c.getId().toString());
        cv.put(COLUMN_CARDNAME, c.getTitle());
        cv.put(COLUMN_CREATE_DATE, c.getCreateDate());
        cv.put(COLUMN_EDIT_DATE, c.getEditDate());
        cv.put(COLUMN_SUCCESSFUL_ATTEMPTS, 0);
        cv.put(COLUMN_FAILED_ATTEMPTS, 0);
        getWritableDatabase().insert(TABLE_CARDS, null, cv);
    }

    public void updateCard(Card mCard) {
        //TODO: implement method
    }

    public void removeCard(Card card) {
        String where = COLUMN_ID + "=" + "'" + card.getId().toString() + "'";
        getWritableDatabase().delete(TABLE_CARDS, where, null);
    }

    public ArrayList<Card> queryCards() {
        String sql = "SELECT * FROM cards";
        Cursor c = getReadableDatabase().rawQuery(sql, null);   //.query(TABLE_CARDS, null, null, null, null, null, COLUMN_CREATE_DATE + " asc");
        c.moveToFirst();
        CardCursor cc = new CardCursor(c);
        ArrayList<Card> cardList = new ArrayList<Card>();
        Card card = cc.getCard();
        while (card != null) {
            cardList.add(card);
            card = cc.getCard();
        }
        cc.close();
        c.close();
        return cardList;
    }

    //TODO: add column comment
    public void insertWord(Word w, UUID cardId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, w.getId().toString());
        cv.put(COLUMN_ENG_WORD, w.getEng());
        cv.put(COLUMN_RUS_WORD, w.getRus());
        cv.put(COLUMN_SUCCESSFUL_ATTEMPTS, 0);
        cv.put(COLUMN_FAILED_ATTEMPTS, 0);
        cv.put(COLUMN_COMMENT, "");
        cv.put(FOREIGN_COLUMN_CARD_ID, cardId.toString());
        getWritableDatabase().insert(TABLE_WORDS, null, cv);
    }

    public void removeWord(Word w) {
        String where = COLUMN_ID + "=" + "'" + w.getId().toString() + "'";
        getWritableDatabase().delete(TABLE_WORDS, where, null);
    }

    public ArrayList<Word> queryWords(UUID cardId) {
        String sql = "SELECT * FROM words WHERE card_id = '" + cardId.toString() + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        WordCursor wc = new WordCursor(c);
        ArrayList<Word> wordList = new ArrayList<Word>();
        Word word = wc.getWord();
        while (word != null) {
            wordList.add(word);
            word = wc.getWord();
        }
        wc.close();
        c.close();
        return wordList;
    }
}
