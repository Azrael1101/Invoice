package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.service.CmDeclarationHeadService;
import tw.com.tm.erp.hbm.service.ImportDBService;

/**
 * 報單匯入
 * 
 * @author T02049
 * 
 */

public class CmDeclarationImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(CmDeclarationImportData.class);
	public static final String split[] = { "{#}" };

	public ImportInfo initial(HashMap uiProperties) {
		log.info("initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.CmDeclarationHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("T1");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("t1");
		imInfo.addFieldName("msgFun");
		imInfo.addFieldName("bondNo");
		imInfo.addFieldName("strType");
		imInfo.addFieldName("boxNo");
		imInfo.addFieldName("declType");
		imInfo.addFieldName("declNo");
		imInfo.addFieldName("importDate");
		imInfo.addFieldName("declDate");
		imInfo.addFieldName("stgPlace");
		imInfo.addFieldName("rlsTime");
		imInfo.addFieldName("rlsPkg");
		imInfo.addFieldName("extraCond");
		imInfo.addFieldName("pkgUnit");
		imInfo.addFieldName("GWgt");
		imInfo.addFieldName("vesselSign");
		imInfo.addFieldName("voyageNo");
		imInfo.addFieldName("shipCode");
		imInfo.addFieldName("exporter");
		imInfo.addFieldName("clearType");
		imInfo.addFieldName("refBillNo");
		imInfo.addFieldName("inbondNo");
		imInfo.addFieldName("outbondNo");

		// set field type
		imInfo.setFieldType("t1", "java.lang.String");
		imInfo.setFieldType("msgFun", "java.lang.String");
		imInfo.setFieldType("bondNo", "java.lang.String");
		imInfo.setFieldType("strType", "java.lang.String");
		imInfo.setFieldType("boxNo", "java.lang.String");
		imInfo.setFieldType("declType", "java.lang.String");
		imInfo.setFieldType("declNo", "java.lang.String");
		imInfo.setFieldType("importDate", "java.util.Date");
		imInfo.setFieldType("declDate", "java.util.Date");
		imInfo.setFieldType("stgPlace", "java.lang.String");
		imInfo.setFieldType("rlsTime", "java.util.Date");
		imInfo.setFieldType("rlsPkg", "java.lang.Long");
		imInfo.setFieldType("extraCond", "java.lang.String");
		imInfo.setFieldType("pkgUnit", "java.lang.String");
		imInfo.setFieldType("GWgt", "java.lang.Double");
		imInfo.setFieldType("vesselSign", "java.lang.String");
		imInfo.setFieldType("voyageNo", "java.lang.String");
		imInfo.setFieldType("shipCode", "java.lang.String");
		imInfo.setFieldType("exporter", "java.lang.String");
		imInfo.setFieldType("clearType", "java.lang.String");
		imInfo.setFieldType("refBillNo", "java.lang.String");
		imInfo.setFieldType("inbondNo", "java.lang.String");
		imInfo.setFieldType("outbondNo", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		imInfo.setFieldNameFormat("rlsTime", "yyyyMMddHHmmss");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", "");
		defaultValue.put("lastUpdatedBy", "");
		defaultValue.put("fileName", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));

		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getCmDeclarationItems());
		imInfo.addDetailImportInfos(getCmDeclarationVehicles());
		imInfo.addDetailImportInfos(getCmDeclarationContainers());

		return imInfo;
	}

	/**
	 * im item price detail config
	 * 
	 * @return
	 */
	private ImportInfo getCmDeclarationItems() {
		log.info("getCmDeclarationItems");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("T2");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.CmDeclarationItem.class.getName());

		imInfo.addFieldName("t2");
		imInfo.addFieldName("declNo");
		imInfo.addFieldName("itemNo");
		imInfo.addFieldName("prdtNo");
		imInfo.addFieldName("descrip");
		imInfo.addFieldName("brand");
		imInfo.addFieldName("model");
		imInfo.addFieldName("spec");
		imInfo.addFieldName("NWght");
		imInfo.addFieldName("qty");
		imInfo.addFieldName("unit");
		imInfo.addFieldName("ODeclNo");
		imInfo.addFieldName("OItemNo");
		imInfo.addFieldName("descripOther");

		imInfo.setFieldType("t2", "java.lang.String");
		imInfo.setFieldType("declNo", "java.lang.String");
		imInfo.setFieldType("itemNo", "java.lang.Long");
		imInfo.setFieldType("prdtNo", "java.lang.String");
		imInfo.setFieldType("descrip", "java.lang.String");
		imInfo.setFieldType("brand", "java.lang.String");
		imInfo.setFieldType("model", "java.lang.String");
		imInfo.setFieldType("spec", "java.lang.String");
		imInfo.setFieldType("NWght", "java.lang.Double");
		imInfo.setFieldType("qty", "java.lang.Double");
		imInfo.setFieldType("unit", "java.lang.String");
		imInfo.setFieldType("ODeclNo", "java.lang.String");
		imInfo.setFieldType("OItemNo", "java.lang.Long");
		imInfo.setFieldType("descripOther", "java.lang.String");

		return imInfo;
	}

	private ImportInfo getCmDeclarationVehicles() {
		log.info("getCmDeclarationVehicles");
		ImportInfo imInfo2 = new ImportInfo();
		imInfo2.setKeyIndex(0);
		imInfo2.setKeyValue("T3");
		imInfo2.setSaveKeyField(true);
		imInfo2.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.CmDeclarationVehicle.class.getName());

		imInfo2.addFieldName("t3");
		imInfo2.addFieldName("declNo");
		imInfo2.addFieldName("itemNo");
		imInfo2.addFieldName("vehicleNo");

		imInfo2.setFieldType("t3", "java.lang.String");
		imInfo2.setFieldType("declNo", "java.lang.String");
		imInfo2.setFieldType("itemNo", "java.lang.Long");
		imInfo2.setFieldType("vehicleNo", "java.lang.String");

		return imInfo2;
	}

	private ImportInfo getCmDeclarationContainers() {
		log.info("getCmDeclarationContainers");
		ImportInfo imInfo2 = new ImportInfo();
		imInfo2.setKeyIndex(0);
		imInfo2.setKeyValue("T4");
		imInfo2.setSaveKeyField(true);
		imInfo2.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.CmDeclarationContainer.class.getName());

		imInfo2.addFieldName("t4");
		imInfo2.addFieldName("declNo");
		imInfo2.addFieldName("contrNo");
		imInfo2.addFieldName("contrType");
		imInfo2.addFieldName("transMode");

		imInfo2.setFieldType("t4", "java.lang.String");
		imInfo2.setFieldType("declNo", "java.lang.String");
		imInfo2.setFieldType("contrNo", "java.lang.String");
		imInfo2.setFieldType("contrType", "java.lang.String");
		imInfo2.setFieldType("transMode", "java.lang.String");

		return imInfo2;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		log.info("updateDB");
		StringBuffer reMsg = new StringBuffer();
		for (int index = 0; index < entityBeans.size(); index++) {
			CmDeclarationHead modifyObj = (CmDeclarationHead) entityBeans.get(index);
			CmDeclarationHeadService cmDeclarationHeadService = (CmDeclarationHeadService) SpringUtils.getApplicationContext().getBean(
					"cmDeclarationHeadService");
			String msg = null;
			try {
				msg = cmDeclarationHeadService.create(modifyObj);
			} catch (Exception e) {
				msg = e.getMessage();
			}
			reMsg.append("報關單號  DEC NO : " + modifyObj.getDeclNo() + " " + msg + "\n\r");
		}
		return reMsg.toString();
	}
}
