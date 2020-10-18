package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;

public class MetalExchangeTask extends AsyncTask<Void, Void, Double> {

    String msg;
    String room;
    String selectValue = "#mc1 > table > tbody > tr:nth-child(3) > td:nth-child(2)";
    String num;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    int rate = 1146;
    String MetalUrl = "https://www.kores.net/komis/price/mineralprice/basemetals/pricetrend/baseMetals.do?mnrl_pc_mc_seq=";
    public MetalExchangeTask(String room, String msg) {
        this.msg = msg;
        this.room = room;
        num = getOptionValue(msg);
    }



    @Override
    protected Double doInBackground(Void... voids) {



        double value = 0;

        if(!num.equals("")) {
            try {
                SSLConnect ssl = new SSLConnect();
                ssl.postHttps(MetalUrl+num, 1000, 1000);
                Document document = Jsoup.connect(MetalUrl+num).get();
                Log.d("여기가진 됨",document.text());
                Elements elements = document.select(selectValue);
                Log.d("여기가진 되나",elements.text());
                value = Double.parseDouble(elements.text().replaceAll(",",""));


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return value;
    }



    //Element mySelect =
    //                      doc.getElementsByAttributeValue("name", "mySelect").get(0);
    //        String optionValueToBeSelected = "Option2";
    //        Elements options = mySelect.getElementsByTag("option");
    //        for (Element option : options) {
    //            if (option.attr("value").equals(optionValueToBeSelected)) {
    //                option.attr("selected", "selected");
    //            } else {
    //                option.removeAttr("selected");
    //            }
    //        }




    public String getOptionValue(String msg){

        switch (msg){
            case "니켈" :
                return "502"; //ton
            case "동" :
                return "503"; //ton
            case "우라늄" :
                return "509"; //lb
            case "철광석" :
                return "504"; //ton
            case "원유" :
                return "505"; //barrel
            case "유연탄" :
                return "510"; //ton
            case "리튬" :
                return "516"; //kg
            case "갈륨" :
                return "518"; //kg
            default:
                return "";
        }

    }

    @Override
    protected void onPostExecute(Double s) {
        super.onPostExecute(s);
        if(msg.equals("우라늄")) {
            Listener.send(room, "톤 당 " + decimalFormat.format(s * 2204 * rate) + "원");
        } else if (msg.equals("원유")) {
            Listener.send(room, "톤 당 " + decimalFormat.format(s * 7.33 * rate) + "원");
        } else if (msg.equals("리튬")||msg.equals("갈륨")) {
            Listener.send(room, "톤 당 " + decimalFormat.format(s * 1000 * rate) + "원");
        } else if (selectValue.equals("")) {
         Listener.send(room,"광물을 잘못 입력하셨습니다..");
        } else {
            Listener.send(room, "톤 당 "+decimalFormat.format(s * rate) + "원");
        }
    }
}



// 가격 기준 #mc1 > div.btn_excel_area > p
// 기준당 시세 #mc1 > table > tbody > tr:nth-child(3) > td:nth-child(2)

//public String searchAttribute(Element element, String str)
//{
//    Elements lists = element.select("[id=mc_seq]");
//
//    for( Element e : lists )
//    {
//        Elements result = e.select("option:contains(" + str + ")");
//
//        if( !result.isEmpty() )
//        {
//            return result.first().attr("value"); //.text();
//        }
//    }
//
//    return null;
//}