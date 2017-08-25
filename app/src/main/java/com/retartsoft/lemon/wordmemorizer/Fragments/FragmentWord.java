package com.retartsoft.lemon.wordmemorizer.Fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.retartsoft.lemon.wordmemorizer.Card;
import com.retartsoft.lemon.wordmemorizer.CardLab;
import com.retartsoft.lemon.wordmemorizer.DBHelper;
import com.retartsoft.lemon.wordmemorizer.R;
import com.retartsoft.lemon.wordmemorizer.Word;

import java.util.UUID;

/**
 * Created by Lemon113 on 18.08.2017.
 */

public class FragmentWord extends Fragment {
    public static final String EXTRA_WORD_ID = "EXTRA_WORD_ID";
    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";
    public static final String EXTRA_EXIST_WORD = "EXTRA_EXIST_CARD";
    private boolean existWord = false;

    private EditText engWord;
    private EditText rusWord;

    private Card mCard;
    private Word mWord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            existWord = savedInstanceState.getBoolean(EXTRA_EXIST_WORD);
        } else {
            existWord = getActivity().getIntent().getBooleanExtra(EXTRA_EXIST_WORD, false);
        }

        UUID cardId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CARD_ID);
        UUID wordId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_WORD_ID);

        mCard = CardLab.get(getActivity()).getCard(cardId);
        mWord = mCard.getWord(wordId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_word, parent, false);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        engWord = (EditText)v.findViewById(R.id.et_eng);
        rusWord = (EditText)v.findViewById(R.id.et_rus);

        engWord.setText(mWord.getEng());
        rusWord.setText(mWord.getRus());
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveWord();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_EXIST_WORD, existWord);
    }

    private void saveWord() {
        DBHelper dbHelper = new DBHelper(getActivity());
        if (!existWord) {
            mWord.setEng(engWord.getText().toString());
            mWord.setRus(rusWord.getText().toString());
            dbHelper.insertWord(mWord, mCard.getId());
        } else {
            dbHelper.updateCard(mCard);
        }
    }
}
