package tw.com.tm.erp.hbm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.RequestBean;
import tw.com.tm.erp.hbm.bean.ResponseBean;
import tw.com.tm.erp.hbm.bean.Transaction;
import tw.com.tm.erp.regulation.TransactionInterface;
import tw.com.tm.erp.utils.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

@WebServlet("/TransactionController")
public class TransactionController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4827839775742030759L;
	private static final Log log = LogFactory.getLog(TransactionController.class);
	private TransactionInterface transactionService = (TransactionInterface)SpringUtils.getApplicationContext().getBean("transactionService");

	public TransactionController() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("TransactionController...doGet");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter pw = response.getWriter();
		Transaction transaction = null;
		
		try {
			String json = JsonUtil.readJSONString(request);
			log.info("json====="+json);
			Gson gson = new Gson();
			RequestBean<Map<String, String>> requestBean = gson.fromJson(json, new TypeToken<RequestBean<Map<String, String>>>() {}.getType());
			ResponseBean responseBean = new ResponseBean();
			
			Map<String, String> posTransaction = requestBean.getPosTransaction();
			String action = posTransaction.get("action");
			log.info("action:"+action);
			
			//算折扣
			if("executeDiscount".equals(action)) {
				transaction = transactionService.executeDiscount(posTransaction);
			}
			if("init".equals(action)) {
				transaction = transactionService.executeInitTransaction(posTransaction);
			}
			//算小計金額
			if("executeTotalCountPage".equals(action)) {
//				return soDepartmentOrderService.findTotalOriginalSalesAmount(null);
			}
			//算當日營業額
			if("calculateTodaySales1".equals(action)) {
//				return soDepartmentOrderService.findTodaySales(null);
			}
			//生日券計算
			if("checkBirthday".equals(action)) {
//				return soDepartmentOrderService.executeBirthdayCoupon(null);
			}
			
			responseBean.setPosTransaction(transaction);
			String returnStr = gson.toJson(responseBean);
			
			pw.print(returnStr);
			
		} catch (Exception e) {
			e.printStackTrace();
			pw.print("發生異常");
			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
