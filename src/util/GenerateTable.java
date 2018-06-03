package util;

import model.Data;

import java.util.ArrayList;

public class GenerateTable {
    public static void main(String [] args) {
        DB db = new DB();
        String course1Path = "src/dataset/train.csv";
        MyFile file1 = new MyFile(course1Path);
        ArrayList<Data> data1 = file1.myFileReader();
        db.createTable(data1);
//
//        String path="C:\\Users\\Cathleen\\Desktop\\test.csv";
//        MyFile myFile = new MyFile(path);
//        ArrayList<Data> data = myFile.myFileReader();
//        db.createTable(data);
    }
}
