package per.jason.ding.pdfScan.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WeekResult {

	private Map<String, GamePoint> points = new LinkedHashMap<String,GamePoint>();
	
	private Date scanDate;
	
	private Map<String, GameRate> rates = new LinkedHashMap<String,GameRate>();

	public Map<String, GameRate> getRates() {
		return rates;
	}

	public void setRates(Map<String, GameRate> rates) {
		this.rates = rates;
	}

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
