package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.EtlEventViewTable;
import tw.com.tm.erp.hbm.dao.EtlEventViewTableDAO;

public class EtlEventViewTableService {
	private static final Log log = LogFactory.getLog(EtlEventViewTableService.class);
	
	private EtlEventViewTableDAO etlEventViewTableDAO = new EtlEventViewTableDAO();
	
	public void setEtlEventViewTableDAO(EtlEventViewTableDAO etlEventViewTableDAO) {
		this.etlEventViewTableDAO = etlEventViewTableDAO;
	}

	public List<EtlEventViewTable> findUnprocessedEtlEventViewTable(){
		return etlEventViewTableDAO.findUnprocessedEtlEventViewTable();
	}
	
	public void save(EtlEventViewTable etlEventViewTable) {
		etlEventViewTableDAO.save(etlEventViewTable);
	}
	
	public void update(EtlEventViewTable etlEventViewTable) {
		etlEventViewTableDAO.update(etlEventViewTable);
	}
}
