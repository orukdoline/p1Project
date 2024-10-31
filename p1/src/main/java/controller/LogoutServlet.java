package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 유효성 검사
		
		// 세션 내용 초기화.
		request.getSession().invalidate();
		
		// 로그아웃 관련 뷰가 없기 때문에, login 컨트롤러로 보냄.
		response.sendRedirect(request.getContextPath() + "/Board/boardList");
	}
}
