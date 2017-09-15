package per.jason.ding.pdfScan;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import per.jason.ding.pdfScan.domain.PdfScanPoint;
import per.jason.ding.pdfScan.service.ScanPdf;

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
	public void testScanPdf(){
		List<PdfScanPoint> points = new ArrayList<PdfScanPoint>();
		Integer[] beginx = {290,435,590,750};
		Integer beginy = 580;
		Integer[] endx = {435,590,750,900};
		Integer iterWigth = 160;
		Integer iterHeight = 65;
		Integer page= 2;
		for(int i = -1,j=2 ; i<=14 ; i++){
			if(i>0){
				PdfScanPoint point = PdfScanPoint.genPoint(page, beginx[j%4], beginy, endx[j%4]-beginx[j%4], iterHeight) ;
				points.add(point);
				j++;
			}
			if(i==2||i==6||i==10){
				beginy += iterHeight;
			}
			
		}
		scanPdf.scanPdf("C:\\Users\\jason.ding.BWH\\Downloads\\1290.pdf",points);
	}

}
