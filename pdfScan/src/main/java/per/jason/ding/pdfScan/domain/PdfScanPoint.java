package per.jason.ding.pdfScan.domain;

public class PdfScanPoint {

	private Integer pageNum ;
	private Integer x; 
	private Integer y;
	private Integer width;
	private Integer height;
	
	public static PdfScanPoint genPoint(Integer page,Integer x,Integer y,Integer width,Integer height){
		PdfScanPoint p = new PdfScanPoint();
		p.setPageNum(page);
		p.setX(x);
		p.setY(y);
		p.setWidth(width);
		p.setHeight(height);
		return p;
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
