package per.jason.ding.pdfScan.service;

import java.math.BigDecimal;
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
		List<Integer> winRates = new ArrayList<Integer>();
		List<Integer> evenRates = new ArrayList<Integer>();
		List<Integer> loseRates = new ArrayList<Integer>();
		List<Integer> winCurryRates = new ArrayList<Integer>();
		List<Integer> evenCurryRates = new ArrayList<Integer>();
		List<Integer> loseCurryRates = new ArrayList<Integer>();
		List<Integer> cwinRates = new ArrayList<Integer>();
		List<Integer> cevenRates = new ArrayList<Integer>();
		List<Integer> closeRates = new ArrayList<Integer>();
		List<Integer> cwinCurryRates = new ArrayList<Integer>();
		List<Integer> cevenCurryRates = new ArrayList<Integer>();
		List<Integer> closeCurryRates = new ArrayList<Integer>();
		List<Integer> nwinRates = new ArrayList<Integer>();
		List<Integer> nevenRates = new ArrayList<Integer>();
		List<Integer> nloseRates = new ArrayList<Integer>();
		List<Integer> nwinCurryRates = new ArrayList<Integer>();
		List<Integer> nevenCurryRates = new ArrayList<Integer>();
		List<Integer> nloseCurryRates = new ArrayList<Integer>();
		for(CompanyRate cr : gr.getCompanyRate().values()){
			if(cr.getInitRate()!=null){
				winRates.add(cr.getInitRate().getWin().multiply(new BigDecimal(100)).intValue());
				evenRates.add(cr.getInitRate().getEven().multiply(new BigDecimal(100)).intValue());
				loseRates.add(cr.getInitRate().getLose().multiply(new BigDecimal(100)).intValue());
				winCurryRates.add(cr.getInitCurry().getWin().multiply(new BigDecimal(100)).intValue());
				evenCurryRates.add(cr.getInitCurry().getEven().multiply(new BigDecimal(100)).intValue());
				loseCurryRates.add(cr.getInitCurry().getLose().multiply(new BigDecimal(100)).intValue());
				if(cr.getCurrentRate()==null){
					System.out.println(cr.getCompanyName()+" current rate is null");
				}else{
					cwinRates.add(cr.getCurrentRate().getWin().multiply(new BigDecimal(100)).intValue());
					cevenRates.add(cr.getCurrentRate().getEven().multiply(new BigDecimal(100)).intValue());
					closeRates.add(cr.getCurrentRate().getLose().multiply(new BigDecimal(100)).intValue());
					cwinCurryRates.add(cr.getCurrentCurry().getWin().multiply(new BigDecimal(100)).intValue());
					cevenCurryRates.add(cr.getCurrentCurry().getEven().multiply(new BigDecimal(100)).intValue());
					closeCurryRates.add(cr.getCurrentCurry().getLose().multiply(new BigDecimal(100)).intValue());
				}
			}
			if(cr.getCurrentRate()!=null){
				nwinRates.add(cr.getCurrentRate().getWin().multiply(new BigDecimal(100)).intValue());
				nevenRates.add(cr.getCurrentRate().getEven().multiply(new BigDecimal(100)).intValue());
				nloseRates.add(cr.getCurrentRate().getLose().multiply(new BigDecimal(100)).intValue());
				nwinCurryRates.add(cr.getCurrentCurry().getWin().multiply(new BigDecimal(100)).intValue());
				nevenCurryRates.add(cr.getCurrentCurry().getEven().multiply(new BigDecimal(100)).intValue());
				nloseCurryRates.add(cr.getCurrentCurry().getLose().multiply(new BigDecimal(100)).intValue());
			}
			
		}
//		result.put(init_win, calDiffent(winRates).get(RESULT));
		System.out.println(gameName);
		System.out.println(calDiffent(winRates).get(RESULT)/1+"    "+calDiffent(evenRates).get(RESULT)/1+"    "+calDiffent(loseRates).get(RESULT)/1);
		System.out.println(calDiffent(winCurryRates).get(RESULT)/1+"    "+calDiffent(evenCurryRates).get(RESULT)/1+"    "+calDiffent(loseCurryRates).get(RESULT)/1);
		System.out.println(calDiffent(cwinRates).get(RESULT)/1+"    "+calDiffent(cevenRates).get(RESULT)/1+"    "+calDiffent(closeRates).get(RESULT)/1);
		System.out.println(calDiffent(cwinCurryRates).get(RESULT)/1+"    "+calDiffent(cevenCurryRates).get(RESULT)/1+"    "+calDiffent(closeCurryRates).get(RESULT)/1);
		System.out.println(calDiffent(nwinRates).get(RESULT)/1+"    "+calDiffent(nevenRates).get(RESULT)/1+"    "+calDiffent(nloseRates).get(RESULT)/1);
		System.out.println(calDiffent(nwinCurryRates).get(RESULT)/1+"    "+calDiffent(nevenCurryRates).get(RESULT)/1+"    "+calDiffent(nloseCurryRates).get(RESULT)/1);
		
		return result;
	}
	
	public Map<String, Integer> calDiffent(List<Integer> rates){
		Integer max = new Integer(-1);
		Integer min = new Integer(99999);
		for(Integer rate : rates){
			max = rate.compareTo(max)>0?rate:max;
			min = rate.compareTo(min)<0?rate:min;
		}
		Map<String,Integer> result = new HashMap<String,Integer>();
		result.put(MAX, max);
		result.put(MIN, min);
		result.put(RESULT, max-min);
		return result;
	}
	
	public Map<String,GameRate> processCurrentRate(List<String> current,Map<String,GameRate> initMap){
		int j = 0;
		for(String key : initMap.keySet()){
			GameRate game = initMap.get(key);
			String init = current.get(j);
			String[] lines = init.split("\n");
			for(int i=0 ; i<lines.length ; i++){
				String line = lines[i];
				if(i>=1){
					String[] items = line.split(" ");
					if(items.length<8)
						continue;
					CompanyRate company = game.getCompanyRate().get(items[0])==null?new CompanyRate():game.getCompanyRate().get(items[0]);
					company.setCompanyName(items[0].toLowerCase());
					company.setCurrentRate(new Rate(items[1],items[2],items[3]));
					company.setCurrentCurry(new Rate(items[7],items[8],items[9]));
					game.getCompanyRate().put(company.getCompanyName(), company);
				}
			}
			initMap.put(game.getGameName(), game);
			j++;
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
					company.setCompanyName(items[0].toLowerCase());
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
