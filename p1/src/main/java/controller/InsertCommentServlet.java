package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import vo.*;
import dao.*;
import db.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Board/insertComment")
public class InsertCommentServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 정보 불러오기.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 세션 유효성 검사.
		if (loginId == null) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		System.out.println(request.getParameter("articleNo"));
		System.out.println(request.getParameter("comment"));
		// 파라미터 유효성 검사 - aritcleNo, comment 둘 다 없을 경우.
		if ((request.getParameter("comment") == null || request.getParameter("comment").equals(""))
				&& (request.getParameter("articleNo") == null || request.getParameter("articleNo").equals(""))) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		
		// 파라미터 유효성 검사 - comment만 없을 경우.
		if (request.getParameter("comment") == null || request.getParameter("comment").equals("")) {
			response.sendRedirect(request.getContextPath() + "/Board/boardOne?articleNo=" + articleNo);
			return;
		}
		String comment = request.getParameter("comment");
		
		Connection conn = null;
		try {
			// 데이터베이스 연결.
			conn = DBUtil.getConnection();
			
			// boardComment 객체에 정보 set.
			BoardComment boardComment = new BoardComment();
			boardComment.setArticleNo(articleNo);
			boardComment.setComment(comment);
			boardComment.setMemberId(loginId);
			
			// 입력한 댓글을 데이터베이스에 추가.
			BoardCommentDao boardCommentDao = new BoardCommentDao();
			boardCommentDao.insertBoardComment(conn, boardComment);
			
			// 페이지 다시 로드.
			response.sendRedirect(request.getContextPath() + "/Board/boardOne?articleNo=" + articleNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
