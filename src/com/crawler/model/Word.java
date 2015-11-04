package com.crawler.model;

/**
 * Created by LVY on 2015/10/27.
 */
public class Word {
    private String word;
    private String paraphrase;

    public Word(String word, String paraphrase){
        this.word = word;
        this.paraphrase = paraphrase;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    public void setParaphrase(String paraphrase) {
        this.paraphrase = paraphrase;
    }
}
