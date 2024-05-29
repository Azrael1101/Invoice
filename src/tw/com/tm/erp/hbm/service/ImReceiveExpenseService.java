package tw.com.tm.erp.hbm.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.dao.ImReceiveExpenseDAO;


/**
 * 進貨單費用 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImReceiveExpenseService {
	private static final Log log = LogFactory.getLog(ImReceiveExpenseService.class);
	private ImReceiveExpenseDAO imReceiveExpenseDAO;
	//private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO ;
	private BuBasicDataService buBasicDataService; 


	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(ImReceiveExpense modifyObj) {
		if (null != modifyObj) {
			if (modifyObj.getLineId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return "";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 */
	public String save(ImReceiveExpense saveObj) {
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveExpenseDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImReceiveExpense updateObj) {
		updateObj.setLastUpdateDate(new Date());
		imReceiveExpenseDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveExpense findById(Long headId) {
		ImReceiveExpense re = (ImReceiveExpense) imReceiveExpenseDAO
				.findByPrimaryKey(ImReceiveExpense.class, headId);
		if (null == re)
			re = new ImReceiveExpense();
		return re;
	}
	
	public void doValidate(ImReceiveHead head,ImReceiveExpense item) throws Exception{
		log.info("ImReceiveExpenseService.doValidate" );
		// item.getSupplierCode() 客戶代號
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(head.getBrandCode(),item.getSupplierCode());
		if(null == buSWAV ){
			throw new Exception("客戶代號有問題 : " + item.getIndexNo() );
		}	
	}

	public void setImReceiveExpenseDAO(
			ImReceiveExpenseDAO imReceiveExpenseDAO) {
		this.imReceiveExpenseDAO = imReceiveExpenseDAO;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}



}