<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	table {
	  width: 98%;
	  border-collapse: collapse; /* 테두리 겹침 방지 */
	  font-family: Arial, sans-serif; /* 깔끔한 폰트 */
	  background-color: #f6f6f6; /* 표 배경 색상 */
	  border: 1px solid #ddd;
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
	  width: 10%;
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
  	
  	input, textarea {
  		width: 100%;
  	}
</style>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
	<!-- 타이틀 -->
	<div style="padding: 5px;">
		<h1>&nbsp;&nbsp;게시글 작성</h1>
	</div>
	
	<!-- 게시글 상세정보 -->
	<form method="post" action="${pageContext.request.contextPath}/Board/insertBoard" enctype="multipart/form-data">
	<div style="display: flex; justify-content: center;">
		<table>
			<tr>
				<th>카테고리</th>
				<td>
				    <select name="category" style="height: 30px; width: 200px;">
				        <option value="영화">영화</option>
				        <option value="오락">오락</option>
				        <option value="다큐">다큐</option>
				        <option value="드라마">드라마</option>
				        <option value="기타">기타</option>
				    </select>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" name="title" style="height: 30px;"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea name="content" style="height: 300px;"></textarea></td>
			</tr>
			<tr>
				<th>첨부파일</th>
				<td><input type="file" name="files" multiple></td>
			</tr>
		</table>
	</div><br>
	<div style="width: 99%; text-align: right;">
		<a href="${pageContext.request.contextPath }/Board/boardList">
			<button type="button" style="width: 100px;">뒤로가기</button>
		</a>
		<button type="submit" style="width: 100px;">게시글 작성</button>
	</div>
	</form>
	
	<!-- footer.jsp -->
	<div>
		<c:import url="/WEB-INF/view/Inc/footer.jsp"></c:import>
	</div>
</body>
</html>