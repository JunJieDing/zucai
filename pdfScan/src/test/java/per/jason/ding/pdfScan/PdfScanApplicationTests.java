package per.jason.ding.pdfScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import per.jason.ding.pdfScan.domain.CompanyRate;
import per.jason.ding.pdfScan.domain.GameRate;
import per.jason.ding.pdfScan.domain.PdfScanPoint;
import per.jason.ding.pdfScan.domain.Rate;
import per.jason.ding.pdfScan.service.ScanPdf;
import per.jason.ding.pdfScan.service.ScanService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PdfScanApplicationTests {

	@Autowired
	private ScanPdf scanPdf;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testScanArea(){
//		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
//		PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[j%4], beginy, endx[j%4]-beginx[j%4], iterHeight) ;
//		points.add(point);
		
	}
	
	@Test
	public void testScanPdfFloatRange(){
//		PdfScanPoint point = PdfScanPoint.genPoint(2, 290, 580, 435-290, 65) ;
//		scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",Arrays.asList(point));
	}
	
	@Test
	public void scanAndProcess(){
		Map<String,GameRate> map = processInitRate(testScanPdf());
		System.out.println(map);
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
	

	public List<String> testScanPdf(){
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
		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
	}
	
	
	public List<String> testScanCurrentRate(){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx={150,600};
		Integer[] endx={360,810};
		Integer iterHeight = 200;
		Integer beginy = 1150;
		Integer page = 2;  
		for(int i=0; i<14;i++){
			PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[i%2], beginy, endx[i%2]-beginx[i%2], iterHeight,"凯利指数 赔付", "", beginy+300,"半全场赔率") ;
			points.add(point);
			if(i!=1&&i!=7&&i%2==1){
				beginy +=(iterHeight+250);
			}else if(i==1||i==7){
				beginy=205;
				page +=1;
			}
		}
		return scanPdf.scanPdf("/Users/dingjunjie/Downloads/1292.pdf",points);
	}
	
	

}
