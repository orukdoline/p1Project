package controller;

import java.io.*;
import java.sql.*;

import vo.*;
import dao.*;
import db.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Board/deleteComment")
public class DeleteCommentServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션에 있는 데이터 불러오기.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 세션 유효성 검사.
		if (loginId == null) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		// 파라미터 유효성 검사.
		if (request.getParameter("commentNo") == null || request.getParameter("commentNo").equals("")
				|| request.getParameter("articleNo") == null || request.getParameter("articleNo").equals("")) {
			response.sendRedirect(request.getContextPath() + "/Board/boardOne");
		}
		int commentNo = Integer.parseInt(request.getParameter("commentNo"));
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		
		// 
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			
			BoardCommentDao boardCommentDao = new BoardCommentDao();
			System.out.println(commentNo);
			boardCommentDao.deleteBoardComment(conn, commentNo);
			response.sendRedirect(request.getContextPath() + "/Board/boardOne?articleNo=" + articleNo);
		} catch (Exception e) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
            out.println("<script>alert('댓글 삭제를 실패하였습니다.');</script>");
            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
            out.close();
			response.sendRedirect(request.getContextPath() + "/Board/boardOne?articleNo=" + articleNo);
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
