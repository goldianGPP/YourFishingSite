package com.goldian.yourfishsingsite.Model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.DetailBarangActivity;
import com.goldian.yourfishsingsite.View.UbahBarangActivity;
import com.goldian.yourfishsingsite.View.UbahEventActivity;
import com.goldian.yourfishsingsite.View.UbahLokasiActivity;
import com.goldian.yourfishsingsite.View.UbahProfileActivity;

public class DialogModel {
    RatingBar ratingItem;
    TextView txtRating;
    Button btnOk, btnBatal;
    Dialog dialog;
    Context context;
    public DialogModel(Context context, int content){
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(content);
    }

    public void defaultView(){
        txtRating = dialog.findViewById(R.id.txtRating);
        btnOk = dialog.findViewById(R.id.btnOk);
        btnBatal = dialog.findViewById(R.id.btnBatal);

        defaultListener();
        dialog.show();
    }

    public void ratingView(){
        ratingItem = dialog.findViewById(R.id.ratingItem);
        txtRating = dialog.findViewById(R.id.txtRating);
        btnOk = dialog.findViewById(R.id.btnOk);
        btnBatal = dialog.findViewById(R.id.btnBatal);

        ratingListener();
        dialog.show();
    }

    private void defaultListener(){
        btnOk.setOnClickListener(onClick);
        btnBatal.setOnClickListener(onClick);

    }

    private void ratingListener(){
        defaultListener();
        ratingItem.setOnRatingBarChangeListener(onRating);
    }

    //listener
    //---------------
    View.OnClickListener onClick = view -> {
        if (view == btnOk){
            if (context instanceof DetailBarangActivity) {
                ((DetailBarangActivity) context).setRating(txtRating.getText().toString());
            }
            else if (context instanceof UbahBarangActivity)
                ((UbahBarangActivity) context).updateImg();
            else if (context instanceof UbahLokasiActivity)
                ((UbahLokasiActivity) context).updateImg();
            else if (context instanceof UbahEventActivity)
                ((UbahEventActivity) context).updateImg();
//            else if (context instanceof UbahProfileActivity)
//                ((UbahProfileActivity) context).updateImg();
            dialog.dismiss();
        }
        else if (view == btnBatal){
            dialog.dismiss();
        }
    };

    RatingBar.OnRatingBarChangeListener onRating = (ratingBar, rating, fromUser) -> {
        txtRating.setText(String.valueOf(rating));
    };
}