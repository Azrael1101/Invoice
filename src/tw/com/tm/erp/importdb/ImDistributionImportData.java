package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;
import tw.com.tm.erp.hbm.bean.ImDistributionLine;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.service.ImDistributionHeadService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.User;

/**
 * 配貨單匯入
 * 
 * @author T02049
 * 
 */

public class ImDistributionImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImDistributionImportData.class);

	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImDistributionImportData.initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImDistributionHead.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

		imInfo.addFieldName("key");
		imInfo.addFieldName("orderNo");

		// set field type
		imInfo.setFieldType("key", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");

		// add detail
		imInfo.addDetailImportInfos(getImImDistributionLine());
		// imInfo.addDetailImportInfos(getImItemCompose());

		return imInfo;
	}

	private ImportInfo getImImDistributionLine() {
		log.info("ImDistributionImportData.getImImDistributionLine");
		ImportInfo imInfo = new ImportInfo();
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "1");
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImDistributionLine.class.getName());
		imInfo.addFieldName("key");
		imInfo.addFieldName("itemCode");
		// imInfo.addFieldName("itemName"); // 這個FIELD 不會寫入
		imInfo.addFieldName("shopCode");
		imInfo.addFieldName("quantity");
		// item price
		imInfo.setFieldType("key", "java.lang.String");
		imInfo.setFieldType("itemCode", "java.lang.String");
		// imInfo.setFieldType("itemName", "java.lang.String");
		imInfo.setFieldType("shopCode", "java.lang.String");
		imInfo.setFieldType("quantity", "java.lang.Double");

		// int noWF[] = { 1 };
		// imInfo.setNotImportFieldIndex(noWF);

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("ImDistributionImportData.updateDB");
		StringBuffer reMsg = new StringBuffer();
		User user = (User) info.getUiProperties().get(SystemConfig.USER_SESSION_NAME);
		if (null != user) {
			for (int index = 0; index < entityBeans.size(); index++) {
				ImDistributionHead modifyObj = (ImDistributionHead) entityBeans.get(index);
				ImDistributionHeadService imDistributionHeadService = (ImDistributionHeadService) SpringUtils.getApplicationContext()
						.getBean("imDistributionHeadService");
				String msg = null;
				try {

					modifyObj.getOrderNo();
					HashMap findObjs = new HashMap();
					findObjs.put("orderTypeCode", "IDO");
					findObjs.put("brandCode", user.getBrandCode());
					findObjs.put("orderNo", modifyObj.getOrderNo());
					List<ImDistributionHead> imDistributionHeads = imDistributionHeadService.find(findObjs);
					if (null != imDistributionHeads && imDistributionHeads.size() > 0) {
						ImDistributionHead imDistributionHead = imDistributionHeads.get(0);						
						
						/*20090113 shan*/						
						List<ImDistributionLine> oldLines = imDistributionHead.getImDistributionLines();
						//MARK OLD DATA
						for (ImDistributionLine line : oldLines) {
							line.setQuantity(-1D);
						}						
						
						List<ImDistributionLine> lines = modifyObj.getImDistributionLines();
						List<ImDistributionLine> addLines = new ArrayList();
						//有匯入的資料
						for (ImDistributionLine line : lines) {
							line.setItemCode(line.getItemCode());
							line.setShopCode(line.getShopCode());
							ImDistributionLine imDistributionLine = imDistributionHeadService.getDistributionLine(imDistributionHead, line
									.getItemCode(), line.getShopCode());							
							//匯入時新增項目
							if( null == imDistributionLine ){
								imDistributionLine = new ImDistributionLine();
								imDistributionLine.setItemCode(line.getItemCode());
								imDistributionLine.setShopCode(line.getShopCode());
								imDistributionLine.setDistributionQuantity(0D);
								imDistributionLine.setQuantity(line.getQuantity());
								addLines.add(imDistributionLine);
							}else{
								imDistributionLine.setQuantity(line.getQuantity());
							}
						}
						
						for(ImDistributionLine addLine : addLines){
							log.info("ImDistributionImportData.updateDB itemCode=" + addLine.getItemCode() + ",shopCode=" + addLine.getShopCode() );
							oldLines.add(addLine);
						}
						
						//移除沒有匯入的LINE
						List<ImDistributionLine> imDistributionLines = imDistributionHead.getImDistributionLines();
						Iterator<ImDistributionLine> it = imDistributionLines.iterator();
						while (it.hasNext()) {
							ImDistributionLine removeObj = it.next();
							if (removeObj.getQuantity() < 0D) {
								it.remove();
								imDistributionLines.remove(removeObj);
							}
						}
						
						List<SortedSet> shopsAndItems = imDistributionHeadService.getShopsAndItems(imDistributionHead);
						
						StringBuffer sbItems = new StringBuffer();						
						SortedSet itemSet = shopsAndItems.get(1);						
						Iterator items = itemSet.iterator();
						while(items.hasNext()){
							sbItems.append("'");
							sbItems.append( (String)items.next() ) ;
							sbItems.append("'");
							sbItems.append(",");							
						}
						if (sbItems.length() > 1)
							sbItems.deleteCharAt(sbItems.length() - 1);						
						imDistributionHead.setAllDistributionItems(sbItems.toString());
						
						StringBuffer sbShops = new StringBuffer();
						SortedSet shopSet = shopsAndItems.get(0);
						Iterator shops = shopSet.iterator();
						while(shops.hasNext()){
							sbShops.append("'");
							sbShops.append( (String)shops.next() ) ;
							sbShops.append("'");
							sbShops.append(",");							
						}
						if (sbShops.length() > 1)
							sbShops.deleteCharAt(sbShops.length() - 1);						
						imDistributionHead.setAllDistributionShops(sbShops.toString());
						
						
						msg = imDistributionHeadService.update(imDistributionHead);
					}
				} catch (Exception e) {
					msg = e.getMessage();
				}
				reMsg.append("配貨單 單號 : " + modifyObj.getOrderNo() + " " + msg + "\n\r");
			}
		} else {
			reMsg.append("請先確認您是否已經登入 .. ");
		}
		return reMsg.toString();
	}

	private String removeCName(String codeWithCName) {
		return codeWithCName.substring(codeWithCName.indexOf("(") + 1, codeWithCName.indexOf(")"));
	}

}
