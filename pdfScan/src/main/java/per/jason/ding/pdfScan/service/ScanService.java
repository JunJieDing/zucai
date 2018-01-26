package per.jason.ding.pdfScan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import per.jason.ding.pdfScan.domain.CompanyRate;
import per.jason.ding.pdfScan.domain.GamePoint;
import per.jason.ding.pdfScan.domain.GameRate;
import per.jason.ding.pdfScan.domain.PdfScanPoint;
import per.jason.ding.pdfScan.domain.Rate;
import per.jason.ding.pdfScan.domain.WeekResult;

@Service
public class ScanService {
	
	@Autowired
	private ScanPdf scanPdf;

	@Value(value="${file.url}")
	private String fileurl;
	
	@Value(value="${file.sat.url}")
	private String satFileUrl;
	
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
	
//	public void outputLateResult(String input){
//		String url = satFileUrl==null?input:satFileUrl;
//		Map<String,GameRate> map = processSatRate(scanLateCurrentRate(url));
//	}
	
	public void genTuresResult(String inputUrl){
		WeekResult wr = new WeekResult();
		outputResult(inputUrl,wr);
		outputSundayResult(inputUrl, wr);

	}
	
	public void genWeekResult(String inputUrl){
		WeekResult wr = new WeekResult();
		outputResult(inputUrl,wr);
		outputSatResult(inputUrl, wr);
		wr = new WeekResult();
		outputSundayResult(inputUrl, wr);
		outputSatSunResult(inputUrl, wr);
		System.out.println(JSON.toJSON(wr));
	}
	
	public void outputFridayResult(String inputUrl){
		String url = fileurl==null?inputUrl:fileurl;
		Map<String,GameRate> map = processFirdayRate(scanFridayRate(url));
		for(GameRate gr: map.values()){
			calFriGameRate(gr);
		}
	}
	
	public void outputSundayResult(String inputUrl, WeekResult wr){
		String url = fileurl==null?inputUrl:fileurl;
		Map<String,GameRate> map = processInitRate(scanSundayInitRate(url));
		processCurrentRate(scanSundayCurrentRate(url),map);
		for(GameRate gr: map.values()){
			wr.getPoints().put(gr.getGameName(), calGameRate(gr));
			wr.getRates().put(gr.getGameName(), gr);
		}
	}
	
	public void outputSatResult(String inputUrl, WeekResult wr){
		String url = fileurl==null?inputUrl:satFileUrl;
		Map<String,GameRate> map = processSatRate(scanLateCurrentRate(url),wr.getRates());
		for(GameRate gr: map.values()){
			GameRate initGr = wr.getRates().get(gr.getGameName());
			gr.setInitCompName(initGr.getInitCompName());
			GamePoint gp = calGameRate(gr);
			GamePoint orinalgp = wr.getPoints().get(gr.getGameName());
			if(orinalgp!=null){
				orinalgp.inputThreeAndFour(gp);
				wr.getPoints().put(gr.getGameName(), orinalgp);
			}else{
				wr.getPoints().put(gr.getGameName(), gp);
			}
		}
	}
	
	public void outputSatSunResult(String inputUrl, WeekResult wr){
		String url = fileurl==null?inputUrl:satFileUrl;
		Map<String,GameRate> map = processSatRate(scanLateSunCurrentRate(url),wr.getRates());
		for(GameRate gr: map.values()){
			GamePoint gp = calGameRate(gr);
			GamePoint orinalgp = wr.getPoints().get(gr.getGameName());
			if(orinalgp!=null){
				orinalgp.inputThreeAndFour(gp);
				wr.getPoints().put(gr.getGameName(), orinalgp);
			}else{
				wr.getPoints().put(gr.getGameName(), gp);
			}
		}
	}
	
	public void outputResult(String inputUrl, WeekResult wr){
		String url = fileurl==null?inputUrl:fileurl;
		Map<String,GameRate> map = processInitRate(scanInitRate(url));
		processCurrentRate(scanCurrentRate(url),map);
		for(GameRate gr: map.values()){
			wr.getPoints().put(gr.getGameName(), calGameRate(gr));
			wr.getRates().put(gr.getGameName(), gr);
		}
	}
	
