<%--
  Created by IntelliJ IDEA.
  User: Cathleen
  Date: 2018/6/3
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="chartHead.jsp"></jsp:include>
<%
    String cid = request.getParameter("cid");
%>
<script>
    var cid='<%=cid%>';
</script>
<html>
<head>
    <title>选课学生列表</title>
    <!-- 引入各种CSS样式表 -->
    <script src="/js/echarts.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>


<div class="myRight" style="float: left; width: 900px">
    <!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
    <div id="studentList"></div>
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

<!--教师上传课程列表-->
<script type="text/javascript">
    window.studentList="";
    $(function () {
        var index=1;
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
        var $sList = $("#studentList");
        $sList.empty();//清空内容

        $.ajax({
            type:"post",
            async:false,
            url:"../sList.do?cid="+cid+"&pageNo="+"1",
            data:{},
            dataType:"json",
            success:function (data) {
                studentList=data;
                    $.each(data, function (uid,item) {
                        strHtml += "<tr><td>"+(index)+"</td>";
                        strHtml += "<td>"+uid+"</td>";
                        strHtml += "<td>"+item[4]+ "</td>";
                        strHtml += "<td>"+item[12]+ "</td>";
                        strHtml += "<td><button class='btn btn-info btn-sm' data-toggle='modal' data-target='#myModal' " +
                            "onclick=\"showModel('"+uid+"')\" >详情</button></td></tr>";
                        index++;

                    });
                $sList.html(strHtml);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    })
</script>

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
    function showModel(userId) {
        var $json = $("#mycContent");
        var strHtml = "";//存储数据的变量
        $json.empty();//清空内容
        $.each(studentList,function(uid,info){
            if (uid===userId) {
                strHtml += "姓名:"+uid +"<br />";
                strHtml += "地区:"+info["0"]+"<br />";
                strHtml += "学历"+info["1"]+"<br />";
                strHtml += "年龄"+info["2"]+"<br />";
                strHtml += "性别"+info["3"]+"<br />";
                strHtml += "成绩"+info["4"]+"<br />";
                strHtml += "注册时间"+info["5"]+"<br />";
                strHtml += "最后登录时间"+info["6"]+"<br />";
                strHtml += "交互次数"+info["7"] +"<br />";
                strHtml += "交互天数"+info["8"] +"<br />";
                strHtml += "播放视频数"+info["9"] +"<br />";
                strHtml += "观看章节数"+info["10"] +"<br />";
                strHtml += "论坛发帖数"+info["11"] +"<br />";
                strHtml += "取得证书"+info["12"] +"<br />";
            }
        });
        $json.html(strHtml);
    }
</script>

</body>
</html>
