package com.example.blooddonorapp.blooddonorrecorder.utility;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.blooddonorapp.blooddonorrecorder.R;

public class Extensions extends Fragment {

    public static void goTo(FragmentActivity act, Fragment other) {
        act.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, other)
                .addToBackStack(null)
                .commit();
    }

}
