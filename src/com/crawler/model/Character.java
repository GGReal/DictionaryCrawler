package com.crawler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LVY on 2015/10/22.
 */
public class Character {

    private String character;

    private String complicated;
    private String first_website;


    private String second_website;
    private String duyin;//¶ÁÒô
    private String pinyin;//Æ´Òô
    private String bushou;//²¿Ê×
    private String bihua;//±Ê»­Êı
    private String wubi86;//86Îå±Ê
    private String wubi98;//98Îå±Ê
    private String unicode;
    private String bishun;//±ÊË³
    private String sijiaoma;//ËÄ½ÇÂë
    private String cangjie;//²Öò¡Âë
    private String zhengma;//Ö£Âë
    private String introduction;//½éÉÜ

    public List<String> words;//´ÊÓï
    public List<String> details;
    public List<String> basic_meaning;


    public Character(String character, String pinyin,String website) {
        this.character = character;
        this.pinyin = pinyin;
        this.first_website = website;
        words = new ArrayList<String>();
        details = new ArrayList<String>();
        basic_meaning = new ArrayList<String>();
    }

    public String ListToString(List<String> list){
        String str="";
        int size = list.size();
        for (int i = 0; i < size; i++) {
            str+=list.get(i);
            if (i+1!=size){
                str+="_";
            }
        }
        return str;
    }

    public String getWordsString(){
        return ListToString(this.words);
    }

    public String getDetailsString(){
        return ListToString(this.details);
    }

    public String getBasicMeaningString(){
        return ListToString(this.basic_meaning);
    }

    public String getSecond_website() {
        return second_website;
    }

    public void setSecond_website(String second_website) {
        this.second_website = second_website;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    public String getDuyin() {
        return duyin;
    }

    public void setDuyin(String duyin) {
        this.duyin = duyin;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getComplicated() {
        return complicated;
    }

    public void setComplicated(String complicated) {
        this.complicated = complicated;
    }

    public String getFirst_website() {
        return first_website;
    }

    public void setFirst_website(String first_website) {
        this.first_website = first_website;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getBushou() {
        return bushou;
    }

    public void setBushou(String bushou) {
        this.bushou = bushou;
    }

    public String getBihua() {
        return bihua;
    }

    public void setBihua(String bihua) {
        this.bihua = bihua;
    }

    public String getWubi86() {
        return wubi86;
    }

    public void setWubi86(String wubi86) {
        this.wubi86 = wubi86;
    }

    public String getWubi98() {
        return wubi98;
    }

    public void setWubi98(String wubi98) {
        this.wubi98 = wubi98;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getBishun() {
        return bishun;
    }

    public void setBishun(String bishun) {
        this.bishun = bishun;
    }

    public String getSijiaoma() {
        return sijiaoma;
    }

    public void setSijiaoma(String sijiaoma) {
        this.sijiaoma = sijiaoma;
    }

    public String getCangjie() {
        return cangjie;
    }

    public void setCangjie(String cangjie) {
        this.cangjie = cangjie;
    }

    public String getZhengma() {
        return zhengma;
    }

    public void setZhengma(String zhengma) {
        this.zhengma = zhengma;
    }
}
