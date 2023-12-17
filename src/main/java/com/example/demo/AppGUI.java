package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppGUI extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Form Sample");

        Parent root = FXMLLoader.load(getClass().getResource("Form.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, ParseException {
        Table table = new Table();
        Date date = new Date();
        //table.addRecord(table.freeId(), "description", date, "tag", "cat", "agent", 100500);
        table.makeSubTable("cat0", "пт 2023.12.14 04:11:11 PM MSK", "пт 2023.12.17 04:11:11 PM MSK");
        System.out.println(table.balance("cat0", "пт 2023.12.14 04:11:11 PM MSK", "пт 2023.12.17 04:11:11 PM MSK"));
        HashMap<String, Integer> st = table.stat();
        int sumValue = 0;
        for(Map.Entry<String, Integer> item : st.entrySet()){
            sumValue += item.getValue();
        }

        for(Map.Entry<String, Integer> item : st.entrySet()){
            System.out.printf("Key: %s  Value: %s Percents: %s \n", item.getKey(), item.getValue(), (float)item.getValue() * 100 / sumValue);
        }
        launch();
    }

}
