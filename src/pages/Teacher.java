package pages;

import algorithm.Kfolder;
import algorithm.Prediction;
import model.Data;
import model.Strings;
import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;
import util.DB;
import util.MyFile;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Teacher {
    private String tid;
    Strings tableNames;
    private Connection conn;
    DB db;

    public Teacher(String tid) {
        db = new DB();
        conn = db.initDB();
        tableNames = new Strings();
        this.tid = tid;
    }

    public  JSONObject importPredictionData(String cid) {
        Kfolder kfolder = new Kfolder();
        String predictionPath="src/dataset/test.csv";
        Map<String,ArrayList<String>> map =  kfolder.C45Tree(predictionPath);
        JSONObject jsonObject = JSONObject.fromObject(map);
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static void main(String[] args) {
        Teacher t = new Teacher("jiaoshi");
        String cid = "HarvardX/PH278x/2013_Spring";
        t.importPredictionData(cid);
    }

}


