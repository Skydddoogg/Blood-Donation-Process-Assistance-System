package com.nawinc27.mac.blooddonorfrontend.loading;

import android.app.AlertDialog;
import android.content.Context;

import com.nawinc27.mac.blooddonorfrontend.R;

import dmax.dialog.SpotsDialog;

public class CustomLoadingDialog {

    private AlertDialog loadingDialog;
    public CustomLoadingDialog(Context context){
        loadingDialog = new SpotsDialog.Builder().setContext(context).setTheme(R.style.CustomLoadingDialog).build();
    }
    public void showDialog(){
        loadingDialog.show();
    }
    public void dismissDialog(){
        loadingDialog.dismiss();
    }

}
