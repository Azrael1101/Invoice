package tw.com.tm.erp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.utils.PageAccessRight;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.UserProgramRight;
import tw.com.tm.erp.xbeans.ControlType;
import tw.com.tm.erp.xbeans.PageDocument;
import tw.com.tm.erp.xbeans.PageType;



public class AccessControlServ extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6435465859189146711L;
	
	
	public static final String REQUEST_FUNCTION_CODE = "rFunctionCode" ;
	public static final String CONTENT_TYPE = "text/xml; charset=UTF-8";
	
	private static final Log log = LogFactory.getLog(AccessControlServ.class);		

	/**
	 * Constructor of the object.
	 */
	public AccessControlServ() {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PageDocument doc = null;
		String functionCode = request.getParameter(REQUEST_FUNCTION_CODE);
		log.info("AccessControlServ function code : " + functionCode );
		if (StringUtils.hasText(functionCode)) {
			HttpSession session = request.getSession();
			if (null != session) {
				Object userObj = session.getAttribute(SystemConfig.USER_SESSION_NAME);
				log.info("AccessControlServ get session user obj " + userObj );
				if(null != userObj){
					doc = setDocData((User) userObj, functionCode);
				}
			}
		}

		// response
		if (null != doc) {
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			doc.save(out);
			out.flush();
			out.close();
		}
		
	}
	
	private PageDocument setDocData(User user, String functionCode) {
		log.info("AccessControlServ.setDocData get session user login name : " + user.getLoginName());
		PageDocument doc = null;
		if ((null != user) && (null != user.getUserProgramRight())) {
			UserProgramRight upr = user.getUserProgramRight();
			if (null != upr) {
				HashMap<String, PageAccessRight> objectCodes = upr.getPageRightManager().get(functionCode);
				if (null != objectCodes) {
					log.info("AccessControlServ.setDocData get upr " + upr.toString() + " upr size " + objectCodes.size());
					if (!objectCodes.keySet().isEmpty()) {
						log.info("AccessControlServ.setDocData objectCodes not empty ");
						doc = PageDocument.Factory.newInstance();
						PageType page = doc.addNewPage();
						Iterator keys = objectCodes.keySet().iterator();
						while (keys.hasNext()) {
							PageAccessRight pr = objectCodes.get(keys.next());
							ControlType control = page.addNewControl();
							control.setObjectCode(pr.getObjectCode());
							control.setObjectName(pr.getObjectName());
							control.setObjectType(pr.getObjectType());
							control.setControlType(pr.getControlType());
						}
					}
				}
			}

		}
		return doc;
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
