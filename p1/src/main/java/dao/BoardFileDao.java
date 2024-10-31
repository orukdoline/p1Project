package dao;

import java.sql.*;
import java.util.*;

import db.DBUtil;
import vo.BoardFile;

public class BoardFileDao {

	// 파일들 정보 조회.
	public List<BoardFile> selectFileList(Connection conn, int articleNo) throws Exception {
		List<BoardFile> list = new ArrayList<BoardFile>();
		String sql = """
				select file_no fileNo, article_no articleNo, origin_filename originFilename, filename, file_ext fileExt
				from board_file
				where article_no = ?
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1,  articleNo);
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			BoardFile bf = new BoardFile();
			bf.setFileNo(rs.getInt("fileNo"));
			bf.setArticleNo(rs.getInt("articleNo"));
			bf.setOriginFileName(rs.getString("originFilename"));
			bf.setFileName(rs.getString("filename"));
			bf.setFileExt(rs.getString("fileExt"));
			list.add(bf);
		}
		stmt.close();
		
		return list;
	}
	
	// insertBoard 파일정보 추가 관련.
	public int insertFile(Connection conn, BoardFile boardFile) throws Exception {
		String sql = """
				insert into board_file(article_no, origin_filename, filename, file_ext, createdate)
				values(?,?,?,?,now())
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, boardFile.getArticleNo());
		stmt.setString(2, boardFile.getOriginFileName());
		stmt.setString(3, boardFile.getFileName());
		stmt.setString(4, boardFile.getFileExt());
		System.out.println("insertFile stmt : " + stmt);
		int row = stmt.executeUpdate();
		stmt.close();
		return row;
	}
	
	// deleteBoard.jsp 삭제를 위한 파일 개수 조회.
	public int countFile(int articleNo) throws Exception {
		int count = 0;
		Connection conn = DBUtil.getConnection();
		String sql = "select count(*) from board_file where article_no = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1,  articleNo);
		System.out.println("countFile stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			count = rs.getInt("count(*)");
		}
		rs.close();
		stmt.close();
		conn.close();
		return count;
	}
	
	// updateBoard.jsp 선택된 파일 삭제.
	public int deleteFileList(Connection conn, String[] deleteFileList)  throws Exception{
		int row = 0;
		String sql = "delete from board_file where filename = ?";
		for (int i = 0; i < deleteFileList.length; i++) {
			// 파일 이름에서 파일 확장자 제거.
			int index = deleteFileList[i].lastIndexOf('.');
			String nonExt = deleteFileList[i].substring(0, index);
			
			// 쿼리문 수행.
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, nonExt);
			System.out.println("deleteFileList[" + i + "] stmt : " + stmt);
			row += stmt.executeUpdate();
			stmt.close();
		}
		return row;
	}
}
