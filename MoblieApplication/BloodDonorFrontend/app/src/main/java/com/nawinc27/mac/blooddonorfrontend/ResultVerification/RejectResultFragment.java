package com.nawinc27.mac.blooddonorfrontend.ResultVerification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nawinc27.mac.blooddonorfrontend.HistoryFragment;
import com.nawinc27.mac.blooddonorfrontend.R;
import com.nawinc27.mac.blooddonorfrontend.utility.SessionManager;

public class RejectResultFragment extends Fragment {
    private Bundle bundle;
    private TextView userId, hospitalId;
    private SessionManager session;
    private Button button;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = getView().findViewById(R.id.reject_userId);
        hospitalId = getView().findViewById(R.id.reject_hospitalId);
        session = new SessionManager(getContext());
        button = getView().findViewById(R.id.reject_button);

        bundle = new Bundle();
        initUserIdAndHospitalId(userId, hospitalId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFragmentBackstack();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HistoryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reject_verified ,container ,false);
    }

    public String getFetchHospitalId(){
        if(getArguments() != null){
            bundle = getArguments();
            return bundle.getString("hospitalid");
        }else {
            return "null";
        }
    }

    public void initUserIdAndHospitalId(TextView userId, TextView hospitalId){
        userId.setText("ผลการตรวจสอบคุณสมบัติของหมายเลข : " + session.getUsername());
        hospitalId.setText(getFetchHospitalId());
    }

    public void clearFragmentBackstack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
