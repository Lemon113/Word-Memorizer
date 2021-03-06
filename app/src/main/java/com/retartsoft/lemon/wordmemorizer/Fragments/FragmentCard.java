package com.retartsoft.lemon.wordmemorizer.Fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.retartsoft.lemon.wordmemorizer.Card;
import com.retartsoft.lemon.wordmemorizer.CardLab;
import com.retartsoft.lemon.wordmemorizer.DBHelper;
import com.retartsoft.lemon.wordmemorizer.FragmentActivities.FragmentActivityWord;
import com.retartsoft.lemon.wordmemorizer.R;
import com.retartsoft.lemon.wordmemorizer.Word;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lemon on 05.08.2017.
 */

public class FragmentCard extends ListFragment {

    private static final String TAG = "FragmentCard";

    private Card mCard;
    private EditText mTitleField;
    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";
    public static final String EXTRA_EXIST_CARD = "EXTRA_EXIST_CARD";
    private boolean existCard = false;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_word_list, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            existCard = savedInstanceState.getBoolean(EXTRA_EXIST_CARD);
        } else {
            existCard = getActivity().getIntent().getBooleanExtra(EXTRA_EXIST_CARD, false);
        }

        UUID cardId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CARD_ID);
        mCard = CardLab.get(getActivity()).getCard(cardId);
        mWords = mCard.getWords(getActivity());

        ArrayAdapter<Word> adapter = new WordAdapter(mWords);
        setListAdapter(adapter);
        if (mCard.getTitle() == null)
            getActivity().setTitle("New card ");
        else
            getActivity().setTitle("Card " + mCard.getTitle());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, parent, false);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.word_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_word:
                            WordAdapter adapter = (WordAdapter)getListAdapter();
                            for (int i = adapter.getCount() - 1; i >= 0; --i) {
                                if (getListView().isItemChecked(i)) {
                                    mCard.deleteWord(adapter.getItem(i));
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
        } else {
            registerForContextMenu(listView);
        }
        mTitleField = (EditText)v.findViewById(R.id.card_title);
        mTitleField.setText(mCard.getTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCard.setTitle(s.toString());
                getActivity().setTitle("Card " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Word w = ((WordAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), FragmentActivityWord.class);
        i.putExtra(FragmentWord.EXTRA_CARD_ID, mCard.getId());
        i.putExtra(FragmentWord.EXTRA_WORD_ID, w.getId());
        i.putExtra(FragmentWord.EXTRA_EXIST_WORD, true);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_new_word:
                Word w = new Word();
                mCard.addWord(w);
                Intent i = new Intent(getActivity(), FragmentActivityWord.class);
                i.putExtra(FragmentWord.EXTRA_CARD_ID, mCard.getId());
                i.putExtra(FragmentWord.EXTRA_WORD_ID, w.getId());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveCard();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_EXIST_CARD, existCard);
    }

    private void saveCard() {
        DBHelper dbHelper = new DBHelper(getActivity());
        if (!existCard) {
            long d = System.currentTimeMillis() / 1000;
            mCard.setCreateDate(d);
            mCard.setEditDate(d);
            dbHelper.insertCard(mCard);
        } else {
            dbHelper.updateCard(mCard);
        }
    }

}
