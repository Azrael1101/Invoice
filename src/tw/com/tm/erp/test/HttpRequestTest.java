package tw.com.tm.erp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.google.gson.Gson;

public class HttpRequestTest {

	public static ElectronicInvoiceResponseBean getInvoiceNumber(String taskCode, String posMachineCode) {
		System.out.println(" getting invoice number api ... ");
		ElectronicInvoiceResponseBean<ElectronicInvoiceResponseBean> electronicInvoiceResponseBean = null;
		HttpURLConnection connection = null;
		int responseCode = 0;
		String response = "";
		String responseStr = "";
		try {
			
			URL url = new URL("http://10.99.50.10:8080/ElectronicInvoice/ElectronicInvoiceServlet");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");// POST,GET,POST,DELETE,INPUT
			connection.setRequestProperty("content-type", "text/xml;charset=UTF-8");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			
			EletronicInvoiceRequestBean eletronicInvoiceRequestBean = new EletronicInvoiceRequestBean();
			Gson gson = new Gson();
			
			eletronicInvoiceRequestBean.setTaskCode(taskCode);
			eletronicInvoiceRequestBean.setPosMachineCode(posMachineCode);
			String requestStr = gson.toJson(eletronicInvoiceRequestBean);
			
			try(OutputStream os = connection.getOutputStream()) {
                byte[] input = requestStr.getBytes("utf-8");
                os.write(input, 0, input.length);			
            }
			
			responseCode = connection.getResponseCode();
			
			if(200 == responseCode) {
				System.out.println(" getting invoice number Success!");
				//獲得輸入,將位元組流轉換為字元流
			    InputStream is = connection.getInputStream();  
			    InputStreamReader isr = new InputStreamReader(is, "utf-8");  
			    //使用快取區
			    BufferedReader br = new BufferedReader(isr);  
			    
			    StringBuffer sb = new StringBuffer();
			    String temp = null;  
			    while ((temp = br.readLine()) != null) {  
			    	sb.append(temp);  
			    } 
			    
			    response = sb.toString().trim();//回應資料轉字串
			    
			    electronicInvoiceResponseBean = gson.fromJson(response, ElectronicInvoiceResponseBean.class);
			    
			    //關閉bufferReader和輸入流
			    br.close();  
			    isr.close();  
			    is.close();  
			    is = null;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			connection.disconnect();
		}
		return electronicInvoiceResponseBean;
	}
	
}
