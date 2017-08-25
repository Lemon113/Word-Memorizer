package com.retartsoft.lemon.wordmemorizer.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    private static final int DIVIDER_HEIGHT = 100;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_card_list, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CardAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setDividerHeight(DIVIDER_HEIGHT);
        getListView().setEmptyView(getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_cardlist, null));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Card c = ((CardAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), FragmentActivityCard.class);
        i.putExtra(FragmentCard.EXTRA_CARD_ID, c.getId());
        i.putExtra(FragmentCard.EXTRA_EXIST_CARD, true);
        startActivity(i);
    }

    public void edit(View v) {
        Log.i("test", "edit clicked");
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
            if (c.getWords(getActivity()) != null) {
                counter.setText(Integer.toString(c.getWords(getActivity()).size()));
            }
            else {
                counter.setText("0");
            }

            return convertView;
        }
    }

    private ArrayList<Card> mCards;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cardlist, parent, false);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            registerForContextMenu(listView);
        else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.card_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_card:
                            CardAdapter adapter = (CardAdapter)getListAdapter();
                            CardLab cardLab = CardLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; --i) {
                                if (getListView().isItemChecked(i)) {
                                    cardLab.deleteCard(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);
        mCards = new ArrayList<Card>();
        mCards = CardLab.get(getActivity()).getCards();

        ArrayAdapter<Card> adapter = new CardAdapter(mCards);
        setListAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_new_crime:
                Card c = new Card();
                CardLab.get(getActivity()).addCard(c);
                Intent i = new Intent(getActivity(), FragmentActivityCard.class);
                i.putExtra(FragmentCard.EXTRA_CARD_ID, c.getId());
                startActivity(i, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.card_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CardAdapter adapter = (CardAdapter)getListAdapter();
        Card card = adapter.getItem(position);

        switch(item.getItemId()) {
            case R.id.menu_item_delete_card:
                CardLab.get(getActivity()).deleteCard(card);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
