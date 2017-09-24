package per.jason.ding.pdfScan.service;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import per.jason.ding.pdfScan.domain.PdfScanPoint;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

@Service
public class ScanPdf {

	
	public List<String> scanPdf(String url, List<PdfScanPoint> scanPoints){
		List<String> result = new ArrayList<String>();
		File pdfFile = new File(url);
        PDDocument document = null;
        try
        {
            document=PDDocument.load(pdfFile);
            
            // 读文本内容
//            PDFTextStripper stripper=new PDFTextStripper();
//            stripper.setSortByPosition(true);
//            stripper.setStartPage(scanPoint.getPageNum());
//            stripper.setEndPage(scanPoint.getPageNum());
//            String content = stripper.getText(document);
//            System.out.println(content);
            Integer y = null;
            Integer firstY = null;
            for(PdfScanPoint scanPoint : scanPoints){
	            PDFTextStripperByArea stripperArea = new PDFTextStripperByArea();
	            stripperArea.setSortByPosition(true);
	            Rectangle rect = new Rectangle(scanPoint.getX(), scanPoint.getY(), scanPoint.getWidth(), (y==null||y<0)?scanPoint.getHeight():y);
	            stripperArea.addRegion("class1", rect);
	            PDPage page = document.getPage(scanPoint.getPageNum());
	            stripperArea.extractRegions( page );
	            
	            String ar = stripperArea.getTextForRegion( "class1" );
	            String fl_ar = ar.substring(0,ar.indexOf("\n"));
	            while((!fl_ar.contains(scanPoint.getStartKeyword1())||!fl_ar.contains(scanPoint.getStartKeyword2()))&&scanPoint.getY()<=scanPoint.getEndY()){
	            	scanPoint.setY(scanPoint.getY()+5);
	            	rect = new Rectangle(scanPoint.getX(), scanPoint.getY(), scanPoint.getWidth(), (y==null||y<0)?scanPoint.getHeight():y);
		            stripperArea.addRegion("class1", rect);
		            stripperArea.extractRegions( page );
		            ar = stripperArea.getTextForRegion( "class1" );
		            fl_ar = ar.substring(0,ar.indexOf("\n"));
	            }
	            if(y==null&&firstY==null){
	            	firstY = scanPoint.getY();
	            }else if(y==null&&scanPoint.getY()!=firstY){
	            	y = scanPoint.getY() - firstY +10 ;
	            }
	            if(ar.indexOf(scanPoint.getEndKeyword1(), ar.indexOf("\n"))!=-1){
		            ar = ar.substring(0, ar.indexOf(scanPoint.getEndKeyword1(), ar.indexOf("\n")));
		            ar = ar.substring(0, ar.lastIndexOf("\n"));
	            }
	            System.out.println( "Text in the area:" + rect +" page:"+scanPoint.getPageNum());
	            System.out.println( ar );
	            result.add(ar);
            }
            
                 
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return result;
	} 
	
}
