package com.example.myapplication;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Answer {
    String[] triggerInfo;

    private static final int NEWARRAYCNT = 3;


    public Answer(String[] triggerInfo) {
        this.triggerInfo = triggerInfo;
    }

    public static String crossLine;
    public void trigger() {
        crossLine = "\n---------------------------\n";
        String[] temp;
        String[] msgCheckArray = {"", "", ""};

        if (triggerInfo[0].contains("/")) {
            if (triggerInfo[0].contains("/뉴스") || triggerInfo[0].contains("환율")) {
                temp = triggerInfo[0].split("\\s", NEWARRAYCNT);
                System.arraycopy(temp, 0, msgCheckArray, 0, temp.length);
                triggerInfo[0] = "";
            } else if (triggerInfo[0].endsWith("뒤") || triggerInfo[0].endsWith("후")) {
                temp = new String[]{"/일뒤", triggerInfo[0].replaceAll("[^0-9]", "").trim()};
                System.arraycopy(temp, 0, msgCheckArray, 0, temp.length);
                Log.d("시간후", msgCheckArray[0] + msgCheckArray[1]);
                triggerInfo[0] = "";
            } else if (triggerInfo[0].endsWith("전")) {
                msgCheckArray = new String[]{"/일전", triggerInfo[0].replaceAll("[^0-9]", "").trim()};
                triggerInfo[0] = "";
            } else {
                temp = triggerInfo[0].split("\\s", 2);
                System.arraycopy(temp, 0, msgCheckArray, 0, temp.length);
            }
        } else if (triggerInfo[0].contains("안녕하세요")) {
            temp = triggerInfo[0].split("\\s", 2);
            System.arraycopy(temp, 0, msgCheckArray, 0, temp.length);

            Log.d("시간후", msgCheckArray[0] + msgCheckArray[1]);
        } else if (triggerInfo[0].contains("이만")) {
            temp = triggerInfo[0].split("\\s", 2);
            System.arraycopy(temp, 0, msgCheckArray, 0, temp.length);
        }
            switch (msgCheckArray[0]) {
                case "/타이머":
                    if (msgCheckArray[1].equals("")) {
                        Listener.send(triggerInfo[2],"초를 입력해주세요.");
                    } else {
                        Listener.send(triggerInfo[2], triggerInfo[1] + "님의 명령으로 " + msgCheckArray[1].replace("초", "") + "초 타이머를 시작합니다.");
                        Timer(msgCheckArray[1].replace("초", ""));
                    }
                    break;
                case "/코로나":
                    getCOVID();
                    break;
                case "/안내":
                case "/도움말":
                case "/기능":
                case "/사용법":
                    Listener.send(triggerInfo[2],howToUse());
                    break;
                case "/낙낙":
                    Listener.send(triggerInfo[2],"낙지개구리는 낙낙하고 울지요");
                    break;
                case "/로또":
                    getLotto();
                    break;
                case "/쇼타임":
                    Listener.send(triggerInfo[2],UsaRoom());
                    break;
                case "/나무":
                    Listener.send(triggerInfo[2],"https://namu.wiki/w/" + msgCheckArray[1]);
                    break;
                case "/국어":
                    dic(msgCheckArray[1], 1);
                    break;
                case "/영어":
                    dic(msgCheckArray[1], 2);
                    break;
                case "/한영":
                    dic(msgCheckArray[1], 3);
                    break;
                case "/영한":
                    dic(msgCheckArray[1], 4);
                    break;
                case "/뉴스":
                    getNews(msgCheckArray[1], msgCheckArray[2]);
                    break;
                case "/환율":
                    getDebate(msgCheckArray[1],msgCheckArray[2]);
                    break;
                case "/시세" :
                    getMetal(triggerInfo[2],msgCheckArray[1]);
                    break;
                case "/단차":
                case "/10연차":
                    fsaf(msgCheckArray[0]);
                    break;
                case "안녕하세요" :
                    Listener.send(triggerInfo[2], triggerInfo[1]+"님 어서오세요");
                    break;
                case "/일뒤":
                case "/일전":
                    Log.d("실행전","어딨니");
                    if(Integer.parseInt(msgCheckArray[0])>0) {
                        getDateCnt(msgCheckArray[0], msgCheckArray[1]);
                    } else {
                        Listener.send(triggerInfo[2],"숫자가 없습니다.");
                    }
                    Log.d("실행여부", "실행됨");
                    break;
                case "/주제" :
                    DebateTask debateTask = new DebateTask(triggerInfo[2]);
                    debateTask.execute();
                    break;
                case "/애도" :
                    Listener.send(triggerInfo[2],"X를 눌러 Joy를 표하십시오");
                    break;

                default:
                    break;
            }
        }
    

    public void getCOVID() {
        COVIDTask getCOVIDtask = new COVIDTask(triggerInfo[2]);
        getCOVIDtask.execute();

    }

    public void getLotto() {
        LottoTask lottoTask = new LottoTask(triggerInfo[2]);
        lottoTask.execute();
    }

    public void getDebate(String msg, String cnt) {
        ExchangeTask exchangeTask = new ExchangeTask(triggerInfo[2],msg,cnt);
        exchangeTask.execute();
    }

    public void getMetal(String room,String msg) {
        MetalExchangeTask metalExchangeTask = new MetalExchangeTask(room,msg);
        metalExchangeTask.execute();
    }


    public void Timer(String msg) {

        int time = Integer.parseInt(msg);
        try {
            if (time <= 60) {
                Thread.sleep(time * 1000);
                Listener.send(triggerInfo[2],time + "초가 끝났습니다.");
            } else {
                Listener.send(triggerInfo[2],"너무 길어요");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String howToUse() {
        return "사용법 (전체보기 클릭)" + Session.ReadAll + crossLine +
                "[/타이머 n]\n타이머입니다. 초를 꼭 입력해주세요."
                + "\n[/낙낙]\n유행어1"
                + "\n[/코로나]\n오늘 기준 세계상황과 국내상황 통계"
                + "\n[/로또]\n금주 로또번호,총당첨금,당첨자수,1인당첨금액"
                + "\n[/뉴스 TAG]\nTAG 대분류 : 종합,시사,스포츠,연예,정치,경제,사회,세계,과학\n스포츠 소분류: 국내야구,해외야구,국내축구,해외축구,배구,농구\n태그 미 입력 시 전체순위에서 랜덤 링크"
                + "\n[/뉴스 TAG 순위]\n특정 태그의 뉴스 1~5위까지 링크 "
                + "\n[/쇼타임]\n미국방이란 무엇인가?"
                + "\n[/한영 blah blah],[/영한 blah blah]\n번역 : 구글번역입니당."
                + "\n[/나무 blah blah]\n빠르게 꺼무위키 켜기"
                + "\n[/n일뒤]\n지금으로부터 n일 뒤의 날짜"
                + "\n[/n일전]\n지금으로부터 n일 전의 날짜"
                + "\n[/주제]\n랜덤한 토론 주제"
                + "\n[/환율 나라이름 n]\n유로는 나라이름에 유로라고 적어주세요"
                + "\n[/시세 TAG]\nTAG 종류 : 니켈,동,우라늄,철광석,원유,유연탄,리튬,갈륨\n알루미늄,금,은"
                + "\n[/사용법],[/안내],[/기능],[/도움말]"
                + "\n\n\n\nMade by 두부조아 with 지대넓얕 톡방여러분";
    }

    public static String UsaRoom() {
        return "미국방의 기원은 다음과 같다." + crossLine +
                "원래 지대넓얕 방이 여기가 오리지널이었는데, \n" +
                "기존에 전방장님이 좀 강압적이셨어요. \n" +
                "친목을 싫어하셨는데 \n" +
                "그 친목의 범위가\n" +
                "\"오, 홍길동님 오랜만에 오셨네요.\"\n" +
                "이것도 친목이라고 경고를 받아서 \n" +
                "참다못한 부방장님(먼지님)이 '나를 따르라'하고 \n" +
                "방을 새로 파셨고, \n" +
                "18.13.13 그날\n" +
                "그 분을 따라 새로운 방이 생겼어요. \n" +
                "\n" +
                "그런데 그 후로 오리지널 방은 조용해졌고,\n" +
                "참다못한 전방장님이 방폭파를 선언\n" +
                "현방장님(끼룩끼룩님)께서 \n" +
                "\"그럼 제가 대신 이어받겠습니다\"\n" +
                "해서 방은 이어졌고요.\n" +
                "\n" +
                "두 군데 모두 있으신 분들이 많기 때문에 \n" +
                "여기를 영국방, 거기를 미국방이라는\n" +
                "닉네임으로 부르고 있답니다! \n" +
                "현재는 영국과 미국처럼 잘 지내고 있습니다\n" +
                "\n후훗" + "\n\n라고 캐롤님께서 말씀하셨습니다.";
    }



    public void dic(String msg, int code) {
        langTask langTask = new langTask(triggerInfo[2],msg, code);
        langTask.execute();
    }

    public void getNews(String msg, String secondMsg) {
        if (secondMsg.equals("순위")) {
            newsTask newsTask = new newsTask(triggerInfo[2],msg, true);
            newsTask.execute();
        } else{
            newsTask newsTask = new newsTask(triggerInfo[2],msg);
            newsTask.execute();
        }

    }

    public void getDateCnt(String direction, String num) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        if (direction.equals("/일뒤")) {
            Log.d("함수안", direction + "하고"+ num);
            cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(num));
            Listener.send(triggerInfo[2],"오늘로부터 " + num + "일 후 : " + sdf.format(cal.getTime()));
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(num));
            Listener.send(triggerInfo[2],"오늘로부터 " + num + "일 전 : " + sdf.format(cal.getTime()));
        }


    }
//    public static String getSwitchParam(String param) {
//
//    }


     public void fsaf(String howMuch) {
         String[] sibal = {"글레온","페루루","유크스","메니트","아무리네아"};
         String[] jang = {"불대장군","물대장군","목대장군","빛대장군","암대장군"};
        if(howMuch.equals("/단차")) {
            Random random = new Random();
            int next;
            int egg = random.nextInt(100)+1;
            if(egg>=1&&egg<=6) {
                next = random.nextInt(6)+1;
                if(next<6) {
                    egg = random.nextInt(5);
                    Listener.send(triggerInfo[2],sibal[egg]+"가 나왔어용");
                } else {
                    egg = random.nextInt(5);
                    Listener.send(triggerInfo[2],jang[egg]+"가 나왔어용");
                }
            } else {
                Listener.send(triggerInfo[2],"금알;");
            }
        } else {
            StringBuilder builder = new StringBuilder();

            Random random = new Random();
            int next;
            for (int i = 0; i < 10; i++) {
                int egg = random.nextInt(100)+1;
                if(egg>=1&&egg<=6) {
                    next = random.nextInt(6)+1;
                    if(next<6) {
                        egg = random.nextInt(5);
                        builder.append("\n"+sibal[egg]+"가 나왔어용");
                    } else {
                        egg = random.nextInt(5);
                        builder.append("\n"+jang[egg]+"가 나왔어용");
                    }
                } else {
                    builder.append("\n"+"금알;");
                }
            }

            Listener.send(triggerInfo[2],builder.toString());
        }

    }

}

