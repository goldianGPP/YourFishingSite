package com.goldian.yourfishsingsite.Model;

import android.widget.EditText;

public class FilterModel {

    //set string to indonesian rupisah's format
    public String setToRupiah(String value){
        int length = value.length(), count = 0;
        String output = "";

        for (char c : value.toCharArray()) {
            if(((length - count) % 3) == 0 && count != 0)
                output += ",";
            output += c;
            count+=1;
        }
        return "Rp. " + output;
    }
}
