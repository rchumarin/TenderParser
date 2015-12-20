package ru.icl.parser.entity;

import java.io.Serializable;

//поиск тендеров
public class Tender implements Serializable {
    private int id;
    private int idTender;
    private String nameCompany;
    private String nameTender;
    private int cost;
    private String deadline;
    private String keyword;
//    private Email email;

    public Tender() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdTender() {
        return idTender;
    }
    public void setIdTender(int idTender) {
        this.idTender = idTender;
    }

    public String getNameCompany() {
        return nameCompany;
    }
    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getNameTender() {
        return nameTender;
    }
    public void setNameTender(String nameTender) {
        this.nameTender = nameTender;
    }

    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
       
}
