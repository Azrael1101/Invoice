package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImDeliveryMainService;
import tw.com.tm.erp.hbm.service.ImItemCategoryService;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImDeliveryService;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.ValidateUtil;

/**
 * 手錶抽成銷售匯入
 * 
 * @author T40622
 * 
 */

public class ImDeliveryImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImItemImportData.class);

	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImItemImportData.initial");
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
		log.info("user.getBrandCode() = " + user.getBrandCode());

		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImDeliveryHead.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

		imInfo.addFieldName("key");
		// set field type
		imInfo.setFieldType("key", "java.lang.String");

		imInfo.setXlsColumnLength(7);
		doInitial(imInfo);
		imInfo.setOneRecordDetailKeys(new int[] {7});

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		imInfo.setDefaultValue(defaultValue);

		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doInitial(ImportInfo imInfo){

		imInfo.addFieldName("brandCode");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.addFieldName("displayPer");
		imInfo.addFieldName("baselPer");
		imInfo.addFieldName("baselPer2");
		imInfo.addFieldName("salePer");

		// set field type
		imInfo.setFieldType("brandCode", "java.lang.String");
		imInfo.setFieldType("orderTypeCode", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");
		imInfo.setFieldType("displayPer", "java.lang.Double");
		imInfo.setFieldType("baselPer", "java.lang.Double");
		imInfo.setFieldType("baselPer2", "java.lang.Double");
		imInfo.setFieldType("salePer", "java.lang.Double");

		return imInfo;
	}


	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("ImDeliveryImportData.updateDB");
		ImDeliveryMainService imDeliveryMainService = (ImDeliveryMainService) SpringUtils.getApplicationContext().getBean("imDeliveryMainService");

		StringBuffer result = new StringBuffer();

		int allCount = 0;
		int correctCount = 0 ;
		int failCount = 0 ;
		for (int index = 0; index < entityBeans.size(); index++) {
			ImDeliveryHead imDeliveryHead = (ImDeliveryHead) entityBeans.get(index); // 匯入的商品

			log.info("ImDeliveryHead");
			log.info(imDeliveryHead.getBrandCode());
			log.info(imDeliveryHead.getOrderTypeCode());
			log.info(imDeliveryHead.getOrderNo());
			log.info(imDeliveryHead.getDisplayPer());
			log.info(imDeliveryHead.getBaselPer());
			log.info(imDeliveryHead.getBaselPer2());
			log.info(imDeliveryHead.getSalePer());

			String brandCode = imDeliveryHead.getBrandCode();
			String orderType = imDeliveryHead.getOrderTypeCode();
			String orderNo = imDeliveryHead.getOrderNo();
			Double displayPer = NumberUtils.getDouble(imDeliveryHead.getDisplayPer());
			Double baselPer = NumberUtils.getDouble(imDeliveryHead.getBaselPer());
			Double baselPer2 = NumberUtils.getDouble(imDeliveryHead.getBaselPer2());
			Double salePer = NumberUtils.getDouble(imDeliveryHead.getSalePer());

			log.info("brandcode="+brandCode);

			if ((null != imDeliveryHead) && (StringUtils.hasText(imDeliveryHead.getOrderNo())) ) {
				Double displayAmount = 0D;			
				Double baselAmount = 0D;
				Double baselAmount2 = 0D;
				Double saleAmount = 0d;

				ImDeliveryHead imDeliveryorderno = imDeliveryMainService.findDeliveryByIdentification(brandCode, orderType, orderNo);
				
				try {
					if (null != imDeliveryorderno) { // 匯入的出貨單資料有在資料庫

						Double totalActualShipAmount = imDeliveryorderno.getTotalActualShipAmount();  //出貨金額
						
						imDeliveryorderno.setDisplayPer(displayPer);
						imDeliveryorderno.setBaselPer(baselPer);
						imDeliveryorderno.setBaselPer2(baselPer2);
						imDeliveryorderno.setSalePer(salePer);
						
						log.info("displayPer " + displayPer+" ========= "+displayPer);
						//計算陳列獎金
						if(displayPer > 0 &&  displayPer < 100){
							displayAmount = NumberUtils.round((totalActualShipAmount*displayPer)/100,2);
							log.info("displayAmount " + displayAmount+" ========= "+displayAmount);
						}

						imDeliveryorderno.setDisplayAmount(displayAmount);

						log.info("baselPer " + baselPer+" ========= "+baselPer);
						//計算base獎金
						if(baselPer > 0 &&  baselPer < 100){
							baselAmount = NumberUtils.round((( totalActualShipAmount - displayAmount ) * baselPer ) / 100, 2);
							log.info("baselAmount " + baselAmount+" ========= "+baselAmount);
						}

						imDeliveryorderno.setBaselAmount(baselAmount);

						log.info("baselPer2 " + baselPer2+" ========= "+baselPer2);						
						//計算base2獎金
						if(baselPer2 > 0 &&  baselPer2 < 100){
							baselAmount2 = NumberUtils.round((( totalActualShipAmount - displayAmount - baselAmount ) * baselPer2 ) / 100, 2);
							log.info("baselAmount2 " + baselAmount2+" ========= "+baselAmount2);
						}

						imDeliveryorderno.setBaselAmount2(baselAmount2);
						
						log.info("salePer " + salePer + " ========="+salePer);
						//計算銷售獎金
						if(salePer > 0 &&  salePer < 100){
							saleAmount = NumberUtils.round((( totalActualShipAmount - displayAmount - baselAmount - baselAmount2 ) * salePer) / 100, 2);
							log.info("saleAmount " + saleAmount+" ========= "+saleAmount);
						}

						imDeliveryorderno.setSaleAmount(saleAmount);

						//出貨單存檔
						imDeliveryMainService.modifyImDelivery(imDeliveryorderno);

					}else{
						//單號不存在，在資料庫的
						failCount++;
						result.append("<br>匯入銷售獎金資料有問題 單號: " + imDeliveryHead.getOrderNo() + " 錯誤原因 : 單號不存在<br/>");
					}
				} catch (Exception ex) {
					failCount++;
					ex.printStackTrace();
					log.error("匯入銷售獎金資料有問題 單號: " + imDeliveryHead.getOrderNo()+ ex.getMessage() );
					result.append("匯入銷售獎金資料有問題 單號: " + imDeliveryHead.getOrderNo() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
				}

			}else{
				//無單號的
				failCount++;
				result.append("<br>匯入銷售獎金資料有問題 無填寫單號<br>");
			}

			if(StringUtils.hasText(imDeliveryHead.getOrderNo())){
				allCount++;
			}

		}
		result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
		return result.toString();
	}    

}
