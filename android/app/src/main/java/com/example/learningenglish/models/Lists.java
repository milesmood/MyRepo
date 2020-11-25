package com.example.learningenglish.models;

public class Lists {
    private String id_list;
    private String listName;
    private String usrId;

    public Lists() {
    }

    public Lists(String listName, String usrId) {
        this.listName = listName;
        this.usrId = usrId;
    }

    public String getId_list() {
        return id_list;
    }

    public void setId_list(String id_list) {
        this.id_list = id_list;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }
}
