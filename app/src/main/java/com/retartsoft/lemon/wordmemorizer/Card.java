package com.retartsoft.lemon.wordmemorizer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class Card {

    private UUID mId;
    private String mTitle;
    private ArrayList<Word> mWords;

    public Card() {
        mId = UUID.randomUUID();
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

    public ArrayList<Word> getWords() {
        return mWords;
    }

    public void setWords(ArrayList<Word> words) {
        mWords = words;
    }
}
