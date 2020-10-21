package com.example.myapplication;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DecimalFormat;

public class ExchangeTask extends AsyncTask<Void, Void, Double> {

    private String room;
    private String msg;
    private String cnt;

    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    public ExchangeTask(String room, String msg, String cnt) {
        this.room = room;
        this.msg = msg;
        if (cnt.equals("")) {
            this.cnt = "1";
        } else{
            this.cnt = cnt;
        }
    }

    @Override
    protected Double doInBackground(Void... voids) {
        int a = 2, b , c;
        int max, min;

        max = (a>b)? a : (b>c)? b: c;
        String strJson = null;
        double exchange = 0;
        try {
            strJson = Jsoup.connect("http://fx.kebhana.com/FER1101M.web").get().select("body").text();
            strJson = strJson.substring(12);
            JsonParser jsonParser = new JsonParser();
            JsonObject obj;
            obj = (JsonObject) jsonParser.parse(strJson);
            JsonArray jsonArray = (JsonArray) obj.get("리스트");
            JsonObject jsonObject;
            for (int i = 0; i < jsonArray.size()-1; i++) {
                jsonObject = (JsonObject) jsonArray.get(i);
                if (jsonObject.get("통화명").toString().contains(msg)) {
                    exchange = Double.parseDouble(jsonObject.get("매매기준율").toString().replaceAll("\"",""));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Integer.parseInt(cnt) * exchange);
    }

    @Override
    protected void onPostExecute(Double s) {
        super.onPostExecute(s);
        if(s==0){
            Listener.send(room, "오타난 것 같습니다용");
        } else {
            Listener.send(room, decimalFormat.format(s) + "원");
        }
    }
}


//       /환율 미국 100  /환율 중국 100  exchange = Float.parseFloat(Objects.requireNonNull(obj.get("매매기준율")).toString());