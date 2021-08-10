package com.goldian.yourfishsingsite.Model;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.UbahBarangActivity;

public class DialogModel  extends Dialog implements android.view.View.OnClickListener {

    public Dialog d;
    public Button yes, no;
    UbahBarangActivity updateActivity;

    public DialogModel(UbahBarangActivity updateActivity) {
        super(updateActivity);
        this.updateActivity = updateActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_yes:
//                updateActivity.updateImg();
//                dismiss();
//                break;
//            case R.id.btn_no:
//                dismiss();
//                break;
//            default:
//                break;
//        }
//        dismiss();
    }
}