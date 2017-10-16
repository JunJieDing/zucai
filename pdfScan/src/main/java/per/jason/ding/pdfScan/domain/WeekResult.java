package per.jason.ding.pdfScan.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeekResult {

	private Map<String, GamePoint> points = new LinkedHashMap<String,GamePoint>();
	
	private Date scanDate;

	public Map<String, GamePoint> getPoints() {
		return points;
	}

	public void setPoints(Map<String, GamePoint> points) {
		this.points = points;
	}

	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	
	
	
}
