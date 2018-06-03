package model;
/**
 * 对原始数据进行处理，使其可以作为分类和预测使用的数据
 */

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ClassfierData {
    //预测数据
    private String uid;
    private String cid;
    private String identify;
    private String certified;
    private String location;
    private String level;
    private String age;
    private String gender;
    private String grade;
    private double timeSpan;
    private double nevents;
    private double ndays;
    private double nplayVideos;
    private double nchapters;
    private double nforumPosts;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdentify() {
        return identify;
    }

    //处理学习者身份，即处理explored.viewed.registered，选取最高身份
    public void setIdentify(String identify) {
            this.identify = identify;
    }

    public String getCertified() {
        return certified;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        switch (level) {
            case "Less than Secondary":
                this.level = "0Less than Secondary";
                break;
            case "Secondary":
                this.level = "1Secondary";
                break;
            case "Bachelor":
                this.level = "2Bachelor";
                break;
            case "Master":
                this.level = "3Master";
                break;
            case "Doctorate":
                this.level = "4Doctorate";
                break;
            case "empty":
                this.level = "5empty";
                break;
        }
        this.level = level;
    }

    public String getAge() {
        return age;
    }

    //处理年龄，将连续的年龄离散化
    public void setAge(String age) {
        if (age.equals("empty")) {
            this.age = "empty";
        } else {
            int ageInteger = Integer.parseInt(age);
            if (ageInteger <= 10) {
                this.age = "0-10";
            } else if (ageInteger <= 20) {
                this.age = "10-20";
            } else if (ageInteger <= 30) {
                this.age = "20-30";
            } else if (ageInteger <= 40) {
                this.age = "30-40";
            } else if (ageInteger <= 50) {
                this.age = "40-50";
            } else if (ageInteger <= 60) {
                this.age = "50-60";
            } else {
                this.age = "60以上";
            }
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public double getTimeSpan() {
        return timeSpan;
    }

    //处理时间跨度，最后登录时间-注册时间
    public void setTimeSpan(String  startTime, String lastTime) {
        double span;
        if ((startTime.equals("null") || (lastTime.equals("null")))) {
            span = 0;
        } else {
            String[] begin = startTime.split("/");
            String[] last = lastTime.split("/");
            LocalDate ld = LocalDate.of(Integer.parseInt(begin[0]), Integer.parseInt(begin[1]), Integer.parseInt(begin[2]));
            LocalDate ld2 = LocalDate.of(Integer.parseInt(last[0]), Integer.parseInt(last[1]), Integer.parseInt(last[2]));
            span = ChronoUnit.DAYS.between(ld, ld2);
            span++;
            if (span < 0) {
                span = 0;
            }
        }
        this.timeSpan = span;
    }

    //********************************************原始数据**************************************************************//

    public double getNevents() {
        return nevents;
    }

    public void setNevents(double nevents) {
        this.nevents = nevents;
    }

    public double getNdays() {
        return ndays;
    }

    public void setNdays(double ndays) {
        this.ndays = ndays;
    }

    public double getNplayVideos() {
        return nplayVideos;
    }

    public void setNplayVideos(double nplayVideos) {
        this.nplayVideos = nplayVideos;
    }

    public double getNchapters() {
        return nchapters;
    }

    public void setNchapters(double nchapters) {
        this.nchapters = nchapters;
    }

    public double getNforumPosts() {
        return nforumPosts;
    }

    public void setNforumPosts(double nforumPosts) {
        this.nforumPosts = nforumPosts;
    }

    //处理成绩
    public void setGrade(String grade) {
        double gradeDouble;
        if (grade.equals("null")){
            this.grade = "empty";
        } else {
            gradeDouble = Double.parseDouble(grade);
            if ((gradeDouble >= 0) && (gradeDouble <= 0.2)) {
                this.grade  = "bad";
            } else if (gradeDouble <= 0.4) {
                this.grade  = "low";
            } else if (gradeDouble <= 0.6) {
                this.grade  = "soso";
            } else if (gradeDouble <= 0.8) {
                this.grade  = "good";
            } else if (gradeDouble <= 0.9) {
                this.grade  = "excellent";
            } else if (gradeDouble <= 1) {
                this.grade  = "super";
            } else {
                this.grade  = "error";
            }
        }
    }

    //*****************************************平均值数据**************************************************************//

    public double getAvgNdays(){
        if (timeSpan==0) {
            return 0;
        }else {
            return ndays/timeSpan;
        }
    }

    public double getAvgNevents(){
        if (ndays==0){
            return 0;
        } else {
            return nevents/ndays;
        }
    }

    public double getAvgNplayVideo(){
        if (ndays==0){
            return 0;
        } else {
            return nplayVideos/ndays;
        }
    }

    public double getAvgNchapters(){
        if (ndays==0){
            return 0;
        } else {
            return nchapters/ndays;
        }
    }

    public double getAvgNforumPosts() {
        if (ndays==0){
            return 0;
        } else {
            return nforumPosts/ndays;
        }
    }
}
