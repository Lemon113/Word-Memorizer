package com.retartsoft.lemon.wordmemorizer.FragmentActivities;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.retartsoft.lemon.wordmemorizer.Fragments.FragmentCardlist;

public class FragmentActivityCardlist extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FragmentCardlist();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
