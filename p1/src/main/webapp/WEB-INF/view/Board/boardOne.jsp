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
	  border-left: 1px solid #ddd;
	  border-right: 1px solid #ddd;
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
	div {
   		 /* 부모 요소에서 자식을 가운데 정렬 */
  	}
</style>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>P1프로젝트::${boardOne.articleTitle}</title>
</head>
<body>
	<!-- 타이틀 -->
	<div style="padding: 5px;">
		<h2>&nbsp;&nbsp;게시글</h2>
	</div>
	
	<!-- 게시글 상세정보 -->
	<div style="display: flex; justify-content: center;">
		<table>
			<tr>
				<th>카테고리</th>
				<td>${boardOne.category}</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td>${boardOne.memberId}</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${boardOne.articleTitle}</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>${boardOne.articleContent}</td>
			</tr>
		</table>
	</div><br>
	
	<div style="display: flex; justify-content: center;">
		<table>
			<tr>
					<th colspan="2">첨부파일</th>
			</tr>
			<c:if test="${empty boardFileList}">
				<tr>
					<td>첨부된 파일이 없습니다.</td>
				</tr>
			</c:if>
			<c:if test="${not empty boardFileList}">
				<c:forEach var="f" items="${boardFileList}">
					<tr>	
						<td style="width: 90%;">${f.originFileName}.${f.fileExt}</td>
						<td>
							<a href="${pageContext.request.contextPath}/upload/${f.fileName}.${f.fileExt}" download="${f.originFileName}.${f.fileExt}">
								<button type="button" style="width: 100%;">다운로드</button>
							</a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
	</div><br>
	
	<!-- 메뉴바 -->
	<div style="width: 99%; text-align: right; ">
		<c:if test="${loginId.equals(boardOne.memberId)}">
			<a href="${pageContext.request.contextPath}/Board/updateBoard?articleNo=${boardOne.articleNo}">
				<button type="button" style="width: 100px;">수정</button>
			</a>
			<a href="${pageContext.request.contextPath}/Board/deleteBoard?articleNo=${boardOne.articleNo}">
				<button type="button" style="width: 100px;">삭제</button>
			</a>
		</c:if>
			<a href="${pageContext.request.contextPath}/Board/boardList">
				<button type="button" style="width: 100px;">홈으로</button>
			</a>
	</div>
	
	<!-- 댓글 -->
	<div style="padding: 5px;">
		<h2>&nbsp;&nbsp;댓글</h2>
	</div>
	
	<!-- 댓글입력 -->
	<div style="display: flex; justify-content: center;">
		<form method="post" action="${pageContext.request.contextPath}/Board/insertComment" style="width: 98%">
			<input type="hidden" name="articleNo" value="${boardOne.articleNo}" />
			<table style="width: 100%">
				<tr>
					<th colspan="2">댓글입력</th>
				</tr>
				<tr>
					<td style="width: 90%;">
						<textarea name="comment" style="width: 100%; height: 50px;"></textarea>
					</td>
					<td>
						<button type="submit" style="width: 100%; height: 50px;">입력</button>
					</td>
				</tr>
			</table>
		</form>
	</div><br>

	<!-- 댓글출력 -->
	<div>
		<div style="display: flex; justify-content: center;">
			<table>
				<tr>
					<th style="width: 10%;">작성자</th>
					<th style="width: 70%;">내용</th>
					<th colspan="2" style="width: 10%;"></th>
				</tr>
				<c:forEach var="c" items="${boardCommentList}">
					<tr>
						<td style="width: 10%;">${c.memberId}</td>
						<td style="width: 80%;">${c.comment}</td>
						<c:if test="${!loginId.equals(c.memberId)}">
							<td style="width: 10%;"><a><button type="button" style="width: 100%" disabled>삭제</button></a></td>
						</c:if>
						<c:if test="${loginId.equals(c.memberId)}">
							<td style="width: 10%;"><a href="${pageContext.request.contextPath}/Board/deleteComment?commentNo=${c.commentNo}&articleNo=${c.articleNo}"><button type="button" style="width: 100%">삭제</button></a></td>
						</c:if>
					</tr>
				</c:forEach>
				
			</table>
		</div><br>
	</div>
	
	<!-- footer.jsp -->
	<div>
		<c:import url="/WEB-INF/view/Inc/footer.jsp"></c:import>
	</div>	
</body>
</html>