<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SpringApp</title>
</head>
<!-- <script type="text/javascript">
	$(document).ready(function() {
		alert("load js success");
	});
</script> -->
<body>
	<h1>Hello SpringApp</h1>
	<table width="280" style="border: 1px solid #ccc" cellspacing="1"
		cellpadding="0">
		<tr>
			<td>上传：</td>
			<td><a href="apiUpload.do">API上传页面</a></td>
		</tr>
		<tr>
			<td>下载：</td>
			<td><a href="apiDownload.do">API下载页面</a></td>
		</tr>
		<!--<tr>
			 			<td>测试：
			</td>
			<td>
				<a href="test.do?id=1&name=daviddai&type=redirect">测试跳转</a>
			</td>
			<td>测试跳转</td>
			<td><a href="test.do?type=redirect">测试跳转</a></td>
		</tr>
		<tr>
			<td>测试转发</td>
			<td><a href="test.do?type=forward">测试转发</a></td>
		</tr>
		 -->
	</table>
</body>
</html>