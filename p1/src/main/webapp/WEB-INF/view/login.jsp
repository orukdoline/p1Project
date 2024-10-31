<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>
</head>
<body>
	<h1>로그인</h1>
	<div>로그인 페이지입니다.</div><br>
	<hr><br>
	<form method="post" action="${contextPath}/login">
		<table border="1">
			<tr>
				<th colspan="3" style="background: lightgray; text-align: center;">로그인</th>
			</tr>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="id"></td>
				<td rowspan="2">
					<!-- 3열에 버튼 배치 -->
					<button type="submit" style="width: 100%; height: 50px;">로그인</button>
				</td>
			</tr>
			<tr>
				<td>패스워드</td>
				<td><input type="password" name="pw"></td>
			</tr>
		</table>
	</form>
</body>
</html>