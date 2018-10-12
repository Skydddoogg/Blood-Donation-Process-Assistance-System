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
import com.example.blooddonorapp.blooddonorrecorder.history.HistoryFragment;

public class LoginFragment extends Fragment {
    private Button buttonLogin;
    private EditText usernameInput;
    private EditText passwordInput;
    private String username;
    private String password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonLogin = getView().findViewById(R.id.login_btn_login);
        usernameInput = getView().findViewById(R.id.login_username);
        passwordInput = getView().findViewById(R.id.login_password);

        buttonLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        username = usernameInput.getText().toString();
//                        password = passwordInput.getText().toString();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_view, new HistoryFragment())
                                .addToBackStack(null)
                                .commit();
                        Log.i("LOGIN", "GO TO HOMEPAGE");
                    }
                }
        );
    }
}
