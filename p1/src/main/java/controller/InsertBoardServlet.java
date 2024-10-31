package controller;

import java.io.*;
import java.nio.file.Files;
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
import jakarta.servlet.http.Part;
import vo.*;

@WebServlet("/Board/insertBoard")
public class InsertBoardServlet extends HttpServlet {
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
		
		// 뷰 이동. 
		request.getRequestDispatcher("/WEB-INF/view/Board/insertBoard.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String loginId = (String)(session.getAttribute("loginId"));
		
		// 로그아웃 되어 있는 상태라면
		if(loginId == null) {
			response.sendRedirect(request.getContextPath()+"/Board/boardList");
			return;
		}
		
		// 폼에서 입력받은 데이터 가져오기.
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String category = request.getParameter("category");
		List<Part> fileParts = new ArrayList<>();
		for (Part part : request.getParts()) {
			if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
	            fileParts.add(part);
	        }
	    }
		
		// 폼에서 값이 제대로 들어오지 않았다면,
		if (title == null || title.equals("") || content == null || content.equals("")
				|| category == null || category.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
            out.println("<script>alert('올바른 입력이 아닙니다.');</script>");
            out.println("<script>history.back();</script>"); // 이전 페이지로 돌아가도록
            out.close();
            return;
		}
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 작업을 위해서 자동커밋 정지.
			
			// 가져온 데이터를 boardArticle, boardFile 객체에 set.
			BoardArticle article = new BoardArticle();
			article.setMemberId(loginId);
			article.setArticleTitle(title);
			article.setArticleContent(content);
			article.setCategory(category);
			
			// 폼에서 입력한 게시물 정보를 데이터베이스에 저장.
			BoardArticleDao boardArticleDao = new BoardArticleDao();
			Map<String, Integer> resultMap = boardArticleDao.insertArticle(conn, article);
			
			// 게시물 정보가 정상적으로 데이터베이스에 삽입되었다면,
			List<Map<String, Object>> fileInfo = new ArrayList<>();
			List<BoardFile> boardFileList = new ArrayList<>();
			
			// 파일이 입력되지 않고 게시물만 입력된 경우, 커밋 후 메인페이지 로드.
			if (resultMap.get("row") == 1 && fileParts.size() == 0) {
				conn.commit();
				response.sendRedirect(request.getContextPath() + "/Board/boardList");
				return;
			}
			
			if (resultMap.get("row") == 1 && fileParts.size() != 0) {
				int row = 0;
				for (Part part : fileParts) {
					Map<String, Object> value = new HashMap<>();
					// 폼에 있는 파일 이름을 로드한 후, 파일에 있는 '.'문자의 위치를 변수에 저장.
					value.put("index", part.getSubmittedFileName().lastIndexOf("."));
					// '.'문자를 기준으로 오른쪽에 있는 문자열 추출.
					value.put("fileExt", part.getSubmittedFileName().substring((Integer)value.get("index") + 1));
					// '.'문자를 기준으로 왼쪽에 있는 문자열 추출.
					value.put("originFilename", part.getSubmittedFileName().substring(0, (Integer)value.get("index")));
					// UUID 값을 랜덤으로 생성한 후, 문자열로 변환한 후, '-'문자를 제거.
					value.put("fileName", UUID.randomUUID().toString().replace("-", ""));
					fileInfo.add(value);
					
					// 폼에서 입력한 파일 정보를 boardFile 객체에 set.
					BoardFile boardFile = new BoardFile();
					boardFile.setArticleNo(resultMap.get("articleNo"));
					boardFile.setFileExt((String)value.get("fileExt"));
					boardFile.setFileName((String)value.get("fileName"));
					boardFile.setOriginFileName((String)value.get("originFilename"));
					boardFileList.add(boardFile);
					
					System.out.println("확인 : " + boardFile.getOriginFileName());
				}
				
				// 폼에 입력한 파일 정보를 데이터베이스에 저장.
				BoardFileDao boardFileDao = new BoardFileDao();
				for (BoardFile boardFile : boardFileList) {
					row += boardFileDao.insertFile(conn, boardFile);
				}
				
				// 파일 정보가 정상적으로 데이터베이스에 입력되었다면, 파일 복사 수행.
				if (row == fileInfo.size()) {
					String path = request.getServletContext().getRealPath("/upload");
					int index = 0;
					for (BoardFile boardFile : boardFileList) {
						File f = new File(path, boardFile.getFileName() + "." + boardFile.getFileExt());
						InputStream is = fileParts.get(index).getInputStream(); // 입력된 파일의 inputstream
						OutputStream os = Files.newOutputStream(f.toPath()); // 저장할 파일의 outputstream
						is.transferTo(os); // 파일 복사가 이루어짐.
						is.close();
						os.close();
						index++;
					}
					
				}
				
				// try절이 정상적으로 수행되었다면, 데이터베이스 커밋 수행.
				conn.commit();
				
				// 메인 페이지로 이동.
				response.sendRedirect(request.getContextPath() + "/Board/boardList");
			}
		} catch (Exception e) {
			// try절이 정상적으로 수행되지 않았다면, 데이터베이스 롤백 수행.
			try {
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
