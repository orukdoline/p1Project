package vo;

public class BoardComment {
	private int commentNo;
	private int articleNo;
	private String memberId;
	private String comment;
	private String createDate;
	
	public int getCommentNo() {
		return commentNo;
	}
	public int getArticleNo() {
		return articleNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getComment() {
		return comment;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}
	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "BoardComment [commentNo=" + commentNo + ", articleNo=" + articleNo + ", memberId=" + memberId
				+ ", comment=" + comment + ", createDate=" + createDate + "]";
	}
}
