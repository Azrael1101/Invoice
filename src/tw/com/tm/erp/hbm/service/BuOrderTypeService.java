package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeApproval;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeApprovalDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;

public class BuOrderTypeService {
	// property constants
	private static final Log log = LogFactory.getLog(BuOrderTypeService.class);
	public static final String MANAGER = "roleApproveLevel01";
	public static final String SALES_MANAGER = "roleApproveLevel02";
	public static final String STORE_MANAGER = "roleApproveLevel03";
	public static final String GOODS_MANAGER = "roleApproveLevel04";
	public static final String FINANCE_MANAGER = "roleApproveLevel05";
	public static final String VP = "roleApproveLevel18";
	public static final String CEO = "roleApproveLevel19";
	public static final String CHAIRMAN = "roleApproveLevel20";

	BuOrderTypeDAO buOrderTypeDAO;
	BuOrderTypeApprovalDAO buOrderTypeApprovalDAO;
	BuEmployeeDAO buEmployeeDAO;
	BuShopMachineService buShopMachineService;

	public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
		this.buOrderTypeDAO = buOrderTypeDAO;
	}

	public void setBuOrderTypeApprovalDAO(BuOrderTypeApprovalDAO buOrderTypeApprovalDAO) {
		this.buOrderTypeApprovalDAO = buOrderTypeApprovalDAO;
	}

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setBuShopMachineService(BuShopMachineService buShopMachineService) {
	    this.buShopMachineService = buShopMachineService;
	}
	

	/**
	 * 列出相同單據類別可用的單別<br>
	 *
	 * @param brandCode
	 *            品牌代號
	 * @param typeCode
	 *            單據類別
	 * @return BuOrderType List
	 */
	public List<BuOrderType> findOrderbyType(String brandCode, String typeCode) {
		return buOrderTypeDAO.findOrderbyType(brandCode, typeCode);
	}

	/**
	 * 依據primary key為查詢條件
	 *
	 * @param id
	 * @return BuOrderType
	 */
	public BuOrderType findById(BuOrderTypeId id) {
		return buOrderTypeDAO.findById(id);
	}

	/**
	 * 單據自動跳號<br>
	 * @param brandCode 品牌代號
	 * @param orderTypeCode 單據類別
	 * @return String orderNo
	 */
	public synchronized String getOrderSerialNo(String brandCode, String orderTypeCode) throws ObtainSerialNoFailedException{
		return getOrderNo(brandCode, orderTypeCode, null, null);
	}

	/**
	 * 單據自動跳號<br>
	 * @param brandCode 品牌代號
	 * @param orderTypeCode 單據類別
	 * @param shopCode 店別代號
	 * @param posMachineCode POS機台代號
	 * @return String orderNo
	 */
	public synchronized String getOrderNo(String brandCode, String orderTypeCode, 
			String shopCode, String posMachineCode ) throws ObtainSerialNoFailedException{
		String currentNo = null;
		String seqType = "";
		String lastNo = "";
		BuOrderTypeId id = new BuOrderTypeId(brandCode, orderTypeCode);
		BuOrderType orderInfo = buOrderTypeDAO.findById(id);
		if (orderInfo != null) {
			seqType = orderInfo.getSequenceType();
			lastNo = orderInfo.getLastOrderNo();
			if ("1".equals(seqType)) {
				currentNo = generateNoByDateSerial(lastNo);
				orderInfo.setLastOrderNo(currentNo);
				buOrderTypeDAO.update(orderInfo);
			}else if ("2".equals(seqType)) {
				currentNo = generateNoByTwDateSerial(lastNo);
				orderInfo.setLastOrderNo(currentNo);
				buOrderTypeDAO.update(orderInfo);
			}else if ("3".equals(seqType)) {
				currentNo = generateNoT2ByDateSerial(orderInfo, lastNo);
				orderInfo.setLastOrderNo(currentNo);
				buOrderTypeDAO.update(orderInfo);
			}else if("4".equals(seqType)) {
				currentNo = buShopMachineService.generateNoByDateSerialForT2(shopCode, posMachineCode);
			}else if("5".equals(seqType)) { // 新增運送單狀態 by Weichun 2011.03.30
				currentNo = generateNoByTwDateSerialNew(lastNo);
				orderInfo.setLastOrderNo(currentNo);
				buOrderTypeDAO.update(orderInfo);
			}
		}
		if(null == currentNo)
			throw new ObtainSerialNoFailedException("查無品牌:" + brandCode + ",單別:" + orderTypeCode + ",店別:" + shopCode+ ",POS機台:" + posMachineCode + " 對應之單號");
		return currentNo;
	}

	/**
	 * 西元年月日+ 4位流水號
	 * @param lastNo
	 * @return
	 */
	public String generateNoByDateSerial(String lastNo) {
		String serialNo = "";
		int seq;
		String prefix = DateUtils.getCurrentDateStr("yyyyMMdd");
		if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(0, 8))) {
			serialNo = "0001";
		} else {
			seq = Integer.parseInt(lastNo.substring(8)) + 1;
			serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 4, "0");
		}

		return prefix + serialNo;
	}

	/**
	 * 民國年月日+ 3位流水號
	 * @param lastNo
	 * @return
	 */
	public String generateNoByTwDateSerial(String lastNo) {
		String serialNo = "";
		int seq;
		String prefix = DateUtils.getCurrentDateStr("yyyyMMdd");
		String twYear = CommonUtils.insertCharacterWithFixLength(String.valueOf(Integer.valueOf(prefix.substring(0,4)) - 1911), 2, "0");
		twYear = twYear.substring(twYear.length()-2); // 民國一百年後，年份只取後面兩位數 by Weichun 2011.01.04
		prefix = twYear +  prefix.substring(4);
		if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(0, 6))) {
			serialNo = "001";
		} else {
			seq = Integer.parseInt(lastNo.substring(6)) + 1;
			serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 3, "0");
		}

		return prefix + serialNo;
	}

	/**
	 * 取得新的單號 民國年3碼＋月份2碼＋日期2碼＋流水號3碼
	 *
	 * @param lastNo
	 * @return
	 */
	public String generateNoByTwDateSerialNew(String lastNo) {
		String serialNo = "";
		int seq;
		String prefix = DateUtils.getCurrentDateStr("yyyyMMdd");
		String twYear = CommonUtils.insertCharacterWithFixLength(String
				.valueOf(Integer.valueOf(prefix.substring(0, 4)) - 1911), 2, "0");
		prefix = twYear + prefix.substring(4);
		if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(0, prefix.length()))) {
			serialNo = "001";
		} else {
			seq = Integer.parseInt(lastNo.substring(prefix.length())) + 1;
			serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 3, "0");
		}

		return prefix + serialNo;
	}

	/**
	 * 固定碼(5碼) + 民國碼(2或3碼) + 關別碼(2碼) + 序號碼(4碼)
	 * @param lastNo
	 * @return
	 */
	public String generateNoT2ByDateSerial(BuOrderType buOrderType, String lastNo) {
		String serialNo = "";
		int seq;
		String prefix = DateUtils.getROCDateStr();
		String cmWarehouse = buOrderType.getId().getOrderTypeCode().substring(1, 3); // 關別
		if(StringUtils.hasText(lastNo)){
		    log.info("lastNo.substring(6, lastNo.indexOf(cmWarehouse) = " + lastNo.substring(6, lastNo.indexOf(cmWarehouse)));
		    log.info("!prefix.equals(lastNo.substring(6, lastNo.indexOf(cmWarehouse))) = " + !prefix.equals(lastNo.substring(6, lastNo.indexOf(cmWarehouse))) );
		}
		if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(6, lastNo.indexOf(cmWarehouse)))) {
			serialNo = "0001";
		} else {
			seq = Integer.parseInt(lastNo.substring(lastNo.length() - 4,lastNo.length())) + 1;
			serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 4, "0");
		}

		return buOrderType.getReserve2() + prefix + cmWarehouse + serialNo;
	}

	public BuOrderTypeApproval findOrderTypeApprovalById(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return obj;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getManagerApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, MANAGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getFinanceApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, FINANCE_MANAGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getStoreApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, STORE_MANAGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getSalesApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, SALES_MANAGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getGoodsManagerApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, GOODS_MANAGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getVPApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, VP));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getCEOApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, CEO));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Integer getChairmanApprovalLevel(String id) {
		try {
			BuOrderTypeApproval obj = buOrderTypeApprovalDAO.findById(id);
			if (obj == null) {
				return null;
			} else {
				return Integer.valueOf(BeanUtils.getProperty(obj, CHAIRMAN));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String findApprovalLevel(String brandCode, String orderTypeCode, String approvalLevelCode) throws Exception {
		try {
			List<BuOrderTypeApproval> obj = buOrderTypeApprovalDAO.findApprovalLevel(brandCode, orderTypeCode, approvalLevelCode);
			if (obj.size() > 0) {
				return obj.get(0).getCode();
			} else {
				throw new NoSuchDataException("查無" + brandCode + "之" + orderTypeCode + "單別簽核資料");

			}
		} catch (Exception ex) {
			log.error("查詢單別簽核資料檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢單別簽核資料檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

	public List findOrderTypeManager(String brandCode, String orderTypeCode) throws Exception {
		try {
			List obj = buOrderTypeDAO.findOrderTypeManagerMail(brandCode, orderTypeCode);
			return obj;
		} catch (Exception ex) {
			log.error("取得單別負責人電子郵件時發生錯誤，原因：" + ex.toString());
			throw new Exception("取得單別負責電子郵件時發生錯誤，原因：" + ex.getMessage());
		}

	}

	/**
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List findByProperty(String propertyName, Object value) {
		return buOrderTypeDAO.findByProperty(propertyName, value);
	}

	/**
	 * 取得稅別代碼
	 * @param brandCode
	 * @param orderTypeCode
	 * @return String
	 */
	public String getTaxCode(String brandCode, String orderTypeCode) throws FormException{
	    String taxCode = null;
	    BuOrderType orderTypePO = findById(new BuOrderTypeId(brandCode, orderTypeCode));
	    if(orderTypePO == null){
                throw new NoSuchObjectException("依據品牌(" + brandCode + ")、單別(" + orderTypeCode + ")查無單別相關資料！");
	    }else{
		taxCode = orderTypePO.getTaxCode();
	    }
	    return taxCode;
	}

	/**
     * 取得指定連動的單別下拉
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXOrderTypeCode(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String typeCode = httpRequest.getProperty("typeCode");
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			List allOrderType = buOrderTypeDAO.findOrderbyTypeCode( brandCode, orderTypeCode, typeCode, "Y" );
			allOrderType = AjaxUtils.produceSelectorData(allOrderType, "orderTypeCode", "name", true, true);
	    	properties.setProperty("allOrderType", AjaxUtils.parseSelectorData(allOrderType));
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定連動的單別下拉發生錯誤，原因：" + e.toString());
			throw new Exception("取得指定連動的單別下拉發生錯誤，原因：" + e.getMessage());
		}
		return list;
	}
	
	public String update(BuOrderType updateObj) throws Exception {
		buOrderTypeDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}
}