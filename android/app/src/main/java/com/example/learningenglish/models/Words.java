package com.example.learningenglish.models;

public class Words {
    private String id_word;
    private String engWord;
    private String rusWord;

    public Words() {
    }

    public Words(String engWord, String rusWord) {
        this.engWord = engWord;
        this.rusWord = rusWord;
    }

    public String getId_word() {
        return id_word;
    }

    public void setId_word(String id_word) {
        this.id_word = id_word;
    }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public String getRusWord() {
        return rusWord;
    }

    public void setRusWord(String rusWord) {
        this.rusWord = rusWord;
    }
}
