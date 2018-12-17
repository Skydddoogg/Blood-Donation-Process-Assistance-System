package com.nawinc27.mac.blooddonorfrontend.form;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nawinc27.mac.blooddonorfrontend.R;

public class FormSender extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference formRef;
    private Form formBuffer;
    private static String TEST_FORM_ELSE = "else";
    private static String TEST_HOSPITAL_NAME = "samitivej";
    private static String TEST_USER_ID = "1234567890111";
    private static boolean DEFAULT_FORM_APPROVE = false;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_test, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button = getView().findViewById(R.id.form_test_button);

        //Fetch Form's Request
        //If hospital request to send form for donate blood
        FormRequest.fetchRequest(getActivity());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForm();
            }
        });
    }

    public void sendForm(){
        Long timeStamp = System.currentTimeMillis();
        String formTimeStamp  = Long.toString(timeStamp);
        formBuffer = new Form(false,
                4,
                false,
                4,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                TEST_FORM_ELSE,
                formTimeStamp,
                DEFAULT_FORM_APPROVE);
        formRef = database.getReference("officer/form/" + TEST_HOSPITAL_NAME + "/" + TEST_USER_ID + "/");
        formRef.push().setValue(formBuffer);

    }
}
