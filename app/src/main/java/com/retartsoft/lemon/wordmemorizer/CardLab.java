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
        updateCards();
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
        mCards.add(c);
    }

    public void deleteCard(Card c) {
        DBHelper dbHelper = new DBHelper(mAppContext);
        dbHelper.removeCard(c);
        mCards.remove(c);
    }

    private void updateCards() {
        DBHelper dbHelper = new DBHelper(this.mAppContext);
        this.mCards = dbHelper.queryCards();
    }
}
