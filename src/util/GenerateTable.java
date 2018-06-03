package util;

import model.Data;

import java.util.ArrayList;

public class GenerateTable {
    public static void main(String [] args) {
        DB db = new DB();
//        String coursePath = "C:\\Users\\Cathleen\\Desktop\\测试生成数据库.csv";
        String coursePath = "C:\\Users\\Cathleen\\Desktop\\train.csv";
        MyFile file = new MyFile(coursePath);
        ArrayList<Data> data = file.myFileReader();
        db.initDB();
        db.createTable(data);
    }
}
