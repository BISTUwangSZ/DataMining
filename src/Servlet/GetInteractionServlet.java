package Servlet;

import model.Strings;
import net.sf.json.JSONObject;
import util.DB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetInteractionServlet  extends HttpServlet {
    private Strings tableNames;

    public GetInteractionServlet(){
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        JSONObject attrs = new JSONObject();
        HttpSession session = req.getSession();
        String cid = session.getAttribute("cid").toString();
        Connection conn = (Connection) session.getAttribute("conn");

        String sql = "select grade,nevents,nplay_videos,nchapters,nforum_posts,uid from " + tableNames.originDataTableName +
                " where cid ='" + cid + "' and grade != 'null'";

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(attrs);
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(attrs);
        out.flush();
        out.close();
    }
}
