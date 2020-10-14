package com.example.wordgameproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SecondFragment.SecondFragmentListener{
    private TextView[] textView = new TextView[8];
    private TextView out;
    private SecondFragment secondFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secondFragment = new SecondFragment();




    }







    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String temp = event.getX() + "," + event.getY()+";";
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                temp+="down";
                break;
            case MotionEvent.ACTION_UP:
                temp+="up";
                break;
            case MotionEvent.ACTION_MOVE:
                temp+="move";
                break;
        }
        secondFragment.sendCords(temp);
        return true;
    }

    @Override
    public void onInputASent(CharSequence input) {

    }

}