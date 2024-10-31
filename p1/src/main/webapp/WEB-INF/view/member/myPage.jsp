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
</style>

<script type="text/javascript">
	function openPopup() {
		window.open("${pageContext.request.contextPath}/member/updateMemberPw", "popupWindow", "width=500, height=500, scrollbars=yes, resizable=no");
	}
</script>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>
	<!-- 타이틀 -->
	<div style="padding: 5px;">
		<h2>&nbsp;&nbsp;마이페이지</h2>
	</div>
	<div style="display: flex; justify-contnent: space-between;">
		<!-- 계정 상세정보 -->
		<div style="width: 78%; padding: 10px; margin: 5px;">
			<table>
				<tr>
					<th>아이디</th>
					<td>${member.memberId}</td>
				</tr>
				<tr>
					<th>가입날짜</th>
					<td>${member.createDate}</td>
				</tr>
				<tr>
					<th>작성한 게시글</th>
					<td>${memberActivity.articleCount}개</td>
				</tr>
				<tr>
					<th>작성한 댓글</th>
					<td>${memberActivity.commentCount}개</td>
				</tr>
				<tr>
					<th>업로드한 파일</th>
					<td>${memberActivity.fileCount}개</td>
				</tr>
			</table><br>
			<div style="text-align: right; width: 98%">
				<a href="${pageContext.request.contextPath}/Board/boardList">
					<button type="button" style="width: 100px">뒤로가기</button>
				</a>
			</div>
		</div>
		
		<!-- 계정관리 메뉴 -->
		<div style="width: 22%; padding: 10px; margin: 1px; ">
			<div>
				<table>
					<tr>
						<th>메뉴</th>
					</tr>
					<tr>
						<td>
							<button type="button" onclick="openPopup()" style="width: 100%">비밀번호 변경</button>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath}/member/deleteMember">
								<button type="button" style="width: 100%">회원탈퇴</button>
							</a>
						</td>
					</tr>
				</table>
			</div><br>
		</div>
	</div>
</body>
</html>