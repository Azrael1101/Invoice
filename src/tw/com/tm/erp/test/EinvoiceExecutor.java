package tw.com.tm.erp.test;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class EinvoiceExecutor {

	private static PrintConnect printConnect = null;
	
	public static void main(String[] args) {
		try {
//			System.out.println("Starting printer detect ...");
//			printConnect  = new PrintConnect();
//			Thread t1 = new Thread(new Detect(printConnect));
//			t1.start();
		}catch(Exception e) {
			e.printStackTrace();		
		}
	}

	
//	public static void main(String[] args) {
//		
//		try {
//			
//			PrintService pservice = null;//... // acquire print service of your printer
//			DocPrintJob job = pservice.createPrintJob();  
//			String commands = "";
//			commands += "\\u001B\\u0045\\u000A"; // plain
//			commands += "Hello ";
//			commands += "\\u001B\\u0045\\u000D"; // bold
//			commands += "ESCP!";
//			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//			Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
//			job.print(doc, null);
//			
//		}catch(Exception e) {
//			e.printStackTrace();		
//		}
//	}
}
