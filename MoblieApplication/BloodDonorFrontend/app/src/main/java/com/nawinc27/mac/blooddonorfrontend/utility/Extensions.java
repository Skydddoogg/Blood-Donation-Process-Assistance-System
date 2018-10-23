package com.nawinc27.mac.blooddonorfrontend.utility;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.nawinc27.mac.blooddonorfrontend.R;

public class Extensions extends Fragment {

    public static void goTo(FragmentActivity act, Fragment other) {
        act.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, other)
                .addToBackStack(null)
                .commit();
    }

}