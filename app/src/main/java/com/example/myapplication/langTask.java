package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;

public class langTask extends AsyncTask<Void,Void,String> {
    private String msg;
    private int code;
    String room;
    String hanDic = "async=translate,sl:ko,tl:en,st:";
    String engDic = "async=translate,sl:ko,tl:en,st:";
    String hanToEng = "async=translate,sl:ko,tl:en,st:";
    String engToHan = "async=translate,sl:en,tl:ko,st:";
    String url;
    public langTask(String room,String msg,int code) {
        this.msg = msg;
        this.code = code;
        this.room = room;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        url = getUrl(code);
        try {
            result = Jsoup.connect("https://www.google.com/async/translate?client=chrome-b-d")
                        .header("content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                        .requestBody(url+msg +",id:1600782062451,qc:true,ac:true,_id:tw-async-translate,_pms:s,_fmt:pc")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .post()
                        .select("#tw-answ-target-text")
                        .text();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public String getUrl(int code) {
        switch(code) {
            case 1:
                return hanDic;

            case 2:
                return engDic;

            case 3:
                return hanToEng;

            case 4:
                return engToHan;
            default:
                Listener.send(room,"에러발생");
                return null;
        }
    }

    public String getTag(int code) {
        switch(code) {
            case 1:
                return "#searchPage_entry > div > div:nth-child(1) > ul > li > p";

            case 2:
                return "#searchPage_entry > div > div:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > p > span";

            case 3:

            case 4:
                return "body > div.container > div.frame > div.page.tlid-homepage.homepage.translate-text > div.homepage-content-wrap > div.tlid-source-target.main-header > div.source-target-row > div.tlid-results-container.results-container > div.tlid-result.result-dict-wrapper > div.result.tlid-copy-target > div.text-wrap.tlid-copy-target > div > span.tlid-translation.translation > span";
            default:
                Listener.send(room,"에러발생");
                return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.length()>30){
            Listener.send(room,msg+Answer.crossLine +Session.ReadAll+ s);
        }
        Listener.send(room,msg+Answer.crossLine + s);
    }
}


//영어사전 #searchPage_entry > div > div:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > p
//영어사전 #searchPage_entry > div > div:nth-child(1) > ul:nth-child(2) > li:nth-child(1)
//#searchPage_entry > div > div:nth-child(1) > ul > li > p
//한영 태그 body > div.container > div.frame > div.page.tlid-homepage.homepage.translate-text > div.homepage-content-wrap > div.tlid-source-target.main-header > div.source-target-row > div.tlid-results-container.results-container > div.tlid-result.result-dict-wrapper > div.result.tlid-copy-target > div.text-wrap.tlid-copy-target > div > span.tlid-translation.translation > span
//영한 태그 body > div.container > div.frame > div.page.tlid-homepage.homepage.translate-text > div.homepage-content-wrap > div.tlid-source-target.main-header > div.source-target-row > div.tlid-results-container.results-container > div.tlid-result.result-dict-wrapper > div.result.tlid-copy-target > div.text-wrap.tlid-copy-target > div > span.tlid-translation.translation > span
//번역 https://translate.google.com/?sxsrf=ALeKk02bAgknq3K3aEH2REolCcXGrjCDZg:1602671383370&gs_lcp=CgZwc3ktYWIQAzIHCAAQRhD_ATIECAAQQzICCAAyBAgAEB4yBAgAEB4yBAgAEB4yBAgAEB4yBggAEAUQHjIGCAAQBRAeMgYIABAFEB46BAgAEEc6BwgAEBQQhwI6BggAEAgQHjoGCAAQDRAeOggIABAIEA0QHlDIDljyPWCKP2gFcAN4AYABzwGIAf4OkgEGMC4xMS4xmAEAoAEBqgEHZ3dzLXdpesgBCMABAQ&uact=5&um=1&ie=UTF-8&hl=ko&client=tw-ob#ko/en/%EC%84%A4%EB%AA%85