package loadCustomsConfig;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import tw.com.tm.erp.hbm.bean.CustomsConfiguration;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.dao.ErpDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;

public class InitialCustomsConfig {

	private static ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	public static CustomsConfiguration configBean;
	public static List<CustomsConfiguration> configs = null;
	public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
    	this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
    }
	private static ErpDAO erpDao = new ErpDAO();
	
	public List<CustomsConfiguration> getConfigs(){
		if (configs == null) {
			this.reloadConfigs();
		}
		return configs;
	}
	
	private static void reloadConfigs(){
		try{
			configs = erpDao.getCustomsConfiguration(null);
			System.out.println("============================%% ReloadCustomsConfig %%============================");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<CustomsConfiguration> getConfigs(String DF){
		List<CustomsConfiguration> returnConfigs = new ArrayList();
		try{
			if(configs == null){
				reloadConfigs();
				for(CustomsConfiguration CusConfig:configs){
					String dstFunction = null==CusConfig.getDestinationFunction()? "":CusConfig.getDestinationFunction();
					if(!dstFunction.equals("")&&dstFunction.equals(DF)){
						returnConfigs.add(CusConfig);
					}
				}
			}else{
				for(CustomsConfiguration CusConfig:configs){
					String dstFunction = null==CusConfig.getDestinationFunction()? "":CusConfig.getDestinationFunction();
					if(!dstFunction.equals("")&&dstFunction.equals(DF)){
						returnConfigs.add(CusConfig);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return returnConfigs;
	}
	
	
	public static List<CustomsConfiguration> getConfigs(String DF,String orderTypeCode){
		List<CustomsConfiguration> returnConfigs = new ArrayList();
		try{
			if(configs == null){
				reloadConfigs();
				System.out.println("============================11============================");
			}else{
				System.out.println("============================22============================");
				if(null != orderTypeCode && !orderTypeCode.equals("")){
					for(CustomsConfiguration CusConfig:configs){					
						String dstFunction = null==CusConfig.getDestinationFunction()? "":CusConfig.getDestinationFunction();
						String otp = null==CusConfig.getOrderTypeCode()? "":CusConfig.getOrderTypeCode();
						if(!otp.equals("")&&otp.toUpperCase().equals(orderTypeCode.toUpperCase()) 
								&& !dstFunction.equals("")&&dstFunction.toUpperCase().equals(DF.toUpperCase())){
							returnConfigs.add(CusConfig);
						}
					}
				}else{
					for(CustomsConfiguration CusConfig:configs){
						System.out.println("============================33============================:"+DF);
					
						String dstFunction = null==CusConfig.getDestinationFunction()? "":CusConfig.getDestinationFunction();
						System.out.println("============================44============================:"+DF);
						if(!dstFunction.equals("")&&dstFunction.toUpperCase().equals(DF.toUpperCase())){
							returnConfigs.add(CusConfig);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return returnConfigs;
	}
	
}
