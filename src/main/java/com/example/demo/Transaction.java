package com.example.demo;

import java.util.Date;

public class Transaction {
    private int id;
    private String description;
    private Date date;
    private String transactionTag;
    private String transactionCategory;
    private String agent;
    private float amount;

    public Transaction(int id, String description, Date date, String transactionTag, String transactionCategory, String agent, float amount) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.transactionTag = transactionTag;
        this.transactionCategory = transactionCategory;
        this.agent = agent;
        this.amount = amount;
    }
    public int getId() {
        return this.id;
    }
    public String getDescription() {
        return this.description;
    }
    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public Date getDate() {
        return this.date;
    }
    public void changeDate(Date newDate) {
        this.date = newDate;
    }

    public String getTag() {
        return this.transactionTag;
    }
    public void changeTag(String newTag) {
        this.transactionTag = newTag;
    }

    public String  getCategory() {
        return this.transactionCategory;
    }
    public void changeCategory(String newCategory) {
        this.transactionCategory = newCategory;
    }

    public String  getAgent() {
        return this.agent;
    }
    public void changeAgent(String newAgent) {
        this.agent = newAgent;
    }

    public float  getAmount() {
        return this.amount;
    }
    public void changeAmount(float newAmount) {
        this.amount = newAmount;
    }

}
