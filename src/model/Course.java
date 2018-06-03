package model;

/**
 * 一门课的所有属性
 */

public class Course {

    private String cid;
    private String identify;
    private String grade;
    private String startTime;
    private String lastEventTime;
    private double nevents;
    private double ndays;
    private double nplayVideos;
    private double nchapters;
    private double nforumPosts;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String registered,String viewed, String explored, String certified) {
        if (certified.equals("yes")) {
            this.identify = "证书达人";
        } else if (explored.equals("yes")) {
            this.identify = "知识猎人";
        } else if (viewed.equals("yes")) {
            this.identify = "晒网达人";
        } else if (registered.equals("yes")){
            this.identify = "路人甲";
        }

    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(String lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

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
}
