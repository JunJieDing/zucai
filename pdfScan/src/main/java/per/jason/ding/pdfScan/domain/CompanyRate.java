package per.jason.ding.pdfScan.domain;

public class CompanyRate {

	private String companyName;
	
	private Rate initRate;
	
	private Rate currentRate;
	
	private Rate initCurry;
	
	private Rate currentCurry;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Rate getInitRate() {
		return initRate;
	}

	public void setInitRate(Rate initRate) {
		this.initRate = initRate;
	}

	public Rate getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(Rate currentRate) {
		this.currentRate = currentRate;
	}

	public Rate getInitCurry() {
		return initCurry;
	}

	public void setInitCurry(Rate initCurry) {
		this.initCurry = initCurry;
	}

	public Rate getCurrentCurry() {
		return currentCurry;
	}

	public void setCurrentCurry(Rate currentCurry) {
		this.currentCurry = currentCurry;
	}

	@Override
	public String toString() {
		return "CompanyRate [companyName=" + companyName + ", initRate=" + initRate + ", currentRate=" + currentRate
				+ ", initCurry=" + initCurry + ", currentCurry=" + currentCurry + "]";
	}
	
}
