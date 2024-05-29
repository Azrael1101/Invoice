package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.DbfExportData;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CustmsControlService;
import tw.com.tm.erp.hbm.service.ExportService;
import tw.com.tm.erp.utils.AjaxUtils;


import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.service.UploadControlService;
import tw.com.tm.erp.hbm.dao.UploadControlDAO;
import tw.com.tm.erp.hbm.service.SoParallelTestService;
import tw.com.tm.erp.hbm.dao.SoParallelTestDAO;

public class SoParallelTestAction
{
    private static final Log log = LogFactory.getLog(UploadControlAction.class);
    private SoParallelTestService soParallelTestService;
    
    Properties config = new Properties();

    public void setSoParallelTestService(SoParallelTestService soParallelTestService)
    {
        this.soParallelTestService = soParallelTestService;
    }
    
    private SoParallelTestDAO soParallelTestDAO;
    public void setSoParallelTestDAO(SoParallelTestDAO soParallelTestDAO) {
		this.soParallelTestDAO = soParallelTestDAO;
	}
    /**
     * 初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    
    public void performInitial()throws Exception{
    	log.info("開始執行SO複製作業");
		List<Long> rs = null;
		soParallelTestService.loadConfig(config);
		String control = config.getProperty("controller");
		
		if(control.equals("0")){
			//flag = 1
			config.setProperty("controller", "1");
			soParallelTestService.clsConfig(config);
			
			rs = soParallelTestDAO.getPosCommand();		
			
			try {
				for(Long l : rs) {
					try{
						log.info("REQUEST_ID:"+l.toString());
						soParallelTestService.updateTestPOSInfo("KGTECH",l);
						
						log.info("into soParallelTestService.updateCopySo:"+l.toString());
					
						//soParallelTestService.updateCopySo(l);
					}catch (Exception e) {
						//log.info("vvvvvvvvvvvvvvvvvvv");
						e.printStackTrace();
					}
				}
				//posDUAction.getPOSInfo(request);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("byself");
				e.printStackTrace();
			}finally {
				//flag = 0
				config.put("controller", "0");
				soParallelTestService.clsConfig(config);
			}
		}else{
			log.info("使用中");
		}
		/*try {
			if(control.equals("0")){
				config.setProperty("controller", "1");
				saveConfig();
				rs = soParallelTestDAO.getPosCommand();
				
				while(rs.next()) {
					try {
						log.error("進入getTestPOSInfo");
						this.getTestPOSInfo("KGTECH",rs.getLong("REQUEST_ID"));						
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						soParallelTestDAO.updatePOSInfo(rs.getLong("REQUEST_ID"));
					}
				}
			}else{
				log.error("使用中");
			}
		}catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//flag = 0
			rs.close();
			config.put("controller", "0");
			saveConfig();
		}*/
    }
	
}