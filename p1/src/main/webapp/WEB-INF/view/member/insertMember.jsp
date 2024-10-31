<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	table {
	  width: 100%;
	  border-collapse: collapse; /* 테두리 겹침 방지 */
	  font-family: Arial, sans-serif; /* 깔끔한 폰트 */
	  background-color: #f6f6f6; /* 표 배경 색상 */
	  border: 1px solid #efefef; /* 테두리 설정 */
	}
	
	th, td {
	  padding: 7px 7px;
	  text-align: left; /* 텍스트 왼쪽 정렬 */
	  border-bottom: 1px solid #ddd; /* 행 구분선 */
	}
	
	th {
	  background-color: #e0e0e0; /* 헤더 색상 */
	  color: #333; /* 텍스트 색상 */
	  font-weight: bold; /* 굵은 텍스트 */
	}
	
	td {
	  background-color: #ffffff; /* 헤더 색상 */
	  color: #333; /* 텍스트 색상 */
	}
	
	tr:nth-child(even) {
	  background-color: #f9f9f9; /* 짝수 행 다른 배경 색 */
	}
	
	td {
	  color: #555; /* 표 안의 텍스트 색상 */
	}
	
	caption {
	  caption-side: top; /* 캡션 상단에 위치 */
	  padding: 10px;
	  font-size: 1.2em;
	  color: #333;
	  font-weight: bold;
	}
</style>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1 style="margin: 15px;">회원가입</h1>
	<!-- ${pageContext.request.contextPath} -->
	<form method="post" action="${pageContext.request.contextPath}/member/insertMember" style="padding: 10px;">
		<table style="width: 95%; ">
			<tr>
				<th>아이디</th>
				<td><input type="text" name="id" style="width: 96%;"></td>
				<td>
					<button type="submit" name="action" value="idCheck" style="width: 99%;">중복검사</button>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td><input type="password" name="pw" style="width: 96%;"></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3" style="text-align: right;">
					<button type="submit" name="action" value="signUp" style="width: 98px;">가입</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>