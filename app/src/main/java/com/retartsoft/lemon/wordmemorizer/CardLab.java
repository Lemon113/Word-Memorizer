package com.retartsoft.lemon.wordmemorizer;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class CardLab {

    private ArrayList<Card> mCards;

    private static CardLab sCardLab;
    private Context mAppContext;

    private CardLab(Context appContext) {
        mAppContext = appContext;
        mCards = new ArrayList<Card>();
    }

    public static CardLab get(Context c) {
        if (sCardLab == null)
            sCardLab = new CardLab(c.getApplicationContext());
        return sCardLab;
    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    public Card getCard(UUID cardId) {
        for (Card c : mCards) {
            if (c.getId().equals(cardId))
                return c;
        }
        return null;
    }

    public void addCard(Card c) {
        ArrayList<Word> words = new ArrayList<Word>();
        for (int j = 0; j < 30; ++j) {
            Word w = new Word();
            w.setEng("English word #" + j);
            w.setRus("Русский слово #" + j);
            words.add(w);
        }
        c.setWords(words);
        mCards.add(c);
    }

    public void deleteCard(Card c) {
        mCards.remove(c);
    }

    public void updateCards() {
        DBHelper dbHelper = new DBHelper(this.mAppContext);
        this.mCards = dbHelper.queryCards();
    }
}
