package com.goldian.yourfishsingsite.Model;

import android.app.Dialog;
import android.content.Context;

import com.goldian.yourfishsingsite.R;

public class ProgressDialogModel {
    Context context;
    Dialog dialog;

    public ProgressDialogModel(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.progress_dialog);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
