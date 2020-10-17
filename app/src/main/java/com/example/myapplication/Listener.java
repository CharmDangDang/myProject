package com.example.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class Listener extends NotificationListenerService {
    static Context context;
    private static final String KAKAOTALK_PACKAGE = "com.kakao.talk";
    private static ArrayList<Session> sessions = new ArrayList<>();


    private Type.Message mMessage;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (sbn.getPackageName().equals(KAKAOTALK_PACKAGE)) {
            Notification.WearableExtender wearableExtender = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wearableExtender.getActions()) {
                if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                    String title = sbn.getNotification().extras.getString("android.title");
                    Object index = sbn.getNotification().extras.get("android.text");
                    Type.Message message = parsingMessage(title, index);
                    context = getApplicationContext();
                    Session session = new Session();
                    session.session = act;
                    session.message = sbn.getNotification().extras.get("android.text").toString();// 카카오톡 내용(롤리팝 이하 단톡에서는 이름 + 내용)
                    session.sender = sbn.getNotification().extras.getString("android.title");// 카카오톡 보낸 사람 이름(롤리팝 이하 단톡에서는 단톡방 이름)
                    String room = (String) act.title;
                    session.room  = room.substring(4,room.length()-1);

                    //종합해서 로그찍기
                    sessions.add(session);
                    Log.i("asd", "message : " + session.message + " sender : " + session.sender + " room : " + session.room + " session : " + session);

                    //여기 아래에 알림 내용에 따라 다른 send()를 호출하면 됨
                    if (session.message.charAt(0) == '/') {
                        String[] msg = {session.message, session.sender, session.room};
                        Answer answer = new Answer(msg);
                        answer.trigger();
                    }



                }
                stopSelf();
            }
        }
    }
    //메세지를 보내는 함수, 원리는 원작자도 모름..
    //send(문자열); 로 작동함
    public static void send(String room ,String message) throws IllegalArgumentException {
        Notification.Action session = null;

        for (Session i : sessions) {
            if (i.room.equals(room)) {
                session = i.session;

                break;
            }
        }

        if (session == null) {
            throw new IllegalArgumentException("Can't find the room");
        }

        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.getRemoteInputs())
            msg.putCharSequence(inputable.getResultKey(), message);

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);
        Log.d("궁금증 해결", Arrays.toString(session.getRemoteInputs()) +"겟인풋"+sendIntent+msg);


        try {
            session.actionIntent.send(context, 0, sendIntent);
            Log.i("send() complete",message);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<Session> getSessions() {
        return sessions;
    }

    private Type.Message parsingMessage(String title, Object index) {
        Type.Message result = new Type.Message();
        result.room = title;

        if (index instanceof String) {
            result.sender = title;
            result.message = (String) index;
        } else {
            String html = Html.toHtml((SpannableString) index);
            result.sender = Html.fromHtml(html.split("<b>")[1].split("</b>")[0]).toString();
            result.message = Html.fromHtml(html.split("</b>")[1].split("</p>")[0].substring(1)).toString();
        }

        return result;
    }

    /*public static void send(String room, String message) throws IllegalArgumentException { // @author ManDongI
        Notification.Action session = null;

        for(Session i : sessions) {
            if(i.room.equals(room)) {
                session = i.session;

                break;
            }
        }

        if(session == null) {
            throw new IllegalArgumentException("Can't find the room");
        }

        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.getRemoteInputs()) msg.putCharSequence(inputable.getResultKey(), message);
        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);

        try {
            session.actionIntent.send(context, 0, sendIntent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }*/
}
