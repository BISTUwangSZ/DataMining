<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<jsp:include page="head.jsp"></jsp:include>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <%-- 在IE运行最新的渲染模式 --%>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- 初始化移动浏览显示 --%>
    <meta name="Author" content="Dreamer-1.">

    <!-- 引入各种CSS样式表 -->
    <script src="/js/echarts.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <title>- 后台管理系统 -</title>
</head>

<body>


<div class="myRight" style="float: left; width: 900px">
    <small>欢迎登录</small>
    <!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
    <div id="jsonTip"></div>
</div>

<!--教师上传课程列表-->
<script type="text/javascript">
    var strHtml = "";//存储数据的变量
    strHtml += "<table class=\"table table-hover\">";
    strHtml += "<thead>" +
        "<tr>" +
        "<th>序号</th>" +
        "<th>课程号</th>" +
        "<th>选课人数</th>" +
        "<th>获得证书人数</th>" +
        "</tr>" +
        "</thead>";
    var $jsontip = $("#jsonTip");
    $jsontip.empty();//清空内容
    var index=1;
    $(function () {
        $.ajax({
            type:"post",
            async:false,
            url:"../cList.do",
            data:{},
            dataType:"json",
            success:function (data) {
                var courseId = data["cid"];
                strHtml += "<tr><td>"+index+"</td>";
                strHtml += "<td><a href='predictionList.jsp?cid="+courseId+"'>"+courseId+"</a></td>";
                strHtml += "<td>"+data["all"]+ "</td>";
                strHtml += "<td>"+data["certified"]+ "</td>";
                index++;
                $jsontip.html(strHtml);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    })
</script>

</body>
</html>