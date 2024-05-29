package tw.com.tm.erp.utils;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class XFireUtils {

    public static List<SoSalesOrderItem> getPosData(String PAT_TRDNO, Date PAT_HDATE) throws Exception {
	Service srvcModel = new ObjectServiceFactory()
		.create(ISoSalesOrderItemService.class);
	XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
		.newInstance().getXFire());
	String url = "http://10.1.98.5:8080/PosTransfer/services/SoSalesOrderItemService";
	ISoSalesOrderItemService srvc = (ISoSalesOrderItemService) factory
		.create(srvcModel, url);
	List<SoSalesOrderItem> items = srvc.getT2PosItems(PAT_TRDNO, PAT_HDATE);
	return items;
    }
}
