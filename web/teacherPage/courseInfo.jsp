<%@ page import="algorithm.Prediction" %>
<%@ page import="pages.Teacher" %>
<%@ page import="net.sf.json.JSONArray" %><%--
  Created by IntelliJ IDEA.
  User: drpeng
  Date: 2018/5/17
  Time: 下午3:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="head.jsp"></jsp:include>
<html>
<head>
    <title>Title</title>
    <script src="../js/echarts.js"></script>
    <script src="../js/ecStat.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<%String cid = request.getParameter("cid");%>
<script>
    var cid='<%=cid%>';
</script>

<body>
<div id="tab" style="width: 900px; float: left">
<ul id="myTab" class="nav nav-tabs">
    <li class="active"><a href="#home" data-toggle="tab">学生列表</a></li>
    <li><a href="#ios" data-toggle="tab">图表分析</a></li>
    <li><a href="#prediction" data-toggle="tab">预测</a></li>
</ul>
</div>

<div id="myTabContent" class="tab-content" style="width: 900px; float: left">
    <!--列表-->
    <div class="tab-pane fade in active" id="home" style="width: 900px">

    </div>

    <!--图表-->
    <div class="tab-pane fade" id="ios" style="width: 900px">
        <br>
        <div id='course1Interaction' style='width:1000px;height:400px'>获得课程次数和成绩的关系</div>
        <div id="location" style='width:900px;height:400px;margin-top: 50px'>获得地区分布</div>
        <div id="age" style='width:600px;height:300px;margin-top: 50px'>年龄</div>
        <div id="ageTable" style='margin-top: 50px'>年龄</div>
        <div id="gender" style='width:600px;height:300px;margin-top: 50px'>性别</div>
        <div id="genderTable" style='width:400px;margin-top: 30px'>性别</div>
        <div id="level" style='width:600px;height:300px;margin-top: 50px'>学历</div>
        <div id="levelTable" style='width:600px;margin-top: 50px'>学历</div>
        <div id="grade" style='width:600px;height:300px;margin-top: 50px'>获得交互次数和人数的和证书的关系</div>
    </div>

    <!--预测-->
    <div class="tab-pane fade" id="prediction" style="width: 900px">
            <input type="text" id="path" />
            <br/><br/>
            <input type="submit" value="提交" />
            </div>
</div>

<!--获取选课学生列表-->
<script type="text/javascript">
    studentListpath = "../json/teacher/"+cid+"_studentList.json";
    var $jsontip = $("#home");
    var strHtml = "";//存储数据的变量
    var index=1;
    window.studentList = "";
    $jsontip.empty();//清空内容
    strHtml += "<table class=\"table table-hover\">";
    strHtml += "<thead>" +
        "<tr>" +
        "<th>序号</th>" +
        "<th>学号</th>" +
        "<th>成绩</th>" +
        "<th>是否获得证书</th>" +
        "<th>详情</th>" +
        "</tr>" +
        "</thead>";
    $(function(){
        $.getJSON(studentListpath,function(data){
            studentList = data;
            $.each(data,function(uid,info){;
                strHtml += "<tr><td>"+index+"</td>";
                strHtml += "<td id='uid'>"+uid+"</td>";
                strHtml += "<td>"+info[4]+"</td>";
                strHtml += "<td>"+info[12]+"</td>";
                strHtml += "<td><button class='btn btn-info btn-sm' data-toggle='modal' data-target='#myModal' " +
                    "onclick=\"showModel('"+uid+"')\" >详情</button></td></tr>";
                index++;
            });
            $jsontip.html(strHtml);
        })
    });
</script>

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

<!--预测-->
<script type="text/javascript">

</script>

