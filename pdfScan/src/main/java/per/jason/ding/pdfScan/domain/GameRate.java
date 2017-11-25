package per.jason.ding.pdfScan.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRate {

	private String date;
	
	private String gameName;
	
	private Map<String,CompanyRate> companyRate = new HashMap<String,CompanyRate>();
	
	private Integer gameNo;
	
	private Set<String> initCompName = new HashSet<String>();
	
	private Set<String> fCompName = new HashSet<String>(Arrays.asList(new String[]{"威廉希尔", "bwin", "bet365", "易胜博"}));
	
	
	
	public Set<String> getfCompName() {
		return fCompName;
	}



	public void setfCompName(Set<String> fCompName) {
		this.fCompName = fCompName;
	}



	public static GameRate initGameRate(String gameName, Integer gameNo){
		GameRate r = new GameRate();
		r.setGameName(gameName);
		r.setGameNo(gameNo);
		return r;
	}
	
	
	
	public Set<String> getInitCompName() {
		return initCompName;
	}



	public void setInitCompName(Set<String> initCompName) {
		this.initCompName = initCompName;
	}



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
