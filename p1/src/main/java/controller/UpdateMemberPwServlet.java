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

@WebServlet("/member/updateMemberPw")
public class UpdateMemberPwServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 정보 가져오기.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 세션 유효성 검사.
		if (loginId == null) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		// 뷰 이동. 
		request.getRequestDispatcher("/WEB-INF/view/member/updateMemberPw.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 가져오기.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 세션 유효성 검사.
		if (loginId == null) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		// 파라미터 값 가져오기.
		String oldPw = request.getParameter("oldPw");
		String newPw = request.getParameter("newPw");
		
		// 파리미터 유효성 검사.
		if (oldPw == null || oldPw.equals("") || newPw == null || newPw.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
            out.println("<script>alert('올바른 입력이 아닙니다.');</script>");
            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
            out.close();
            return;
		}
		
		Connection conn = null;
		try {
			// 데이터베이스 연결.
			conn = DBUtil.getConnection();
			
			// 기존 패스워드 정보 데이터베이스에서 불러오기.
			MemberDao memberDao = new MemberDao();
			String realOldPw = memberDao.selectMemberPw(conn, loginId);
			
			// 데이터베이스에 있는 패스워드와 입력한 패스워드가 같지않다면,
			if (!realOldPw.equals(oldPw)) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
	            out.println("<script>alert('기존 비밀번호를 잘못입력하셨습니다.');</script>");
	            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
	            out.close();
	            return;
			}
			
			// 데이터베이스에 패스워드 정보 업데이트.
			int row = memberDao.updateMemberPw(conn, loginId, newPw);
			
			if (row == 1) {
				// 정상적으로 모든 코드가 수행되었다면, 창 닫기.
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('비밀번호가 정상적으로 수정되었습니다.');</script>");
	            out.println("<script>window.close()</script>;"); // 팝업창 닫기
	            out.close();
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
	            out.println("<script>alert('올바르지 않은 데이터베이스 처리입니다.');</script>");
	            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
	            out.close();
			}
			
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
