package tw.com.tm.erp.hbm.service;

import java.util.Date;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.ImReceiveInvoice;
import tw.com.tm.erp.hbm.dao.ImReceiveInvoiceDAO;

/**
 * 進貨單 Invoice Service.
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImReceiveInvoiceService {

	private ImReceiveInvoiceDAO imReceiveInvoiceDAO;


	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(ImReceiveInvoice modifyObj) {
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
	public String save(ImReceiveInvoice saveObj) {
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveInvoiceDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImReceiveInvoice updateObj) {
		updateObj.setLastUpdateDate(new Date());
		imReceiveInvoiceDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveInvoice findById(Long headId) {
		ImReceiveInvoice re = (ImReceiveInvoice) imReceiveInvoiceDAO
				.findByPrimaryKey(ImReceiveInvoice.class, headId);
		if (null == re)
			re = new ImReceiveInvoice();
		return re;
	}
	
	public void setImReceiveInvoiceDAO(
			ImReceiveInvoiceDAO imReceiveInvoiceDAO) {
		this.imReceiveInvoiceDAO = imReceiveInvoiceDAO;
	}


}