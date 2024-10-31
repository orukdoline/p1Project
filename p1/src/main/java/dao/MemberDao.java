package dao;

import java.sql.*;
import java.util.*;

import db.*;
import vo.*;

public class MemberDao {
	// DB에 가입하는 회원정보 삽입.
	public void insertMemberDao(Connection conn, Member m) throws Exception {
		String sql = "insert member(member_id, member_pw, createdate) values(?, ?, now())";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, m.getMemberId());
		stmt.setString(2, m.getMemberPw());
		System.out.println("insertMemberDao.stmt : " + stmt);
		stmt.executeUpdate();
	}
	
	// 로그인 프로세스 처리.
	public String login(Connection conn, Member m) throws Exception {
		String loginId = null;
		String sql = """
				select member_id memberId
				from member 
				where member_id = ? and member_pw = ?
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, m.getMemberId());
		stmt.setString(2, m.getMemberPw());
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			loginId = rs.getString("memberId");
		}
		return loginId;
	}
	
	// 아이디 중복 확인.
	public String idCheck(Connection conn, String inputId) throws Exception {
		String checkedId = null;
		String sql = "select member_id from member where member_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, inputId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			checkedId = rs.getString("member_id");
		}
		rs.close();
		stmt.close();
		System.out.println(checkedId);
		return checkedId;
	}
	
	// myPage 멤버 정보 조회.
	public Member selectMember(Connection conn, String id) throws Exception {
		Member m = new Member();
		String sql = "select member_id, createdate from member where member_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		System.out.println("selectMember : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			m.setMemberId(rs.getString("member_id"));
			m.setCreateDate(rs.getString("createdate"));
		}
		stmt.close();
		return m;
	}
	
	// myPage 멤버 활동 조회.
	public Map<String, Integer> selectMemberActivity(Connection conn, String id) throws Exception {
		Map<String, Integer> activity = new HashMap<>();
		String sql = """
				select member.member_id, 
				    ifnull(a.cnt, 0) as article_count, 
				    ifnull(f.cnt, 0) as file_count, 
				    ifnull(c.cnt, 0) as comment_count 
				from member 
				left outer join (
				    select member_id, count(*) as cnt 
				    from board_article 
				    group by member_id 
				) a on member.member_id = a.member_id 
				left outer join (
				    select ba.member_id, count(*) as cnt 
				    from board_file bf
				    left join board_article ba on bf.article_no = ba.article_no
				    group by ba.member_id
				) f on member.member_id = f.member_id 
				left outer join (
				    select ba.member_id, count(*) as cnt 
				    from board_comment bc
				    left join board_article ba on bc.article_no = ba.article_no
				    group by ba.member_id
				) c on member.member_id = c.member_id 
				where member.member_id = ?
				""";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		System.out.println("selectMemberActivity stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			activity.put("articleCount", rs.getInt("article_count"));
			activity.put("fileCount", rs.getInt("file_count"));
			activity.put("commentCount", rs.getInt("comment_count"));
		}
		rs.close();
		stmt.close();
		return activity;
	}
	
	// updateMemberPw.jsp 멤버 비밀번호 수정.
	public int updateMemberPw(Connection conn, String id, String newPw) throws Exception {
		String sql = "update member set member_pw = ? where member_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, newPw);
		stmt.setString(2, id);
		System.out.println("updateMemberPw stmt : " + stmt);
		int row = stmt.executeUpdate();
		stmt.close();
		return row;
	}
	
	// updateMemberPw.jsp 멤버 기존 비밀번호 조회.
	public String selectMemberPw(Connection conn, String id) throws Exception {
		String pw = null;
		String sql = "select member_pw from member where member_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		System.out.println("selectMemberPw stmt : " + stmt);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			pw = rs.getString("member_pw");
		}
		rs.close();
		stmt.close();
		return pw;
	}
	
	// deleteMember.jsp 멤버 삭제 후, 탈퇴 회원 테이블로 이동.
	public int deleteMember(Connection conn, String id) throws Exception {
		int resultRow = 0;
		String insertSql = "insert into member_away(away_id, away_date) values(?, now())";
		PreparedStatement insertStmt = conn.prepareStatement(insertSql);
		insertStmt.setString(1, id);
		System.out.println("deleteMember insert stmt :" + insertStmt);
		int insertRow = insertStmt.executeUpdate();
		insertStmt.close();
		
		if (insertRow == 1) {
			String deleteSql = "delete from member where member_id = ?";
			PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
			deleteStmt.setString(1, id);
			System.out.println("deleteMember delete stmt : " + deleteStmt);
			int deleteRow = deleteStmt.executeUpdate();
			deleteStmt.close();
			resultRow++;
		}
		return resultRow;
	}
}
