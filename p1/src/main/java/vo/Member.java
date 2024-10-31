package vo;

public class Member {
	private String memberId;
	private String memberPw;
	private String createDate;
	
	public String getMemberId() {
		return memberId;
	}
	public String getMemberPw() {
		return memberPw;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setMemberPw(String memeberPw) {
		this.memberPw = memeberPw;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "Member [memberId=" + memberId + ", memeberPw=" + memberPw + ", createDate=" + createDate + "]";
	}
}
