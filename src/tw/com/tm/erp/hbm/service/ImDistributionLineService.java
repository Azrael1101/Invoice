package tw.com.tm.erp.hbm.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImDistributionLine;
import tw.com.tm.erp.hbm.dao.ImDistributionLineDAO;


/**
 * 配貨單 Line Service
 * 
 * @author T02049
 * 
 */
public class ImDistributionLineService {
	private static final Log log = LogFactory.getLog(ImDistributionLineService.class);
	private ImDistributionLineDAO imDistributionLineDAO;

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<ImDistributionLine> find(HashMap findObjs) {
		log.info("ImDistributionLineService.find");
		return imDistributionLineDAO.find(findObjs);
	}

	public void setImDistributionLineDAO(ImDistributionLineDAO imDistributionLineDAO) {
		this.imDistributionLineDAO = imDistributionLineDAO;
	}


}
