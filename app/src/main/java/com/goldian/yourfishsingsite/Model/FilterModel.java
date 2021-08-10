package com.goldian.yourfishsingsite.Model;

public class FilterModel {

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

    public Integer subtotal(Integer a, Integer b){
        return (a + b);
    }

    public Integer total(Integer a, Integer b){
        return (a * b);
    }
}