	public GamePoint calFriGameRate(GameRate gr){
		GamePoint gp = new GamePoint();
		String gameName = gr.getGameName();
		List<Integer> winRates = new ArrayList<Integer>();
		List<Integer> evenRates = new ArrayList<Integer>();
		List<Integer> loseRates = new ArrayList<Integer>();
		List<Integer> winCurryRates = new ArrayList<Integer>();
		List<Integer> evenCurryRates = new ArrayList<Integer>();
		List<Integer> loseCurryRates = new ArrayList<Integer>();
		
		for(CompanyRate cr : gr.getCompanyRate().values()){
			if(cr.getInitRate()!=null){
				gr.getInitCompName().add(cr.getCompanyName().toLowerCase());
				winRates.add(cr.getInitRate().getWin().multiply(new BigDecimal(100)).intValue());
				evenRates.add(cr.getInitRate().getEven().multiply(new BigDecimal(100)).intValue());
				loseRates.add(cr.getInitRate().getLose().multiply(new BigDecimal(100)).intValue());
				winCurryRates.add(cr.getInitCurry().getWin().multiply(new BigDecimal(100)).intValue());
				evenCurryRates.add(cr.getInitCurry().getEven().multiply(new BigDecimal(100)).intValue());
				loseCurryRates.add(cr.getInitCurry().getLose().multiply(new BigDecimal(100)).intValue());
				
			}
		}
//		result.put(init_win, calDiffent(winRates).get(RESULT));
		System.out.println(gameName);
		gp.setGameName(gameName);
		gp.setGameSeq(gr.getGameNo());
		System.out.println("init rate");
//			System.out.println(calDiffent(winRates).get(MAX)/1+"    "+calDiffent(evenRates).get(MAX)/1+"    "+calDiffent(loseRates).get(MAX)/1);
//			System.out.println(calDiffent(winRates).get(MIN)/1+"    "+calDiffent(evenRates).get(MIN)/1+"    "+calDiffent(loseRates).get(MIN)/1);
		System.out.println(calDiffent(winRates).get(RESULT)/1+"    "+calDiffent(evenRates).get(RESULT)/1+"    "+calDiffent(loseRates).get(RESULT)/1);
		System.out.println("init curry");
//		System.out.println(calDiffent(winCurryRates).get(MAX)/1+"    "+calDiffent(evenCurryRates).get(MAX)/1+"    "+calDiffent(loseCurryRates).get(MAX)/1);
//		System.out.println(calDiffent(winCurryRates).get(MIN)/1+"    "+calDiffent(evenCurryRates).get(MIN)/1+"    "+calDiffent(loseCurryRates).get(MIN)/1);
		System.out.println(calDiffent(winCurryRates).get(RESULT)/1+"    "+calDiffent(evenCurryRates).get(RESULT)/1+"    "+calDiffent(loseCurryRates).get(RESULT)/1);
		gp.setOne_win(calDiffent(winCurryRates).get(RESULT)/1);
		gp.setOne_even(calDiffent(evenCurryRates).get(RESULT)/1);
		gp.setOne_lose(calDiffent(loseCurryRates).get(RESULT)/1);
		return gp;
	}
	
