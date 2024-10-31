package controller;

import java.io.*;
import java.sql.*;
import dao.*;
import db.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/member/deleteMember")
public class DeleteMemberServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 가져오기.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 세션 유효성 검사.
		if (loginId == null) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		Connection conn = null;
		try {
			// 데이터베이스 연결. 
			conn = DBUtil.getConnection();
			
			// 데이터베이스에서 멤버 삭제.
			MemberDao memberDao = new MemberDao();
			int row = memberDao.deleteMember(conn, loginId);
			
			// 세션 데이터 삭제.
			session.invalidate();
			
			// 메인페이지로 이동.
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			
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
