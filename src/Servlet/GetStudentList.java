package Servlet;

import model.Strings;
import net.sf.json.JSONObject;
import util.DB;

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
import java.util.HashMap;
import java.util.Map;

public class GetStudentList extends HttpServlet {
    private Strings tableNames;

    public GetStudentList(){
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        String page = req.getParameter("pageNo");

        HttpSession session = req.getSession();
        String cid = session.getAttribute("cid").toString();
        Connection conn = (Connection) session.getAttribute("conn");

        int pageNo = Integer.parseInt(page);

        JSONObject students = new JSONObject();

        String sql = "SELECT uid, location, learner_level,age, gender, grade, start_time, last_time, nevents, ndays, nplay_videos, nchapters, " +
                "nforum_posts, certified FROM " + tableNames.originDataTableName + " WHERE cid ='" + cid + "' limit ?,?";
        int pageSize = 15;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, (pageNo - 1) * pageSize);//距离这一页的第一行数据，其前面有多少行数据
            pst.setInt(2, pageSize);//每页有多少行
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ArrayList<String> dataItem = new ArrayList<>();
                dataItem.add(rs.getString(2));
                dataItem.add(rs.getString(3));
                dataItem.add(rs.getString(4));
                dataItem.add(rs.getString(5));
                dataItem.add(rs.getString(6));
                dataItem.add(rs.getString(7));
                dataItem.add(rs.getString(8));
                dataItem.add(rs.getString(9));
                dataItem.add(rs.getString(10));
                dataItem.add(rs.getString(11));
                dataItem.add(rs.getString(12));
                dataItem.add(rs.getString(13));
                dataItem.add(rs.getString(14));
                students.put(rs.getString(1), dataItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(students);
        out.flush();
        out.close();


    }

}