	public GamePoint calGameRate(GameRate gr){
		GamePoint gp = new GamePoint();
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
		List<Integer> fwinCurry = new ArrayList<Integer>();
		List<Integer> fevenCurry = new ArrayList<Integer>();
		List<Integer> floseCurry = new ArrayList<Integer>();
		List<Integer> swinCurryRates = new ArrayList<Integer>();
		List<Integer> sevenCurryRates = new ArrayList<Integer>();
		List<Integer> sloseCurryRates = new ArrayList<Integer>();
		
		for(CompanyRate cr : gr.getCompanyRate().values()){
			if(cr.getInitRate()!=null){
				gr.getInitCompName().add(cr.getCompanyName().toLowerCase());
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
			if(cr.getSatRate()!=null){
				if(gr.getfCompName().contains(cr.getCompanyName())){
					fwinCurry.add(cr.getSatCurry().getWin().multiply(new BigDecimal(100)).intValue());
					fevenCurry.add(cr.getSatCurry().getEven().multiply(new BigDecimal(100)).intValue());
					floseCurry.add(cr.getSatCurry().getLose().multiply(new BigDecimal(100)).intValue());
				}
				if(gr.getInitCompName().contains(cr.getCompanyName().toLowerCase().trim())){
					swinCurryRates.add(cr.getSatCurry().getWin().multiply(new BigDecimal(100)).intValue());
					sevenCurryRates.add(cr.getSatCurry().getEven().multiply(new BigDecimal(100)).intValue());
					sloseCurryRates.add(cr.getSatCurry().getLose().multiply(new BigDecimal(100)).intValue());
				}
			}
			
		}
//		result.put(init_win, calDiffent(winRates).get(RESULT));
		System.out.println(gr.getGameNo()+" "+gameName);
		gp.setGameName(gameName);
		gp.setGameSeq(gr.getGameNo());
		if(fwinCurry.size()>0){
			System.out.println("three curry");
			System.out.println(calDiffent(fwinCurry).get(RESULT)/1+"    "+calDiffent(fevenCurry).get(RESULT)/1+"    "+calDiffent(floseCurry).get(RESULT)/1);
			gp.setThree_win(calDiffent(fwinCurry).get(RESULT)/1);
			gp.setThree_even(calDiffent(fevenCurry).get(RESULT)/1);
			gp.setThree_lose(calDiffent(floseCurry).get(RESULT)/1);
			System.out.println("four curry");
			System.out.println(calDiffent(swinCurryRates).get(RESULT)/1+"    "+calDiffent(sevenCurryRates).get(RESULT)/1+"    "+calDiffent(sloseCurryRates).get(RESULT)/1);
			gp.setFour_win(calDiffent(swinCurryRates).get(RESULT)/1);
			gp.setFour_even(calDiffent(sevenCurryRates).get(RESULT)/1);
			gp.setFour_lose(calDiffent(sloseCurryRates).get(RESULT)/1);
		}else{
	//		System.out.println("init rate");
	//		System.out.println(calDiffent(winRates).get(MAX)/1+"    "+calDiffent(evenRates).get(MAX)/1+"    "+calDiffent(loseRates).get(MAX)/1);
	//		System.out.println(calDiffent(winRates).get(MIN)/1+"    "+calDiffent(evenRates).get(MIN)/1+"    "+calDiffent(loseRates).get(MIN)/1);
	//		System.out.println(calDiffent(winRates).get(RESULT)/1+"    "+calDiffent(evenRates).get(RESULT)/1+"    "+calDiffent(loseRates).get(RESULT)/1);
			System.out.println("init curry");
	//		System.out.println(calDiffent(winCurryRates).get(MAX)/1+"    "+calDiffent(evenCurryRates).get(MAX)/1+"    "+calDiffent(loseCurryRates).get(MAX)/1);
	//		System.out.println(calDiffent(winCurryRates).get(MIN)/1+"    "+calDiffent(evenCurryRates).get(MIN)/1+"    "+calDiffent(loseCurryRates).get(MIN)/1);
			System.out.println(calDiffent(winCurryRates).get(RESULT)/1+"    "+calDiffent(evenCurryRates).get(RESULT)/1+"    "+calDiffent(loseCurryRates).get(RESULT)/1);
			gp.setOne_win(calDiffent(winCurryRates).get(RESULT)/1);
			gp.setOne_even(calDiffent(evenCurryRates).get(RESULT)/1);
			gp.setOne_lose(calDiffent(loseCurryRates).get(RESULT)/1);
			System.out.println("current curry");
	//		System.out.println(calDiffent(cwinRates).get(RESULT)/1+"    "+calDiffent(cevenRates).get(RESULT)/1+"    "+calDiffent(closeRates).get(RESULT)/1);
			System.out.println(calDiffent(cwinCurryRates).get(RESULT)/1+"    "+calDiffent(cevenCurryRates).get(RESULT)/1+"    "+calDiffent(closeCurryRates).get(RESULT)/1);
			gp.setTwo_win(calDiffent(cwinCurryRates).get(RESULT)/1);
			gp.setTwo_even(calDiffent(cevenCurryRates).get(RESULT)/1);
			gp.setTwo_lose(calDiffent(closeCurryRates).get(RESULT)/1);
			System.out.println("nature curry");
			System.out.println(calDiffent(nwinCurryRates).get(RESULT)/1+"    "+calDiffent(nevenCurryRates).get(RESULT)/1+"    "+calDiffent(nloseCurryRates).get(RESULT)/1);
			gp.setNature_curry_win(calDiffent(nwinCurryRates).get(RESULT)/1);
			gp.setNature_curry_even(calDiffent(nevenCurryRates).get(RESULT)/1);
			gp.setNature_curry_lose(calDiffent(nloseCurryRates).get(RESULT)/1);
			System.out.println("nature rate");
			gp.setNature_win(calDiffent(nwinRates).get(RESULT)/1);
			gp.setNature_even(calDiffent(nevenRates).get(RESULT)/1);
			gp.setNature_lose(calDiffent(nloseRates).get(RESULT)/1);
			System.out.println(calDiffent(nwinRates).get(RESULT)/1+"    "+calDiffent(nevenRates).get(RESULT)/1+"    "+calDiffent(nloseRates).get(RESULT)/1);
		}
		
		
		return gp;
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
	
	//TODO
	public Map<String,GameRate>  processFirdayRate(List<String> inits){
		Map<String,GameRate> map = new LinkedHashMap<String, GameRate>();
		for(String init : inits){
			GameRate game = new GameRate();
			String[] lines = init.split("\n");
			boolean flag = false;
			for(int i=0 ; i<lines.length ; i++){
				CompanyRate company = new CompanyRate();
				String line = lines[i];
				if(i==0){
					game.setGameName(line.substring(0,line.lastIndexOf("北京时间")));
				}
					String[] items = line.split(" ");
					if(items.length<11) continue;
					company.setCompanyName(items[1].toLowerCase());
					company.setInitRate(new Rate(items[2],items[3],items[4]));
					company.setInitCurry(new Rate(items[8],items[9],items[10]));
					game.getCompanyRate().put(company.getCompanyName(), company);
			}
			map.put(game.getGameName(), game);
		}
		return map;
	}
	
	public Map<String,GameRate> processLateRate(List<String> late){
		List<String> tempGame = new ArrayList<String>();
		int gameNo = 0;
		Map<String,GameRate> result = new LinkedHashMap<String,GameRate>();
		for(String line : late){
			if(line.contains("VS")){
				tempGame = new ArrayList<String>();
				String[] games = line.split("、1");
				for(String gameName : line.split("、1")){
					if(gameName.contains("VS")){
						GameRate gr = new GameRate();
						gr.setGameName(line.substring(0,line.indexOf(" 推介")));
						tempGame.add(gr.getGameName());
						result.put(gr.getGameName(), gr);
						gameNo ++ ;

					}
				}
			}else{
				String[] temp =  line.split(" ");
				String compName = temp[0];
				List<String[]> rates = getListStringArray(Arrays.copyOfRange(temp, 1, temp.length),7);
				for(int i =0 ;i<rates.size();i++){
					result.get(tempGame.get(i)).getCompanyRate().put(compName, sambleCompanyRage(compName, rates.get(i)));
				}
			}
		}
		return result;
	}
	
	private static List<String[]> getListStringArray(String[] dd, int b) {  
        List<String[]> aa = new ArrayList<String[]>();  
        // tyy 取整代表可以拆分的数组个数  
        int f = dd.length / b;  
        for (int i = 0; i < f; i++) {  
            String[] bbb = new String[b];  
            for (int j = 0; j < b; j++) {  
                bbb[j] = dd[j + i * b];  
            }  
            aa.add(bbb);  
        }  
        return aa;  
    }

	public CompanyRate sambleCompanyRage(String companyName, String[] items){
		CompanyRate rate = new CompanyRate();
		rate.setCompanyName(companyName);
		rate.setInitRate(new Rate(items[0],items[1],items[2]));
		rate.setInitCurry(new Rate(items[3],items[4],items[5]));
		return rate;
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
					CompanyRate company = game.getCompanyRate().get(items[0].toLowerCase())==null?new CompanyRate():game.getCompanyRate().get(items[0].toLowerCase());
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
	
	public static String removeNum(String str) {  
        String regEx = "[0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
    //替换与模式匹配的所有字符（即数字的字符将被""替换）  
        return m.replaceAll("").trim();  
    } 
	
	public Map<String,GameRate> processSatRate(List<String> scanResult,Map<String,GameRate> map){
		String[] gameList = new String[14];
		int count=0;
		for(String result : scanResult){
			String[] lines = result.split("\n");
			int s = count;
			for(int lcount=0; lcount<lines.length;lcount++){
				String line = lines[lcount];
				if(lcount == 0)
				for(String item : line.split("推介")){
					if(item.contains("VS")){
						gameList[count] = removeNum(item.substring(item.indexOf("、")>0 ? item.indexOf("、")+1 +(item.indexOf("、1")>0?1:0):item.indexOf(" ")).trim());
						count++;
					}
				}
				if(lcount>1){
					String[] items = line.substring(line.indexOf(" ")+1).split(" ");
					String compNam = line.substring(0, line.indexOf(" "));
					for(int i=0,j=s; i<items.length ; i++){
						if(i%7==0){
							//for not open match
							if(!Pattern.matches("^[0-9]+(.[0-9]{1,3})?$", items[i].trim())){
								j++;i+=6; continue;
							}
						}
						if(i%7==6){
							GameRate gameR = map.get(gameList[j])==null?GameRate.initGameRate(gameList[j], j):map.get(gameList[j]);
							CompanyRate compR = 	gameR.getCompanyRate().get(compNam)==null?
									CompanyRate.initsatRate(compNam, new Rate(items[i-6], items[i-5], items[i-4]), new Rate(items[i-3], items[i-2],items[i-1])):
									gameR.getCompanyRate().get(compNam).inputSatRate(new Rate(items[i-6], items[i-5], items[i-4]), new Rate(items[i-3], items[i-2],items[i-1]));
							gameR.getCompanyRate().put(compR.getCompanyName(), compR);
							map.put(gameR.getGameName(), gameR);
							j++;
						}
					}
				}
			}
		}
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
				line = line.replace("Ineterwetten", "Interwetten");
				if(i==0){
					game.setGameNo(Integer.valueOf(line.substring(0,line.indexOf("、"))));
					game.setGameName(line.substring(line.indexOf("、")+1,line.lastIndexOf(" ")));
				}
				if(i>1){
					String[] items = line.split(" ");
					if(items.length<7) continue;
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
	
	public List<String> scanLateCurrentRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer page= 1;
		points.add(PdfScanPoint.genPoint(page, 705, 100, 350, 250,"公司名称", "", 1500,"最高赔率"));
		points.add(PdfScanPoint.genPoint(page, 0, 350, 1500, 250,"公司名称", "", 1500,"最高赔率"));
		points.add(PdfScanPoint.genPoint(page, 0, 600, 1500, 250,"公司名称", "", 1500,"最高赔率"));
		return scanPdf.scanPdf(url, points);
	
	}
	
	public List<String> scanLateSunCurrentRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer page= 4;
		points.add(PdfScanPoint.genPoint(page, 705, 100, 350, 250,"公司名称", "", 1500,"最高赔率"));
		points.add(PdfScanPoint.genPoint(page, 0, 350, 1500, 250,"公司名称", "", 1500,"最高赔率"));
		points.add(PdfScanPoint.genPoint(page, 0, 600, 1500, 250,"公司名称", "", 1500,"最高赔率"));
		return scanPdf.scanPdf(url, points);
	
	}
	
	public List<String> scanSundayInitRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx = {290,435,590,750};
		Integer beginy = 460;
		Integer[] endx = {435,590,750,900};
		Integer iterWigth = 160;
		Integer iterHeight = 80;
		Integer page= 10;
		for(int i = -1,j=2 ; i<=14 ; i++){
			if(i>0){
				PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[j%4], beginy, endx[j%4]-beginx[j%4], iterHeight,"VS", "推介", 1500,"VS") ;
				points.add(point);
				j++;
			}
			if(i==2||i==6||i==10){
				beginy += (iterHeight);
			}
			
		}
//		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
		return scanPdf.scanPdf(url, points);
	}
	
	public List<String> scanSundayCurrentRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx={150,600};
		Integer[] endx={360,810};
		Integer iterHeight = 200;
		Integer beginy = 1150;
		Integer page = 7;
		for(int i=0; i<14;i++){
			PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[i%2], beginy, endx[i%2]-beginx[i%2], iterHeight,"凯利指数 赔付", "", beginy+300,"99家平均") ;
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
	
	public List<String> grebDate(String inputUrl){
		String url = fileurl==null?inputUrl:fileurl;
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		points.add(PdfScanPoint.genPoint(0, 0, 0, 300, 300, null, null, 300, null));
		return scanPdf.scanPdf(url, points);
	}
	
	public List<String> scanFridayRate(String url){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx = {50,440,750};
		Integer beginy = 150;
		Integer[] endx = {350,660,945};
		Integer iterHeight = 200;
		Integer page= 11;
		for(int i =0 ; i<14 ; i++){
			PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[i%3], beginy, endx[i%3]-beginx[i%3], iterHeight,"北京时间", "", 3000,"全 场") ;
			points.add(point);
			if(i%3==2){
				beginy += (iterHeight);
			}
			
		}
//		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
		return scanPdf.scanPdf(url, points);

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
