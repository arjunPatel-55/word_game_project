package com.example.wordgameproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class SecondFragment extends Fragment {
    private SecondFragmentListener listener;
    private static TextView[] textView = new TextView[8];
    private static TextView out;
    private static String wordrn=new String();
    private static String cordrn = new String();
    private static ArrayList<String> allGoodWords = new ArrayList<>();
    private static ListView doneWords;
    private static TextView timer;
    private static TextView points;
    private int point = 0;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    private static String tempMove = new String();
    private static String tempMove2 = new String();


    public interface SecondFragmentListener {
        void onInputASent(CharSequence input);
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView[0] = view.findViewById(R.id.textView1);
        textView[1] = view.findViewById(R.id.textView2);
        textView[2] = view.findViewById(R.id.textView3);
        textView[3] = view.findViewById(R.id.textView4);
        textView[4] = view.findViewById(R.id.textView5);
        textView[5] = view.findViewById(R.id.textView6);
        textView[6] = view.findViewById(R.id.textView7);
        textView[7] = view.findViewById(R.id.textView8);
        out = view.findViewById(R.id.words);
        doneWords = view.findViewById(R.id.doneWords);
        timer = view.findViewById(R.id.timer);
        points = view.findViewById(R.id.points);
        points.setText("points:"+point);
        boolean w = true;
        while (w) {
            ArrayList<String> temp = new ArrayList<String>();
            temp.addAll(Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}));
            Random r = new Random();
            for (TextView value : textView) {
                int t = r.nextInt(temp.size());
                value.setText(temp.get(t));
                temp.remove(t);
            }
            w= getWords();
        }
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        doneWords.setAdapter(adapter);


        timer.setText("Time: 30");
        new Timer().schedule(new TimerTask() {
            Date date1 = new Date();
            Date date2 = new Date();
            int seconds = 30;
            Handler handler = new Handler();

            @SuppressLint("SetTextI18n") //this command lets you use ui features like setText from the textview
            @Override
            public void run() {
                date2 = new Date();
                if (TimeUnit.MILLISECONDS.toSeconds(date2.getTime()-date1.getTime()) > .5 && seconds>0) {
                    date1 = new Date();
                    seconds--;
                    timer = timer.findViewById(R.id.timer);
//                    timer.setText("Time: " + seconds);
                }
                if (seconds == 0) {
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    this.cancel();
                }

            }
        }, 0, 25);


    }





    public void sendCords(CharSequence txt){

        String temp = txt.toString();
        double x = Double.parseDouble(temp.substring(0,temp.indexOf(',')));
        double y = Double.parseDouble(temp.substring(temp.indexOf(',')+1,temp.indexOf(';')))-100;
        String what = temp.substring(temp.indexOf(';')+1);
        for (int i=0;i<textView.length;i++){
            if (textView[i].getX()<x && textView[i].getX()+50>x   &&   textView[i].getY()<y && textView[i].getY()+50>y){
                if (wordrn.length()>0){
                    boolean t =true;
                    for (int z=0;z<wordrn.length();z++) {
                        if (wordrn.substring(z,z+1).equals((String) textView[i].getText())){
                            t=false;
                        }
                    }
                    if (t){
                        wordrn += (String) textView[i].getText();
                    }
                }else {
                    wordrn += (String) textView[i].getText();
                }
                out.setText(wordrn);
            }
        }
        if (what.equals("up")){
            isItAGoodWord();
        }

    }


    private void isItAGoodWord(){
//        TextView points = out.findViewById(R.id.points);
//        point+=wordrn.length();
//        String t= "points:";
//        points.setText(t);
        arrayList.add("shdfhjkh");
        adapter.notifyDataSetChanged();
        System.out.println("sdkljfklsjdj" + allGoodWords.toString());
        for (int i=0;i<allGoodWords.size();i++){
            if (wordrn.equals(allGoodWords.get(i).substring(allGoodWords.get(i).indexOf('"'),allGoodWords.get(i).length()-1))){
//                point+=wordrn.length();
//                points.setText("points:"+point);
                System.out.println(allGoodWords.get(i).indexOf('"') +"   " +(allGoodWords.get(i).length()-1));
                System.out.println("sldkfhsdlkfhjksdhfsdhfhjsd");
                arrayList.add(wordrn);
                adapter.notifyDataSetChanged();

            }
        }
        wordrn ="";
    }

    private boolean getWords(){
        boolean tOrF = false;
        StringBuilder t = new StringBuilder(new String());
        for (int i=0;i<textView.length;i++){
            t.append(textView[i].getText());
        }
        t.append(":");
        String url1 = "http://www.anagramica.com/all/"+t.toString();
        new AsyncHttpClient().get(url1, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String t = new String(responseBody);
                t = t.substring(t.indexOf('[') + 1);
                ArrayList<String> t2 = new ArrayList<>(Arrays.asList(t.split(",")));
                ArrayList<String> wordsasd = new ArrayList<>();

//                for (int i = 0; i < t2.size(); i++) {
//                    if (!(t2.get(i).substring(t2.get(i).indexOf('"') + 1, t2.get(i).length() - 1).length() < 2)) {
//                        isItAWord(t2.get(i).substring(t2.get(i).indexOf('"') + 1, t2.get(i).length() - 1));
//                        wordsasd.add(tempMove2);
//                    }
//                }
                for (int i = 0; i < t2.size(); i++) {
                    if (t2.get(i).length() > 8) {
                        allGoodWords.add(t2.get(i));
//                        System.out.println(t2.get(i).length()+t2.get(i));
                    }
                }
                System.out.println(allGoodWords + "    sdjfklsdjklfjsdkljfkljsd");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }

        });
        if (allGoodWords.size()<7){
            tOrF=false;
        }
        System.out.println(allGoodWords.toString());
        return tOrF;
    }



    private void isItAWord(String w){
//        w = "banana";
        String url1 = "https://api.datamuse.com/words?ml="+w+ "&md=f";
        new AsyncHttpClient().get(url1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String t = new String(responseBody);
                if(!t.equals("[]")) {
                    String w2 = t.substring(t.indexOf("word\":\"") + 7, t.indexOf("\",\"score"));
                    isItAWord2(w2);
                    w2 = tempMove2;

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    private void isItAWord2(String w) {
        String url1 = "https://api.datamuse.com/words?ml="+w+ "&md=f";
        new AsyncHttpClient().get(url1, new AsyncHttpResponseHandler() {
            String z = "";
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                z = new String(responseBody);
                z = z.substring(z.indexOf("word\":\"")+7,z.indexOf("\",\"score")) + "," + z.substring(z.indexOf("\"f:")+3,z.indexOf("\"]},{\"word"));
                tempMove2 = new String(z);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }








}


