package dao;

import java.sql.*;
import java.util.*;

import db.DBUtil;
import vo.*;

public class BoardCommentDao {
	
	// boardOne.jsp 댓글 조회.
	public List<BoardComment> selectBoardComment(Connection conn, int articleNo) throws Exception {
		List<BoardComment> list = new ArrayList<>();
		String sql = """
				select comment_no, member_id, comment, createdate from board_comment where article_no = ?
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, articleNo);
		System.out.println("selectBoardComment stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			BoardComment b = new BoardComment();
			b.setArticleNo(articleNo);
			b.setMemberId(rs.getString("member_id"));
			b.setComment(rs.getString("comment"));
			b.setCommentNo(rs.getInt("comment_no"));
			b.setCreateDate(rs.getString("createdate"));
			list.add(b);
		}
		rs.close();
		stmt.close();
		return list;
	}
	
	// boardOne.jsp 댓글 삭제.
	public void deleteBoardComment(Connection conn, int commentNo) throws Exception{
		String sql = "delete from board_comment where comment_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, commentNo);
		System.out.println("deleteBoardComment stmt : " + stmt);
		stmt.executeUpdate();
		stmt.close();
	}
	
	// boardOne.jsp 댓글 추가.
	public void insertBoardComment(Connection conn, BoardComment comment) throws Exception {
		String sql = """
				insert into board_comment(article_no, member_id, comment, createdate)
				values(?, ?, ?, now())
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, comment.getArticleNo());
		stmt.setString(2, comment.getMemberId());
		stmt.setString(3, comment.getComment());
		System.out.println("insertBoardComment stmt : " + stmt);
		stmt.executeUpdate();
		stmt.close();
	}
	
	// deleteBoard.jsp 삭제를 위한 댓글 개수 조회.
	public int countComment(int articleNo) throws Exception {
		int count = 0;
		Connection conn = DBUtil.getConnection();
		String sql = "select count(*) from board_comment where article_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1,  articleNo);
		System.out.println("countComment stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			count = rs.getInt("count(*)");
		}
		rs.close();
		stmt.close();
		conn.close();
		return count;
	}

}
