package Servlet;

import algorithm.Kfolder;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class getResult extends HttpServlet {
    public getResult(){

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Kfolder kfolder = new Kfolder();
        String path = req.getParameter("path");

        Map<String,ArrayList<String>> map =  kfolder.C45Tree(path);
        JSONObject json = JSONObject.fromObject(map);
        //返回给前段页面
        PrintWriter out = resp.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }
}
