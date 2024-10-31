package controller;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;

import db.*;
import vo.*;
import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/Board/updateBoard")
public class UpdateBoard extends HttpServlet {
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
		
		// 파리미터 값 유효성 검사.
		if (request.getParameter("articleNo") == null || request.getParameter("articleNo").equals("")) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		
		Connection conn = null;;
		try {
			// 데이터베이스 연결.
			conn = DBUtil.getConnection();
			
			// 데이터베이스에서 게시물 내용 조회.
			BoardArticleDao boardDaoArticle = new BoardArticleDao();
			Map<String, Object> boardOne = boardDaoArticle.selectBoardOne(conn, articleNo);
			
			// 데이터베이스에서 파일 리스트 조회.
			BoardFileDao boardFileDao = new BoardFileDao();
			List<BoardFile> fileList = boardFileDao.selectFileList(conn, articleNo);
			
			
			// boardOne 객체를 request 객체에 저장하여 JSP 뷰 페이지로 전달.
			request.setAttribute("boardOne", boardOne);
			request.setAttribute("fileList", fileList);
			
			// 뷰 이동. 
			request.getRequestDispatcher("/WEB-INF/view/Board/updateBoard.jsp").forward(request, response);
			
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
		
		// 파라미터 가져오기.
		int articleNo = Integer.parseInt(request.getParameter("articleNo"));
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] deleteFileList = request.getParameterValues("deleteFile");
		List<Part> fileParts = new ArrayList<>();
		for (Part part : request.getParts()) {
			if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
				fileParts.add(part);
			}
		}
		
		// 파라미터 유효성 검사.
		if (category == null || category.equals("") || title == null || title.equals("")
				|| content == null || content.equals("")) {
			response.sendRedirect(request.getContextPath() + "/Board/boardList");
			return;
		}
		
		// 게시물 정보 객체화.
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setArticleNo(articleNo);
		boardArticle.setArticleTitle(title);
		boardArticle.setArticleContent(content);
		boardArticle.setCategory(category);
		
		Connection conn = null;
		try {
			// 데이터베이스 연결.
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			// 데이터베이스에서 게시글 정보 업데이트.
			BoardArticleDao boardArticleDao = new BoardArticleDao();
			int row = boardArticleDao.updateBoard(conn, boardArticle);
			
			if (row == 1) {
				// 삭제할 파일이 있다면,
				if (deleteFileList != null) {
					// 데이터베이스에서 파일정보 삭제.
					BoardFileDao boardFileDao = new BoardFileDao();
					int fileRow = boardFileDao.deleteFileList(conn, deleteFileList);
				
					if (fileRow == deleteFileList.length) {
						// 파일 삭제.
						String path = request.getServletContext().getRealPath("/upload");
						for (int i = 0; i < deleteFileList.length; i++) {
							File f = new File(path, deleteFileList[i]);
							f.delete();
						}
					}
				}
				
				// 추가할 파일이 있다면,
				if (fileParts.size() != 0) {
					List<BoardFile> boardFileList = new ArrayList<>();
					for (Part part : fileParts) {
						// 입력된 파일의 실제 파일명을 가져와서 마지막 '.'이 있는 인덱스 번호 저장.
						int index = part.getSubmittedFileName().lastIndexOf('.');
						
						// 폼에서 입력받은 파일 정보를 boardFile 객체에 set.
						BoardFile boardFile = new BoardFile();
						boardFile.setArticleNo(articleNo);
						boardFile.setFileExt(part.getSubmittedFileName().substring(index + 1)); // 파일 확장자 추출.
						boardFile.setOriginFileName(part.getSubmittedFileName().substring(0, index)); // 실제 파일명을 추출.
						boardFile.setFileName(UUID.randomUUID().toString().replace("-", "")); // UUID 생성 후, 저장할 파일명 정의.
						boardFileList.add(boardFile); // 리스트에 추가.
					}
					
					// 데이터베이스에 파일정보 추가.
					BoardFileDao boardFileDao = new BoardFileDao();
					int count = 0;
					for (BoardFile boardFile : boardFileList) {
						count += boardFileDao.insertFile(conn, boardFile);
					}
					
					// 데이터베이스에 파일정보가 정상적으로 입력되었다면,
					if (count == boardFileList.size()) {
						// 파일을 저장할 경로 불러오기.
						String path = request.getServletContext().getRealPath("/upload");
						// 파일 저장.
						int index = 0;
						for (BoardFile boardFile : boardFileList) {
							File file = new File(path, boardFile.getFileName() + "." + boardFile.getFileExt());
							InputStream is = fileParts.get(index).getInputStream();
							OutputStream os = Files.newOutputStream(file.toPath());
							is.transferTo(os);
							is.close();
							os.close();
							index++;
						}
					}
				}
				
				// 커밋.
				conn.commit();
				
				// 게시물 상세보기 페이지 로드.
				response.sendRedirect(request.getContextPath() + "/Board/boardOne?articleNo=" + articleNo);
			} else {
				conn.rollback();
				// 게시물 수정 페이지 로드.
				response.sendRedirect(request.getContextPath() + "/Board/updateBoard?articleNo=" + articleNo);
			}
		} catch (Exception e) {
			try {
				// 실패 시, 롤백.
				conn.rollback();
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
