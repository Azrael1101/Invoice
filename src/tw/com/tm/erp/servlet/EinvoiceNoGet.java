package tw.com.tm.erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.bean.RequestBean;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.test.ElectronicInvoiceRequestBean;
import tw.com.tm.erp.utils.JsonUtil;
import tw.com.tm.erp.utils.PageAccessRight;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.UserProgramRight;
import tw.com.tm.erp.xbeans.ControlType;
import tw.com.tm.erp.xbeans.PageDocument;
import tw.com.tm.erp.xbeans.PageType;

public class EinvoiceNoGet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EinvoiceNoGet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String json = JsonUtil.readJSONString(request);
			System.out.println("json====="+json);
			//ElectronicInvoiceRequestBean reqBean = new ElectronicInvoiceRequestBean();
			Gson gson = new Gson();
			ElectronicInvoiceRequestBean reqBean = gson.fromJson(json, new ElectronicInvoiceRequestBean().getClass());
			System.out.println("reqBean.getInvoiceId():"+reqBean.getInvoiceId());
			// get from oracle
			PosMachineNumberController posMachineNumberController = getInvoiceNo(reqBean);
			// set to mysql
			setInvoiceNo(posMachineNumberController);
			//update oracle finish
			setTaStatusRes(posMachineNumberController);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	// get from oracle
	private PosMachineNumberController getInvoiceNo(ElectronicInvoiceRequestBean reqBean) {
		PosMachineNumberController posMachineNumberController = new PosMachineNumberController();
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@10.99.50.26:1521:KWEDB1", "KWE_ERP", "1QAZ8IK,!@#$");
            ResultSet rs = conn.prepareStatement("select * from ERP.SO_INVOICE_CONTROLLER where INVOICE_ID =" +  reqBean.getInvoiceId() ).executeQuery();
            while (rs.next()) {
            //SO_INVOICE_CONTROLLER
//            	INVOICE_ID
            	posMachineNumberController.setInvoiceSno(rs.getLong("INVOICE_ID"));
//            	INVOICE_HEADER
            	posMachineNumberController.setInvoiceHeader(rs.getString("INVOICE_HEADER"));
            	posMachineNumberController.setStatus("N");
            	posMachineNumberController.setPosMachineCode(rs.getString("POS_MACHINE_CODE"));
//            	INVOICE_START
            	String start = rs.getString("INVOICE_START");
            	posMachineNumberController.setInvoiceStart(start);
            	posMachineNumberController.setCurrentUse(start);
//            	INVOICE_END
            	posMachineNumberController.setInvoiceEnd(rs.getString("INVOICE_END"));
//            	TAX_YEAR_MONTH
            	posMachineNumberController.setTaxYearMonth(rs.getString("TAX_YEAR_MONTH"));
//            	SYS_TIME
            	posMachineNumberController.setSysTime(rs.getString("SYS_TIME"));
            	//start_date
            	posMachineNumberController.setStartDate(rs.getString("START_DATE"));
            	//end_date
            	posMachineNumberController.setEndDate(rs.getString("END_DATE"));
            }
            
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return posMachineNumberController;
	}
	
	// set to mysql
	private void setInvoiceNo(PosMachineNumberController posMachineNumberController) {
		try {
			//BaseDAO baseDao = SpringUtils.getApplicationContext().getBean("BaseDAO");
			BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
			baseDAO.save(posMachineNumberController); 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// set to oracle
	private void setTaStatusRes(PosMachineNumberController posMachineNumberController) {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@10.99.50.26:1521:KWEDB1", "KWE_ERP", "1QAZ8IK,!@#$");
            PreparedStatement pstmt = conn.prepareStatement("update ERP.SO_INVOICE_CONTROLLER set TA_STATUS = 'FINISH' where INVOICE_ID = '"+posMachineNumberController.getInvoiceSno()+"'");
			pstmt.executeUpdate();
			System.out.println("回寫 SO_INVOICE_CONTROLLER 狀態  成功");
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	
}
