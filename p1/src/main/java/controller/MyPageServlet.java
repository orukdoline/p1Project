package controller;

import java.io.*;
import java.sql.*;
import java.util.*;

import dao.*;
import db.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vo.*;

@WebServlet("/member/myPage")
public class MyPageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 로그아웃 되어 있는 상태라면
		if(loginId == null) {
			response.sendRedirect(request.getContextPath()+"/Board/boardList");
			return;
		}
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			MemberDao memberDao = new MemberDao();
			Member member = memberDao.selectMember(conn, loginId);
			Map<String, Integer> memberAtivity = memberDao.selectMemberActivity(conn, loginId);
			
			request.setAttribute("member", member);
			request.setAttribute("memberActivity", memberAtivity);
			
			// 뷰 이동.
			request.getRequestDispatcher("/WEB-INF/view/member/myPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
