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
import android.widget.ListView;
import android.widget.TextView;

import com.example.blooddonorapp.blooddonorrecorder.history.History;
import com.example.blooddonorapp.blooddonorrecorder.history.HistoryAdapter;
import com.example.blooddonorapp.blooddonorrecorder.utility.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private ArrayList<History> histories = new ArrayList<History>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private TextView name;
    private TextView bloodType;
    private TextView age;
    private TextView times;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getContext());
        showHistory();
        showUserProfile();
        initLogoutBtn();
    }

    public void showUserProfile(){
        //This function use to show User profile by setText in fragment_history
        name = getView().findViewById(R.id.home_name);
        bloodType = getView().findViewById(R.id.home_bloodType);
        age = getView().findViewById(R.id.home_age);
        myRef = database.getReference("/donor/profile/" + session.getUsername());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameStr = dataSnapshot.child("firstname").child("0").getValue(String.class)
                        + " " + dataSnapshot.child("lastname").child("0").getValue(String.class);
                String bloodtypeStr = dataSnapshot.child("bloodtype").child("0").getValue(String.class);
                int ageInt = getAge(dataSnapshot.child("dob").child("0").getValue(String.class));
                name.setText(nameStr);
                bloodType.setText("กรุ๊ปเลือด : " + bloodtypeStr);
                age.setText("อายุ : " + ageInt + " ปี");
                Log.i("HISTORY", "RETREIVE USERPROFILE SUCCESS");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CAN'T RETREIVE USERPROFILE FROM FIREBASE");
            }
        });
    }

    public void showHistory(){
        //This function use to show History that get from firebase and show on LiseView
        times = getView().findViewById(R.id.home_times);
        myRef = database.getReference("/donor/blood_donation/" + session.getUsername());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListView historyList = getView().findViewById(R.id.history_list);
                final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(),
                        R.layout.fragment_history_item, histories);
                historyList.setAdapter(historyAdapter);
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    histories.add(child.getValue(History.class));
                }
                times.setText("เข้าบริจาคเลือดทั้งหมด : " + histories.size() + " ครั้ง");
                Log.i("HISTORY", "RETREIVE HISTORY SUCCESS");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HISTORY", "ERROR CAN'T RETREIVE HISTORY FROM FIREBASE");
            }
        });
    }

    public int getAge(String dateOfBirth){
        int age;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String[] year = dateOfBirth.split("-");
        age = Integer.parseInt(sdf.format(timestamp)) - Integer.parseInt(year[0]) + 543;
        return age;
    }

    public void initLogoutBtn() {
        Button logoutBtn = getView().findViewById(R.id.history_btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Log.d("REGISTERFRAGMENT", "Goto LoginFragment");
                Extensions.goTo(getActivity(), new LoginFragment());
            }
        });
    }
}
