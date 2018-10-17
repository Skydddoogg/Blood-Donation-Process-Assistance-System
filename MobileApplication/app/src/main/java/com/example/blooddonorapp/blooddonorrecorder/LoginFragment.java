package com.example.blooddonorapp.blooddonorrecorder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.blooddonorapp.blooddonorrecorder.utility.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.digest.DigestUtils;

public class LoginFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    private SessionManager session;
    private String username;
    private String password;
    private String hashedPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getContext());
        if (session.checkLogin()) {
            Extensions.goTo(getActivity(), new HistoryFragment());
        }
        initLoginBtn();
    }

    void initLoginBtn() {
        Button buttonLogin = getView().findViewById(R.id.login_btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) getView()
                        .findViewById(R.id.login_username))
                        .getText()
                        .toString();
                password = ((EditText) getView()
                        .findViewById(R.id.login_password))
                        .getText()
                        .toString();
                hashedPassword = DigestUtils.sha1Hex(password);
                login();
            }
        });
    }

    void login() {
        ref = database.getReference("/donor/authen/" + username + "/password");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (hashedPassword.equals(dataSnapshot.getValue(String.class))) {
                        session.createLoginSession(username);
                        Extensions.goTo(getActivity(), new HistoryFragment());
                        Log.d("LOGIN", "GO TO HOMEPAGE");
                    } else {
                        Log.d("LOGIN", "CAN'T LOGIN");
                    }
                } catch (NullPointerException e) {
                    Log.d("LOGIN", e.toString());
                    Log.d("LOGIN", "CAN'T LOGIN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LOGIN", "onCancelled", databaseError.toException());
            }
        });
    }
}
