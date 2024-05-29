package tw.com.tm.erp.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.test.EtlThreadPool;

public class TransmissionServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(TransmissionServlet.class);
	
	private EtlThreadPool etlThreadPool;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	@Override
	public void init() throws ServletException{
		log.info("TransmissionServlet init開始");;
		System.out.print("TransmissionServlet init開始");
		this.etlThreadPool = (EtlThreadPool) SpringUtils.getApplicationContext().getBean("etlThreadPool");
		log.info("TransmissionServlet init結束");;
		System.out.print("TransmissionServlet init結束");
	}
	

}
