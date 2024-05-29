package tw.com.tm.erp.batch;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.utils.EmployeeDataParse;

public class EmployeeDataParseJob {
    
    public void execute(){
	try{
	    EmployeeDataParse employeeDataParse = (EmployeeDataParse) SpringUtils.getApplicationContext().getBean("employeeDataParse");
	    employeeDataParse.execute();
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
