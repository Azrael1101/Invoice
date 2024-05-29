package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuLocationArea;
import tw.com.tm.erp.hbm.dao.BuLocationAreaDAO;

public class BuLocationAreaService {
	private static final Log log = LogFactory.getLog(BuLocationAreaService.class);
	private BuLocationAreaDAO buLocationAreaDAO;


	public List<BuLocationArea> findByValue(String city, String area, String zipCode) throws Exception {
		try {
			return  buLocationAreaDAO.findByValue(city, area, zipCode);
		} catch (Exception ex) {
			log.error("地點資料查詢時發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料查詢發生錯誤，原因：" + ex.getMessage());
		}

	}

	public void setBuLocationAreaDAO(BuLocationAreaDAO buLocationAreaDAO) {
		this.buLocationAreaDAO = buLocationAreaDAO;
	}

}