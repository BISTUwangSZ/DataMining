<%--
  Created by IntelliJ IDEA.
  User: Cathleen
  Date: 2018/6/3
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="head.jsp"></jsp:include>
<%
    String cid = request.getParameter("cid");
%>
<script>
    var cid='<%=cid%>';
    console.info(cid);
</script>
<html>
<head>
    <title>选课学生列表</title>
    <!-- 引入各种CSS样式表 -->
    <script src="/js/echarts.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="myRight" style="float: left; width: 900px">
    <small>选课学生列表</small>
    <!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
    <div id="studentList"></div>
</div>


<!--教师上传课程列表-->
<script type="text/javascript">
    var strHtml = "";//存储数据的变量
    strHtml += "<table class=\"table table-hover\">";
    strHtml += "<thead>" +
        "<tr>" +
        "<th>序号</th>" +
        "<th>学生号</th>" +
        "<th>成绩</th>" +
        "<th>是否获得证书</th>" +
        "<th>详情</th>" +
        "</tr>" +
        "</thead>";
    var $studentList = $("#studentList");
    $studentList.empty();//清空内容
    var index=1;
    $(function () {
        $.ajax({
            type:"post",
            async:false,
            url:"../sList.do?cid="+cid+"&pageNo="+"1",
            data:{},
            dataType:"json",
            success:function (data) {
                    $.each(data, function (uid,item) {
                        strHtml += "<tr><td>"+(index)+"</td>";
                        strHtml += "<td>"+uid+"</td>";
                        strHtml += "<td>"+item[4]+ "</td>";
                        strHtml += "<td>"+item[12]+ "</td>";
                        strHtml += "<td><button class='btn btn-info btn-sm' data-toggle='modal' data-target='#myModal' " +
                            "onclick=\"showModel('"+uid+"')\" >详情</button></td></tr>";
                        index++;

                    });
                    $studentList.html(strHtml);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    })
</script>




</body>
</html>
