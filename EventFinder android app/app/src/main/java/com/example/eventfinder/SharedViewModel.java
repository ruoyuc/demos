package com.example.eventfinder;
import androidx.lifecycle.ViewModel;
public class SharedViewModel extends ViewModel{
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
