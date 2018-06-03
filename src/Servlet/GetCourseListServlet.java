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

public class GetCourseListServlet extends HttpServlet{
    private Connection conn;
    private Strings tableNames;

    public GetCourseListServlet(){
        DB db = new DB();
        conn = db.initDB();
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        JSONObject courses = new JSONObject();
        HttpSession session = req.getSession();
        String tid = session.getAttribute("tid").toString();
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
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(courses);
        out.flush();
        out.close();
    }
}
