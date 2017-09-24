package per.jason.ding.pdfScan.domain;

import java.math.BigDecimal;

public class Rate {

	private BigDecimal win;
	
	private BigDecimal even;
	
	private BigDecimal lose;

	public Rate(String win,String even,String lose){
		this.win = new BigDecimal(win);
		this.even = new BigDecimal(even);
		this.lose = new BigDecimal(lose);
		
	}
	
	public BigDecimal getWin() {
		return win;
	}

	public void setWin(BigDecimal win) {
		this.win = win;
	}

	public BigDecimal getEven() {
		return even;
	}

	public void setEven(BigDecimal even) {
		this.even = even;
	}

	public BigDecimal getLose() {
		return lose;
	}

	public void setLose(BigDecimal lose) {
		this.lose = lose;
	}

	@Override
	public String toString() {
		return "Rate [win=" + win + ", even=" + even + ", lose=" + lose + "]";
	}
	
	
	
}
