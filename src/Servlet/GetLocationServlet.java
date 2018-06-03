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


public class GetLocationServlet extends HttpServlet {
    private Strings tableNames;

    public GetLocationServlet(){
        tableNames = new Strings();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String cid = session.getAttribute("cid").toString();
        Connection conn = (Connection) session.getAttribute("conn");

        String sql = "SELECT location,COUNT(*) FROM " + tableNames.originDataTableName + " WHERE cid = '" + cid + "' and location != 'Unknown_Other' GROUP BY location ORDER BY count(*) DESC LIMIT 30 ";
        JSONObject json = new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ArrayList<String> location = new ArrayList<>();
            ArrayList<String> number = new ArrayList<>();
            while (rs.next()) {
                location.add(rs.getString(1));
                number.add(rs.getString(2));
            }
            json.put("location",location);
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
