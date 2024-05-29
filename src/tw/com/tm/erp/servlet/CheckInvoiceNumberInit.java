package tw.com.tm.erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.hbm.dao.PosMachineNumberControllerDAO;
import tw.com.tm.erp.hbm.service.PosMachineNumberService;
import tw.com.tm.erp.test.EInvoiceCheck;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.PageAccessRight;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.UserProgramRight;
import tw.com.tm.erp.xbeans.ControlType;
import tw.com.tm.erp.xbeans.PageDocument;
import tw.com.tm.erp.xbeans.PageType;

public class CheckInvoiceNumberInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor of the object.
	 */
	public CheckInvoiceNumberInit() {
		super();
		try {
			String posMachineCode = "99";
			PosMachineNumberService posMachineNumberService = (PosMachineNumberService) SpringUtils.getApplicationContext().getBean("posMachineNumberService");
			posMachineNumberService.checkAvaliableNumberInvoice(posMachineCode,"INIT");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String posMachineCode = "99";
			PosMachineNumberService posMachineNumberService = (PosMachineNumberService) SpringUtils.getApplicationContext().getBean("posMachineNumberService");
			posMachineNumberService.checkAvaliableNumberInvoice(posMachineCode,"INIT");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
