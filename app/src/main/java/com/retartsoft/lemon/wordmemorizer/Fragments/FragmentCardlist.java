package com.retartsoft.lemon.wordmemorizer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.retartsoft.lemon.wordmemorizer.Card;
import com.retartsoft.lemon.wordmemorizer.CardLab;
import com.retartsoft.lemon.wordmemorizer.FragmentActivities.FragmentActivityCard;
import com.retartsoft.lemon.wordmemorizer.R;

import java.util.ArrayList;

/**
 * Created by Lemon on 05.08.2017.
 */

public class FragmentCardlist extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setDividerHeight(100);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Card c = ((CardAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), FragmentActivityCard.class);
        i.putExtra(FragmentCard.EXTRA_CARD_ID, c.getId());
        startActivity(i);
    }

    private class CardAdapter extends ArrayAdapter<Card> {

        public CardAdapter(ArrayList<Card> cards) {
            super(getActivity(), 0, cards);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_card, null);
            }
            Card c = getItem(position);
            TextView tvCardTitle = (TextView)convertView.findViewById(R.id.title);
            TextView word1 = (TextView)convertView.findViewById(R.id.word1);
            TextView word2 = (TextView)convertView.findViewById(R.id.word2);
            TextView word3 = (TextView)convertView.findViewById(R.id.word3);
            TextView counter = (TextView)convertView.findViewById(R.id.wordCount);

            tvCardTitle.setText(c.getTitle());
            word1.setText("ENG WORD1");
            word2.setText("ENG WORD2");
            word3.setText("ENG WORD3");
            counter.setText(Integer.toString(c.getWords().size()));
            return convertView;
        }
    }

    private ArrayList<Card> mCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        mCards = CardLab.get(getActivity()).getCards();

        ArrayAdapter<Card> adapter = new CardAdapter(mCards);
        setListAdapter(adapter);
    }



}
