package pages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private Connection conn;

    Login(){
    }
    /**
     * 登录
     * @param uid           用户账号
     * @param pwd           用户密码
     * @param identify      用户身份：1:学生，2:老师，3：管理员
     * @return              返回登录信息，1：登录成功  2：用户不存在  3：密码错误
     */
    public int Login(String uid,String pwd, String identify){
        int result=0;
        String sql = "SELECT pwd,identify FROM login_users where uid = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,uid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if ( pwd.equals(rs.getString(1)) && identify.equals(rs.getString(2))) {
                    result=1;
                } else {
                    result=3;
                }
            }
        } catch (SQLException e) {
            result=2;
        }
        return result;
    }


    public boolean Registered(String uid,String pwd,String identify) {
        boolean result=false;
        String sql = "INSERT INTO login_users VALUES(?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql );
            pst.setString(1,uid);
            pst.setString(2,pwd);
            pst.setString(3,identify);
            pst.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] srgs){
        Login login = new Login();
//        System.out.println(login.LoginServlet("a","a","3"));
        System.out.println(login.Registered("b","bbb","2"));
    }
}
