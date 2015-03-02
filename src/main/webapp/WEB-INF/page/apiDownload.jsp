<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>API下载页面</title>
</head>
<body>
	<a href="apiUpload.do?serviceName=${model.serviceName}">返回上传页面</a>
	<c:out value="${model.serviceName}"></c:out>
</body>
</html>