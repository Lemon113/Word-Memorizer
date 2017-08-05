package com.retartsoft.lemon.wordmemorizer.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.retartsoft.lemon.wordmemorizer.Card;
import com.retartsoft.lemon.wordmemorizer.CardLab;
import com.retartsoft.lemon.wordmemorizer.R;
import com.retartsoft.lemon.wordmemorizer.Word;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class FragmentCard extends ListFragment {

    private Card mCard;
    private EditText mTitleField;
    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";

    private ArrayList<Word> mWords;

    private class WordAdapter extends ArrayAdapter<Word> {

        public WordAdapter(ArrayList<Word> words) {
            super(getActivity(), 0, words);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_word, null);
            }
            Word w = getItem(position);

            TextView tvEng = (TextView)convertView.findViewById(R.id.engWord);
            TextView tvRus = (TextView)convertView.findViewById(R.id.rusWord);

            tvEng.setText(w.getEng());
            tvRus.setText(w.getRus());
            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID cardId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CARD_ID);
        mCard = CardLab.get(getActivity()).getCard(cardId);
        mWords = mCard.getWords();

        ArrayAdapter<Word> adapter = new WordAdapter(mWords);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, parent, false);
        mTitleField = (EditText)v.findViewById(R.id.card_title);
        mTitleField.setText(mCard.getTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCard.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
}
