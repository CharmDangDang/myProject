package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class newsTask extends AsyncTask<Void, Void, ArrayList<NewsData>> {
    String room;
    String tag;
    String mTag;
    String date;
    String qChild = "div:nth-child(";
    String mainTitle = ") > div > a > span.tb > strong";
    String mainLink = ") > div > a";
    boolean isSports = false;
    boolean rank = false;
    String[] tags = {"시사", "경제", "스포츠", "사회", "과학", "세계", "연예", "정치", "야구", "국내야구", "한국야구", "해외야구", "mlb", "축구", "한국축구", "국내축구", "해축", "해외축구", "배구", "농구"};
    ArrayList<NewsData> newsDataArrayList = new ArrayList<>();

    public newsTask(String room, String tag) {
        this.room = room;
        mTag = tag;
        isSports = isSportsTag(tag);
        this.tag = tagCheck(tag);
        if (this.tag.equals("all")) {
            mTag = "종합";
        }
    }

    public newsTask(String room, String tag, boolean rank) {
        this.room = room;
        mTag = tag;
        isSports = isSportsTag(tag);
        this.tag = tagCheck(tag);
        if (this.tag.equals("all")) {
            mTag = "종합";
        }
        this.rank = rank;
    }


    @Override
    protected ArrayList<NewsData> doInBackground(Void... voids) {


        try {
            date = getDate();
            Document document;
            Elements elements;
            if (isSports) {
                document = Jsoup.connect("https://sports.news.nate.com/" + tag + "/recent").get();
                elements = document.select("#cntArea > ul.mduTitcnt");
                qChild = "li:nth-child(";
                mainTitle = ") > a > span.tb > strong";
                mainLink = ") > a";
                putData(elements, 21);
            } else {
                document = Jsoup.connect("https://news.nate.com/rank/interest?sc=" + tag + "&p=day&date=" + date).get();
                elements = document.select("#newsContents > div > div.postRankSubjectList.f_clear");
                putData(elements, 6);
                Elements subElements = document.select("#postRankSubject");
                for (int i = 1; i < 6; i++) {
                    for (int j = 1; j < 6; j++) {
                        NewsData data = new NewsData();
                        data.setNewsLink("https:" + subElements.select("#postRankSubject > ul:nth-child(" + i + ") > li:nth-child(" + j + ") > a").attr("href"));
                        newsDataArrayList.add(data);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsDataArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsData> arrayList) {
        if (rank) {
            Listener.send(room, mTag + "뉴스 1~5위 보기\n(전체보기 클릭)" + Session.ReadAll + Answer.crossLine
                    + arrayList.get(0).getNewsTitle() + "\n" + arrayList.get(0).getNewsLink() + Answer.crossLine
                    + arrayList.get(1).getNewsTitle() + "\n" + arrayList.get(1).getNewsLink() + Answer.crossLine
                    + arrayList.get(2).getNewsTitle() + "\n" + arrayList.get(2).getNewsLink() + Answer.crossLine
                    + arrayList.get(3).getNewsTitle() + "\n" + arrayList.get(3).getNewsLink() + Answer.crossLine
                    + arrayList.get(4).getNewsTitle() + "\n" + arrayList.get(4).getNewsLink() + Answer.crossLine);
        } else {
            int i = arrayList.size();
            Random random = new Random();
            Listener.send(room, mTag + "뉴스중 랜덤 링크" + Answer.crossLine + arrayList.get(random.nextInt(i)).getNewsLink());
        }
    }

    public String tagCheck(String tag) {
        int cnt = 0;
        for (int i = 0; i < tags.length; i++) {
            if (tag.toLowerCase().equals(tag)) {
                cnt++;
            }
        }
        if (cnt == 0) {
            tag = "종합";
        }
        switch (tag) {
            case "시사":
                return "sisa";
            case "스포츠":
                return "spo";
            case "연예":
                return "ent";
            case "정치":
                return "pol";
            case "경제":
                return "eco";
            case "사회":
                return "soc";
            case "세계":
                return "int";
            case "과학":
                return "its";
            case "국내야구":
            case "한국야구":
            case "야구":
                return "baseball";
            case "해외야구":
            case "mlb":
                return "abrbaseball";
            case "축구":
            case "한국축구":
            case "국내축구":
                return "soccer";
            case "해축":
            case "해외축구":
                return "abrsoccer";
            case "배구":
            case "농구":
                return "basketvolley";
            default:
                return "all";
        }

    }


    public String getDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        return sdf.format(c1.getTime());
    }

    public boolean isSportsTag(String smallTag) {
        int cnt = 0;
        for (int i = 8; i < tags.length; i++) {
            if (smallTag.equals(tags[i]))
                cnt++;
        }
        return cnt > 0;
    }

    public void putData(Elements elements, int max) {
        for (int i = 1; i < max; i++) {
            NewsData data = new NewsData();
            data.setNewsTitle(elements.select(qChild + i + mainTitle).text());
            data.setNewsLink("https:" + elements.select(qChild + i + mainLink).attr("href"));
            newsDataArrayList.add(data);
            Log.d("뉴스데이터", elements.select(qChild + i + mainTitle).text());
            Log.d("뉴스 링크", elements.select(qChild + i + mainLink).attr("href"));
        }

    }

}

/*  String qChild = "div:nth-child(";
    String mainTitle = ") > div > a > span.tb > strong";
    String mainLink = ") > div > a";
     qChild = "li:nth-child(";
                mainTitle = ") > a > span.tb > strong";
                mainLink = ") > a";
                putData(elements,21);

                putData(elements,6);
                Elements subElements = document.select("#postRankSubject");
                for (int i = 1; i < 6; i++) {
                    for (int j = 1; j < 6; j++) {
                        NewsData data = new NewsData();
                        data.setNewsLink("https:" + subElements.select("#postRankSubject > ul:nth-child(" + i + ") > li:nth-child(" + j + ") > a").attr("href"));
                        newsDataArrayList.add(data);
                    }*/
/*

    public void putData(Elements elements,int max) {
        for (int i = 1; i < max; i++) {
            public void putData(Elements elements) {
                for (Element element : elements) {
                    NewsData data = new NewsData();
                    data.setNewsTitle(elements.select(qChild + i + mainTitle).text());
                    data.setNewsLink("https:" + elements.select(qChild + i + mainLink).attr("href"));
                    newsDataArrayList.add(data);
                    Log.d("뉴스데이터",elements.select(qChild + i + mainTitle).text());
                    Log.d("뉴스 링크",elements.select(qChild + i + mainLink).attr("href"));*/
