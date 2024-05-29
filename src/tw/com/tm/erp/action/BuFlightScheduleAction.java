package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuFlightScheduleService;
import tw.com.tm.erp.hbm.service.TmpAjaxSearchDataService;
import tw.com.tm.erp.utils.AjaxUtils;


public class BuFlightScheduleAction {

    private static final Log log = LogFactory.getLog(BuFlightScheduleAction.class);

	private BuFlightScheduleService buFlightScheduleService;
	private SiProgramLogAction siProgramLogAction;
	private TmpAjaxSearchDataService tmpAjaxSearchDataService;
	public void setBuFlightScheduleService(BuFlightScheduleService buFlightScheduleService) {
		this.buFlightScheduleService = buFlightScheduleService;
	}

	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}



    /**
	 * 執行航班交易流程
	 *
	 * @param parameterMap
	 * @return
     * @throws Exception 
	 */
	 
	public List<Properties> performTransaction(Map parameterMap) {

    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			
			HashMap resultMap = buFlightScheduleService.save(parameterMap);
			log.info("update Data compile");
			BuFlightSchedule bean = (BuFlightSchedule) resultMap.get("entityBean");
			log.info("get entityBean to BuFlightSchedule...");
			msgBox.setMessage((String) resultMap.get("resultMsg") + "");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.WIN_CLOSE);
			cmd_bf.setParameters(new String[] { "", "" });
			msgBox.setOk(cmd_bf);
		} catch (FormException fex) {
			msgBox.setMessage("執行航班存檔時發生錯誤");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("執行航班存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行航班存檔時發生錯誤，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	
    
}

