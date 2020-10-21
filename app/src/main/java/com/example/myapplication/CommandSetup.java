package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class CommandSetup {
    Context context;
    String key;
    String value;
    String temp_room;
    private final static String DEFAULT_STRING = "";
    public String PREF_NAME;   //db이름

    public CommandSetup(Context context, String key, String value, String room) {
        this.context = context;
        this.key = key;
        this.value = value;
        temp_room = room;
        if(temp_room.equals("끼룩끼룩")) {
            PREF_NAME = "지대넓얕영국방";
        } else {
            PREF_NAME = room;
        }
    }

    //먼저 변수를 선언
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    //키를 모두 불러와서 출력
    public void getCommands() {
        getPrefs();
        sharedPref.getAll();
        String msg = "목록" + Answer.crossLine;
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        Set<String> keys = sharedPref.getAll().keySet();
        if(keys.size()>10) {
            sb.append(Session.ReadAll);
        }
        for (String k : keys) {
            sb.append(k).append("\n");
        }
        if(temp_room.equals("끼룩끼룩")){
            Listener.send(temp_room, String.valueOf(sb));
        } else {
            Listener.send(PREF_NAME, String.valueOf(sb));
        }
    }

    //해당 키에 밸류가 설정되어있지 않으면 저장
    public void putCommand() {
        getPrefs();
        if (sharedPref.getString(key, DEFAULT_STRING).equals(DEFAULT_STRING) && !value.equals("")) {
            editor.putString(key, value);
            editor.apply();
            if (temp_room.equals("끼룩끼룩")) {
                Listener.send(temp_room, "정상적으로 추가되었습니다.");
            } else {
                Listener.send(PREF_NAME, "정상적으로 추가되었습니다.");
            }
        } else {
            if (temp_room.equals("끼룩끼룩")) {
                Listener.send(temp_room, "이미 있는 커맨드입니다.");
            } else {
                Listener.send(PREF_NAME, "이미 있는 커맨드입니다.");
            }
        }
    }

    //해당 키에 밸류가 설정되어 있으면 저장
    public void modifyCommand() {
        getPrefs();
        if (!sharedPref.getString(key, DEFAULT_STRING).equals(DEFAULT_STRING) && !value.equals("")) {
            editor.putString(key, value);
            editor.apply();
            if (temp_room.equals("끼룩끼룩")) {
                Listener.send(temp_room, "정상적으로 추가되었습니다.");
            } else {
                Listener.send(PREF_NAME, "정상적으로 추가되었습니다.");
            }
        } else {
            if (temp_room.equals("끼룩끼룩")) {
                Listener.send(temp_room, "없는 커맨드입니다.");
            } else {
                Listener.send(PREF_NAME, "없는 커맨드입니다.");
            }

        }
    }

    //해당 키에 밸류가 설정되어 있으면 삭제
    public void deleteCommand() {
        getPrefs();
        if (sharedPref.contains(key)) {
            Log.d("키있냐",key);
            editor.remove(key);
            editor.apply();
            if(temp_room.equals("끼룩끼룩")){
                Listener.send(temp_room,"정상적으로 삭제되었습니다.");
            } else {
                Listener.send(PREF_NAME,"정상적으로 삭제되었습니다.");
            }
        } else {
            if (temp_room.equals("끼룩끼룩")) {
                Listener.send(temp_room, "없는 커맨드입니다.");
            } else {
                Listener.send(PREF_NAME, "없는 커맨드입니다.");
            }

        }
    }


    //메소드마다 쓰이는 코드라서 따로 작성(근데 suppresslint가 뜬다)
    @SuppressLint("CommitPrefEdits")
    public void getPrefs() {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }


}
