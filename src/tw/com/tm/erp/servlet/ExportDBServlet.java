package tw.com.tm.erp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.ExportDBService;

public class ExportDBServlet extends HttpServlet {
	public static final String CONTENT_TYPE = "application/vnd.ms-excel";
	
	private ExportDBService exportDBService ;
	/**
	 * Constructor of the object.
	 */
	public ExportDBServlet() {
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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
		String fileName = new String(System.currentTimeMillis() + ".xls" ) ;
		response.setHeader("Content-Disposition","attachment;filename=" + fileName );//指定下载的文件名
		response.setContentType(CONTENT_TYPE);
		exportDBService.exportXlsConfig(response.getOutputStream());
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		this.exportDBService = (ExportDBService) SpringUtils.getApplicationContext().getBean("exportDBService");
	}
}
