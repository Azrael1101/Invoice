package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceLine;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceLineId;
import tw.com.tm.erp.hbm.service.ImMovementService;

public class ImMonthlyBalanceLineDAO extends BaseDAO {
	 private static final Log log = LogFactory.getLog(ImMonthlyBalanceLineDAO.class);	
	 
	 public void updatePeriodMovementQantity( String brandCode, String year, String month, 
			 	String itemCode, String warehouseCode, String lotNo, Double quantity, Double averageUnitCost ) throws ValidationErrorException {

		 log.info("updatePeriodMovementQantityDAO.."+brandCode+"/"+year+"/"+month+"/"+itemCode+"/"+warehouseCode+"/"+lotNo);
		 ImMonthlyBalanceLineId id =  new ImMonthlyBalanceLineId();
		 id.setBrandCode(brandCode);
		 id.setYear(year);
		 id.setMonth(month);
		 id.setItemCode(itemCode);
		 id.setWarehouseCode(warehouseCode);
		 id.setLotNo(lotNo);
		 
		 ImMonthlyBalanceLine updateObj = (ImMonthlyBalanceLine)super.findByPrimaryKey(ImMonthlyBalanceLine.class, id);
		 if(null != quantity){
			 if(null != updateObj){
				
					 Double periodMovementQuantity = new Double(updateObj.getPeriodMovementQuantity()+quantity);
					 Double endingOnHandQuantity = new Double(updateObj.getEndingOnHandQuantity()+quantity);
					 Double endingOnHandAmount = new Double(endingOnHandQuantity * updateObj.getAverageUnitCost());
					 updateObj.setPeriodMovementQuantity(periodMovementQuantity);
					 updateObj.setEndingOnHandQuantity(endingOnHandQuantity);
					 updateObj.setEndingOnHandAmount(endingOnHandAmount);
					 
				 	 super.update(updateObj);
				
			 }else{
				 if(quantity < 0D )
					 throw  new ValidationErrorException("於ImMonthlyBalanceLine查無" + year+month + "品號："+itemCode+ " 庫別："+warehouseCode+ " 批號："+lotNo+"明細資料！");
				 else{
					 ImMonthlyBalanceLine imMonthlyBalanceLine =  new ImMonthlyBalanceLine();
					 imMonthlyBalanceLine.setId(id);
					 imMonthlyBalanceLine.setBeginningOnHandQuantity(0D);
					 imMonthlyBalanceLine.setBeginningOnHandAmount(0D);
					 imMonthlyBalanceLine.setPeriodPurchaseQuantity(0D);
					 imMonthlyBalanceLine.setPeriodPurchaseAmount(0D);
					 imMonthlyBalanceLine.setPeriodSalesQuantity(0D);
					 imMonthlyBalanceLine.setPeriodSalesAmount(0D);
					 imMonthlyBalanceLine.setPeriodMovementQuantity(quantity);
					 imMonthlyBalanceLine.setPeriodMovementAmount(0D);
					 imMonthlyBalanceLine.setPeriodAdjustmentQuantity(0D);
					 imMonthlyBalanceLine.setPeriodAdjustmentAmount(0D);
					 imMonthlyBalanceLine.setPeriodOtherQuantity(0D);
					 imMonthlyBalanceLine.setPeriodOtherAmount(0D);
					 imMonthlyBalanceLine.setPeriodPosSalesQuantity(0D);
					 imMonthlyBalanceLine.setPeriodPosSalesOriginalAmt(0D);
					 imMonthlyBalanceLine.setPeriodPosSalesActualAmt(0D);
					 imMonthlyBalanceLine.setEndingOnHandQuantity(quantity);
					 imMonthlyBalanceLine.setEndingOnHandAmount(quantity*averageUnitCost);
					 imMonthlyBalanceLine.setAverageUnitCost(averageUnitCost);
					 super.save(imMonthlyBalanceLine);
					 
				 }
			 }
	
		 }else
			 throw  new ValidationErrorException("更新ImMonthlyBalanceLine 品號："+itemCode+ " 庫別："+warehouseCode+ " 批號："+lotNo+"明細資料時，其數量為null");
	 	}
	 
	 
	 public ImMonthlyBalanceLine findById(String brandCode, String itemCode, String warehouseCode, String lotNo, String year, String month){
		 log.info("ImMonthlyBalanceLine.findById: brandCode="+brandCode+" itemCode="+itemCode+" warehouseCode="+warehouseCode+" lotNo="+lotNo+" year="+year+" month="+month);

		 StringBuffer hql = new StringBuffer(
         "from ImMonthlyBalanceLine as model where 1=1");
		 		hql.append(" and model.id.brandCode = ?");
		 		hql.append(" and model.id.itemCode = ?");
		 		hql.append(" and model.id.warehouseCode = ?");
		 		hql.append(" and model.id.lotNo = ?");
		 		hql.append(" and model.id.year = ?");
		 		hql.append(" and model.id.month = ?");

     	List<ImMonthlyBalanceLine> result = getHibernateTemplate().find(
         hql.toString(), new String[] { brandCode, itemCode ,warehouseCode, lotNo, year, month});
     
	     return  (result.size()>0? result.get(0):null);	    	
	 }

}
