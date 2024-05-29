package tw.com.tm.erp.test;


import java.util.Random;

public class Detect implements Runnable {
	
	public Detect() {
		System.out.println(" initial Detect() ...");
	}
	
	public void run() {
		while (true) {
			try {
				
				Thread.sleep(5000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}
