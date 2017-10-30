package per.jason.ding.pdfScan.domain;

import java.util.HashMap;
import java.util.Map;

public class GameRate {

	private String date;
	
	private String gameName;
	
	private Map<String,CompanyRate> companyRate = new HashMap<String,CompanyRate>();
	
	private Integer gameNo;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Map<String, CompanyRate> getCompanyRate() {
		return companyRate;
	}

	public void setCompanyRate(Map<String, CompanyRate> companyRate) {
		this.companyRate = companyRate;
	}

	
	public Integer getGameNo() {
		return gameNo;
	}

	public void setGameNo(Integer gameNo) {
		this.gameNo = gameNo;
	}

	@Override
	public String toString() {
		return "GameRate [date=" + date + ", gameName=" + gameName + ", companyRate=" + companyRate + "]";
	}

	
	
}
