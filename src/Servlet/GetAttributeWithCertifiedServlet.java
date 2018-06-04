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

public class GetAttributeWithCertifiedServlet extends HttpServlet {

    private Connection conn;
    private Strings tableNames;

    public GetAttributeWithCertifiedServlet(){
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       JSONObject attrs = new JSONObject();
        String kind = req.getParameter("kind");
        HttpSession session = req.getSession();
        String cid = session.getAttribute("cid").toString();
        conn = (Connection) session.getAttribute("conn");
        String attribute = "select " + kind + ", count(*) from " + tableNames.classfierTableName + " where cid='" + cid + "' group by " + kind +" order by " + kind;
        String certified = "select " + kind + ", count(*) from " + tableNames.classfierTableName + " where cid = '" + cid + "' and certified = 'yes' group by " + kind +" order by " + kind;;
        Map<String,Integer> attributeJson = getNumberOfAttributes(attribute);
        Map<String,Integer> certifiedJson = getNumberOfAttributes(certified);

        ArrayList<String> kindList = new ArrayList<>();
        ArrayList<String> allList = new ArrayList<>();
        ArrayList<String> certifiedList = new ArrayList<>();
        ArrayList<String> perList = new ArrayList<>();

        for (Object obj : attributeJson.keySet()) {
            kindList.add((String) obj);
            allList.add(String.valueOf(attributeJson.get(obj)));
            double d=0;
            if(certifiedJson.get(obj)==null){
                d=0;
            } else {
                d=certifiedJson.get(obj);
            }
            certifiedList.add(String.valueOf(d));
            double percent = d /attributeJson.get(obj);
            double d1 = percent*10000;
            int d2 = (int)d1;
            double d3 = (double)d2/100;
            String per = String.valueOf(d3);
            perList.add(per);
        }
        attrs.put("kind",kindList);
        attrs.put("all",allList);
        attrs.put("certified",certifiedList);
        attrs.put("per",perList);
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(attrs);
        out.flush();
        out.close();
    }

    /**
     * 查询某一个属性的人数
     *
     * @param sql 查询语句
     * @return 属性值：人数
     */
    private Map<String,Integer> getNumberOfAttributes(String sql) {
        Map<String,Integer> map = new HashMap<>();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
