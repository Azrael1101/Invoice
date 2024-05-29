package tw.com.tm.erp.utils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.dao.DAOFactory;

public class brandServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException{

    res.setContentType("text/plain;charset=Big5");
    PrintWriter output = res.getWriter();
   
    String uid= req.getParameter("name");
    String vsUserDomain = UserUtils.getDomainByEmployeeCode(uid);
    String msg= "<|" + vsUserDomain + "|>";
    
    //2013.09.05更新 先找出啟用的品牌 再找品牌員工
    BuBrandDAO buBrandDAO = DAOFactory.getInstance().getBuBrandDAO();
    BuEmployeeDAO empDAO = DAOFactory.getInstance().getBuEmployeeDAO();
    BuEmployeeBrandDAO buEmployeeBrandDAO = DAOFactory.getInstance().getBuEmployeeBrandDAO();

    List buBrands = buBrandDAO.findByEnable("Y");
    
    BuEmployee emp = empDAO.findByEmployeeCode(uid).get(0);
    
    for (Iterator iterator = buBrands.iterator(); iterator.hasNext();) {
		BuBrand buBrand = (BuBrand) iterator.next();
		BuEmployeeBrandId id = new BuEmployeeBrandId(uid, buBrand.getBrandCode());
		BuEmployeeBrand buEmployeeBrand = buEmployeeBrandDAO.findById(id);
		if(null != buEmployeeBrand)
			msg=msg+buEmployeeBrand.getId().getBrandCode()+"@@";
	}
/*    
    if(emp.getLeaveDate()!=null){
    	
    	msg="Hi";
    }
*/
    /*
    List buEmployeeBrands =buEmployeeBrandDAO.findByProperty("id.employeeCode",uid);
    for(int i =0;i<buEmployeeBrands.size();i++){
    	msg=msg+((BuEmployeeBrand)buEmployeeBrands.get(i)).getId().getBrandCode()+"@@";
    }
     */
    
    msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");

    StringBuffer buf = new StringBuffer();
    buf.append(msg); 
    output.println(buf.toString());
    output.close();
  }
  
  /**
   * Initialization of the servlet. <br>
   * 
   * @throws ServletException
   *             if an error occurs
   */
  public void init() throws ServletException {
      SpringUtils.getApplicationContext();
  }
}

