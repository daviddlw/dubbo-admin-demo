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
	<br />
	<div>
		<h2>Source Jar</h2>
		<table style="width: 80%;" cellpadding="2" cellspacing="0" border="1"
			bordercolor="#000000">
			<tbody>
				<tr>
					<td>文件名</td>
					<td>下载地址</td>
				</tr>
				<c:forEach items="${model.sourceLib}" var="sourceLib">
					<tr>
						<td><c:out value="${sourceLib.fileName}"></c:out></td>
						<td><a href="${sourceLib.ftpFilepath}"><c:out
									value="${sourceLib.fileName}"></c:out></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br />
	<div>
		<h2>Bin Jar</h2>
		<table style="width: 80%;" cellpadding="2" cellspacing="0" border="1"
			bordercolor="#000000">
			<tbody>
				<tr>
					<td>文件名</td>
					<td>下载地址</td>
				</tr>
				<c:forEach items="${model.binLib}" var="binLib">
					<tr>
						<td><c:out value="${binLib.fileName}"></c:out></td>
						<td><a href="${binLib.ftpFilepath}"><c:out
									value="${binLib.fileName}"></c:out></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div>
		<h2>PHP Class</h2>
		<table style="width: 80%;" cellpadding="2" cellspacing="0" border="1"
			bordercolor="#000000">
			<tbody>
				<tr>
					<td>文件名</td>
					<td>下载地址</td>
				</tr>
				<c:forEach items="${model.phpLib}" var="phpLib">
					<tr>
						<td><c:out value="${phpLib.fileName}"></c:out></td>
						<td><a href="${phpLib.ftpFilepath}"><c:out
									value="${phpLib.fileName}"></c:out></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>