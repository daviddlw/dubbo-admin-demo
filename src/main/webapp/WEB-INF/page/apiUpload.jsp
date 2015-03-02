<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>API上传页面</title>
</head>
<body>
	<form name="apiUploadForm" action="apiUploadForm.do" method="post"
		enctype="multipart/form-data">
		<table style="width: 30%;" cellpadding="2" cellspacing="0" border="1"
			bordercolor="#000000">
			<tbody>
				<tr>
					<td>服务名称</td>
					<td><c:out value="${model.serviceName}"></c:out> <input
						type="hidden" name="hdServiceName" value="${model.serviceName}">
					</td>
				</tr>
				<tr>
					<td>Jar类型</td>
					<td><select name="dlApiType">
							<option value="Bin" selected="selected">Bin</option>
							<option value="Source">Source</option>
					</select></td>
				</tr>
				<tr>
					<td>版本</td>
					<td><input name="txtVersion" type="text" value="" /></td>
				</tr>
				<tr>
					<td>文件类型</td>
					<td>Java</td>
				</tr>
				<tr>
					<td>请选择文件</td>
					<td><input type="file" name="txtFilepath" value="" /></td>
				</tr>
				<tr>
					<td><input type="submit" name="btnUpload" value="上传" /></td>
					<td><input type="reset" name="btnReset" value="重置" /></td>
				</tr>
			</tbody>
		</table>
	</form>
	<br />
</body>
</html>