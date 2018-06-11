package Servlet;

import model.Strings;
import net.sf.json.JSONArray;
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

public class GetGradeServlet extends HttpServlet {
    private Strings tableNames;

    public GetGradeServlet(){
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        JSONObject json = new JSONObject();
        HttpSession session = req.getSession();
        String cid = session.getAttribute("cid").toString();
        Connection conn = (Connection) session.getAttribute("conn");

        String sql = "select grade,count(*) from " + tableNames.originDataTableName + " where cid = '" + cid
                +"'and grade != 'null' group by grade";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ArrayList<String> grade = new ArrayList<>();
            ArrayList<String> number = new ArrayList<>();

            while (rs.next()){
                grade.add(rs.getString(1));
                number.add(rs.getString(2));
            }
            json.put("grade",grade);
            json.put("number",number);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }
}
