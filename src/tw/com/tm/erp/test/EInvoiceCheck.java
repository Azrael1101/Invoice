package tw.com.tm.erp.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class EInvoiceCheck {

	private PrintConnect printConnect;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("EInvoiceCheck");
		try {
			Map<String,Long> rtnMap = EInvoiceCheck.getInvoiceNumber();
			EInvoiceCheck.checkInvoiceNumber(rtnMap.get("endNum"),rtnMap.get("currentNum"),"SALE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void checkInvoiceNumber(Long currentUse, Long endNum, String status) {
		Long check = endNum-currentUse;
		ElectronicInvoiceRequestBean reqBean = new ElectronicInvoiceRequestBean();
		System.out.println("check:"+check);
		try {
			if(check<=200) {
				System.out.println("剩餘可用量:"+check+",號碼量達危險值，進行取號作業");
				//call Brian
	            URL url = new URL("http://10.3.96.58:9090/erp/test/ElectronicInvoiceServlet");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setConnectTimeout(3000);
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setUseCaches(false);
	            connection.setInstanceFollowRedirects(true);
	            // 設定使用標準編碼格式編碼引數的名-值對
	            connection.setRequestProperty("Content-Type","application/json");
	            // 連線
	            connection.connect();
	            /* 4. 處理輸入輸出 */
	            reqBean.setExecuteWay(status);
	            reqBean.setPosMachineCode("99");
	            reqBean.setShopCode("32130");
	            reqBean.setTaskCode("A01");
	            System.out.println(new Gson().toJson(reqBean));
	            // 寫入引數到請求中
	            //String params = "posMachineCode=99";
	            OutputStream out = connection.getOutputStream();
	            out.write(new Gson().toJson(reqBean).getBytes());
	            out.flush();
	            out.close();
	            
	            String msg = "";
	            int code = connection.getResponseCode();
	            if (code == 200) {
	                BufferedReader reader = new BufferedReader(
	                        new InputStreamReader(connection.getInputStream()));
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    msg += line + "\n";
	                }
	                reader.close();
	            }
	            // 5. 斷開連線
	            connection.disconnect();
	            // 處理結果
	            System.out.println(msg);
	            System.out.println("END");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	//A01,C01
	public static void callGetInvoiceNumber(String machineCode,String functionCode,String executeWay) {
		try {
			ElectronicInvoiceRequestBean reqBean = new ElectronicInvoiceRequestBean();
			//call Brian
            URL url = new URL("http://10.3.96.58:9090/erp/test/ElectronicInvoiceServlet");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 設定使用標準編碼格式編碼引數的名-值對
            connection.setRequestProperty("Content-Type","application/json");
            // 連線
            connection.connect();
            /* 4. 處理輸入輸出 */
            reqBean.setExecuteWay(executeWay);
            reqBean.setPosMachineCode(machineCode);
            //reqBean.setShopCode("32130");
            reqBean.setTaskCode(functionCode);
            System.out.println(new Gson().toJson(reqBean));
            // 寫入引數到請求中
            OutputStream out = connection.getOutputStream();
            out.write(new Gson().toJson(reqBean).getBytes());
            out.flush();
            out.close();
            
            String msg = "";
            int code = connection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                reader.close();
            }
            // 5. 斷開連線
            connection.disconnect();
            // 處理結果
            System.out.println(msg);
            System.out.println("END");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,Long> getInvoiceNumber() {
		Map<String,Long> rtnMap = new HashMap<String,Long>();
		Connection conn = new PrintConnect().getSaleConnection();
		try {
			String invoiceNum = ""; 
			String invoiceStart = "";
			String invoiceEnd = "";
			Long   currentNum = null;
			Long   endNum = null;
			
			ResultSet invoice = conn.prepareStatement(
					"select * from tasameng.pos_machine_number"
					).executeQuery();
			while (invoice.next()) {
				invoiceNum = String.valueOf(invoice.getString(2));
				invoiceStart = String.valueOf(invoice.getString(4));
				System.out.println("起始號碼:"+invoiceStart);
				invoiceEnd = String.valueOf(invoice.getString(5));
				System.out.println("結束號碼:"+invoiceEnd);
				
				endNum = Long.parseLong(invoiceEnd.substring(2, 10));
				currentNum = Long.parseLong(invoiceNum.substring(2, 10));
				rtnMap.put("endNum", endNum);
				rtnMap.put("currentNum", currentNum);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			
		}
		return rtnMap;
	}
	
}
