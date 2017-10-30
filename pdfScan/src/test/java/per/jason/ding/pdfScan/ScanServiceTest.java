package per.jason.ding.pdfScan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import per.jason.ding.pdfScan.service.ScanService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScanServiceTest {

	@Autowired
	private ScanService scanService;
	
	@Test
	public void testScan(){
		scanService.genWeekResult("");
	}
	
}
