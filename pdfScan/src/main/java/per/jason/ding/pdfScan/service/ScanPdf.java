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

	
	public void scanPdf(String url, List<PdfScanPoint> scanPoints){
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
            for(PdfScanPoint scanPoint : scanPoints){
	            PDFTextStripperByArea stripperArea = new PDFTextStripperByArea();
	            stripperArea.setSortByPosition(true);
	            Rectangle rect = new Rectangle(scanPoint.getX(), scanPoint.getY(), scanPoint.getWidth(), scanPoint.getHeight());
	            stripperArea.addRegion("class1", rect);
	            PDPage page = document.getPage(scanPoint.getPageNum());
	            stripperArea.extractRegions( page );
	            System.out.println( "Text in the area:" + rect );
	            System.out.println( stripperArea.getTextForRegion( "class1" ) );
            }
            
                 
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
	} 
	
}
