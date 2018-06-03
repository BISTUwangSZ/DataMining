<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<%
    HttpSession session1 = request.getSession();
    String uid = session1.getAttribute("uid").toString();
    System.out.println(uid);
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
    <div id="courseList"></div>
</div>

<!-- 模态框div（Modal） -->
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">
                    详细信息
                </h4>
            </div>
            <div class="modal-body">

                <div id="mycContent"></div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!--学生已选课程列表-->
<script type="text/javascript">
    window.courseList="";
    uid='<%=uid%>';
    $(function () {
        var index=1;
        var strHtml = "";//存储数据的变量
        strHtml += "<table class=\"table table-hover\">";
        strHtml += "<thead>" +
            "<tr>" +
            "<th>序号</th>" +
            "<th>课程号</th>" +
            "<th>注册时间</th>" +
            "<th>最后登录时间</th>" +
            "<th>成绩</th>" +
            "<th>获得证书情况</th>" +
            "<th>详情</th>" +
            "</tr>" +
            "</thead>";
        var $courseList = $("#courseList");
        $courseList.empty();//清空内容

        $.ajax({
            type:"post",
            async:false,
            url:"../stuCList.do?&pageNo="+"1",
            data:{},
            dataType:"json",
            success:function (data) {
                courseList =data;
                $.each(data, function (cid,item) {
                    console.info(data);
                    strHtml += "<tr><td>"+(index)+"</td>";
                    strHtml += "<td>"+cid+"</td>";
                    strHtml += "<td>"+item[7]+ "</td>";
                    strHtml += "<td>"+item[8]+ "</td>";
                    strHtml += "<td>"+item[6]+ "</td>";
                    strHtml += "<td>"+item[1]+ "</td>";
                    strHtml += "<td><button class='btn btn-info btn-sm' data-toggle='modal' data-target='#myModal' " +
                        "onclick=\"showModel('"+cid+"')\" >详情</button></td></tr>";
                    index++;
                    index++;

                });
                $courseList.html(strHtml);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    })
</script>

</body>
</html>

<!--点击详情显示模态框-->
<script>
    $(function(){
        // dom加载完毕
        var $m_btn = $('#btn-sm');
        var $modal = $('#myModal');
        // 测试 bootstrap 居中
        $modal.on('show.bs.modal', function(){
            var $this = $(this);
            var $modal_dialog = $this.find('.modal-dialog');
            // 关键代码，如没将modal设置为 block，则$modala_dialog.height() 为零
            $this.css('display', 'block');
            $modal_dialog.css({'margin-top': Math.max(0, ($(window).height() - $modal_dialog.height()) / 3) });
        });
    });
</script>

<!--模态框数据-->
<script type="text/javascript">
    function showModel(courseId) {
        var $json = $("#mycContent");
        var strHtml = "";//存储数据的变量
        $json.empty();//清空内容
        $.each(courseList,function(cid,info){
            if (cid===courseId) {
                strHtml += "姓名:"+cid +"<br />";
                strHtml += "身份:"+info[0]+"<br />";
                strHtml += "地区:"+info[2]+"<br />";
                strHtml += "学历"+info[3]+"<br />";
                strHtml += "年龄"+info[4]+"<br />";
                strHtml += "性别"+info[5]+"<br />";
                strHtml += "成绩"+info[6]+"<br />";
                strHtml += "注册时间"+info[7]+"<br />";
                strHtml += "最后登录时间"+info[8]+"<br />";
                strHtml += "交互次数"+info[9] +"<br />";
                strHtml += "交互天数"+info[10] +"<br />";
                strHtml += "播放视频数"+info[11] +"<br />";
                strHtml += "观看章节数"+info[12] +"<br />";
                strHtml += "论坛发帖数"+info[13] +"<br />";
                strHtml += "取得证书"+info[1] +"<br />";
            }
        });
        $json.html(strHtml);
    }
</script>
