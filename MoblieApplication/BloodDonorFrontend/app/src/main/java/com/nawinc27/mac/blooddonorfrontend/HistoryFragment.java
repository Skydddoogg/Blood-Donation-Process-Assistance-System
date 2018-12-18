package com.nawinc27.mac.blooddonorfrontend;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawinc27.mac.blooddonorfrontend.ResultVerification.PassResultFragment;
import com.nawinc27.mac.blooddonorfrontend.ResultVerification.RejectResultFragment;
import com.nawinc27.mac.blooddonorfrontend.form.Form;
import com.nawinc27.mac.blooddonorfrontend.history.History;
import com.nawinc27.mac.blooddonorfrontend.history.HistoryAdapter;
import com.nawinc27.mac.blooddonorfrontend.loading.CustomLoadingDialog;
import com.nawinc27.mac.blooddonorfrontend.maps.MapsActivity;
import com.nawinc27.mac.blooddonorfrontend.utility.Extensions;
import com.nawinc27.mac.blooddonorfrontend.utility.SessionManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class HistoryFragment extends Fragment {

    private ArrayList<History> histories = new ArrayList<History>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private TextView name;
    private TextView times;
    private SessionManager session;
    private static DatabaseReference requestRef;
    private DatabaseReference hospitalRef;
    private DatabaseReference approveRef;
    private Bundle bundle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FormFragment formFragment;
    private CustomLoadingDialog customLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mainpage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getContext());
        customLoadingDialog = new CustomLoadingDialog(getContext());
        bundle = new Bundle();

        if (!session.checkLogin()) {
            Extensions.goTo(getActivity(), new LoginFragment());
        } else {
            customLoadingDialog.showDialog();
            showHistory();
            showUserProfile();
            initLogoutBtn();
        }

        fetchRequest();
        checkFetchApprove();

        TextView goto_from_btn = getActivity().findViewById(R.id.goto_from_btn);
        goto_from_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HISTORY", "Goto MapFragment");
                Intent intent = new Intent(getContext(), MapsActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void showUserProfile(){
        //This function use to show User profile by setText in fragment_history
        try {
            name = getView().findViewById(R.id.main_name);
            myRef = database.getReference("/donor/profile/" + session.getUsername());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nameStr = dataSnapshot.child("firstname").child("0").getValue(String.class)
                            + " " + dataSnapshot.child("lastname").child("0").getValue(String.class);
                    name.setText(nameStr);
                    Log.i("HISTORY", "RETREIVE USER PROFILE SUCCESS");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("HISTORY", "ERROR CAN'T RETREIVE USER PROFILE FROM FIREBASE");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showHistory(){
        //This function use to show History that get from firebase and show on LiseView
        try {
            myRef = database.getReference("/donor/blood_donation/" + session.getUsername());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ListView historyList;
                    if (getView() != null){
                        historyList = getView().findViewById(R.id.main_history_donor);
                        final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(),
                                R.layout.list_item_history, histories);
                        historyList.setAdapter(historyAdapter);
                        histories.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            histories.add(child.getValue(History.class));
                        }
                        Log.i("TESTTTT " , histories.size()+"");
                        Collections.reverse(histories);
                        times = getView().findViewById(R.id.main_number_donate);
                        times.setText(""+histories.size());

                        customLoadingDialog.dismissDialog();
                        Log.i("HISTORY", "RETREIVE HISTORY SUCCESS");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    customLoadingDialog.dismissDialog();
                    Log.i("HISTORY", "ERROR CANNOT RETREIVE HISTORY FROM FIREBASE");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getAge(String dateOfBirth){
        int age;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String[] year = dateOfBirth.split("-");
        age = Integer.parseInt(sdf.format(timestamp)) - Integer.parseInt(year[0]);
        return age;
    }

    public void initLogoutBtn() {
        try {
            ImageView logoutBtn = getView().findViewById(R.id.main_btn_logout);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    session.logoutUser();
                    Log.d("HISTORY", "Goto LoginFragment");
                    Extensions.goTo(getActivity(), new LoginFragment());
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showDialog(){
        //This func will show dialog when request = true
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_custom);
            dialog.setTitle("FORM");
            dialog.cancel();

            Button accept = dialog.findViewById(R.id.dialog_accept);
            TextView cancel = dialog.findViewById(R.id.dialog_cancel);

            accept.setEnabled(true);
            cancel.setEnabled(true);
            dialog.show();

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    hospitalRef = database.getReference("/donor/form_request/" + session.getUsername() + "/");
                    hospitalRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                    bundle.putString("hospitalid", dataSnapshot.child("hospital_id").getValue(String.class));
                                    bundle.putString("hospitalname", dataSnapshot.child("hospital_name").getValue(String.class));

                                    fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    formFragment = new FormFragment();
                                    formFragment.setArguments(bundle);

                                    fragmentTransaction.replace(R.id.main_view, formFragment);
                                    fragmentTransaction.commit();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("FORMFRAGMENT", "CAN'T SET HOSPITAL NAME");
                        }
                    });
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchRequest(){
        //This func will call showDialog when hospital request form
        try {
            requestRef = database.getReference("/donor/form_request/" + session.getUsername() + "/");
            requestRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("request")){
                        boolean request = dataSnapshot.child("request").getValue(boolean.class);
                        if(request){
                            setRequest();
                            try {
                                showDialog();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        Log.i("REQUEST", "REQUEST : " + request);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("REQUEST", "ERROR CAN'T GET REQUEST");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setRequest(){
        try {
            requestRef = database.getReference("/donor/form_request/" + session.getUsername() + "/");
            requestRef.child("request").setValue(false);
            Log.i("REQUEST", "SET REQUEST TO FALSE");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getFetchHospitalId(){
        if(getArguments() != null){
            bundle = getArguments();
            return bundle.getString("hospitalid");
        }else {
            return "null";
        }
    }

    public void checkFetchApprove(){
        if(!getFetchHospitalId().equals("null")){
            fetchApprove();
        }
    }

    public void fetchApprove(){
        //This func will call showDialog when hospital request form
        try {
            approveRef = database.getReference("/officer/form/" + getFetchHospitalId() + "/" + session.getUsername() + "/");
            approveRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Form formBuffer = dataSnapshot.child("form").getValue(Form.class);
                    if(!formBuffer.getApprove().equals("unproved") && !formBuffer.getApprove().equals("proved")){
                        setApprove();
                        if(formBuffer.getApprove().equals("accepted")){
                            try {
                                moveToResultFragment(new PassResultFragment());
                                Log.i("APPROVE", formBuffer.getApprove() + "");

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else if(formBuffer.getApprove().equals("rejected")){
                            try {
                                moveToResultFragment(new RejectResultFragment());
                                Log.i("APPROVE", formBuffer.getApprove() + "");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setApprove(){
        try {
            approveRef = database.getReference("/officer/form/" + getFetchHospitalId() + "/" + session.getUsername() + "/");
            approveRef.child("form").child("approve").setValue("proved");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearFragmentBackstack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void moveToResultFragment(Fragment fragment){
        try {
            clearFragmentBackstack();

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.main_view, fragment);
            fragmentTransaction.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
