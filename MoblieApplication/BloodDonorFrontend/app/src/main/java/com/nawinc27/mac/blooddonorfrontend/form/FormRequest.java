package com.nawinc27.mac.blooddonorfrontend.form;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawinc27.mac.blooddonorfrontend.R;

public class FormRequest {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference requestRef;
    private static String TEST_USER_ID = "1234567890111";

    public static void showDialog(Activity activity){
        //This func will show dialog when request = true
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setTitle("FORM");

        Button accept = dialog.findViewById(R.id.dialog_accept);
        TextView cancel = dialog.findViewById(R.id.dialog_cancel);

        accept.setEnabled(true);
        cancel.setEnabled(true);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to FormFragment
                Log.i("DIALOG", "ACCEPT");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public static void fetchRequest(final Activity activity){
        //This func will call showDialog when hospital request form
        requestRef = database.getReference("/donor/form_request/" + TEST_USER_ID + "/");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean request = dataSnapshot.child("request").getValue(boolean.class);
                if(request){
                    setRequest();
                    showDialog(activity);
                }
                Log.i("REQUEST", "REQUEST : " + request);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("REQUEST", "ERROR CAN'T GET REQUEST");
            }
        });
    }

    public static void setRequest(){
        requestRef = database.getReference("/donor/form_request/" + TEST_USER_ID + "/");
        requestRef.child("request").setValue(false);
        Log.i("REQUEST", "SET REQUEST TO FALSE");
    }
}
