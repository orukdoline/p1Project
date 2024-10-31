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
import jakarta.servlet.http.HttpSession;
import vo.*;

@WebServlet("/Board/boardOne")
public class BoardOneServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 유효성 검사.
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 로그아웃 되어 있는 상태라면
		if(loginId == null) {
			response.sendRedirect(request.getContextPath()+"/Board/boardList");
			return;
		}
		
		// 파라미터 유효성 검사.
		if (request.getParameter("articleNo") == null) {
			response.sendRedirect(request.getContextPath()+"/Board/boardList");
			return;
		}
		
		// 이전 페이지에서 클릭한 게시물 번호 가져오기.
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			
			// 데이터베이스에서 게시글 조회.
			BoardArticleDao boardArticleDao = new BoardArticleDao();
			Map<String, Object> boardOne = boardArticleDao.selectBoardOne(conn, articleNo);
			
			// 데이터베이스에서 파일 조회.
			BoardFileDao boardFileDao = new BoardFileDao();
			List<BoardFile> boardFileList = boardFileDao.selectFileList(conn, articleNo);
			
			// 데이터베이스에서 댓글 조회.
			BoardCommentDao boardCommentDao = new BoardCommentDao();
			List<BoardComment> boardCommentList = boardCommentDao.selectBoardComment(conn, articleNo);
			
			// 
			request.setAttribute("loginId", loginId);
			request.setAttribute("boardOne", boardOne);
			request.setAttribute("boardFileList", boardFileList);
			request.setAttribute("boardCommentList", boardCommentList);
			
			// 뷰 이동.
			request.getRequestDispatcher("/WEB-INF/view/Board/boardOne.jsp").forward(request, response);
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
