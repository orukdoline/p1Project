<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	table {
	  width: 100%;
	  border-collapse: collapse; /* 테두리 겹침 방지 */
	  font-family: Arial, sans-serif; /* 깔끔한 폰트 */
	  background-color: #f6f6f6; /* 표 배경 색상 */
	}
	
	th, td {
	  padding: 7px 7px;
	  text-align: left; /* 텍스트 왼쪽 정렬 */
	}
	
	th {
	  background-color: #e0e0e0; /* 헤더 색상 */
	  color: #333; /* 텍스트 색상 */
	  font-weight: bold; /* 굵은 텍스트 */
	}
	
	td {
	  background-color: #ffffff; /* 헤더 색상 */
	  color: #333; /* 텍스트 색상 */
	  border-left: 1px solid #ddd;
	  border-right: 1px solid #ddd;
	  border-top: 1px soid #ddd;
	  border-bottom: 1px solid #ddd; /* 행 구분선 */
	}
	
	tr:hover {
	  background-color: #e0e0e0; /* 마우스 오버 시 강조 */
	}
	
	tr:nth-child(even) {
	  background-color: #f9f9f9; /* 짝수 행 다른 배경 색 */
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
		window.open("${pageContext.request.contextPath}/member/insertMember", "popupWindow", "width=500, height=500, scrollbars=yes, resizable=no");
	}
</script>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>P1 프로젝트 메인 페이지</title>
</head>
<body>
	<div style="width: 100%; margin-left: 1% ">
		<br><h1 style="margin: 15px;">P1 프로젝트 메인페이지</h1>
	</div><br>
	
	<div style="width: 97.5%; margin-left: 1.5%; background-color: #eee;">
		<div style="width: 76.9%; background-color: #eee;">
			<!-- 카테고리 표시 -->
			<table>
				<tr>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">전체</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=영화">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">영화</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=오락">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">오락</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=다큐">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">다큐</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=드라마">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">드라마</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=만화">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">만화</button>
						</a>
					</td>
					<td style="text-align: center; background: #eee">
						<a href="${pageContext.request.contextPath}/Board/boardList?category=기타">
							<button type="button" style="width: 100%; border: none; font-size: 17px; background: none;">기타</button>
						</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div style="display: flex; justify-content: space-between;">
		<!-- 게시판 관련 파트 -->
		<div style="width: 78%; padding: 20px; margin: 5px;">
			<div style="height: 450px;">
			<div style="display: flex; justify-contnent: space-between;">
				<div style="width: 90.4%;">
					<!-- 게시판 타이틀 표시 -->
					<h3>
						<c:if test="${category == null}">
							전체 
						</c:if>
						<c:if test="${category != null}">
							${category} 
						</c:if>
						게시판
					</h3>
				</div>
				<div>
					<!-- 게시글 입력 버튼 표시 -->
					<c:if test="${sessionScope.loginId != null}">
						<div style="margin-top: 23%; text-align: right;">
						<a href="${pageContext.request.contextPath }/Board/insertBoard"><button type="button">게시글 작성하기</button></a>
					</div>
					</c:if>
				</div>
			</div>
			
			<!-- 내용 표시 -->
			<table style="width: 100%;">
				
				<tr>
					<th style="width: 7%; background: lightgray; text-align: center;">번호</th>
					<th style="width: 7%; background: lightgray; text-align: center;">카테고리</th>
					<th style="background: lightgray; text-align: center;">제목</th>
				</tr>
				<c:forEach items="${boardList}" var="m">
					<tr>
						<td style="background: #efefef;">${m.articleNo}</td>
						<td>${m.category}</td>
						<td>
							<c:if test="${sessionScope.loginId == null}">
								${m.articleTitle} (${m.commentCount})
								<c:if test="${m.fileCount != 0}">
									<a style="font-size: 14px;">
										&#128190;
									</a>
								</c:if>
							</c:if>
							<c:if test="${sessionScope.loginId != null}">
								<a href="${pageContext.request.contextPath }/Board/boardOne?articleNo=${m.articleNo}">
									${m.articleTitle} (${m.commentCount})
									<c:if test="${m.fileCount != 0}">
										<a style="font-size: 14px;">
											&#128190;
										</a>
									</c:if>
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
			</div><br>
			
			<!-- 페이징 관련 파트 -->
			<div>
			<div style="display: flex; justify-contnent: space-between;">
				<div style="width: 80%; margin: 5px;">
					<c:if test="${currentPage > 1}">
						<a href="${pageContext.request.contextPath}/Board/boardList?currentPage=1">
						<button type="button">처음페이지</button>
						</a>
						<a href="${pageContext.request.contextPath}/Board/boardList?currentPage=${currentPage-1}">
							<button type="button">이전페이지</button>
						</a>
					</c:if>
				</div>
				<div style="width: 10%; margin: 5px;">
					[ ${currentPage} ]
				</div>
				<div style="width: 80%; margin: 5px; text-align: right;">
					<c:if test="${currentPage < lastPage}">
						<a href="${pageContext.request.contextPath}/Board/boardList?currentPage=${currentPage+1}">
							<button type="button">다음페이지</button>
						</a>
						<a href="${pageContext.request.contextPath}/Board/boardList?currentPage=${lastPage}">
							<button type="button">마지막페이지</button>
						</a>
					</c:if>
				</div>
			</div>
		</div>
		</div>
		
		<!-- 계정 관련 파트 -->
		<div style="width: 22%; padding-right: 20px; margin-top: 25px; ">
			<h3>회원정보</h3>
			<c:if test="${sessionScope.loginId == null}">
				<form method="post" action="${pageContext.request.contextPath}/login">
					<table>
						<tr>
							<th colspan="3" style="background: lightgray; text-align: center;">로그인</th>
						</tr>
						<tr>
							<td>아이디</td>
							<td><input type="text" name="memberId" style="width: 100%;"></td>
							<td rowspan="2">
								<!-- 3열에 버튼 배치 -->
								<button type="submit" style="width: 100%; height: 50px;">로그인</button>
							</td>
						</tr>
						<tr>
							<td>패스워드</td>
							<td><input type="password" name="memberPw" style="width: 100%;"></td>
						</tr>
						<tr>
							<td colspan="3" style="text-align: right;">
								<a>
									<button type="button" onclick="openPopup()" style="width: 100px;">회원가입</button>
								</a>
							</td>
						</tr>
					</table>
				</form>
			</c:if>
			
			<c:if test="${sessionScope.loginId != null}">
				<table style="width: 100%;">
					<tr>
						<th colspan="2" style="background: lightgray; text-align: center;">로그인 정보</th>
					</tr>
					<tr>
						<td>ID</td>
						<td><c:out value="${sessionScope.loginId}"/>님</td>
					</tr>
					<tr>
						<td colspan="2">
							<a href="${pageContext.request.contextPath}/member/myPage">
								<button type="button" style="width: 100%;">마이페이지</button>
							</a>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<a href="${pageContext.request.contextPath}/logout">
								<button type="button" style="width: 100%;">로그아웃</button>
							</a>
						</td>
					</tr>
				</table>
			</c:if>
		</div>
	</div>
	
	<!-- footer.jsp -->
	<div>
		<c:import url="/WEB-INF/view/Inc/footer.jsp"></c:import>
	</div>	
	
</body>
</html>