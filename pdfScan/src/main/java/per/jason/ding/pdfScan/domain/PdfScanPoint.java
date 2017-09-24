package per.jason.ding.pdfScan.domain;

public class PdfScanPoint {

	private Integer pageNum ;
	private Integer x; 
	private Integer y;
	private Integer width;
	private Integer height;
	
	private String startKeyword1;
	private String startKeyword2;
	private String endKeyword1;
	
	private Integer endY;
	
	
	public static PdfScanPoint genPoint(Integer page,Integer x,Integer y,Integer width,Integer height,String k1, String k2, Integer endY,String k3){
		PdfScanPoint p = new PdfScanPoint();
		p.setPageNum(page);
		p.setX(x);
		p.setY(y);
		p.setWidth(width);
		p.setHeight(height);
		p.setStartKeyword1(k1);
		p.setStartKeyword2(k2);
		p.setEndY(endY);
		p.setEndKeyword1(k3);
		return p;
	}
	
	
	
	public String getEndKeyword1() {
		return endKeyword1;
	}



	public void setEndKeyword1(String endKeyword1) {
		this.endKeyword1 = endKeyword1;
	}



	public String getStartKeyword1() {
		return startKeyword1;
	}


	public void setStartKeyword1(String startKeyword1) {
		this.startKeyword1 = startKeyword1;
	}


	public String getStartKeyword2() {
		return startKeyword2;
	}


	public void setStartKeyword2(String startKeyword2) {
		this.startKeyword2 = startKeyword2;
	}


	public Integer getEndY() {
		return endY;
	}


	public void setEndY(Integer endY) {
		this.endY = endY;
	}


	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	
}
