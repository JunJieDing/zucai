package per.jason.ding.pdfScan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import per.jason.ding.pdfScan.domain.CompanyRate;
import per.jason.ding.pdfScan.domain.GameRate;
import per.jason.ding.pdfScan.domain.PdfScanPoint;
import per.jason.ding.pdfScan.domain.Rate;

@Service
public class ScanService {
	
	@Autowired
	private ScanPdf scanPdf;

	@Value(value="${file.url}")
	private String fileurl;
	
	private static String MAX = "max";
	private static String MIN = "min";
	private static String RESULT = "result";
	private static int init_win = 0;
	private static int init_even = 1;
	private static int init_lose = 2;
	private static int init_curry_win = 3;
	private static int init_curry_even = 4;
	private static int init_curry_lose = 5;
	private static int win = 6;
	private static int even = 7;
	private static int lose = 8;
	private static int curry_win = 9;
	private static int curry_even = 10;
	private static int curry_lose = 11;
	
	public void outputResult(String inputUrl){
		String url = fileurl==null?inputUrl:fileurl;
		Map<String,GameRate> map = processInitRate(scanInitRate(url));
		processCurrentRate(scanCurrentRate(url),map);
		for(GameRate gr: map.values()){
			calGameRate(gr);
		}
	}
	
	public Map<Integer,Double> calGameRate(GameRate gr){
		Map<Integer ,Double> result = new HashMap<Integer, Double>();
		String gameName = gr.getGameName();
		List<Double> winRates = new ArrayList<Double>();
		List<Double> evenRates = new ArrayList<Double>();
		List<Double> loseRates = new ArrayList<Double>();
		List<Double> winCurryRates = new ArrayList<Double>();
		List<Double> evenCurryRates = new ArrayList<Double>();
		List<Double> loseCurryRates = new ArrayList<Double>();
		for(CompanyRate cr : gr.getCompanyRate().values()){
			winRates.add(cr.getInitRate().getWin().doubleValue()*100);
			evenRates.add(cr.getInitRate().getEven().doubleValue()*100);
			loseRates.add(cr.getInitRate().getLose().doubleValue()*100);
			winCurryRates.add(cr.getInitCurry().getWin().doubleValue()*100);
			evenCurryRates.add(cr.getInitCurry().getEven().doubleValue()*100);
			loseCurryRates.add(cr.getInitCurry().getLose().doubleValue()*100);
		}
//		result.put(init_win, calDiffent(winRates).get(RESULT));
		System.out.println(gameName);
		System.out.println(calDiffent(winRates).get(RESULT)/1+"    "+calDiffent(evenRates).get(RESULT)/1+"    "+calDiffent(loseRates).get(RESULT)/1);
		System.out.println(calDiffent(winCurryRates).get(RESULT)/1+"    "+calDiffent(evenCurryRates).get(RESULT)/1+"    "+calDiffent(loseCurryRates).get(RESULT)/1);
		return result;
	}
	
	public Map<String, Double> calDiffent(List<Double> rates){
		Double max = new Double(-1);
		Double min = new Double(99999);
		for(Double rate : rates){
			max = rate.compareTo(max)>0?rate:max;
			min = rate.compareTo(min)<0?rate:min;
		}
		Map<String,Double> result = new HashMap<String,Double>();
		result.put(MAX, max);
		result.put(MIN, min);
		result.put(RESULT, max-min);
		return result;
	}
	
	public Map<String,GameRate> processCurrentRate(List<String> current,Map<String,GameRate> initMap){
		int i = 0;
		for(String key : initMap.keySet()){
			GameRate game = initMap.get(key);
//			String current.get(i);
			
			i++;
		}
		return initMap;
	}
	
	public Map<String, String> processCurrent(String item){
		Map<String, String> map = new HashMap<String, String>();
//		map.put(, value)
		
		return map;
	}
	
	public Map<String,GameRate>  processInitRate(List<String> inits){
		Map<String,GameRate> map = new LinkedHashMap<String, GameRate>();
		for(String init : inits){
			GameRate game = new GameRate();
			String[] lines = init.split("\n");
			for(int i=0 ; i<lines.length ; i++){
				CompanyRate company = new CompanyRate();
				String line = lines[i];
				if(i==0){
					game.setGameName(line.substring(line.indexOf("、")+1,line.lastIndexOf(" ")));
				}
				if(i>1){
					String[] items = line.split(" ");
					company.setCompanyName(items[0]);
					company.setInitRate(new Rate(items[1],items[2],items[3]));
					company.setInitCurry(new Rate(items[4],items[5],items[6]));
					game.getCompanyRate().put(company.getCompanyName(), company);
				}
			}
			map.put(game.getGameName(), game);
		}
		return map;
	}
	

	public List<String> scanInitRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx = {290,435,590,750};
		Integer beginy = 580;
		Integer[] endx = {435,590,750,900};
		Integer iterWigth = 160;
		Integer iterHeight = 100;
		Integer page= 2;
		for(int i = -1,j=2 ; i<=14 ; i++){
			if(i>0){
				PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[j%4], beginy, endx[j%4]-beginx[j%4], iterHeight,"VS", "推介", 1500,"VS") ;
				points.add(point);
				j++;
			}
			if(i==2||i==6||i==10){
				beginy += (iterHeight-40);
			}
			
		}
//		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
		return scanPdf.scanPdf(url, points);
	}
	
	
	public List<String> scanCurrentRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx={150,600};
		Integer[] endx={360,810};
		Integer iterHeight = 200;
		Integer beginy = 1150;
		Integer page = 2;  
		for(int i=0; i<14;i++){
			PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[i%2], beginy, endx[i%2]-beginx[i%2], iterHeight,"凯利指数 赔付", "", beginy+300,"周三99家平均") ;
			points.add(point);
			if(i!=1&&i!=7&&i%2==1){
				beginy +=(iterHeight+250);
			}else if(i==1||i==7){
				beginy=205;
				page +=1;
			}
		}
//		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
		return scanPdf.scanPdf(url, points);
	}
	
	
}
