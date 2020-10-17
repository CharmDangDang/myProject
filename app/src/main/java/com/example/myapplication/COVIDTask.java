package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class COVIDTask extends AsyncTask<Void, Void, String> {

    String room;

    public COVIDTask(String room) {
        this.room = room;
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = null;
        try {
            Document document = Jsoup.connect("https://bit.ly/2EwlTVT").get();
            Elements elements = document.select("#_cs_production_type > div:nth-child(8) > div.status_info.abroad_info > ul > li.info_01 > p");
            String world_num = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(8) > div.status_info.abroad_info > ul > li.info_01 > em");
            String world_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(8) > div.status_info.abroad_info > ul > li:nth-child(2) > p");
            String world_death = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(8) > div.status_info.abroad_info > ul > li:nth-child(2) > em");
            String world_d_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_01 > p");
            String korea_num = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_01 > em");
            String korea_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_02 > p");
            String korea_exam = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_02 > em");
            String korea_exam_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_03 > p");
            String korea_clear = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_03 > em");
            String korea_clear_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_04 > p");
            String korea_death = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_info > ul > li.info_04 > em");
            String korea_death_variation = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_today > ul > li.info_02 > em");
            String korea_inside = elements.text();
            elements = document.select("#_cs_production_type > div:nth-child(7) > div.status_today > ul > li.info_03 > em");
            String korea_outside = elements.text();

            msg = "코로나 현황" + Answer.crossLine + "[세계현황]\n" + "확진환자 : " + world_num + "(+" + world_variation + ")"
                    + "\n사망자 : " + world_death + "(+" + world_d_variation + ")"
                    + "\n[국내현황]\n" + "확진환자 : " + korea_num + "(+" + korea_variation + ")"
                    + "\n검사중 : " + korea_exam + "(+" + korea_exam_variation + ")"
                    + "\n격리해제 : " + korea_clear + "(+" + korea_clear_variation + ")"
                    + "\n사망자 : " + korea_death + "(+" + korea_death_variation + ")"
                    + "\n일일국내발생 : " + korea_inside
                    + "\n일일해외유입 : " + korea_outside
                    + Answer.crossLine + "코로나 관련 링크(전체보기 클릭)\n" + Session.ReadAll + "\nhttps://coronaboard.kr/\n(코로나상황판 링크-직관적,빠른갱신)"
                    + Answer.crossLine + "\nhttp://ncov.mohw.go.kr/\n(질병관리본부 링크-국내위주,자세함)"
                    + Answer.crossLine + "\nhttps://coronamap.site/\n(코로나 맵 링크-국내위주,카카오맵 기반 지도자료)"
                    + Answer.crossLine + "\nhttps://livecorona.co.kr/\n(라이브 코로나 맵 링크-직관적,국내외 지도자료)"
                    + Answer.crossLine + "\nhttps://korean.cdc.gov/coronavirus/2019-ncov/symptoms-testing/coronavirus-self-checker.html\n(코로나 대비 및 대처 링크-현실적인 도움)"
                    + Answer.crossLine + "\nhttp://www.ksid.or.kr/rang_board/list.html?code=ncov_site\n(코로나 학술 논문 링크)";


        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }


    @Override
    protected void onPostExecute(String msg) {
        Listener.send(room,msg);
    }
}
