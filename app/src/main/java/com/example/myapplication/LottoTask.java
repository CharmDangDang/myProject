package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LottoTask extends AsyncTask<Void, Void, Map<String, String>> {
    String room;

    public LottoTask(String room) {
        this.room = room;
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            Document document = Jsoup.connect("https://dhlottery.co.kr/gameResult.do?method=byWin").get();
            Elements elements = document.select(".win_result h4 strong");
            result.put("latestLottoCount", elements.text());
            elements = document.select(".win_result .nums");
            result.put("number", elements.select(".win span").text());
            result.put("bonus", elements.select(".bonus span").text());
            elements = document.select("#article > div:nth-child(2) > div > div.win_result > p");
            result.put("drawDate", elements.text());
            elements = document.select("#article > div:nth-child(2) > div > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong");
            result.put("totalWinMoney", elements.text());
            elements = document.select("#article > div:nth-child(2) > div > table > tbody > tr:nth-child(1) > td:nth-child(3)");
            result.put("totalWinner", elements.text());
            elements = document.select("#article > div:nth-child(2) > div > table > tbody > tr:nth-child(1) > td:nth-child(4)");
            result.put("personalMoney", elements.text());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Map<String, String> map) {
        Listener.send(room,"로또 결과 (전체보기 클릭)" + Session.ReadAll + Answer.crossLine
                + map.get("latestLottoCount") + "\t" + map.get("drawDate") + "\t"
                + "\n당첨번호 "
                + map.get("number") + " 보너스 숫자 " + map.get("bonus")
                + "\n1등 총 : " + map.get("totalWinMoney")
                + "\n1등 총 : " + map.get("totalWinner") + "명 당첨"
                + "\n1등 개인 당첨금 : " + map.get("personalMoney"));
    }
}