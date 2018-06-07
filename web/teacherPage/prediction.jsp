<%--
  Created by IntelliJ IDEA.
  User: Cathleen
  Date: 2018/6/5
  Time: 7:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String cid = request.getParameter("cid");
    HttpSession session1 = request.getSession();
    session1.setAttribute("cid",cid);
    String tid = session1.getAttribute("tid").toString();
%>
<html>
<head>
    <title>edX数据集预测分析系统</title>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<!-- 顶部菜单（来自bootstrap官方Demon）==================================== -->
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><%=tid%></a>
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
                <li><a id="chart" style="width: 100px" href="courseChart.jsp?cid=<%=cid%>">
                    <i class="fa fa-list"></i> 课程图表</a></li>
                <li><a id="prediction" style="width: 100px" href="prediction.jsp?cid=<%=cid%>">
                    <i class="fa fa-list"></i> 预测</a></li>
            </ul>
        </div>
    </div>
</div>

<div style="float: left">
    <input type="text" placeholder="请输入预测文件地址" id="path" style="margin-left: 50px">
    <input type="button" class="btn btn-primary dropdown-toggle" onclick="showResult()" value="预测" style="margin-left:50px">
    <input type="button" class="btn btn-primary dropdown-toggle" onclick="var showAccuracy = function () {
        var msg = '准确率：'+predictionData['acc'][0][0]+'\n'+'精度：'+predictionData['acc'][0][1]+'\n'+'召回率：'+predictionData['acc'][0][2];
        alert(msg);
    };
    showAccuracy()" value="显示分类器准确率" style="margin-left:50px">

</div>

<div class="myRight" style="float: left; width: 900px">
    <!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
    <div id="predict"></div>
</div>


<!--获取预测数据列表-->
<script>
    function showResult() {
        var $sList = $("#predict");
        $sList.empty();//清空内容
        var path = document.getElementById("path").value;
        var strHtml = "";//存储数据的变量
        strHtml += "<table class=\"table table-hover\">";
        strHtml += "<thead>" +
            "<tr>" +
            "<th>序号</th>" +
            "<th>学生号</th>" +
            "<th>课程号</th>" +
            "<th>能否获得证书</th>" +
            "<th>详情</th>" +
            "</tr>" +
            "</thead>";
        $.ajax({
            type: "post",
            async: false,
            url: "../p.do",
            data: {"path":path},
            dataType: "json",
            success: function (data) {
                window.predictionData=data;
                $.each(data["prediction"],function (index,item) {
                    var uid = item[0];
                    strHtml += "<tr><td>"+(index+1)+"</td>";
                    strHtml += "<td>"+uid+"</td>";
                    strHtml += "<td>"+item[1]+ "</td>";
                    strHtml += "<td>"+item[2]+ "</td>";
                    strHtml += "<td><button class='btn btn-info btn-sm'onclick=\"showReason('"+uid+"')\" >详情</button></td></tr>";
                });
                $sList.html(strHtml);
                console.info(predictionData["acc"]);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    }
    function showReason(userID) {
        $.each(predictionData["prediction"],function (index,item) {
            if(item[0]===userID){
                alert(item[0]+"\n"+item[3]);
            }
        })
    }
</script>


</body>
</html>
