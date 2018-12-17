package com.nawinc27.mac.blooddonorfrontend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawinc27.mac.blooddonorfrontend.form.Form;
import com.nawinc27.mac.blooddonorfrontend.utility.SessionManager;

public class FormFragment extends Fragment {
    private Button hospitalName, submitForm;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference requestRef;
    private DatabaseReference formRef;
    private SessionManager session;
    private CheckBox q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12
            , q13, q14, q15, q16, q17, q18, q19, q20, q21, q22
            , q23, q24, q25, q26, q27, q28, q29, q30, q31;
    private EditText ansQ2, q32;
    private static boolean DEFAULT_FORM_APPROVE = false;
    private Bundle bundle;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        session = new SessionManager(getContext());

        hospitalName = getView().findViewById(R.id.spinner1);
        submitForm = getView().findViewById(R.id.form_submit);

        initQuestion();

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()){
                    sendForm();
                }
            }
        });

        setHospital(hospitalName);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container , false);
    }

    public void setHospital(final Button button){
        if(getArguments() != null){
            bundle = getArguments();
            button.setText(bundle.getString("hospitalname"));
        }
    }

    public void initQuestion(){

        try {
            q1 = getView().findViewById(R.id.q1);
            q2 = getView().findViewById(R.id.q2);
            q3 = getView().findViewById(R.id.q3);
            q4 = getView().findViewById(R.id.q4);
            q5 = getView().findViewById(R.id.q5);
            q6 = getView().findViewById(R.id.q6);
            q7 = getView().findViewById(R.id.q7);
            q8 = getView().findViewById(R.id.q8);
            q9 = getView().findViewById(R.id.q9);
            q10 = getView().findViewById(R.id.q10);
            q11 = getView().findViewById(R.id.q11);
            q12 = getView().findViewById(R.id.q12);
            q13 = getView().findViewById(R.id.q13);
            q14 = getView().findViewById(R.id.q14);
            q15 = getView().findViewById(R.id.q15);
            q16 = getView().findViewById(R.id.q16);
            q17 = getView().findViewById(R.id.q17);
            q18 = getView().findViewById(R.id.q18);
            q19 = getView().findViewById(R.id.q19);
            q20 = getView().findViewById(R.id.q20);
            q21 = getView().findViewById(R.id.q21);
            q22 = getView().findViewById(R.id.q22);
            q23 = getView().findViewById(R.id.q23);
            q24 = getView().findViewById(R.id.q24);
            q25 = getView().findViewById(R.id.q25);
            q26 = getView().findViewById(R.id.q26);
            q27 = getView().findViewById(R.id.q27);
            q28 = getView().findViewById(R.id.q28);
            q29 = getView().findViewById(R.id.q29);
            q30 = getView().findViewById(R.id.q30);
            q31 = getView().findViewById(R.id.q31);
            q32 = getView().findViewById(R.id.q32);

            ansQ2 = getView().findViewById(R.id.ans_q2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean checkForm(){
        boolean result = false;
        try {
            if(q2.isChecked()){
                if(ansQ2.getText().toString().isEmpty()){
                    result = false;
                    Toast.makeText(getActivity(), "กรุณากรอก 'ระยะเวลาที่จะคลอดบุตร'", Toast.LENGTH_SHORT).show();
                }else {
                    result = true;
                }
            }else{
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int getAnswerQuestion2(){
        int result = 0;
        try {
            if(q2.isChecked()){
                result = Integer.parseInt(ansQ2.getText().toString());
            }else{
                result = 0;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void sendForm(){
        try {
            Long timeStamp = System.currentTimeMillis();
            String formTimeStamp  = Long.toString(timeStamp);
            Form formBuffer = new Form(
                    q1.isChecked(),
                    getAnswerQuestion2(),
                    q3.isChecked(),
                    q4.isChecked(),
                    q5.isChecked(),
                    q6.isChecked(),
                    q7.isChecked(),
                    q8.isChecked(),
                    q9.isChecked(),
                    q10.isChecked(),
                    q11.isChecked(),
                    q12.isChecked(),
                    q13.isChecked(),
                    q14.isChecked(),
                    q15.isChecked(),
                    q16.isChecked(),
                    q17.isChecked(),
                    q18.isChecked(),
                    q19.isChecked(),
                    q20.isChecked(),
                    q21.isChecked(),
                    q22.isChecked(),
                    q23.isChecked(),
                    q24.isChecked(),
                    q25.isChecked(),
                    q26.isChecked(),
                    q27.isChecked(),
                    q28.isChecked(),
                    q29.isChecked(),
                    q30.isChecked(),
                    q31.isChecked(),
                    q32.getText().toString() + " ",
                    formTimeStamp,
                    DEFAULT_FORM_APPROVE);
            formRef = database.getReference("officer/form/" + bundle.getString("hospitalid") + "/" + session.getUsername() + "/");
            formRef.push().setValue(formBuffer);

            Toast.makeText(getActivity(), "ส่งแบบฟอร์มสำเร็จ", Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new HistoryFragment())
                    .commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void sendForm(){
//        try {
//            requestRef = database.getReference("/donor/form_request/" + session.getUsername() + "/");
//            requestRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    initform(dataSnapshot.child("hospital_id").getValue(String.class));
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.i("FORMFRAGMENT", "CAN'T SET HOSPITAL NAME");
//                }
//            });
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
