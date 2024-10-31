package vo;

public class BoardArticle {
	private int articleNo;
	private String memberId;
	private String articleTitle;
	private String articleContent;
	private String createDate;
	private String category;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getArticleNo() {
		return articleNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public String getArticleContent() {
		return articleContent;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "BoardArticle [articleNo=" + articleNo + ", memberId=" + memberId + ", articleTitle=" + articleTitle
				+ ", articleContent=" + articleContent + ", createDate=" + createDate + ", category=" + category + "]";
	}
}
