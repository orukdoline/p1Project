package controller;

import java.io.*;
import java.sql.*;
import dao.*;
import db.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vo.Member;

@WebServlet("/login")
public class LoginSevlet extends HttpServlet {
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		HttpSession session = request.getSession();
//		String loginId = (String)(session.getAttribute("loginId"));
//		
//		// 세션 유효성 검사 - 로그인이 이미 되어있는 상태라면
//		if (loginId != null) {
//			response.sendRedirect(request.getContextPath() + "/Board/boardList");
//			return;
//		}
//		
//		// 로그인 상태가 아니라면 뷰 호출.
//		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
//	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 이미 로그인이 되어 있는 상태라면
		if(loginId != null) {
			response.sendRedirect(request.getContextPath()+"/Board/boardList");
			return;
		}
		
		// 아이디, 비밀번호 값이 없으면,
		if (request.getParameter("memberId") == null || request.getParameter("memberId").equals("")
				|| request.getParameter("memberPw") == null || request.getParameter("memberPw").equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
            out.println("<script>alert('아이디나 비밀번호를 입력해주세요.');</script>");
            out.println("<script>window.location.href = '" + request.getContextPath() + "/Board/boardList';</script>");
            out.close();
			return;
		}
		
		// ID, PW 값 폼에서 받아온 후 member 객체에 저장.
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		Member m = new Member();
		m.setMemberId(memberId);
		m.setMemberPw(memberPw);
		
		// 로그인 관련 DB 수행.
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			MemberDao memberDao = new MemberDao();
			String resultId = memberDao.login(conn, m);
			if (resultId == null) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
	            out.println("<script>alert('로그인에 실패하였습니다.');</script>");
	            out.println("<script>window.location.href = '" + request.getContextPath() + "/Board/boardList';</script>");
	            out.close();
				// 응용 : login.jsp로 포워딩(+실패 메시지)해도 됨.
				return;
			}
			session.setAttribute("loginId", resultId);
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
