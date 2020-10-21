package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class DebateTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private String room;
    public DebateTask(String room) {
        this.room = room;
    }

    ArrayList<String> dataSet = new ArrayList<>();
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        Document document = null;
        try {
            document = Jsoup.connect("https://debatingday.com/status/").get();
            Elements elements = document.select("#main > div > div.col-md-9 > table > tbody");
            for(int i = 1; i<150; i++){
                String data = elements.select("tr:nth-child("+i+") > td:nth-child(2) > a").text();
               dataSet.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);

        if(result.get(new Random().nextInt((result.size()))).equals("")){
            Listener.send(room,"사이트가 고장났습니다. 두부조아에게 화를 내세용");
        } else {
        Listener.send(room,"랜덤 주제 : "+ result.get(new Random().nextInt((result.size())))+"에 관하여");

        }
    }
}
