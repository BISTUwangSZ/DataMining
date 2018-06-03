<%--
  Created by IntelliJ IDEA.
  User: Cathleen
  Date: 2018/6/3
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
        String cid1 = request.getParameter("cid");
    System.out.println("dk"+cid1);
%>
<script type="text/javascript">
    var headCid=<%=cid1%>;
</script>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- 顶部菜单（来自bootstrap官方Demon）==================================== -->
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">菜鸟教程</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="../login.jsp"><span class="glyphicon glyphicon-user"></span> 退出</a></li>
        </ul>
    </div>
</nav>

<!-- 左侧菜单选项========================================= -->
<div id="leftMenu" class="container-fluid" style="float: left;">
    <div class="row-fluie">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li><a id="index" href="index.jsp" style="width: 100px">
                    <i class="fa fa-list"></i> 首页</a></li>
                <li><a id="list" href=""style="width: 100px">
                    <i class="fa fa-list"></i> 课程列表</a></li>
                <li><a id="chart" style="width: 100px" href="courseChart.jsp?cid="+cid>
                    <i class="fa fa-list"></i> 课程图表</a></li>
            </ul>

        </div>
    </div>
</div>
</body>
</html>
