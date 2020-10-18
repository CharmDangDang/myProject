package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MetalExchangeTask extends AsyncTask<Void, Void, Double> {

    String msg;
    String room;

    String selectValue = "#mc1 > table > tbody > tr:nth-child(3) > td:nth-child(2)";
    boolean others = false;
    String num;
    DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
    double troyRate = 32.1507;
    double donRate = 0.1205653;
    int usdRate = 1146;
    HashMap<String,String> metals = new HashMap<>();
    String MetalUrl = "https://www.kores.net/komis/price/mineralprice/basemetals/pricetrend/baseMetals.do?mnrl_pc_mc_seq=";

    public MetalExchangeTask(String room, String msg) {
        this.msg = msg.trim();
        this.room = room;
        num = getOptionValue(msg);
    }


    @Override
    protected Double doInBackground(Void... voids) {


        double value = 0;

        if (others) {
            MetalUrl = "https://www.lme.com/";
            try {
                SSLConnect ssl = new SSLConnect();
                ssl.postHttps(MetalUrl, 1000, 1000);
                Document document = Jsoup.connect(MetalUrl).get();
                Elements elements = document.select("body > div > div.content-container > div > div.col-sm-6.col-md-12.col-lg-4 > table > tbody > tr:nth-child(" + num + ") > td");
                value = Double.parseDouble(elements.text().replaceAll(",", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (msg.equals("팔라듐")) {
            MetalUrl = "https://www.lme.com/Metals/Precious-metals/Palladium";
            try {
                Document document = Jsoup.connect(MetalUrl).get();
                Elements elements = document.select(num);
                value = Double.parseDouble(elements.text().trim());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                SSLConnect ssl = new SSLConnect();
                ssl.postHttps(MetalUrl + num, 1000, 1000);
                Document document = Jsoup.connect(MetalUrl + num).get();
                Elements elements = document.select(selectValue);
                value = Double.parseDouble(elements.text().replaceAll(",", ""));

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
    //        }body > div > div.content-container > div > div.col-sm-6.col-md-12.col-lg-4 > table > tbody > tr:nth-child(10) > td


    public String getOptionValue(String msg) {

        switch (msg) {
            case "니켈":
                return "502"; //tonne
            case "동":
                return "503"; //tonne
            case "우라늄":
                return "509"; //lb
            case "철광석":
                return "504"; //tonne
            case "원유":
                return "505"; //barrel
            case "유연탄":
                return "510"; //tonne
            case "리튬":
                return "516"; //kg
            case "갈륨":
                return "518"; //kg
            case "팔라듐":
                return "#module-60 > table > tbody > tr:nth-child(1) > td:nth-child(2)";
            case "알루미늄":
                others = true;  //tonne
                return "1";
            case "아연":
                others = true; //tonne
                return "3";
            case "납":
                others = true; //tonne
                return "5";
            case "주석":
                others = true; //tonne
                return "6";
            case "알루미늄합금":
                others = true; //tonne
                return "7";
            case "코발트":
                others = true; //tonne
                return "9";
            case "금":
                others = true;
                return "10";
            case "은":
                others = true;
                return "11";
            default:
                return "";
        }

    }

    @Override
    protected void onPostExecute(Double s) {
        super.onPostExecute(s);
        if (msg.equals("우라늄")) {
            Listener.send(room, "1kg 당 " + decimalFormat.format(s * 2.205 * usdRate) + "원");
        } else if (msg.equals("원유")) {
            Listener.send(room, "1리터 당 " + decimalFormat.format(s / 159 * usdRate) + "원");
        } else if (msg.equals("리튬") || msg.equals("갈륨")) {
            Listener.send(room, "1kg 당 " + decimalFormat.format(s * usdRate) + "원");
        } else if (msg.equals("금") || msg.equals("은")) {
            Listener.send(room, "한 돈 당 " + decimalFormat.format(s / 31.1033 * usdRate * 3.75) + "원");
        } else if (msg.equals("")) {
            Listener.send(room, msg + "은(는) 전설의 광물.");
        } else {
            Listener.send(room, "1kg 당 " + decimalFormat.format(s / 1000 * usdRate) + "원");
        }
    }


    public void metalMapMake(){
        metals.put("니켈","502");
        metals.put("동","503");
        metals.put("우라늄","509");
        metals.put("철광석","504");
        metals.put("원유","505");
        metals.put("유연탄","510");
        metals.put("리튬","516");
        metals.put("갈륨","518");
        metals.put("팔라듐","#module-60 > table > tbody > tr:nth-child(1) > td:nth-child(2)");
        metals.put("알루미늄","502");
        metals.put("납","1");
        metals.put("아연","502");
        metals.put("주석","502");
        metals.put("알루미늄합금","502");
        metals.put("코발트","502");
        metals.put("금","502");
        metals.put("은","502");
    }


}


// 가격 기준 #mc1 > div.btn_excel_area > p
// 기준당 시세 #mc1 > table > tbody > tr:nth-child(3) > td:nth-child(2)body > div > div.content-container > div > div.col-sm-6.col-md-12.col-lg-4 > table > tbody > tr:nth-child(1) > td

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


//body > div > div.content-container > div > div.col-sm-6.col-md-12.col-lg-4 > table > tbody > tr:nth-child(10) > td  금
//body > div > div.content-container > div > div.col-sm-6.col-md-12.col-lg-4 > table > tbody > tr:nth-child(11) > td  은