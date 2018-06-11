package Servlet;

import model.Course;
import model.Strings;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StuGetCourseList extends HttpServlet {
    private Strings tableNames;

    public StuGetCourseList(){
        tableNames = new Strings();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        HttpSession session = req.getSession();
        String uid = session.getAttribute("uid").toString();
        Connection conn = (Connection) session.getAttribute("conn");

        String page = req.getParameter("pageNo");
        int pageNo = Integer.parseInt(page);
        JSONObject courseJson = new JSONObject();
        //语句中字段和表名需要修改为pre_users
        String sql = "SELECT * from " + tableNames.originDataTableName + " where uid = '" + uid +"' limit ?,?";
        int pageSize = 15;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, (pageNo-1)*pageSize);//距离这一页的第一行数据，其前面有多少行数据
            pst.setInt(2, pageSize);//每页有多少行
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                ArrayList<String> list = new ArrayList<>();
                list.add(rs.getString("identify"));
                list.add(rs.getString("certified"));
                list.add(rs.getString("location"));
                list.add(rs.getString("learner_level"));
                list.add(rs.getString("age"));
                list.add(rs.getString("gender"));
                list.add(rs.getString("grade"));
                list.add(rs.getString("start_time"));
                list.add(rs.getString("last_time"));
                list.add(rs.getString("nevents"));
                list.add(rs.getString("ndays"));
                list.add(rs.getString("nplay_videos"));
                list.add(rs.getString("nchapters"));
                list.add(rs.getString("nforum_posts"));
                courseJson.put(rs.getString("cid"),list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(courseJson);
        out.flush();
        out.close();
    }
}
