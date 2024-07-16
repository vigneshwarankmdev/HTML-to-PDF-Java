package htmlPDF;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
//import java.nio.file.Files;

import com.google.common.io.Files;
//import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

//Import statement for IronPDF for Java
import com.ironsoftware.ironpdf.*;

//Opt - 6
import com.itextpdf.html2pdf.HtmlConverter;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PageMargin;
import org.openqa.selenium.print.PrintOptions;

//Opt - 7
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.page.Page;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class HtmlToPdfFlyingSaucer {

	static String htmlSource = "C:\\Users\\kghf523\\eclipse-workspace\\htmlPDF\\src\\main\\resources\\Input\\US-88434.html";
	static String pdfDest = "C:\\Users\\kghf523\\eclipse-workspace\\htmlPDF\\src\\main\\resources\\Output\\destination-{{opt}}.pdf";
	static String htmlVal = HTMLfile();
	static Integer opt = 7;
	 static {
	        new File(pdfDest).getParentFile().mkdirs();
	    }
	 
	public static void main(String[] args) throws IOException {
		System.out.println("check");
		pdfDest = pdfDest.replace("{{opt}}", opt.toString());
		if(opt==1){
			opt1();
		}
		else if(opt==2) {
			opt2();
		}
		else if(opt==3) {
			opt3();
		}		
		else if(opt==4) {
			opt4();
		}
		else if(opt==5) {
			opt5();
		}
		else if(opt==6) {
			opt6();
		}
		else if(opt==7) {
			opt7();
		}
	}
	

	private static String HTMLfile() {
	    String HTMLVal = null;
	    try {
			HTMLVal = Files.asCharSource(new File(htmlSource), StandardCharsets.UTF_8).read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return HTMLVal;
	}
	
	private static void opt1() throws IOException {
		//Working with wrong Format
		HtmlToPdf.create().object(HtmlToPdfObject.forHtml(htmlVal)).convert(pdfDest);
		System.out.println("Completed 1");
	}
	
	private static void opt2() throws IOException {
		//Working with wrong Format
		Document document = Jsoup.parse(htmlVal);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		String xhtml = document.html();
		File output = new File(pdfDest);
	    ITextRenderer iTextRenderer = new ITextRenderer();
	    iTextRenderer.setDocumentFromString(xhtml);
	    iTextRenderer.layout();
	    OutputStream os = new FileOutputStream(output);
	    iTextRenderer.createPDF(os);
	    os.close();
		System.out.println("Completed 2");
	}
	
	private static void opt3() throws IOException {
		// 
		String xhtml = htmlToXhtml(htmlVal);
		OutputStream outputStream = new FileOutputStream(pdfDest);
		ITextRenderer renderer = new ITextRenderer();
		SharedContext sharedContext = renderer.getSharedContext();
		sharedContext.setPrint(true);
		sharedContext.setInteractive(false);
		renderer.setDocumentFromString(xhtml);
		renderer.layout();
		renderer.createPDF(outputStream);
		System.out.println("Completed 3");
	}

	private static void opt4() throws IOException {
		//IronPDF
		// Apply your license key
		License.setLicenseKey("IRONSUITE.VIGNESHWARAN.KM.ASTRAZENECA.COM.16177-D24A67DC42-AOUNFIVEI5K4DBJA-BC44QBFRHOJB-WFPG77W6VQCM-EPWDJKJU7UOL-AAQVCTKWLA2F-ABX3GAY37GYA-F2DJNR-TDIUA2IJJC2NEA-DEPLOYMENT.TRIAL-4YGTGV.TRIAL.EXPIRES.27.JUL.2024");
		PdfDocument pdf = PdfDocument.renderHtmlAsPdf(htmlVal);
		pdf.saveAs(pdfDest);
		System.out.println("Completed 4");
	}
	
	private static void opt5() throws IOException {
		//
		//HtmlConverter.convertToPdf(new File(htmlSource), new File(pdfDest));
		HtmlConverter.convertToPdf(htmlVal, new FileOutputStream(pdfDest));
		System.out.println("Completed 5");
	}

	private static void opt6() throws IOException {
		//
		//String html =  new String(java.nio.file.Files.readAllBytes(Paths.get(htmlSource)));
		String html = htmlVal;
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\kghf523\\eclipse-workspace\\htmlPDF\\src\\main\\resources\\Drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //Spin up the browser in "headless" mode, this way Selenium will not open up a graphical user interface.
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        // Open up the HTML file in our headless browser. This step will evaluate the JavaScript for us.
        
        //will not support bigger file in URL method
        //driver.navigate().to("data:text/html;charset=utf-8," + html);
        
        //Use file as a source
        driver.navigate().to(htmlSource);
        // Now all we have to do is extract the evaluated HTML syntax and convert it to a PDF using iText's HtmlConverter.
        String evaluatedHtml = (String) driver.executeScript("return document.documentElement.innerHTML;");
        HtmlConverter.convertToPdf(evaluatedHtml, new FileOutputStream(pdfDest));
		System.out.println("Completed 6");
	}

	private static void opt7() throws IOException {
		//
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\kghf523\\eclipse-workspace\\htmlPDF\\src\\main\\resources\\Drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu"); // Applicable to Windows OS
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        ChromeDriver driver = new ChromeDriver(options);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        try {
        	//driver.get("data:text/html,"+htmlVal);
        	driver.get(htmlSource);
        	//driver.navigate().to(htmlSource);
        	
            // Enable print-to-PDF in DevTools
            devTools.send(Page.enable());

            // Capture PDF output
            Map<String, Object> printOptions = new HashMap<>();
            printOptions.put("paperWidth", 8.27);  // A4 width in inches
            printOptions.put("paperHeight", 11.69); // A4 height in inches

            String printToPdfBase64 = (String) driver.executeCdpCommand("Page.printToPDF", printOptions).get("data");
            // Decode base64 PDF data to binary and save to file
            byte[] pdfData = Base64.getDecoder().decode(printToPdfBase64);
            try (FileOutputStream fos = new FileOutputStream(new File(pdfDest))) {
                fos.write(pdfData);
            }

            System.out.println("PDF saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
		System.out.println("Completed 7");
	}
	
	
	private static String htmlToXhtml(String html) {
		Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		return document.html();
	}
	private static void xhtmlToPdf(String xhtml, String outFileName) throws IOException {
	    File output = new File(outFileName);
	    ITextRenderer iTextRenderer = new ITextRenderer();
	    iTextRenderer.setDocumentFromString(xhtml);
	    iTextRenderer.layout();
	    OutputStream os = new FileOutputStream(output);
	    iTextRenderer.createPDF(os);
	    os.close();
	}
	
}
