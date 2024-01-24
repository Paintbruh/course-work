package com.example.demo;

import com.example.demo.ExcelReader;
import com.example.demo.Transaction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Table {
    public Transaction[] table;
    public int prevId = 0;

    public Table() throws IOException, ParseException {
        ExcelReader er = new ExcelReader();
        String path = "input.xlsx";
        HashMap<Integer, List<Object>> excelData = er.read(path);
        this.table = new Transaction[excelData.size() - 1];
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        for (int i = 1; i < excelData.size(); ++i) {
            String agent = excelData.get(i).get(0).toString();
            Date date = formatForDateNow.parse(excelData.get(i).get(1).toString());
            String description = excelData.get(i).get(2).toString();
            String cat = excelData.get(i).get(3).toString();
            String tag = excelData.get(i).get(4).toString();
            int amount = (int)Float.parseFloat(excelData.get(i).get(5).toString());
            int id = (int)Float.parseFloat(excelData.get(i).get(6).toString());
            this.prevId = this.prevId > id ? this.prevId + 1 : id + 1;
            this.table[i - 1] = new Transaction(i, description, date, tag, cat, agent, amount);
        }
    }
    public int freeId() {
        return this.prevId + 1;
    }
    public void addRecord(int id, String description, Date date, String transactionTag, String transactionCategory, String agent, float amount) throws IOException {
        Transaction rec = new Transaction(id, description, date, transactionTag, transactionCategory, agent, amount);
        Transaction[] newTable = new Transaction[this.table.length + 1];
        for (int i = 0; i < this.table.length; i++) {
            newTable[i] = this.table[i];
        }

        newTable[this.table.length] = rec;
        this.table = newTable;
        this.prevId++;

        ExcelReader er = new ExcelReader();
        String path = "input.xlsx";
        er.write(path, this.table);
    }
    public int getLen() {
        return this.table.length;
    }
    public void deleteRecord(int id) throws IOException {
        Transaction[] newTable = new Transaction[this.table.length - 1];
        int i = 0;
        for (i = 0; this.table[i].getId() != id; i++) {
            newTable[i] = this.table[i];
        }
        i += 1;
        for (; i < this.table.length; i++) {
            newTable[i - 1] = this.table[i];
        }

        this.table = newTable;

        ExcelReader er = new ExcelReader();
        String path = "input.xlsx";
        er.write(path, this.table);
    }

    public void makeSubTable(String category, String start, String finish) throws IOException, ParseException {
        int count = 0;
        ExcelReader er = new ExcelReader();
        String path = "output.xlsx";
        if (start.equals("") || finish.equals("")) {

            for (int i = 0; i < this.table.length; ++i) {
                if (category.equals("") || (category.equals(this.table[i].getCategory()))) {
                    count++;
                }
            }
            Transaction[] subtable = new Transaction[count];
            count = 0;
            for (int i = 0; i < this.table.length; ++i) {
                if (((category.equals("")) || (category.equals(this.table[i].getCategory())))) {
                    subtable[count] = this.table[i];
                    count++;
                }
            }
            er.write(path, subtable);
        } else {
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");

            Date s = formatForDateNow.parse(start);
            Date f = formatForDateNow.parse(finish);
            for (int i = 0; i < this.table.length; ++i) {
                if (((category.equals("")) || (category.equals(this.table[i].getCategory()))) && (s.getTime() <= this.table[i].getDate().getTime()) && (f.getTime() >= this.table[i].getDate().getTime())) {
                    count++;
                }
            }

            Transaction[] subtable = new Transaction[count];
            count = 0;
            for (int i = 0; i < this.table.length; ++i) {
                if (((category.equals("")) || (category.equals(this.table[i].getCategory()))) && (s.getTime() <= this.table[i].getDate().getTime()) && (f.getTime() >= this.table[i].getDate().getTime())) {
                    subtable[count] = this.table[i];
                    count++;
                }
            }
            er.write(path, subtable);
        }
    }

    public int balance(String category, String start, String finish) throws ParseException {
        int sum = 0;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        if (start.equals("") || finish.equals("")) {
            for (int i = 0; i < this.table.length; ++i) {
                if ((category.equals("")) || (category.equals(this.table[i].getCategory()))) {
                    sum += this.table[i].getAmount();
                }
            }
        } else {
            Date s = formatForDateNow.parse(start);
            Date f = formatForDateNow.parse(finish);
            for (int i = 0; i < this.table.length; ++i) {
                if (((category.equals("")) || (category.equals(this.table[i].getCategory()))) && (s.getTime() <= this.table[i].getDate().getTime()) && (f.getTime() >= this.table[i].getDate().getTime())) {
                    sum += this.table[i].getAmount();
                }
            }
        }

        return sum;
    }

    public HashMap<String, Integer> stat() {
        HashSet<String> cats = new HashSet<String>();
        for (int i = 0; i < this.table.length; i++) {
            cats.add(this.table[i].getCategory());
        }
        HashMap<String, Integer> catAmounts = new HashMap<String, Integer>();
        for (String cat: cats) {
            catAmounts.put(cat, 0);
        }

        for (int i = 0; i < this.table.length; i++) {
            int value = catAmounts.get(this.table[i].getCategory()) + (int)this.table[i].getAmount();
            catAmounts.put(this.table[i].getCategory(), value);
        }
        return catAmounts;
    }
}












