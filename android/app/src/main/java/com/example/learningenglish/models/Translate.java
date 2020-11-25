package com.example.learningenglish.models;

public class Translate {
    private long id;
    private String rusWord;
    private String engWord;

    public Translate() {
    }

    public Translate(String rusWord, String engWord) {
        this.rusWord = rusWord;
        this.engWord = engWord;
    }

    public String getRusWord() {
        return rusWord;
    }

    public void setRusWord(String rusWord) {
        this.rusWord = rusWord;
    }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }
}
