package com.retartsoft.lemon.wordmemorizer.FragmentActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.retartsoft.lemon.wordmemorizer.Fragments.FragmentWord;

/**
 * Created by Lemon113 on 18.08.2017.
 */

public class FragmentActivityWord extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FragmentWord();
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
