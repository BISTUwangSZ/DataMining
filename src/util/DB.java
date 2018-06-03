package util;

import model.ClassfierData;
import model.Data;
import model.Strings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DB {
    private Connection conn;
    Strings tablenames = new Strings();
    double TP, FP, TA, FA;
    double max[] = {0, 0, 0, 0, 0};
    double min[] = {100000, 100000, 100000, 100000, 100000};

    public Connection initDB() {
        String url = "jdbc:mysql://localhost/edx?characterEncoding=utf8&useSSL=true";
        String user = "root";
        String psw = "123456";
//        String url ="jdbc:mysql://123.206.205.246:3306/student?characterEncoding=utf8";
//        String user = "liuqing";
//        String psw = "liuqing123456";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            try {
                this.conn = DriverManager.getConnection(url, user, psw);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Connection connected" + conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 创建原始数据数据表、分类数据数据表
     *
     * @param data 从csv读取的数据
     */
    public void createTable(ArrayList<Data> data) {
        System.out.println("正在创建数据表……");
        ArrayList<Data> originData = data;
        createOriginTable(originData);

        ArrayList<ClassfierData> classfierData = classfyData(data);
        createClassfierTable(classfierData);
    }

    /**
     * 创建原始数据数据表
     *
     * @param originData 从csv读取的数据
     */
    private void createOriginTable(ArrayList<Data> originData) {
        String originSql = "create table if not EXISTS " + tablenames.originDataTableName + "(cid varchar(30), uid varchar(30), PRIMARY KEY (uid,cid), identify varchar(20)," +
                " certified varchar(20), location varchar(40), learner_level varchar(30), age varchar(10)," +
                "gender varchar(10), grade varchar(10), start_time VARCHAR(20), last_time varchar(20)," +
                "nevents double, ndays double, nplay_videos double,nchapters double,nforum_posts double)";
        try {
            PreparedStatement pst1 = conn.prepareStatement(originSql);
            pst1.execute();
            System.out.println("原始数据表创建完毕");
            insertOriginData(originData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入原始数据
     *
     * @param data 数据
     */
    private void insertOriginData(ArrayList<Data> data) {
        System.out.println("正在插入数据……");
        String sql = "INSERT INTO " + tablenames.originDataTableName + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            for (Data dataItem : data) {
                pst.setString(1, dataItem.getCid());
                pst.setString(2, dataItem.getUid());
                pst.setString(3, dataItem.getIdentify());
                pst.setString(4, dataItem.getCertified());
                pst.setString(5, dataItem.getLocation());
                pst.setString(6, dataItem.getLevel());
                pst.setString(7, dataItem.getAge());
                pst.setString(8, dataItem.getGender());
                pst.setString(9, dataItem.getGrade());
                pst.setString(10, dataItem.getStartTime());
                pst.setString(11, dataItem.getLastEventTime());
                pst.setDouble(12, dataItem.getNevents());
                pst.setDouble(13, dataItem.getNdays());
                pst.setDouble(14, dataItem.getNplayVideos());
                pst.setDouble(15, dataItem.getNchapters());
                pst.setDouble(16, dataItem.getNforumPosts());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("原始数据插入完毕");

    }

    /**
     * 将原始数据进行处理，成为分类数据
     *
     * @param data 原始数据
     * @return 可用于分类的分类数据
     */
    private ArrayList<ClassfierData> classfyData(ArrayList<Data> data) {
        System.out.println("正在创建分类数据……");
        ArrayList<ClassfierData> cDatas = new ArrayList<>();
        for (Data dataItem : data) {
            ClassfierData cData = new ClassfierData();
            cData.setCid(dataItem.getCid());
            cData.setUid(dataItem.getUid());
            cData.setIdentify(dataItem.getIdentify());
            cData.setCertified(dataItem.getCertified());
            cData.setLocation(dataItem.getLocation());
            cData.setLevel(dataItem.getLevel());
            cData.setAge(dataItem.getAge());
            cData.setGender(dataItem.getGender());
            cData.setGrade(dataItem.getGrade());
            cData.setTimeSpan(dataItem.getStartTime(), dataItem.getLastEventTime());
            cData.setNdays(dataItem.getNdays());
            cData.setNevents(dataItem.getNevents());
            cData.setNplayVideos(dataItem.getNplayVideos());
            cData.setNchapters(dataItem.getNchapters());
            cData.setNforumPosts(dataItem.getNforumPosts());
            cDatas.add(cData);
        }
        normalize(cDatas);
        System.out.println("分类数据构造完毕");
        return cDatas;
    }

    /**
     * 创建分类数据表
     *
     * @param classfierData 分类数据
     */
    private void createClassfierTable(ArrayList<ClassfierData> classfierData) {
        String classfierSql = "create table if not EXISTS " + tablenames.classfierTableName + "(cid varchar(30), uid varchar(30), PRIMARY KEY (uid,cid), " +
                "identify varchar(20), certified varchar(5), location varchar(40), learner_level varchar(30), age varchar(10),gender varchar(10)," +
                " grade varchar(10), nevents double, ndays double, nplay_videos double,nchapters double,nforum_posts double );";

        try {
            PreparedStatement pst2 = conn.prepareStatement(classfierSql);
            pst2.execute();
            System.out.println("分类数据表创建完毕");
            //使用交互数据的原始数据进行分类
            insertClassfierData(classfierData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入分类数据
     *
     * @param cDatas 分类数据
     */
    private void insertClassfierData(ArrayList<ClassfierData> cDatas) {
        System.out.println("正在插入数据……");
        String sql = "INSERT INTO " + tablenames.classfierTableName + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            for (ClassfierData cData : cDatas) {
                pst.setString(1, cData.getCid());
                pst.setString(2, cData.getUid());
                pst.setString(3, cData.getIdentify());
                pst.setString(4, cData.getCertified());
                pst.setString(5, cData.getLocation());
                pst.setString(6, cData.getLevel());
                pst.setString(7, cData.getAge());
                pst.setString(8, cData.getGender());
                pst.setString(9, cData.getGrade());
                pst.setDouble(10, cData.getNevents());
                pst.setDouble(11, cData.getNdays());
                pst.setDouble(12, cData.getNplayVideos());
                pst.setDouble(13, cData.getNchapters());
                pst.setDouble(14, cData.getNforumPosts());

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //计算离差
        System.out.println("分类数据表数据插入完毕");
    }


    private void normalize(ArrayList<ClassfierData> tuples) {
        //获取最大最小值
        for (ClassfierData item : tuples) {
            double nenevtsTemp = item.getNevents();
            setMaxMin(nenevtsTemp, 0);
            double ndaysTemp = item.getNdays();
            setMaxMin(ndaysTemp, 1);
            double nplay_videosTemp = item.getNplayVideos();
            setMaxMin(nplay_videosTemp, 2);
            double nchaptersTemp = item.getNchapters();
            setMaxMin(nchaptersTemp, 3);
            double nforumPostsTemp = item.getNforumPosts();
            setMaxMin(nforumPostsTemp, 4);
        }
        //计算离差并更新数据
        for (ClassfierData item : tuples) {
            //nevents
            if (max[0] == min[0]) {
                item.setNevents(0);
            } else {
                double present = item.getNevents();
                double result = (present - min[0]) / (max[0] - min[0]);
                item.setNevents(smooth(result));
            }
            //ndays
            if (max[1] == min[1]) {
                item.setNdays(0);
            } else {
                double present = item.getNdays();
                double result = (present - min[1]) / (max[1] - min[1]);
                item.setNdays(smooth(result));
            }
            //nplay_videos
            if (max[2] == min[2]) {
                item.setNplayVideos(0);
            } else {
                double present = item.getNplayVideos();
                double result = (present - min[2]) / (max[2] - min[2]);
                item.setNplayVideos(smooth(result));
            }
            //nchapters
            if (max[3] == min[3]) {
                item.setNchapters(0);
            } else {
                double present = item.getNchapters();
                double result = (present - min[3]) / (max[3] - min[3]);
                item.setNchapters(smooth(result));
            }
            //ndforumposts
            if (max[4] == min[4]) {
                item.setNforumPosts(0);
            } else {
                double present = item.getNforumPosts();
                double result = (present - min[4]) / (max[4] - min[4]);
                item.setNforumPosts(smooth(result));
            }
        }
    }

    private void setMaxMin(double temp, int i) {
        if (temp > max[i]) {
            max[i] = temp;
        }
        if (temp < min[i]) {
            min[i] = temp;
        }
    }

    //对于交互信息归一化处理后的数据，向上取整，光滑数据
    private double smooth(double d) {
        double result;
        if (d == 0) {
            result = 0;
        } else if (d > 0 && d <= 0.1) {
            result = 0.1;
        } else if (d <= 0.2) {
            result = 0.2;
        } else if (d <= 0.3) {
            result = 0.3;
        } else if (d <= 0.4) {
            result = 0.4;
        } else if (d <= 0.5) {
            result = 0.5;
        } else if (d <= 0.6) {
            result = 0.6;
        } else if (d <= 0.7) {
            result = 0.7;
        } else if (d <= 0.8) {
            result = 0.8;
        } else if (d <= 0.9) {
            result = 0.9;
        } else if (d <= 1) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }


    /**
     * 创建预测数据库
     *
     * @param data 从csv文件读出的数据
     */
    public void createPredictionTable(ArrayList<Data> data, int kind) {
        ArrayList<ClassfierData> cData;
        String predictionSql = "create table if not EXISTS " + tablenames.predictionTableName + "(cid varchar(30), uid varchar(30), PRIMARY KEY (uid,cid), " +
                "identify varchar(20), certified varchar(5), location varchar(40), learner_level varchar(30), age varchar(10),gender varchar(10)," +
                " grade varchar(10), nevents double, ndays double, nplay_videos double,nchapters double,nforum_posts double )";
        try {
            PreparedStatement pst = conn.prepareStatement(predictionSql);
            pst.execute();
            System.out.println("预测数据库创建完毕");
            //将原始数据转化为分类数据
            cData = classfyData(data);
            //插入数据
            insertPredictionData(cData, kind);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入预测数据（用户ID，课程ID，是否通过）
     *
     * @param cData 分类使用的数据
     */
    private void insertPredictionData(ArrayList<ClassfierData> cData, int kind) {
        System.out.println("正在插入预测数据");
        String sql = "INSERT INTO " + tablenames.predictionTableName + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            for (ClassfierData dataItem : cData) {
                pst.setString(1, dataItem.getCid());
                pst.setString(2, dataItem.getUid());
                pst.setString(3, dataItem.getIdentify());
                pst.setString(4, dataItem.getCertified());
                pst.setString(5, dataItem.getLocation());
                pst.setString(6, dataItem.getLevel());
                pst.setString(7, dataItem.getAge());
                pst.setString(8, dataItem.getGender());
                pst.setString(9, dataItem.getGrade());
                switch (kind) {
                    case 0:
                        pst.setDouble(10, dataItem.getNevents());
                        pst.setDouble(11, dataItem.getNdays());
                        pst.setDouble(12, dataItem.getNplayVideos());
                        pst.setDouble(13, dataItem.getNchapters());
                        pst.setDouble(14, dataItem.getNforumPosts());
                        break;
                    case 1:
                        pst.setDouble(10, dataItem.getAvgNevents());
                        pst.setDouble(11, dataItem.getAvgNdays());
                        pst.setDouble(12, dataItem.getAvgNplayVideo());
                        pst.setDouble(13, dataItem.getAvgNchapters());
                        pst.setDouble(14, dataItem.getAvgNforumPosts());
                }
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("预测数据插入完毕");
    }
}
