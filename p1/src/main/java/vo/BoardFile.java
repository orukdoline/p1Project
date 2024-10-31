package vo;

public class BoardFile {
	private int fileNo;
	private int articleNo;
	private String originFileName;
	private String fileName;
	private String fileExt;
	private String createDate;
	
	public int getFileNo() {
		return fileNo;
	}
	public int getArticleNo() {
		return articleNo;
	}
	public String getOriginFileName() {
		return originFileName;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}
	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "BoardFile [fileNo=" + fileNo + ", articleNo=" + articleNo + ", originFileName=" + originFileName
				+ ", fileName=" + fileName + ", fileExt=" + fileExt + ", createDate=" + createDate + "]";
	}
}
