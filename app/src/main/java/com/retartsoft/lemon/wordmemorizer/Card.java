package com.retartsoft.lemon.wordmemorizer;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class Card {

    private UUID mId;
    private String mTitle;
    private ArrayList<Word> mWords;
    private long createDate;
    private long editDate;
    private String comment;

    public Card() {
        mWords = new ArrayList<Word>();
        mId = UUID.randomUUID();
        createDate = 0;
        editDate = 0;
        comment = "";
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ArrayList<Word> getWords(Context context) {
        updateWords(context);
        return mWords;
    }

    private void updateWords(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.mWords = dbHelper.queryWords(this.getId());
    }

    public void setWords(ArrayList<Word> words) {
        mWords = words;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long date) {
        createDate = date;
    }

    public void setEditDate(long date) { editDate = date; }

    public long getEditDate() { return editDate; }

    public void setComment(String s) { comment = s; }

    public String getComment() { return comment; }

    public void deleteWord(Word w) { mWords.remove(w); }

    public void addWord(Word w) { mWords.add(w); }

    public Word getWord(UUID wordId) {
        for (Word w : mWords) {
            if (w.getId().equals(wordId))
                return w;
        }
        return null;
    }
}
