<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	Calendar cal = Calendar.getInstance();
	int curYear = cal.get(Calendar.YEAR);
	request.setAttribute("curYear", curYear);
	
	List yearList = new ArrayList();
	for(int i=curYear;i>curYear-5;i--){
		yearList.add(i);
	}
	request.setAttribute("yearList", yearList);
%>

<!DOCTYPE HTML>
<html>
  <head>
    <%@include file="/common/header.jsp"%>
    <title>年度投诉统计图</title>
  </head>
  <script type="text/javascript" src="${basePath }js/fusioncharts/fusioncharts.js"></script>
<!--    <script type="text/javascript" src="${basePath }js/fusioncharts/fusioncharts.charts.js"></script> -->
  <script type="text/javascript" src="${basePath }js/themes/fusioncharts.theme.fint.js"></script>
  <script type="text/javascript">
  	$(document).ready(doAnnualStatistic());
	function doAnnualStatistic(){
		var year = $("#year option:selected").val();
		if(year == "" || year == undefined){
			year = "${curYear}";
		}
		$.ajax({
			url:"${basePath}/nsfw/complain_getAnnualStatisticData.action",
			data:{"year":year},
			type:"post",
			dataType:"json",
			success: function(data){
				if(data != null && data!="" && data!=undefined){
				    var revenueChart = new FusionCharts({
				        "type": "line",
				        "renderAt": "chartContainer",
				        "width": "600",
				        "height": "400",
				        "dataFormat": "json",
				        "dataSource":  {
				          "chart": {
				            "caption": year + "年度投诉数统计图",
				            "xAxisName": "月份",
				            "yAxisName": "投诉数",
				            "theme": "fint"
				          },
				          "data": data.charData
				      	}
				  	});
					revenueChart.render();
				}else{
					alert("统计投诉数失败！");
				}
			},
			error: function(){alert("统计投诉数失败！");}
		});
	}
  </script>
  <body>
  	<br>
  	<div style="text-align: center;width: 100%;">
    <s:select id="year" list="#request.yearList" onchange="doAnnualStatistic()"></s:select></div>
    <br>
    <div id="chartContainer" style="text-align:center;width:100%;"></div>
  </body>
</html>
