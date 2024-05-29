package tw.com.tm.erp.hbm.service;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuShopMachineId;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;


public class BuShopMachineService {

    private static final Log log = LogFactory.getLog(BuShopMachineService.class);

    private BuShopMachineDAO buShopMachineDAO;

    /* Spring IoC */
    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
    	this.buShopMachineDAO = buShopMachineDAO;
    }

    public List findByShopCode(String shopCode) throws Exception{
    	try{	
    		return buShopMachineDAO.findByShopCode(shopCode);
    	}catch(Exception ex){
    		log.error("查詢櫃號：" + shopCode + "的POS機號時發生錯誤，原因：" + ex.toString());
    		throw new Exception("查詢櫃號：" + shopCode + "的POS機號時發生錯誤，原因：" + ex.getMessage());
    	}	
    }

    /**
     * 單據自動跳號. 單號為yyyymmdd+machinecode+流水號(4位)
     * @param soSalesOrderHead
     * @param deliveryHead
     * @return
     * @throws ObtainSerialNoFailedException 
     */
    public String generateNoByDateSerialForT2(String shopCode, String posMachineCode){
    	List<BuShopMachine> buShopMachineList = buShopMachineDAO.getLockedOrderNo(posMachineCode);
    	String currentNo = "unknow";
    	if (buShopMachineList != null && buShopMachineList.size()>0) {
    		BuShopMachine buShopMachine = buShopMachineList.get(0);
    		String serialNo = "";
    		String lastNo = buShopMachine.getLastOrderNo();
    		int seq;
    		String prefix = DateUtils.getCurrentDateStr("yyyyMMdd");
    		if (lastNo == null || "".equals(lastNo) || !prefix.equals(lastNo.substring(0, 8))) {
    			serialNo = "0001";
    		} else {
    			seq = Integer.parseInt(lastNo.substring(10)) + 1;
    			serialNo = CommonUtils.insertCharacterWithFixLength(String.valueOf(seq), 4, "0");
    		}
    		currentNo = prefix + posMachineCode + serialNo;
    		buShopMachine.setLastOrderNo(prefix + posMachineCode + serialNo);
    		buShopMachineDAO.update(buShopMachine);
    	}
    	return currentNo;
    }

}
