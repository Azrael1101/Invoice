package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopTarget;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BuShopTargetDAO;

public class BuShopTargetService {

	private static final Log log = LogFactory.getLog(BuShopTargetService.class);

	private BuShopTargetDAO buShopTargetDAO;
	private List<BuShopTarget> buShopTargets;

	public BuShopTargetDAO getBuShopTargetDAO() {
		return buShopTargetDAO;
	}

	public void setBuShopTargetDAO(BuShopTargetDAO buShopTargetDAO) {
		this.buShopTargetDAO = buShopTargetDAO;
	}

	public String saveOrUpdateBuShop(List buShopTargets, String loginUser) throws FormException, Exception {
		Iterator<BuShopTarget> list = buShopTargets.iterator();
		String msg = "";
		while (list.hasNext()) {
			msg = saveOrUpdateBuShop(list.next(), loginUser, msg);
		}
		return msg;
	}

	public String saveOrUpdateBuShop(BuShopTarget buShopTarget, String loginUser, String msg) throws FormException, Exception {
		try {
//			HashMap findObjs = new HashMap();
//			findObjs.put("shopCode", buShopTarget.getBuShop().getShopCode());
//			findObjs.put("year", buShopTarget.getYear());
//			findObjs.put("month", buShopTarget.getMonth());
//			List<BuShopTarget> buShopTargets = buShopTargetDAO.find(findObjs);
			msg = "";
			if (buShopTarget.getLineId() == null) {
				insertBuShopTarget(buShopTarget, loginUser);
				msg = "此專櫃業績存檔成功！";
			} else {
				modifyBuShopTarget(buShopTarget, loginUser);
				msg = "此專櫃業績修改成功！";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "資料存檔失敗";
			log.error("資料存檔時發生錯誤，Exception原因：" + ex.toString(), ex);
			throw new Exception("業績資料存檔時發生錯誤，原因：" + ex.getMessage());
		}
		return msg;
	}

	private void insertBuShopTarget(BuShopTarget buShopTarget, String loginUser) {
		buShopTarget.setLastUpdatedBy(loginUser);
		buShopTarget.setLastUpdateDate(new Date());
		buShopTarget.setCreatedBy(loginUser);
		buShopTarget.setCreationDate(new Date());
		buShopTargetDAO.save(buShopTarget);
	}

	private void modifyBuShopTarget(BuShopTarget buShopTarget, String loginUser) {
		buShopTarget.setLastUpdatedBy(loginUser);
		buShopTarget.setLastUpdateDate(new Date());
		buShopTargetDAO.update(buShopTarget);
	}

	private void doBuShopTargetValidate(String year) throws ValidationErrorException, NoSuchObjectException, Exception {
		if (!StringUtils.hasText(year)) {
			throw new ValidationErrorException("請輸入年！");
		}
		try {
			int bInt = Integer.parseInt(year);
		} catch (NumberFormatException e) {
			throw new ValidationErrorException("年請輸入數字！");
		}

	}

	public BuShopTarget findByPrimaryKey(BuShopTarget buShopTarget) throws Exception {
		try {
			return (BuShopTarget) buShopTargetDAO.findByPrimaryKey(BuShopTarget.class, buShopTarget.getLineId().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	public List<BuShopTarget> getBuShopTargets() {
		return buShopTargets;
	}

	public void setBuShopTargets(List<BuShopTarget> buShopTargets) {
		this.buShopTargets = buShopTargets;
	}

	public List<BuShopTarget> find(HashMap findObjs, String loginUser) throws Exception {
		try {
			List<BuShopTarget> buShopTargets = buShopTargetDAO.find(findObjs);
			String year = findObjs.get("year").toString();
			String shopCode = findObjs.get("shopCode").toString();
			if (buShopTargets == null) {
				buShopTargets = new ArrayList();
			}
			if (buShopTargets.size() != 12) {
				while (buShopTargets.size() < 12) {
					buShopTargets.add(new BuShopTarget());
				}
				for (int i = 0; i <= 11; i++) {
					if (buShopTargets.get(i).getLineId() != null) {
						BuShopTarget bean = buShopTargets.get(Integer.parseInt(buShopTargets.get(i).getMonth()) - 1);
						buShopTargets.set(Integer.parseInt(buShopTargets.get(i).getMonth()) - 1, buShopTargets.get(i));
						buShopTargets.set(i, bean);
					}
					BuShopTarget bean = buShopTargets.get(i);
					if (!getStrMonth(i + 1).equals(bean.getMonth())) {
						bean.setYear(year);
						bean.setMonth(getStrMonth(i + 1));
						bean.setSalesTarget(0l);
						bean.setAccountTarget(0l);
						BuShop shop = new BuShop();
						shop.setShopCode(shopCode);
						bean.setBuShop(shop);
						bean.setCreatedBy(loginUser);
						bean.setLastUpdatedBy(loginUser);
						bean.setIndexNo(Long.parseLong(String.valueOf(i + 1)));
						buShopTargets.set(i, bean);
					}
				}
			}else{
				int j = 0;
				while (j < 12) {
					buShopTargets.get(j).setMonth(getStrMonth(j+1));
					j++;
				}
				
			}
			return buShopTargets;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查詢發生錯誤，原因：" + e.getMessage());
		}
	}
	
	private String getStrMonth(int month){
		if(month < 10){
			return "0" + String.valueOf(month);
		}
		return String.valueOf(month);
		
	}
}
