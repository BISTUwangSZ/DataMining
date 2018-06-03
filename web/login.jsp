<%--
  Created by IntelliJ IDEA.
  User: drpeng
  Date: 2018/5/5
  Time: 下午8:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>登录</title>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/login.css">
  </head>
  <body>
  <!-- Form 用来提取用户填入并提交的信息-->
  <div class="container-fluid">
    <!-- 登陆框 -->
    <div id="login-form">
      <h2 class="text-center">用户登录</h2>
      <form class="form-horizontal" name="frmLogin" action="LoginServlet" method="post">
        <div class="input-group">
          <div class="input-group-addon">
            <span class="glyphicon glyphicon-user"></span>
          </div>
          <input type="text" class="form-control" id="username"
                 name="txtUserName" placeholder="请输入用户名" required="required" />
          <!-- 必填字段required -->
          <!-- 提示信息placeholder -->
        </div>
        <div class="input-group">
          <div class="input-group-addon">
            <span class="glyphicon glyphicon-lock"></span>
          </div>
          <input type="password" class="form-control" id="password"
                 name="txtPassword" placeholder="请输入密码" required="required" />
        </div>
        <div style="margin-top: 40px">
          <button class="btn btn-default btn-primary btn-block" name="Submit"
                  onClick="validateLogin()" >登录</button>
          <input type="reset" class="btn btn-default btn-primary btn-block" name="Reset" value="重置"></input>
        </div>
      </form>
    </div>
  </div>
  <!-- javaScript 函数 validateLogin(),用来验证用户名和密码是否为空 -->
  <script language="javaScript">
      function validateLogin()
      {
          var sUserName = document.frmLogin.txtUserName.value;
          var sPassword = document.frmLogin.txtPassword.value;
          if( sUserName=="" )
          {
              alert("请输入用户名！");
              return false;
          }
          if( sPassword=="" )
          {
              alert("请输入密码！");
              return false;
          }
      }
  </script>
  </body>
</html>
