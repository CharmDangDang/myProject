package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class CommandSetup {
    Context context;
    String key;
    String value;
    private final static String DEFAULT_STRING = "";
    private final static String PREF_NAME = "TalkCommand";   //db이름

    public CommandSetup(Context context,String key,String value) {
        this.context = context;
        this.key = key;
        this.value = value;
    }

    //먼저 변수를 선언
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    //키를 모두 불러와서 출력
    public String getCommands() {
        getPrefs();
        sharedPref.getString(key,DEFAULT_STRING).equals(DEFAULT_STRING);
        sharedPref.getAll();
        StringBuilder sb = null;
        Set<String> keys = sharedPref.getAll().keySet();
        for (String k : keys) {
            sb.append(k).append("\n");
        }
        return sb.toString();
    }

    //해당 키에 밸류가 설정되어있지 않으면 저장
    public String putCommand(){
        getPrefs();
        if(sharedPref.getString(key,DEFAULT_STRING).equals(DEFAULT_STRING)) {
            editor.putString(key, value);
            editor.apply();
            return "정상적으로 추가되었습니다.";
        } else {
            return "이미 사용중인 커맨드입니다.";
        }
    }

    //해당 키에 밸류가 설정되어 있으면 저장
    public String modifyCommand(){
        getPrefs();
        if(!sharedPref.getString(key,DEFAULT_STRING).equals(DEFAULT_STRING)) {
            editor.putString(key,value);
            editor.apply();
        return "정상적으로 추가되었습니다.";
        }  else {
            return "없는 커맨드입니다.";
        }

    }
    //해당 키에 밸류가 설정되어 있으면 삭제
    public String deleteCommand(){
        getPrefs();
        if(!sharedPref.getString(key,DEFAULT_STRING).equals(DEFAULT_STRING)) {
            editor.remove(key);
            editor.apply();
            return "정상적으로 추가되었습니다.";
        } else {
            return "없는 커맨드입니다.";
        }
    }


    //메소드마다 쓰이는 코드라서 따로 작성(근데 suppresslint가 뜬다)
    @SuppressLint("CommitPrefEdits")
    public void getPrefs() {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }



}
