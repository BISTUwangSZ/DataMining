package pages;

import algorithm.Kfolder;
import algorithm.Prediction;
import model.Data;
import model.Strings;
import net.sf.json.JSONArray;
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

    //页面1:

    /**
     * 获得课程列表(已完成）
     *
     * @return （课程索引（课程号，选课人数，证书人数））
     */
    public JSONObject getCourseList() {
        JSONObject courses = new JSONObject();
        //创建视图
        String sql = "select cid c,count(*)," +
                " (SELECT count(*) from " + tableNames.originDataTableName + " where certified='yes' and cid=c) certifiedCount " +
                " from " + tableNames.originDataTableName + " where cid in(select cid from teacher where tid = '" + tid + "' ) group by cid";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ArrayList<Integer> course = new ArrayList<>();
                course.add(rs.getInt(2));
                course.add(rs.getInt(3));
                courses.put(rs.getString(1), course);
            }
            String path = "web/json/teacher/" + tid + "_courseList.json";
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            outputStream.write(courses.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }

    //页面2 图1  散点四合一图 （已完成）
    /**
     * 获得某一门课程的成绩和交互情况的关系（有用）
     *
     * @param cid
     * @return
     */
    public JSONObject getInteractionAndGrade(String cid) {
        String sql = "select grade,nevents,nplay_videos,nchapters,nforum_posts,uid from " + tableNames.originDataTableName +
                " where cid ='" + cid + "' and grade != 'null'";
        JSONObject attrs = new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            String[] keys = new String[]{"成绩", "交互次数", "观看视频数", "学习章节数", "论坛发帖数","学号"};
            ArrayList<Double> list0 = new ArrayList<>();
            ArrayList<Double> list1 = new ArrayList<>();
            ArrayList<Double> list2 = new ArrayList<>();
            ArrayList<Double> list3 = new ArrayList<>();
            ArrayList<Double> list4 = new ArrayList<>();
            ArrayList<String> list5 = new ArrayList<>();
            while (rs.next()) {
                list0.add(rs.getDouble(1));
                list1.add(rs.getDouble(2));
                list2.add(rs.getDouble(3));
                list3.add(rs.getDouble(4));
                list4.add(rs.getDouble(5));
                list5.add(rs.getString(6));
            }
            attrs.put(keys[0], list0);
            attrs.put(keys[1], list1);
            attrs.put(keys[2], list2);
            attrs.put(keys[3], list3);
            attrs.put(keys[4], list4);
            attrs.put(keys[5], list5);
            String path = "web/json/teacher/" + cid.replace("/", "-") + "_gradeWithInteraction.json";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            out.write(attrs.toString().getBytes());
            out.flush();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attrs;
    }

    //页面3 图2

    /**
     * 获得某一门课程的学习者地区展示
     *
     * @param cid 待查询课程号
     * @return json对象(关键字, 数量)
     */
    public JSONObject getLocation(String cid) {
        String sql = "SELECT location,COUNT(*) FROM " + tableNames.originDataTableName + " WHERE cid = '" + cid + "' and location != 'Unknown/Other' GROUP BY location ORDER BY count(*) DESC LIMIT 30 ";
        JSONObject json = new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String key = rs.getString(1);
                if (key.equals("NA")) {
                    key = "未填写";
                }
                json.put(key, rs.getInt(2));
            }
            String newCid = cid.replace("/", "-");
            String path = "web/json/teacher/" + newCid + "_location.json";
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
            bufferedOutputStream.write(json.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    //页面2 图3,4,5

    /**
     * 获得学历和取得证书情况的关系
     *
     * @param cid  课程号
     * @param kind 查询属性：learner_level,age,gender
     * @return 学历（该学历总人数，该学历取得证书人数）
     */
    public JSONObject attributeWithCertified(String cid, String kind) {
        JSONObject attrs = new JSONObject();
        String attribute = "select " + kind + ", count(*) from " + tableNames.classfierTableName + " where cid='" + cid + "' group by " + kind +" order by " + kind;
        String certified = "select " + kind + ", count(*) from " + tableNames.classfierTableName + " where cid = '" + cid + "' and certified = 'yes' group by " + kind +" order by " + kind;;
        JSONObject attributeJson = getNumberOfAttributes(attribute);
        JSONObject certifiedJson = getNumberOfAttributes(certified);

        for (Object obj : attributeJson.keySet()) {
            ArrayList attr = new ArrayList();
            attr.add(attributeJson.get(obj));
            if(certifiedJson.get(obj)==null){
                attr.add(0);
            } else {
                attr.add(certifiedJson.get(obj));
            }
            double percent = Double.parseDouble(String.valueOf(attr.get(1))) / Double.parseDouble(String.valueOf(attr.get(0)));
            double d1 = percent*10000;
            int d2 = (int)d1;
            double d3 = (double)d2/100;
            String per = String.valueOf(d3);
            attr.add(per);
            attrs.put(toMyString(obj), attr);
        }
        String path = "web/json/teacher/" + cid.replace("/", "-") + "_" + kind + ".json";
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(path));
            outputStream.write(attrs.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attrs;
    }

    public String toMyString(Object obj){
        String str = (String)obj;
        String result="";
        switch (str){
            case "female":result="男";break;
            case "male" : result="女";break;
            case "0Less than Secondary":result="中学以下";break;
            case "1Secondary":result="中学";break;
            case "2Bachelor":result="本科";break;
            case "3Master":result="硕士";break;
            case "4Doctorate":result="博士";break;
            case "5empty":
            case "empty":
                result="未填写";break;
            default:result=str;
        }
        return result;
    }

    //页面2，图5，成绩
    public JSONArray getGrade(String cid){
        JSONArray json = null;
        String sql = "select grade,count(*) from " + tableNames.originDataTableName + " where cid = '" + cid
                +"'and grade != 'null' group by grade";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ArrayList<double[]> grade = new ArrayList<>();
            while (rs.next()){
                double[] d = new double[2];
                d[0] = rs.getDouble(1);
                d[1] = rs.getInt(2);
                grade.add(d);
            }
            json = JSONArray.fromObject(grade);
            String path = "web/json/teacher/" + cid.replace("/","-") + "_grade.json";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            out.write(json.toString().getBytes());
            out.flush();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    //页面2，图6,7,8,9
    /**
     * 查询某一门课程交互次数和人数的记录:页面2:，图6,7,8,9（折线图，横轴次数，纵轴人数）
     *
     * @param kind 查询类型：nevents,nplay_videos,nchapters,nforum_posts
     * @param cid 课程号
     * @return 课程（次数：人数）
     */
    public JSONObject getInteractionWithNumber(String cid, String kind) {
        JSONObject course = new JSONObject();
        String nevents = "select " + kind + ",count(*) from " + tableNames.originDataTableName + " where cid = '" + cid + "' GROUP BY " + kind;
        try {
            PreparedStatement pst = conn.prepareStatement(nevents);
            ResultSet rs = pst.executeQuery();
            ArrayList<Integer> times = new ArrayList<>();
            ArrayList<Integer> numbers = new ArrayList<>();
            while (rs.next()) {
                times.add(rs.getInt(1));
                numbers.add(rs.getInt(2));
            }
            course.put("times",times);
            course.put("numbers",numbers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String path = "web/json/teacher/"+cid.replace("/","-")+ "_"+ kind+"_interactionOfNumber.json";
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
            bufferedOutputStream.write(course.toString().getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return course;
    }

    //页面3
    /**
     * 查看某一门课程的选课学生列表
     *
     * @param cid 课程Id
     * @return 学生信息
     */
//    public JSONObject getStudentList(String cid, int pageNo, int pageSize) {
//        JSONObject students = new JSONObject();
//        String sql = "SELECT uid, location, learner_level,age, gender, grade, start_time, last_time, nevents, ndays, nplay_videos, nchapters, " +
//                "nforum_posts, certified FROM " + tableNames.originDataTableName + " WHERE cid ='" + cid + "' limit ?,?";
//        String newCid = cid.replace("/", "-");
//        try {
//            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setInt(1, (pageNo - 1) * pageSize);//距离这一页的第一行数据，其前面有多少行数据
//            pst.setInt(2, pageSize);//每页有多少行
//            ResultSet rs = pst.executeQuery();
//            while (rs.next()) {
//                ArrayList<String> dataItem = new ArrayList<>();
//                dataItem.add(rs.getString(2));
//                dataItem.add(rs.getString(3));
//                dataItem.add(rs.getString(4));
//                dataItem.add(rs.getString(5));
//                dataItem.add(rs.getString(6));
//                dataItem.add(rs.getString(7));
//                dataItem.add(rs.getString(8));
//                dataItem.add(rs.getString(9));
//                dataItem.add(rs.getString(10));
//                dataItem.add(rs.getString(11));
//                dataItem.add(rs.getString(12));
//                dataItem.add(rs.getString(13));
//                dataItem.add(rs.getString(14));
//                students.put(rs.getString(1), dataItem);
//            }
//            String path = "web/json/teacher/" +newCid + "_studentList.json";
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
//            outputStream.write(students.toString().getBytes());
//            outputStream.flush();
//            outputStream.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return students;
//    }

    //页面4
    public  Map<String,ArrayList<String>> importPredictionData(String cid) {
        Kfolder kfolder = new Kfolder();
        Map<String,ArrayList<String>> map =  kfolder.C45Tree();
        return map;
    }


    /**
     * 查询某一个属性的人数
     *
     * @param sql 查询语句
     * @return 属性值：人数
     */
    private JSONObject getNumberOfAttributes(String sql) {
        JSONObject json = new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                json.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return json;
    }

    /**
     * 查看某一门课程&成绩的分布
     *
     * @param cid 课程号
     * @return 成绩：人数
     */
    public JSONObject getOneCourseGrade(String cid) {
        JSONObject json = new JSONObject();
        String sql = "select grade,count(*) from " + tableNames.classfierTableName + " where grade != 'empty' and cid = '" + cid + "' group by grade";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                json.put(rs.getString(1), rs.getDouble(2));
            }
            String newCid = cid.replace("/", "_");
            String path = "web/json/teacher/" + newCid + "_grade.json";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            out.write(json.toString().getBytes());
            out.flush();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void main(String[] args) {
        Teacher t = new Teacher("jiaoshi");
        String cid = "HarvardX/PH278x/2013_Spring";

//        System.out.println(t.getCourseList());
//        System.out.println(t.getLocation(cid));
//        System.out.println(t.attributeWithCertified(cid,"learner_level"));
//        System.out.println(t.attributeWithCertified(cid,"age"));
//        System.out.println(t.attributeWithCertified(cid,"gender"));
//
//        System.out.println(t.getStudentList(cid,1,15));
//        System.out.println(t.getInteractionAndGrade(cid));

//        System.out.println(t.getGrade(cid));

        t.importPredictionData(cid);
    }

}


