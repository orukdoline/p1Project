package controller;

import java.io.*;
import java.sql.*;
import java.util.*;

import dao.*;
import db.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Board/boardList")
public class BoardListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 유효성 검사.
		
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			BoardArticleDao boardArticleDao = new BoardArticleDao();
			
			// 페이지 수 계산.
			int currentPage = 1;
			if (request.getParameter("currentPage") != null) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			int rowPerpage = 10;
			int beginRow = (currentPage - 1) * rowPerpage;
			int countRow = boardArticleDao.pageCount(conn);
			int lastPage = countRow / rowPerpage;
			if (countRow % rowPerpage != 0) lastPage++;
			
			// 카테고리.
			String category = null;
			if (request.getParameter("category") != null) {
				category = request.getParameter("category");
			}
			
			// DB 데이터 조회.
			List<Map<String, Object>> boardList = boardArticleDao.selectBoardList(conn, category, beginRow, rowPerpage);
			
			// 데이터를 JSP 페이지로 전달.
			request.setAttribute("category", category);
			request.setAttribute("boardList", boardList);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("lastPage", lastPage);
			
			// 뷰 이동.
			request.getRequestDispatcher("/WEB-INF/view/Board/boardList.jsp").forward(request, response);
					
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
