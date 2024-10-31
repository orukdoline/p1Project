package controller;

import java.io.*;
import java.sql.*;

import dao.*;
import db.*;
import vo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/member/insertMember")
public class InsertMemberController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 세션 불러오기.
		HttpSession session = request.getSession();
		
		// 세션 유효성 검사.
		if (session.getAttribute("loginId") != null) { // loginId에 값이 있다면,
            PrintWriter out = response.getWriter();
            out.println("<script>window.close()</script>;"); // 팝업창 닫기
            out.close();
		}
		
		// 회원가입 폼 /member/insertMember.jsp
		request.getRequestDispatcher("/WEB-INF/view/member/insertMember.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		// Member 객체에 입력값 저장.
		Member m = new Member();
		m.setMemberId(request.getParameter("id"));
		m.setMemberPw(request.getParameter("pw"));
		
		Connection conn = null;
		// 중복검사 버튼을 클릭하면,
		if ("idCheck".equals(action)) {
			MemberDao memberDao = new MemberDao();
			try {
				System.out.println("중복체크 버튼 동작");
				
				// 데이터베이스 연결.
				conn = DBUtil.getConnection();
				
				// 아이디와 버튼값이 없으면, 경고문구 출력.
				if (request.getParameter("id") == null || request.getParameter("id").equals("")
						|| request.getParameter("action") == null || request.getParameter("action").equals("")) {
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out = response.getWriter();
		            out.println("<script>alert('아이디를 입력해주세요.');</script>");
		            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
		            out.close();
					conn.close();
					return;
				}
				
				// 아이디 중복 검사 관련 쿼리문 실행.
				String checkId = memberDao.idCheck(conn, m.getMemberId());
				
				// 데이터베이스에 같은 아이디가 없다면,
				if (checkId == null) {
					response.setContentType("text/html; charset=UTF-8");
		            PrintWriter out = response.getWriter();
		            out.println("<script>alert('가입가능한 아이디입니다.');</script>");
		            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
		            out.close();
		            conn.close();
		        // 데이터베이스에 같은 아이디가 있다면,
				} else {
					response.setContentType("text/html; charset=UTF-8");
		            PrintWriter out = response.getWriter();
		            out.println("<script>alert('이미 존재하는 아이디입니다.');</script>");
		            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
		            out.close();
		            conn.close();
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
		// 가입 버튼을 클릭하면,
		} else if ("signUp".equals(action)) {
			try {
				System.out.println("가입 버튼 동작");
				
				// 데이터베이스 연결.
				conn = DBUtil.getConnection();
				
				// 아이디, 비밀번호, 버튼값이 없으면, 경고문구 출력.
				if (request.getParameter("id") == null || request.getParameter("id").equals("")
						|| request.getParameter("pw") == null || request.getParameter("pw").equals("")
						|| request.getParameter("action") == null || request.getParameter("action").equals("")) {
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out = response.getWriter();
		            out.println("<script>alert('아이디나 비밀번호를 입력해주세요.');</script>");
		            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
		            out.close();
					conn.close();
					return;
				}
				
				// 데이터베이스에 입력한 아이디와 같은 아이디가 있으면, 작업 중단.
				conn = DBUtil.getConnection();
				MemberDao memberDao = new MemberDao();
				String checkId = memberDao.idCheck(conn, m.getMemberId());
				if (checkId != null) {
					response.setContentType("text/html; charset=UTF-8");
		            PrintWriter out = response.getWriter();
		            out.println("<script>alert('이미 존재하는 아이디입니다.');</script>");
		            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
		            out.close();
		            response.sendRedirect(request.getContextPath() + "/member/insertMember");
		            conn.close();
		            return;
				}
				
				// 없으면, 새로운 회원 정보 입력 관련 쿼리문 실행 후, 메인페이지 이동.
				memberDao.insertMemberDao(conn, m);
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('정상적으로 가입되셨습니다. 로그인을 해주세요.');</script>");
	            out.println("<script>window.close()</script>;"); // 팝업창 닫기
	            out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/member/insertMember");
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
