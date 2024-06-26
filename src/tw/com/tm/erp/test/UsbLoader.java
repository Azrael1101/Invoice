package tw.com.tm.erp.test;

import jssc.SerialPortList;

public class UsbLoader {

	/**
	 * find all available serial ports
	 *
	 * @return a list of port names
	 */
	public String[] listConnections() {
	  String OS = System.getProperty("os.name").toLowerCase();
	  String[] portsDetected = null;
		if (OS.indexOf("mac") >= 0) {
		    portsDetected = SerialPortList.getPortNames("/dev/");
		    //System.out.println("OS X");
		  } else if (OS.indexOf("win") >= 0) {
		    portsDetected = SerialPortList.getPortNames("COM");
		    //System.out.println("Windows");
		  } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
		    portsDetected = SerialPortList.getPortNames("/dev/");
		    //System.out.println("Linux/Unix");
		  } else {
		    System.out.println("OS ERROR");
		    System.out.println("OS NAME=" + System.getProperty("os.name"));
		  }
	  return portsDetected;
	  
	}

}
