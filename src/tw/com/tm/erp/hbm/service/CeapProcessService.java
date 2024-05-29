package tw.com.tm.erp.hbm.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.dao.CeapProcessDAO;

public class CeapProcessService {

	private CeapProcessDAO ceapProcessDAO;
    private static final Log log = LogFactory.getLog(CeapProcessService.class);
    
    public void updateProcessSubject(Long processId, String subject) throws Exception{
		ceapProcessDAO.updateCeapProcessSubject(processId, subject);
	}

	public void setCeapProcessDAO(CeapProcessDAO ceapProcessDAO) {
		this.ceapProcessDAO = ceapProcessDAO;
	}

}
