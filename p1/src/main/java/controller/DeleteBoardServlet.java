package controller;

import java.io.*;
import java.sql.*;
import java.util.*;

import dao.*;
import db.DBUtil;
import vo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Board/deleteBoard")
public class DeleteBoardServlet extends HttpServlet {
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
		
		// 파라미터 유효성 검사.
		if (request.getParameter("articleNo") == null || request.getParameter("articleNo").equals("")) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		// 파리미터 정보 가져오기.
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		
		Connection conn = null;
		try {
			// 데이터베이스 연결.
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			// 파일 리스트 조회.
			// 데이터베이스에서 파일 정보가 삭제되기 전에 실행해야 함.
			BoardFileDao boardFileDao = new BoardFileDao();
			List<BoardFile> boardFileList = boardFileDao.selectFileList(conn, articleNo);
			
			// 게시물 삭제 데이터베이스 실행.
			BoardArticleDao boardArticleDao = new BoardArticleDao();
			boardArticleDao.deleteBoard(conn, articleNo);
			
			// 파일 삭제
			String path = request.getServletContext().getRealPath("/upload");
			for (BoardFile boardFile : boardFileList) {
				File f = new File(path, boardFile.getFileName() + '.' + boardFile.getFileExt());
				f.delete();
			}
			
			// 커밋.
			conn.commit();
			
			// 메인페이지로 이동.
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			
		} catch (Exception e) {
			try {
				// 실패할 시, 롤백.
				conn.rollback();
				System.out.println("삭제기능 롤백발생.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
