package com.goldian.yourfishsingsite.Model;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    protected class Message{
        String message, status;
        TextView textView;

        public Message(String message, String status, TextView textView) {
            this.message = message;
            this.status = status;
            this.textView = textView;
        }
    }

    List<Message> messages = new ArrayList<>();

    public void addMessages(Message message){
        messages.add(message);
    }

    public Validation isEmpty(TextView textView){
        if (textView.getText().toString().equals("")){
            addMessages(new Message(textView.getHint().toString(), " kosong ", textView));
        }

        return this;
    }

    public Validation isFieldOk(TextView textView, int min, int max){
        int length = textView.getText().toString().length();
        if (length > max){
            addMessages(new Message(textView.getHint().toString(), " maximal " + max + " karakter ", textView));
        }
        else if (length < min){
            addMessages(new Message(textView.getHint().toString(), " minimal " + min + " karakter ", textView));
        }

        return this;
    }

    public Boolean validate(){
        if (messages.size() != 0) {
            for (Message message : messages) {
                message.textView.setError(
                        "kolom "
                        + message.message
                        + message.status
                        );
                    return false;
            }
        }
        return true;
    }
}