<!--每一门课程交互情况和成绩的比较-->
<script type="text/javascript">
    var course1Interaction = echarts.init(document.getElementById("course1Interaction"));
    var path="../json/teacher/"+cid+"_gradeWithInteraction.json";
    $.get(path, function (data) {
        var sizeValue = '57%';
        var symbolSize = 3;
        option = {
            legend: {},
            tooltip: {},
            toolbox: {
                left: 'center',
                feature: {
                    dataZoom: {}
                }
            },
            title:{
              text:'交互情况对课程的影响'
            },
            grid: [
                {right: sizeValue, bottom: sizeValue},
                {left: sizeValue, bottom: sizeValue},
                {right: sizeValue, top: sizeValue},
                {left: sizeValue, top: sizeValue}
            ],
            xAxis: [
                {type: 'value', gridIndex: 0, name: '交互次数', axisLabel: {rotate: 50, interval: 0}},
                {type: 'value', gridIndex: 1, name: '学习章节数', boundaryGap: false, axisLabel: {rotate: 50, interval: 0}},
                {type: 'value', gridIndex: 2, name: '观看视频数', axisLabel: {rotate: 50, interval: 0}},
                {type: 'value', gridIndex: 3, name: '论坛发帖数', axisLabel: {rotate: 50, interval: 0}}
            ],
            yAxis: [
                {type: 'value', gridIndex: 0, name: '成绩'},
                {type: 'value', gridIndex: 1, name: '成绩'},
                {type: 'value', gridIndex: 2, name: '成绩'},
                {type: 'value', gridIndex: 3, name: '成绩'}
            ],
            dataset: {
                dimensions: [
                    '成绩',
                    '交互次数',
                    '观看视频数',
                    '学习章节数',
                    '论坛发帖数',
                    '学号'

                ],
                source: data
            },
            series: [
                {
                    type: 'scatter',
                    symbolSize: symbolSize,
                    xAxisIndex: 0,
                    yAxisIndex: 0,
                    encode: {
                        x: '交互次数',
                        y: '成绩',
                        tooltip: [0,1,5]
                    }
                },
                {
                    type: 'scatter',
                    symbolSize: symbolSize,
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    encode: {
                        x: '学习章节数',
                        y: '成绩',
                        tooltip: [0,3,5]
                    }
                },
                {
                    type: 'scatter',
                    symbolSize: symbolSize,
                    xAxisIndex: 2,
                    yAxisIndex: 2,
                    encode: {
                        x: '观看视频数',
                        y: '成绩',
                        tooltip: [0,2,5]
                    }
                },
                {
                    type: 'scatter',
                    symbolSize: symbolSize,
                    xAxisIndex: 3,
                    yAxisIndex: 3,
                    encode: {
                        x: '论坛发帖数',
                        y: '成绩',
                        tooltip: [0,4,5]
                    }
                }
            ]
        };
        course1Interaction.setOption(option);
    });
</script>

<!--每一门课程的学习地区展示-->
<script type="text/javascript" >
    var locationChart = echarts.init(document.getElementById('location'));
    var path = "../json/teacher/" + cid +"_location.json";
    var locationArr = [];
    var numbersArr = [];
    $.get(path, function (data) {
        $.each(data,function (location,number) {
            locationArr.push(location)
            numbersArr.push(number);
        });
        option = {
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            title:{
                text:'学习者地区统计'
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : locationArr,
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLabel: {
                        interval: 0,
                        rotate: 30
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'直接访问',
                    type:'bar',
                    barWidth: '60%',
                    data:numbersArr
                }
            ]
        };
        locationChart.setOption(option);
    });
</script>

<!--每一门课程的年龄证书的关系-->
<script type="text/javascript" >
    var ageChart = echarts.init(document.getElementById("age"));
    var path = "../json/teacher/"+cid+"_age.json";
    $.get(path,function (data) {
        var kindArr = [];
        var allArr = [];
        var certifiedArr = [];

        var per = [];
        var table = "";//存储数据的变量
        table += "<table class=\"table table-hover\">";
        table += "<thead>" +
            "<tr>" +
            "<th>年龄</th>" +
            "<th>0-10</th>" +
            "<th>10-20</th>" +
            "<th>20-30</th>" +
            "<th>30-40</th>" +
            "<th>40-50</th>" +
            "<th>50-60</th>" +
            "<th>60以上</th>" +
            "<th>未填写</th>" +
            "</tr>" +
            "</thead>";
        var $gender = $("#ageTable");
        $gender.empty();//清空内容

        $.each(data,function (age,number) {
            kindArr.push(age);
            allArr.push(number[0]);
            certifiedArr.push(number[1]);
            per.push(number[2]);
        });
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            title:{
                text:'年龄和证书影响图'
            },
            legend: {
                data:['总人数','获得证书人数']
            },
            xAxis: [
                {
                    type: 'category',
                    data: kindArr,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '总人数',
                    min: 0,
                    max: 12000,
                    interval: 1000,
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '获得证书人数',
                    min: 0,
                    max: 1200,
                    interval: 100,
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    name:'总人数',
                    type:'bar',
                    data:allArr
                },
                {
                    name:'获得证书人数',
                    type:'line',
                    yAxisIndex: 1,
                    data:certifiedArr
                }
            ]
        };
        ageChart.setOption(option);

        table += "<tr><td>"+"获得证书比率(%)"+"</td>";
        table += "<td>"+per[0]+"</td>";
        table += "<td>"+per[1]+"</td>";
        table += "<td>"+per[2]+"</td>";
        table += "<td>"+per[3]+"</td>";
        table += "<td>"+per[4]+"</td>";
        table += "<td>"+per[5]+"</td>";
        table += "<td>"+per[6]+"</td>";
        table += "<td>"+per[7]+ "</td></tr>";
        $gender.html(table);

    })
