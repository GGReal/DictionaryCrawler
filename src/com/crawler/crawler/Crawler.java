package com.crawler.crawler;

import com.crawler.model.*;
import com.crawler.model.Character;
import com.crawler.tools.Constant;
import com.crawler.tools.Steper;
import javafx.util.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LVY on 2015/10/27.
 */
public class Crawler {

    private static final String basicUrl = "http://www.guoxuedashi.com";
    private List<Pair<String,String>> piyinList = new ArrayList<>();
    private List<Character> dictionary = new ArrayList<Character>();

    private static boolean threadStatus[];

    CloseableHttpClient httpClient;
    RequestConfig requestConfig;

    public Crawler(){
        httpClient = HttpClients.createDefault();
        requestConfig = RequestConfig.custom().setSocketTimeout(31000).setConnectTimeout(31000).build();
        threadStatus = new boolean[Constant.THREAD_NUM];
        for (int i = 0; i < Constant.THREAD_NUM ; i++) {
            threadStatus[i] = true;
        }
    }

    private Document connect(String website) throws Exception {
        HttpGet httpget = new HttpGet(website);
        httpget.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpget);
        Document document = null;
        HttpEntity entity = response.getEntity();
        if (entity != null){
            InputStream instream = entity.getContent();
            document = Jsoup.parse(instream,"utf-8","");
            instream.close();
        }
        response.close();
        return document;
    }

    public void crawlPinYin(){
        Document document = null;
        try {
            document = connect(basicUrl+"/zidian/");
            Elements elements = document.getElementsByClass("table2").get(0).getElementsByTag("tr");
            for(int i = 0; i< elements.size();i++){
                Element element = elements.get(i);
                Elements link_elements = element.getElementsByTag("a");
                for (int j = 0; j < link_elements.size(); j++) {
                    Element link = link_elements.get(j);
                    piyinList.add(new Pair(link.text(),basicUrl+link.attr("href")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(piyinList.size());
    }

    public void crawCharacter() {
        Document document = null;
        int counter = 0;
        try {
            for (int k = 0; k < piyinList.size(); k++) {
                Pair<String, String> piyin_pair = piyinList.get(k);
                document = connect(piyin_pair.getValue());
                //System.out.println(document.toString());
                Elements elements = document.getElementsByClass("info_txt2").get(0).getElementsByTag("a");
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    String link = element.attr("href");
                    System.out.println(counter + "\t"+piyin_pair.getKey()+"\t"+element.text()+"\t"+link);
                    Character character = new Character(element.text(), piyin_pair.getKey(), link);
                    dictionary.add(character);
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("size:\t"+dictionary.size());
    }

    public void crawlCharcaterInfo(){
        Steper.Reset(0, -1);
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            final int index = i;
            Thread thread = new Thread(){
                @Override
                public void run(){
                    int start = Steper.getNext(0);
                    if (start>= dictionary.size()) return;
                    Character character = dictionary.get(start);
                    try {
                        System.out.println("Thread "+index+": \t start crawling the "+start);
                        Document document = connect(character.getFirst_website());
                        if (document!=null){
                            parseCharacterInfo(document.getElementsByClass("zui").get(0), character);
                            parseIntro(document.getElementsByClass("info_txt2"), character);
                        }
                        Document document2 = connect(character.getSecond_website());
                        if (document2!=null){
                            parseDetailInfo(character,document.getElementsByClass("info_txt2"));
                            parseWords(character,document2.getElementsByClass("info_cate"));
                        }
                        System.out.println("Thread "+index+": \t finishing crawling the "+start);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            thread.start();
        }

    }

//    Æ´Òô£º¨¡i
//    ²¿Ê×£ºÍÁ	    ËÄ½ÇÂë£º43184	   ²Öò¡£ºGIOK
//    86Îå±Ê£ºfctd	98Îå±Ê£ºfctd	   Ö£Âë£ºBZMA
//    Unicode£º57C3	×Ü±Ê»­Êý£º10	   ±ÊË³£º1215431134

    public void parseCharacterInfo(Element table,Character character){
        if (table != null){
            Elements elements = table.getElementsByTag("table").get(0).getElementsByTag("td");
            String context[] = new String[10];
            for (int i = 0; i < elements.size(); i++) {
                String text = elements.get(i).text();
                context[i]=text.split("£º")[1];
            }
            character.setDuyin(context[0]);
            character.setBushou(context[1]);
            character.setSijiaoma(context[2]);
            character.setCangjie(context[3]);
            character.setWubi86(context[4]);
            character.setWubi98(context[5]);
            character.setZhengma(context[6]);
            character.setUnicode(context[7]);
            character.setBihua(context[8]);
            character.setBishun(context[9]);
        }
    }

    public void parseIntro(Elements div_element,Character character){
        try {
            if (div_element!=null && div_element.size()>0) {
                Elements elements = div_element.get(0).getElementsByTag("p");
                if (elements!=null && elements.size()>0){
                    String[] strs = elements.get(0).toString().substring(3).split("<br />");
                    if (strs.length>1) {
                        for (int i = 0; i < strs.length-1 ; i++) {
                            character.basic_meaning.add(strs[i]);
                        }
                    }
                    Elements link_elements = elements.get(0).getElementsByTag("a");
                    if(link_elements!=null){
                        int index = 0;
                        if (link_elements.size()>1){
                            index = 1;
                        }
                        character.setSecond_website(link_elements.get(index).attr("href"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parseDetailInfo(Character character, Elements divs){
        try {
//            Document document = connect("http://www.guoxuedashi.com/hydcd/94334n.html");
//            Elements divs = document.getElementsByClass("info_txt2");
            System.out.println(divs.get(0).text());
            if (divs!=null && divs.size()>0){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseWords(Character character,Elements divs){
        try {
//            Document document = connect("http://www.guoxuedashi.com/hydcd/516467d.html");
//            Elements divs = document.getElementsByClass("info_cate");
            if (divs!=null && divs.size()>0){
                Elements dds = divs.get(0).getElementsByTag("dd");
                if (dds!=null && dds.size()>0){
                    for (int i = 0; i < dds.size() ; i++) {
                        character.words.add(dds.get(i).text());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile(Character character){
        File file = new File(Constant.SAVE_PATH+character.getCharacter()+".txt");
        try {
            FileWriter fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
