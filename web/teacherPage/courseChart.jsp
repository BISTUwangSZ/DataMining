<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String cid = request.getParameter("cid");
    HttpSession session1 = request.getSession();
    session1.setAttribute("cid",cid);
   String tid =  session1.getAttribute("tid").toString();
%>
<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2018/5/13
  Time: 23:13
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>edX数据集预测分析系统</title>
    <!-- 引入 echarts.js -->
    <script src="/js/echarts.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

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
                <li><a id="list" href="courseList.jsp?cid=<%=cid%>"style="width: 100px">
                    <i class="fa fa-list"></i> 课程列表</a></li>
                <li><a id="chart" style="width: 100px" href="courseChart.jsp?cid=<%=cid%>">
                    <i class="fa fa-list"></i> 课程图表</a></li>
            </ul>

        </div>
    </div>
</div>

<div class="btn-group" style="margin-left: 300px">
    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
        交互情况 <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" role="menu">
        <li><a id="interaction" onclick="showInteraction()">交互情况</a></li>
    </ul>
    <div class="btn-group" style="margin-left: 10px">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
            成绩 <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li><a id="grade" onclick="showGrade()">成绩</a></li>
        </ul>
    </div>
    <div class="btn-group" style="margin-left: 10px">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">证书率
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li><a id="gender" onclick="showAttribute('gender')">性别</a></li>
            <li><a id="age" onclick="showAttribute('age')">年龄</a></li>
            <li><a id="level" onclick="showAttribute('learner_level')">学历</a></li>
        </ul>
    </div>
    <div class="btn-group" style="margin-left: 10px">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">地区分布
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li><a id="location" onclick="showLocation()">地区</a></li>
        </ul>
    </div>
</div>

<div class="myRight" style="float: bottom; margin-top: 30px">

    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="allCourse" style="width: 800px;height:400px;margin: 0 auto">
    </div>
    <div id="table" style="width: 800px;height: 50px;margin: 0 auto">
    </div>

</div>


</body>
</html>

<!--每一门课程的学习地区展示-->
<script type="text/javascript">
    function showLocation() {
        echarts.dispose(document.getElementById("allCourse"));
        var $kind = $("#table");
        $kind.empty();//清空内容
        var locationChart = echarts.init(document.getElementById('allCourse'));
        $.ajax({
            type: "post",
            async: false,
            url: "../location.do",
            data: {},
            dataType: "json",
            success: function (data) {
                option = {
                    color: ['#3398DB'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    title: {
                        text: '学习者地区统计'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: data["location"],
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLabel: {
                                interval: 0,
                                rotate: 30
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '人数',
                            type: 'bar',
                            barWidth: '60%',
                            data: data["number"]
                        }
                    ]
                };
                locationChart.setOption(option);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })

    }
</script>

<!--每一门课程的学习者属性-证书率展示-->
<script type="text/javascript">
    function showAttribute(kind) {
        echarts.dispose(document.getElementById("allCourse"));
        var levelChart = echarts.init(document.getElementById("allCourse"));

        var $kind = $("#table");
        $kind.empty();//清空内容
        var table = "";//存储数据的变量
        table += "<table class=\"table table-hover\">";
        table += "<thead><tr>";

        $.ajax({
            type: "post",
            async: false,
            url: "../awc.do?kind=" + kind,
            data: {},
            dataType: "json",
            success: function (data) {
                console.info(data);
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
                    title: {
                        text: '学历和证书影响图'
                    },
                    legend: {
                        data: ['总人数', '获得证书人数']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: data["kind"],
                            axisPointer: {
                                type: 'shadow'
                            },
                            axisLabel: {
                                interval: 0,
                                rotate: 30
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '总人数',
                            min: 0,
                            max: 14000,
                            interval: 1000,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        },
                        {
                            type: 'value',
                            name: '获得证书人数',
                            min: 0,
                            max: 1400,
                            interval: 100,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '总人数',
                            type: 'bar',
                            data: data["all"]
                        },
                        {
                            name: '获得证书人数',
                            type: 'line',
                            yAxisIndex: 1,
                            data: data["certified"]
                        }
                    ]
                };
                levelChart.setOption(option);

                table += "<th>" + kind + "</th>";
                $.each(data["kind"], function (index, item) {
                    table += "<th>" + item + "</th>";
                });
                table += "</thead>";

                table += "<tr><td>" + "获得证书比率(%)" + "</td>";
                $.each(data["per"], function (index, item) {
                    table += "<td>" + item + "</td>";
                });
                $kind.html(table);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })
    }
</script>

<!--成绩-->
<script type="text/javascript">
    function showGrade() {
        echarts.dispose(document.getElementById("allCourse"));
        var myChart = echarts.init(document.getElementById("allCourse"));
        $.ajax({
            type: "post",
            async: false,
            url: "../grade.do",
            data: {},
            dataType: "json",
            success: function (data) {
                option = {
                    title:{
                        text:'成绩和人数分布图'
                    },
                    toolbox: {
                        left: 'center',
                        feature: {
                            dataZoom: {}
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        type: 'category',
                        data: data["grade"]
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data:data["number"],
                        type: 'line',
                        smooth: true
                    }]
                };

                myChart.setOption(option);
            },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })

    }
</script>

<script type="text/javascript">
    function showInteraction() {
        echarts.dispose(document.getElementById("allCourse"));
        var myChart = echarts.init(document.getElementById("allCourse"));
        $.ajax({
            type: "post",
            async: false,
            url: "../interaction.do",
            data: {},
            dataType: "json",
            success: function (data) {
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
                        {type: 'value', gridIndex: 0, name: 'events', axisLabel: {rotate: 50, interval: 0}},
                        {type: 'value', gridIndex: 1, name: 'chapters', boundaryGap: false, axisLabel: {rotate: 50, interval: 0}},
                        {type: 'value', gridIndex: 2, name: 'videos', axisLabel: {rotate: 50, interval: 0}},
                        {type: 'value', gridIndex: 3, name: 'forum_posts', axisLabel: {rotate: 50, interval: 0}}
                    ],
                    yAxis: [
                        {type: 'value', gridIndex: 0, name: 'grade'},
                        {type: 'value', gridIndex: 1, name: 'grade'},
                        {type: 'value', gridIndex: 2, name: 'grade'},
                        {type: 'value', gridIndex: 3, name: 'grade'}
                    ],
                    dataset: {
                        dimensions: [
                            'grade',
                            'events',
                            'videos',
                            'chapters',
                            'forum_posts',
                            'uid'
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
                                x: 'events',
                                y: 'grade',
                                tooltip: [0,1,5]
                            }
                        },
                        {
                            type: 'scatter',
                            symbolSize: symbolSize,
                            xAxisIndex: 1,
                            yAxisIndex: 1,
                            encode: {
                                x: 'chapters',
                                y: 'grade',
                                tooltip: [0,3,5]
                            }
                        },
                        {
                            type: 'scatter',
                            symbolSize: symbolSize,
                            xAxisIndex: 2,
                            yAxisIndex: 2,
                            encode: {
                                x: 'videos',
                                y: 'grade',
                                tooltip: [0,2,5]
                            }
                        },
                        {
                            type: 'scatter',
                            symbolSize: symbolSize,
                            xAxisIndex: 3,
                            yAxisIndex: 3,
                            encode: {
                                x: 'forum_posts',
                                y: 'grade',
                                tooltip: [0,4,5]
                            }
                        }
                    ]
                };
                myChart.setOption(option);
          },
            error: function (errorMsg) {
                alert("加载失败");
            }
        })

    }
</script>