</script>

<!--每一门课程的学历和证书的关系-->
<script type="text/javascript" >
    var levelChart = echarts.init(document.getElementById("level"));
    var path = "../json/teacher/"+cid+"_learner_level.json";
    $.get(path,function (data) {
        var kindArr = [];
        var allArr = [];
        var certifiedArr = [];

        var per = [];
        var table = "";//存储数据的变量
        table += "<table class=\"table table-hover\">";
        table += "<thead>" +
            "<tr>" +
            "<th>学历</th>" +
            "<th>中学以下</th>" +
            "<th>中学</th>" +
            "<th>本科</th>" +
            "<th>硕士</th>" +
            "<th>博士</th>" +
            "<th>未填写</th>" +
            "</tr>" +
            "</thead>";
        var $gender = $("#levelTable");
        $gender.empty();//清空内容


        $.each(data,function (level,number) {
            kindArr.push(level);
            allArr.push(number[0]);
            certifiedArr.push(number[1]);
            per.push(number[2]);
        });
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            title:{
                text:'学历和证书影响图'
            },
            legend: {
                data:['总人数','获得证书人数']
            },
            xAxis: [
                {
                    type: 'category',
                    data: kindArr,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '总人数',
                    min: 0,
                    max: 12000,
                    interval: 1000,
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '获得证书人数',
                    min: 0,
                    max: 1200,
                    interval: 100,
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    name:'总人数',
                    type:'bar',
                    data:allArr
                },
                {
                    name:'获得证书人数',
                    type:'line',
                    yAxisIndex: 1,
                    data:certifiedArr
                }
            ]
        };
        levelChart.setOption(option);

        table += "<tr><td>"+"获得证书比率(%)"+"</td>";
        table += "<td>"+per[0]+"</td>";
        table += "<td>"+per[1]+"</td>";
        table += "<td>"+per[2]+"</td>";
        table += "<td>"+per[3]+"</td>";
        table += "<td>"+per[4]+"</td>";
        table += "<td>"+per[5]+ "</td></tr>";
        $gender.html(table);
    })
</script>

<!--每一门课程的性别和证书的关系-->
<script type="text/javascript" >
    //图
    var genderChart = echarts.init(document.getElementById("gender"));
    var path = "../json/teacher/"+cid+"_gender.json";

    $.get(path,function (data) {
        var kindArr = [];
        var allArr = [];
        var certifiedArr = [];

        var per = [];
        var table = "";//存储数据的变量
        table += "<table class=\"table table-hover\">";
        table += "<thead>" +
            "<tr>" +
            "<th>性别</th>" +
            "<th>男</th>" +
            "<th>女</th>" +
            "<th>未填写</th>" +
            "</tr>" +
            "</thead>";
        var $gender = $("#genderTable");
        $gender.empty();//清空内容

        $.each(data,function (age,number) {
            kindArr.push(age);
            allArr.push(number[0]);
            certifiedArr.push(number[1]);
            per.push(number[2]);
        });
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            title:{
                text:'性别和证书影响图'
            },
            legend: {
                data:['总人数','获得证书人数']
            },
            xAxis: [
                {
                    type: 'category',
                    data: kindArr,
                    axisPointer: {
                        type: 'shadow',
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '总人数',
                    min: 0,
                    max: 15000,
                    interval: 1000,
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '获得证书人数',
                    min: 0,
                    max: 1500,
                    interval: 100,
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    name:'总人数',
                    type:'bar',
                    data:allArr,
                    barWidth : 40
                },
                {
                    name:'获得证书人数',
                    type:'line',
                    yAxisIndex: 1,
                    data:certifiedArr
                }
            ]
        };
        genderChart.setOption(option);

        table += "<tr><td>"+"获得证书比率(%)"+"</td>";
        table += "<td>"+per[0]+"</td>";
        table += "<td>"+per[1]+"</td>";
        table += "<td>"+per[2]+ "</td></tr>";
        $gender.html(table);
    });
</script>


<!--成绩和人数的分布-->
<script type="text/javascript">
    var gradeChart = echarts.init(document.getElementById('grade'));
    var gradePath = "../json/teacher/" + cid +"_grade.json";
    $.get(gradePath, function (data) {
        var arr = [];
        var gradeAll = [];
       $.each(data,function () {
           option = {
               xAxis: {},
               yAxis: {},
               title:{
                   text:'成绩和人数分布图'
               },
               toolbox: {
                   left: 'center',
                   feature: {
                       dataZoom: {}
                   }
               },
               series: [{
                   data: data,
                   type: 'line'
               }]
           };
           gradeChart.setOption(option);
       });

    });

</script>

</body>
</html>
