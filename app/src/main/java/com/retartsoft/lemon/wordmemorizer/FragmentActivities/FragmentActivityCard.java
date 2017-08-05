package com.retartsoft.lemon.wordmemorizer.FragmentActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.retartsoft.lemon.wordmemorizer.Fragments.FragmentCard;

/**
 * Created by Lemon on 05.08.2017.
 */

public class FragmentActivityCard extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FragmentCard();
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("test");
    }

}
