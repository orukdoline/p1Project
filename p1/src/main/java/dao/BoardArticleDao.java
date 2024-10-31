package dao;

import java.sql.*;
import java.util.*;
import db.*;
import vo.*;

public class BoardArticleDao {
	// boardList 게시글 출력 관련.
	public List<Map<String, Object>> selectBoardList(Connection conn, String category, int beginRow, int rowPerPage) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = """
				select b.article_no articleNo, 
					b.article_title articleTitle, 
					b.category categoty,
					ifnull(c.cnt, 0) commentCount, 
					ifnull(f.cnt, 0) fileCount
				from board_article b 
				left outer join (
					select article_no, count(*) cnt
					from board_comment
					group by article_no
				) c on b.article_no = c.article_no
				left outer join (
					select article_no, count(*) cnt
					from board_file
					group by article_no
				) f on b.article_no = f.article_no
				""";
		PreparedStatement stmt = null;
		if (category != null) {
			sql += """
				where category=?
				order by b.article_no desc limit ?, ?
				""";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, category);
			stmt.setInt(2, beginRow);
			stmt.setInt(3, rowPerPage);
		}
		else if (category == null) {
			sql += """
					order by b.article_no desc limit ?, ?;
					""";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, beginRow);
			stmt.setInt(2, rowPerPage);
		}
		System.out.println("selectBoardList stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("articleNo", rs.getInt("articleNo"));
			m.put("articleTitle", rs.getString("articleTitle"));
			m.put("commentCount", rs.getInt("commentCount"));
			m.put("fileCount", rs.getInt("fileCount"));
			m.put("category", rs.getString("categoty"));
			list.add(m);
		}
		rs.close();
		stmt.close();
		return list;
	}
	
	// BoardList 게시글 페이지 수 관련.
	public int pageCount(Connection conn) throws Exception {
		int count = 0;
		conn = DBUtil.getConnection();
		String sql = "select count(*) from board_article";
		PreparedStatement stmt = conn.prepareStatement(sql);
		System.out.println("pageCount stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			count = rs.getInt("count(*)");
		}
		rs.close();
		stmt.close();
		return count;
	}
	
	// insertBoard 게시글 추가 관련.
	public Map<String, Integer> insertArticle(Connection conn, BoardArticle article) throws Exception {
		Map<String, Integer> resultMap = new HashMap<>();
		resultMap.put("row", 0);
		resultMap.put("articleNo", 0);
		String sql = """
				insert into board_article(member_id, article_title, article_content, category, createdate)
				values(?,?,?,?,now())
				""";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, article.getMemberId());
		stmt.setString(2,  article.getArticleTitle());
		stmt.setString(3,  article.getArticleContent());
		stmt.setString(4, article.getCategory());
		System.out.println("insertArticle : " + stmt);
		int row = stmt.executeUpdate();
		resultMap.put("row", row);
		
		// 실행 후 auto_increment 키 값을 반환받는 메서드.
		ResultSet rs = stmt.getGeneratedKeys(); // select max(article_no) from board_article
		int articleNo = 0;
		if (rs.next()) {
			articleNo = rs.getInt(1);
			resultMap.put("articleNo", articleNo);
		}
		rs.close();
		stmt.close();
		return resultMap;
	}
	
	// BoardOne 게시글 출력 관련.
	public Map<String, Object> selectBoardOne(Connection conn, int articleNo) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		conn = DBUtil.getConnection();
		String sql = """
				select b.category category, b.article_no articleNo, b.member_id memberId, b.article_title articleTitle, b.article_content articleContent, b.createdate createdate, f.origin_filename originFilename, f.file_ext fileExt  
				from board_article b left outer join board_file f
				on b.article_no = f.article_no
				where b.article_no = ?
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, articleNo);
		System.out.println("selectBoardOne stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			m.put("articleNo", rs.getInt("articleNo"));
			m.put("memberId", rs.getString("memberId"));
			m.put("articleTitle", rs.getString("articleTitle"));
			m.put("articleContent", rs.getString("articleContent"));
			m.put("createdate", rs.getString("createdate"));
			m.put("originFilename", rs.getString("originFilename"));
			m.put("fileExt", rs.getString("fileExt"));
			m.put("category", rs.getString("category"));
		}
		rs.close();
		stmt.close();
		return m;
	}
	
	// BoardOne 게시글 삭제 관련.
	public void deleteBoard(Connection conn, int articleNo) throws Exception {
		int resultRow = 0;
		// 무결성 제약조건을 지키기 위해서 첫 번째로 해당 게시물의 댓글 삭제.
		String commentSql = "delete from board_comment where article_no = ?";
		PreparedStatement commentStmt = conn.prepareStatement(commentSql);
		commentStmt.setInt(1, articleNo);
		System.out.println("deleteBoard commentStmt : " + commentStmt);
		resultRow += commentStmt.executeUpdate();
		commentStmt.close();
		// 두 번째로 해당 게시물의 파일 정보 삭제.
		String fileSql = "delete from board_file where article_no = ?";
		PreparedStatement fileStmt = conn.prepareStatement(fileSql);
		fileStmt.setInt(1, articleNo);
		System.out.println("deleteBoard fileStmt : " + fileStmt);
		resultRow += fileStmt.executeUpdate();
		fileStmt.close();
		// 댓글 삭제가 성공적이라면, 게시물 삭제.
		// 파일, 댓글 개수 조회.
		BoardFileDao boardFileDao = new BoardFileDao();
		int fileCount = boardFileDao.countFile(articleNo);
		BoardCommentDao boardCommentDao = new BoardCommentDao();
		int commentCount = boardCommentDao.countComment(articleNo);
		int passResult = fileCount + commentCount;
		
		if (resultRow == passResult) {
			String boardSql = "delete from board_article where article_no = ?";
			PreparedStatement boardStmt = conn.prepareStatement(boardSql);
			boardStmt.setInt(1, articleNo);
			System.out.println("deleteBoard boardStmt : " + boardStmt);
			boardStmt.executeUpdate();
			boardStmt.close();
		}
	}
	
	// upadateBoard.jsp 게시물 업데이트.
	public int updateBoard(Connection conn, BoardArticle boardArticle) throws Exception {
		String sql = "update board_article set article_title = ?, article_content = ?, category = ? where article_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, boardArticle.getArticleTitle());
		stmt.setString(2, boardArticle.getArticleContent());
		stmt.setString(3, boardArticle.getCategory());
		stmt.setInt(4, boardArticle.getArticleNo());
		System.out.println("updateBoard : " + stmt);
		int row = stmt.executeUpdate();
		stmt.close();
		return row;
	}
	
}
