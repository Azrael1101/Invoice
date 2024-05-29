package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.hbm.bean.BuGoalCommissionException;
import tw.com.tm.erp.hbm.bean.SoBonus;
import tw.com.tm.erp.hbm.dao.BuGoalCommissionExceptionDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SoBonusDAO;

public class SoBonusService {
	private static final Log log = LogFactory.getLog(SoBonusService.class);
	public static final String PROGRAM_ID = "SOBONUS";
	private NativeQueryDAO nativeQueryDAO;
	private SoBonusDAO soBonusDAO;
	private BuGoalCommissionExceptionDAO buGoalCommissionExceptionDAO;
	private DataSource dataSource;
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO){
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
	public void setSoBonusDAO(SoBonusDAO soBonusDAO) {
		this.soBonusDAO = soBonusDAO;
	}
	
	public void setBuGoalCommissionExceptionDAO(BuGoalCommissionExceptionDAO buGoalCommissionExceptionDAO){
		this.buGoalCommissionExceptionDAO = buGoalCommissionExceptionDAO;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public long getBuGoalEmployeeCount(String year, String month){
	    String sql = "SELECT COUNT(*) FROM ERP.BU_GOAL G, ERP.BU_GOAL_EMPLOYEE E WHERE G.HEAD_ID = E.HEAD_ID AND G.YEAR = '" + year + "' AND G.MONTH = '" + month + "'";
	    List result = nativeQueryDAO.executeNativeSql(sql);
	    return result == null || result.size() < 1 ? 0 : result.get(0) == null ? 0 : ((BigDecimal)result.get(0)).longValue();
	}
	
	public long getBuGoalTargetCount(String year, String month){
	    String sql = "SELECT COUNT(*) FROM ERP.BU_GOAL G, ERP.BU_GOAL_TARGET T WHERE G.HEAD_ID = T.HEAD_ID AND G.YEAR='" + year + "' AND MONTH='" + month + "'";
	    List result = nativeQueryDAO.executeNativeSql(sql);
	    return result == null || result.size() < 1 ? 0 : result.get(0) == null ? 0 : ((BigDecimal)result.get(0)).longValue();
	}
	
	public long getBuGoalWorkCount(String year, String month){
	    String sql = "SELECT COUNT(*) FROM ERP.BU_GOAL G, ERP.BU_GOAL_WORK W WHERE G.HEAD_ID = W.HEAD_ID AND G.YEAR='" + year + "' AND MONTH='" + month + "'";
	    List result = nativeQueryDAO.executeNativeSql(sql);
	    return result == null || result.size() < 1 ? 0 : result.get(0) == null ? 0 : ((BigDecimal)result.get(0)).longValue();
	}
	
	public long getEmployeeCountWithoutGoal(String startDate, String endDate, String year, String month){
	    String sql = "SELECT COUNT(DISTINCT V.SUPERINTENDENT_CODE) FROM ERP.SO_BONUS_VIEW V WHERE NOT EXISTS (SELECT 1 FROM ERP.BU_GOAL G, ERP.BU_GOAL_EMPLOYEE E WHERE G.HEAD_ID = E.HEAD_ID AND G.YEAR = '" + 
	    		year + "' AND G.MONTH = '" + month + "' AND V.SUPERINTENDENT_CODE = E.EMPLOYEE_CODE) AND V.SHIP_DATE BETWEEN TO_DATE('" + startDate + "', 'yyyy-MM-dd') AND TO_DATE('" + endDate + "', 'yyyy-MM-dd')";
	    List result = nativeQueryDAO.executeNativeSql(sql);
	    return result == null || result.size() < 1 ? 0 : result.get(0) == null ? 0 : ((BigDecimal)result.get(0)).longValue();
	}
	
	public void saveTmpSoBonus(String startDate, String endDate){
	    //刪除TMP_SO_BONUS舊資料...
	    soBonusDAO.deleteTmpSoBonus(startDate, endDate);
	    //儲存新資料至TMP_SO_BONUS...
	    String sql = "INSERT INTO ERP.TMP_SO_BONUS " + 
        	    "SELECT ERP.TMP_SO_BONUS_SEQ.nextval, S.SHIP_DATE, S.SUPERINTENDENT_CODE, S.ITEM_CODE, S.ITEM_C_NAME, S.ITEM_E_NAME, S.BRAND_CODE, S.BRAND_NAME, S.SHOP_CODE, " +
        	    "S.CATEGORY01, S.CATEGORY02, S.CATEGORY03, S.CATEGORY04, S.CATEGORY05, S.CATEGORY06, S.CATEGORY07, S.CATEGORY08, S.CATEGORY09, S.CATEGORY10, S.CATEGORY11, " + 
        	    "S.CATEGORY12, S.CATEGORY13, S.CATEGORY14, S.CATEGORY15, S.CATEGORY16, S.CATEGORY17, S.CATEGORY18, S.CATEGORY19, S.CATEGORY20, S.ITEM_BRAND, S.ACTUAL_SHIP_AMOUNT, " + 
        	    "S.DISCOUNT_RATE, S.TOTAL, ROUND(NVL((S.TOTAL * C.COMMISSION_RATE) / 100, 0), 0) AS COMMISSION, C.COMMISSION_TYPE AS TYPE, 'SYS', sysdate, C.COMMISSION_RATE COMMISSION_RATE " +                
        	    //"S.DISCOUNT_RATE, S.TOTAL, ROUND(NVL((S.ACTUAL_SHIP_AMOUNT * C.COMMISSION_RATE) / 100, 0), 0) AS COMMISSION, C.COMMISSION_TYPE AS TYPE, 'SYS', sysdate, C.COMMISSION_RATE COMMISSION_RATE " +
        	    "FROM ERP.SO_BONUS_VIEW S, ERP.BU_GOAL_COMMISSION C " +  
        	    "WHERE S.CATEGORY02 = C.CATEGORY02(+) " +  
        	    "AND S.SHIP_DATE >= TO_DATE ('" + startDate + "', 'yyyy-MM-dd') " +  
        	    "AND S.SHIP_DATE <= TO_DATE('" + endDate + "', 'yyyy-MM-dd') ";
	    try{
		nativeQueryDAO.executeNativeUpdateSql(sql);
	    }
	    catch(Exception e){
		log.error(e.getStackTrace());
	    }
	}
	
	//依據BU_GOAL_COMMISSION_EXCEPTION更新TMP_SO_BONUS的抽成率
	public void updateTmpSoBonusDiscountRate(String year, String month){
		List<BuGoalCommissionException> buGoalCommissionExceptions = buGoalCommissionExceptionDAO.findAll();
		for(BuGoalCommissionException ce : buGoalCommissionExceptions){
			if(ce.getCommissionRate() == null) continue;
			int j = 0;
			StringBuilder hql = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			hql.append("update TmpSoBonus set commissionRate=? where commissionType=? and to_char(shipDate,'YYYY')=? and to_char(shipDate,'MM')=? ");
			params.add(ce.getCommissionRate());
			params.add(ce.getCommissionType());
			params.add(year);
			params.add(month);
			
			if(ce.getShopCode() != null && ce.getShopCode().trim().length() > 0){
				hql.append("and shopCode=? ");
				params.add(ce.getShopCode());
				j++;
			}
			if(ce.getItemBrand() != null && ce.getItemBrand().trim().length() > 0){
				hql.append("and itemBrand=? ");
				params.add(ce.getItemBrand());
				j++;
			}
			if(ce.getCategory01() != null && ce.getCategory01().trim().length() > 0){
				hql.append("and category01=? ");
				params.add(ce.getCategory01());
				j++;
			}
			if(ce.getCategory02() != null && ce.getCategory02().trim().length() > 0){
				hql.append("and category02=? ");
				params.add(ce.getCategory02());
				j++;
			}
			if(ce.getCategory03() != null && ce.getCategory03().trim().length() > 0){
				hql.append("and category03=? ");
				params.add(ce.getCategory03());
				j++;
			}
			if(ce.getCategory04() != null && ce.getCategory04().trim().length() > 0){
				hql.append("and category04=? ");
				params.add(ce.getCategory04());
				j++;
			}
			if(ce.getCategory05() != null && ce.getCategory05().trim().length() > 0){
				hql.append("and category05=? ");
				params.add(ce.getCategory05());
				j++;
			}
			if(ce.getCategory06() != null && ce.getCategory06().trim().length() > 0){
				hql.append("and category06=? ");
				params.add(ce.getCategory06());
				j++;
			}
			if(ce.getCategory07() != null && ce.getCategory07().trim().length() > 0){
				hql.append("and category07=? ");
				params.add(ce.getCategory07());
				j++;
			}
			if(ce.getCategory08() != null && ce.getCategory08().trim().length() > 0){
				hql.append("and category08=? ");
				params.add(ce.getCategory08());
				j++;
			}
			if(ce.getCategory09() != null && ce.getCategory09().trim().length() > 0){
				hql.append("and category09=? ");
				params.add(ce.getCategory09());
				j++;
			}
			if(ce.getCategory10() != null && ce.getCategory10().trim().length() > 0){
				hql.append("and category10=? ");
				params.add(ce.getCategory10());
				j++;
			}
			if(ce.getCategory11() != null && ce.getCategory11().trim().length() > 0){
				hql.append("and category11=? ");
				params.add(ce.getCategory11());
				j++;
			}
			if(ce.getCategory12() != null && ce.getCategory12().trim().length() > 0){
				hql.append("and category12=? ");
				params.add(ce.getCategory12());
				j++;
			}
			if(ce.getCategory13() != null && ce.getCategory13().trim().length() > 0){
				hql.append("and category13=? ");
				params.add(ce.getCategory13());
				j++;
			}
			if(ce.getCategory14() != null && ce.getCategory14().trim().length() > 0){
				hql.append("and category14=? ");
				params.add(ce.getCategory14());
				j++;
			}
			if(ce.getCategory15() != null && ce.getCategory15().trim().length() > 0){
				hql.append("and category15=? ");
				params.add(ce.getCategory15());
				j++;
			}
			if(ce.getCategory16() != null && ce.getCategory16().trim().length() > 0){
				hql.append("and category16=? ");
				params.add(ce.getCategory16());
				j++;
			}
			if(ce.getCategory17() != null && ce.getCategory17().trim().length() > 0){
				hql.append("and category17=? ");
				params.add(ce.getCategory17());
				j++;
			}
			if(ce.getCategory18() != null && ce.getCategory18().trim().length() > 0){
				hql.append("and category18=? ");
				params.add(ce.getCategory18());
				j++;
			}
			if(ce.getCategory19() != null && ce.getCategory19().trim().length() > 0){
				hql.append("and category19=? ");
				params.add(ce.getCategory19());
				j++;
			}
			if(ce.getCategory20() != null && ce.getCategory20().trim().length() > 0){
				hql.append("and category20=? ");
				params.add(ce.getCategory20());
				j++;
			}
			if(j > 0)
				soBonusDAO.update(hql.toString(), params.toArray());
		}
	}
	
	//重新計算分項抽成獎金
	public void calculateTmpSoBonusCommission(String year, String month){
	    try{						       
	    	String sql = "UPDATE ERP.TMP_SO_BONUS SET COMMISSION = ROUND(NVL((TOTAL * COMMISSION_RATE) / 100, 0), 0) WHERE TO_CHAR(SHIP_DATE,'YYYY')='" + year + "' AND TO_CHAR(SHIP_DATE,'MM')='" + month + "'";
	    	nativeQueryDAO.executeNativeUpdateSql(sql);
	    }
	    catch(Exception e){
	    	log.error(e.toString());
	    }
	}
	
	//計算加總抽成獎金並寫入SO_BONUS
	public void saveSoBonus(String date){
	    String year = getYear(date);
	    String month = getMonth(date);
	    soBonusDAO.deleteSoBonus(getYear(date), getMonth(date));
	    String sql = 
		"SELECT EMPLOYEE_CODE, GOAL_CODE, SUM(AMOUNT_A) AMOUNT_A, SUM(COMMISSION_A) COMMISSION_A,SUM(AMOUNT_B) AMOUNT_B, " +  
		"SUM(COMMISSION_B) COMMISSION_B, SUM(AMOUNT_C) AMOUNT_C, SUM(COMMISSION_C) COMMISSION_C, SUM(AMOUNT_D) AMOUNT_D, " + 
		"SUM(COMMISSION_D) COMMISSION_D, SUM(AMOUNT_E) AMOUNT_E, SUM(COMMISSION_E) COMMISSION_E, SUM(AMOUNT_F) AMOUNT_F, " + 
		"SUM(COMMISSION_F) COMMISSION_F, SUM(AMOUNT_G) AMOUNT_G, SUM(COMMISSION_G) COMMISSION_G, SUM(AMOUNT_X) AMOUNT_X, " + 
		"SUM(COMMISSION_X) COMMISSION_X, SUM(TOTAL_AMOUNT), SUM(TOTAL_COMMISSION) " +
		"FROM ( " + 
		"    SELECT SUPERINTENDENT_CODE AS EMPLOYEE_CODE, G.GOAL_CODE AS GOAL_CODE, " +  
		"    NVL (DECODE (COMMISSION_TYPE, 'A', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_A,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'A', COMMISSION), 0) AS COMMISSION_A,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'B', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_B,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'B', COMMISSION), 0) AS COMMISSION_B,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'C', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_C,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'C', COMMISSION), 0) AS COMMISSION_C,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'D', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_D,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'D', COMMISSION), 0) AS COMMISSION_D,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'E', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_E,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'E', COMMISSION), 0) AS COMMISSION_E,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'F', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_F,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'F', COMMISSION), 0) AS COMMISSION_F,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'G', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_G,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'G', COMMISSION), 0) AS COMMISSION_G,   " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'X', ACTUAL_SHIP_AMOUNT), 0) AS AMOUNT_X,  " + 
		"    NVL (DECODE (COMMISSION_TYPE, 'X', COMMISSION), 0) AS COMMISSION_X,   " + 
		"    (                                                                     " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'A', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'B', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'C', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'D', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'E', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'F', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'G', ACTUAL_SHIP_AMOUNT), 0) +        " + 
		"      NVL (DECODE (COMMISSION_TYPE, 'X', ACTUAL_SHIP_AMOUNT), 0)          " + 
		"    ) AS TOTAL_AMOUNT,                                                    " + 
		"    (                                                                     " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'A', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'B', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'C', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'D', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'E', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'F', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'G', COMMISSION), 0) +              " + 
		"        NVL (DECODE (COMMISSION_TYPE, 'X', COMMISSION), 0)                " + 
		"    ) AS TOTAL_COMMISSION                                                 " + 
		"    FROM(                                                                 " + 
		"        SELECT B.SUPERINTENDENT_CODE, B.COMMISSION_TYPE, SUM(B.ACTUAL_SHIP_AMOUNT) AS ACTUAL_SHIP_AMOUNT, SUM(B.TOTAL) AS DISCOUNT_AMOUNT, SUM(B.COMMISSION) AS COMMISSION " +  
		"        FROM TMP_SO_BONUS B                               " +
		"        WHERE TO_CHAR(B.SHIP_DATE,'YYYY') = '" + year       + "'" +
		"        AND TO_CHAR(B.SHIP_DATE,'MM') = '" + month          + "'" + 
		"        GROUP BY B.SUPERINTENDENT_CODE, B.COMMISSION_TYPE " +
		"        ORDER BY B.SUPERINTENDENT_CODE, B.COMMISSION_TYPE " + 
		"    ),                                                    " + 
		"    (                                                     " + 
		"        select E.EMPLOYEE_CODE, L.GOAL_CODE               " + 
		"        from ERP.BU_GOAL L, ERP.BU_GOAL_EMPLOYEE E        " + 
		"        where L.HEAD_ID = E.HEAD_ID                       " + 
		"        and L.YEAR='" + year + "' and L.MONTH='" + month + "'" + 
		"        group by E.EMPLOYEE_CODE, L.GOAL_CODE             " + 
		"    ) G                                                   " + 
		"    where G.EMPLOYEE_CODE = SUPERINTENDENT_CODE           " + 
		") " +
		"SO_BONUS GROUP BY EMPLOYEE_CODE, GOAL_CODE";
		
		List datas = nativeQueryDAO.executeNativeSql(sql);
		if(datas != null && datas.size() > 0){
			for(int i=0; i<datas.size(); i++){
				Object[] obj = (Object[])datas.get(i);
				SoBonus soBonus = new SoBonus();
				soBonus.setEmployeeCode(obj[0] == null ? null :obj[0].toString());
				soBonus.setGoalCode(obj[1] == null ? "" : obj[1].toString());
				soBonus.setYear(year);
				soBonus.setMonth(month);
				soBonus.setAmountA(obj[2] == null ? 0D : ((BigDecimal)obj[2]).doubleValue());
				soBonus.setCommissionA(obj[3] == null ? 0D : ((BigDecimal)obj[3]).doubleValue());
				soBonus.setAmountB(obj[4] == null ? 0D : ((BigDecimal)obj[4]).doubleValue());
				soBonus.setCommissionB(obj[5] == null ? 0D : ((BigDecimal)obj[5]).doubleValue());
				soBonus.setAmountC(obj[6] == null ? 0D : ((BigDecimal)obj[6]).doubleValue());
				soBonus.setCommissionC(obj[7] == null ? 0D : ((BigDecimal)obj[7]).doubleValue());
				soBonus.setAmountD(obj[8] == null ? 0D : ((BigDecimal)obj[8]).doubleValue());
				soBonus.setCommissionD(obj[9] == null ? 0D : ((BigDecimal)obj[9]).doubleValue());
				soBonus.setAmountE(obj[10] == null ? 0D : ((BigDecimal)obj[10]).doubleValue());
				soBonus.setCommissionE(obj[11] == null ? 0D : ((BigDecimal)obj[11]).doubleValue());
				soBonus.setAmountF(obj[12] == null ? 0D : ((BigDecimal)obj[12]).doubleValue());
				soBonus.setCommissionF(obj[13] == null ? 0D : ((BigDecimal)obj[13]).doubleValue());
				soBonus.setAmountG(obj[14] == null ? 0D : ((BigDecimal)obj[14]).doubleValue());
				soBonus.setCommissionG(obj[15] == null ? 0D : ((BigDecimal)obj[15]).doubleValue());
				soBonus.setAmountX(obj[16] == null ? 0D : ((BigDecimal)obj[16]).doubleValue());
				soBonus.setCommissionX(obj[17] == null ? 0D : ((BigDecimal)obj[17]).doubleValue());
				soBonus.setTotalAmount(obj[18] == null ? 0D : ((BigDecimal)obj[18]).doubleValue());
				soBonus.setTotalCommission(obj[19] == null ? 0D : ((BigDecimal)obj[19]).doubleValue());
				soBonus.setGoalAmount(0D);	//個人業績(非所屬群組的業績不算), 預設0
				soBonus.setEmployeeGoal(0D);	//個人業績目標, 預設0
				soBonusDAO.save(soBonus);
			}
		}
	}
	
	public void calculateBonusPercentage(String startDate, String endDate){
	    Object[] obj = getBonusPercentage(startDate, endDate);
	    Map<String, Double> cardinals = (Map<String, Double>)obj[0];//降抽基數
	    Map<String, Double> goalPercentages = (Map<String, Double>)obj[1];//達成率
	    Map<String, Double> amounts = (Map<String, Double>)obj[2];//個人總業績(所屬群組才算)
	    Map<String, Double> goals = (Map<String, Double>)obj[3];//個人業績目標
	    
	    List<SoBonus> soBonuses = soBonusDAO.findAllSoBonus();
	    for(SoBonus s : soBonuses){
			String employeeCode = s.getEmployeeCode();
			String hql = "update SoBonus set totalCommission=? , achevement=?, goalAmount=?, employeeGoal=? where employeeCode=? and goalCode=? and year=? and month=?";
			try{
			    double totalCommission = new BigDecimal(s.getTotalCommission() * cardinals.get(employeeCode)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			    soBonusDAO.update(hql, new Object[]{
				totalCommission, goalPercentages.get(employeeCode), amounts.get(employeeCode), goals.get(employeeCode), employeeCode, s.getGoalCode(), s.getYear(), s.getMonth()
			    });
			}
			catch(Exception e){
			    log.error(e.getStackTrace());
			}
	    }
	}
	
	public void calculateGroupAchevement(String year, String month, String day) throws Exception {
		Connection conn = null;
		CallableStatement calStmt = null;
		try{
			conn = dataSource.getConnection();
			//ERP.RPT_SO0099_PACKAGE.start_so0099(:c, 'T2', '', '2014', '03', '31');
			calStmt = conn.prepareCall("{call ERP.RPT_SO0099_PACKAGE.start_so0099(?, ?, ?, ?, ?, ?)}"); // 呼叫store procedure
			calStmt.registerOutParameter(1, OracleTypes.CURSOR);
			calStmt.setString(2, "T2");
			calStmt.setString(3, null);//G1AG
			calStmt.setString(4, year);
			calStmt.setString(5, month);
			calStmt.setString(6, day);
			calStmt.execute();
			log.info("已經處理完成");
		} 
		catch(Exception ex){
			log.error("呼叫posOnlineExport發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} 
		finally{
			if(calStmt != null){
				try{
					calStmt.close();
				} 
				catch(SQLException e){
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if(conn != null){
				try{
					conn.close();
				} 
				catch(SQLException e){
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
	//所有營業員個人達成基數(傳回 Object[]{降抽基數, 達成率, 個人總業績, 個人業績目標}
	private Object[] getBonusPercentage(String startDate, String endDate){
	    Map<String, Double> cardinals = new HashMap<String, Double>();//降抽基數
	    Map<String, Double> goalPercentages = new HashMap<String, Double>();//達成率
	    Map<String, Double> amounts = new HashMap<String, Double>();//個人總業績(所屬群組才算)
	    Map<String, Double> goals = new HashMap<String, Double>();//個人業績目標
	    try{
			amounts = getEmployeeAmount(startDate, endDate);
			goals = getEmployeeGoal(getYear(startDate), getMonth(startDate));
			for(String employeeCode : amounts.keySet()){ 
			    Double amount = amounts.get(employeeCode);//個人業績
			    if(amount == null) log.error("沒有個人業績 employee Code >>>>>>>>>>>>>>>>>>>>>>>>> " + employeeCode);
				Double goal = goals.get(employeeCode);//個人目標
				if(goal == null) log.error("沒有個人目標 employee Code >>>>>>>>>>>>>>>>>>>>>>>>>> " + employeeCode);
				
				if(amount == null || goal == null || amount == 0 || goal == 0){//業績或目標為null或0達成率直接為0
					goalPercentages.put(employeeCode, 0D);
					cardinals.put(employeeCode, 0D);}
				else{
					double achevement = new BigDecimal((amount/goal)*100).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();//依人資要求無條件捨去(20140418) by wade
				    cardinals.put(employeeCode, achevement >= 80 ? 1D : (achevement >= 70 && achevement <= 79) ? 0.8 : (achevement >= 50 && achevement <= 69) ? 0.5 : 0);
				    goalPercentages.put(employeeCode, Double.valueOf(achevement));
				}
			}
	    }
	    catch(Exception e){
	    	log.error(e.toString());
	    }
	    return new Object[]{cardinals, goalPercentages, amounts, goals};
	}
		
	//所有營業員個人總業績
	private Map<String, Double> getEmployeeAmount(String startDate, String endDate){
	    	String year = getYear(startDate), month = getMonth(startDate);
		Map<String, Double> amounts = new HashMap<String, Double>();
		String sql = 
		    "SELECT A.EMPLOTEE_CODE, A.PERSONAL_GOAL_AMOUNT FROM (" + 
		    "SELECT V.SUPERINTENDENT_CODE EMPLOTEE_CODE, GT.GOAL_CODE, SUM(V.ACTUAL_SHIP_AMOUNT) PERSONAL_GOAL_AMOUNT " +                                                                                                  
		    "       FROM " +                                                                                                                                      
		    "           (" +                                                                                                                                                               
		    "            SELECT G.BRAND_CODE, G.GOAL_CODE, G.YEAR, G.MONTH, G.GOAL, T.SHOP_CODE, T.TARGET_VALUE " +                                                
		    "            FROM ERP.BU_GOAL G, ERP.BU_GOAL_TARGET T " +                                                                                               
		    "            WHERE G.HEAD_ID = T.HEAD_ID " + 
		    "            AND G.YEAR='" + year + "' AND MONTH='" + month + "' " +                           
		    "           ) GT, " +                                                                                                                                      
		    "           (" +                                                                                                                                         
		    "             SELECT SHIP_DATE, SUPERINTENDENT_CODE, SHOP_CODE, ITEM_CODE, ACTUAL_SHIP_AMOUNT, DISCOUNT_RATE, TOTAL, CATEGORY01 " +                      
		    "             FROM ERP.SO_BONUS_VIEW WHERE SHIP_DATE BETWEEN TO_DATE('" + startDate + "','yyyy-MM-dd') AND TO_DATE('" + endDate + "','yyyy-MM-dd') " + 
		    "           ) V " +                                                                                                                                       
		    "       WHERE GT.SHOP_CODE = V.SHOP_CODE " +                                                                                                          
		    "       AND GT.TARGET_VALUE = V.CATEGORY01 " +                                                                                                                     
		    "GROUP BY V.SUPERINTENDENT_CODE, GT.GOAL_CODE, GT.GOAL " + 
		    "ORDER BY V.SUPERINTENDENT_CODE " + 
		    ") A " + 
		    "WHERE EXISTS ( " + 
		    "    SELECT 1 " +  
		    "    FROM ERP.BU_GOAL G, ERP.BU_GOAL_EMPLOYEE E " + 
		    "    WHERE G.HEAD_ID = E.HEAD_ID " + 
		    "    AND G.YEAR = '" + year + "' AND G.MONTH = '" + month + "' " + 
		    "    AND E.EMPLOYEE_CODE = A.EMPLOTEE_CODE  " + 
		    "    AND G.GOAL_CODE = A.GOAL_CODE " + 
		    ")";
		List datas = nativeQueryDAO.executeNativeSql(sql);//回傳多個Object[]{"EMPLOYEE_CODE", "AMOUNT"}
		for(int i=0; i<datas.size(); i++){
			Object[] obj = (Object[])datas.get(i);
			System.out.println("code:" + obj[0] + "containsKey:" + amounts.containsKey(obj[0]));
			if(obj[0] != null && obj[1] != null && !amounts.containsKey(obj[0]))
			    amounts.put(obj[0].toString(), ((BigDecimal)obj[1]).doubleValue());
		}
		return amounts;
	}
	
	//所有營業員個人業績目標
	private Map<String, Double> getEmployeeGoal(String year, String month){
		Map<String, Double> goals = new HashMap<String, Double>();
		String sql = 
		    "SELECT E.EMPLOYEE_CODE, E.EMPLOYEE_GOAL " +
		    "FROM ERP.BU_GOAL G, ERP.BU_GOAL_EMPLOYEE E " + 
		    "WHERE G.HEAD_ID = E.HEAD_ID " +
		    "AND G.YEAR = '" + year + "' " +
		    "AND MONTH = '" + month + "' ";
		List datas = nativeQueryDAO.executeNativeSql(sql);//回傳多個Object[]{"EMPLOYEE_CODE", "EMPLOYEE_GOAL"}
		for(int i=0; i<datas.size(); i++){
			Object[] obj = (Object[])datas.get(i);
			if(obj[0] != null && obj[1] != null && !goals.containsKey(obj[0])) 
				goals.put(obj[0].toString(), ((BigDecimal)obj[1]).doubleValue());
		}
		return goals;
	}
	
	//取得年份
	public String getYear(String date){
	    if(date == null || date.indexOf("/") == -1){//今年年份
		Calendar cal = Calendar.getInstance();  
		return String.valueOf(cal.get(Calendar.YEAR));
	    }
	    else{//取得年份
		String[] ymd = date.split("/");
		return ymd[0];
	    }
	}
	
	//取得月份
	public String getMonth(String date){
            if(date == null || date.indexOf("/") == -1)//當月月份
		return getThisMonth();
            else{
        	String[] ymd = date.split("/");
        	if(ymd == null || ymd.length < 2)
        	    return getThisMonth();
        	else{
        	    int m = Integer.parseInt(ymd[1]);
        	    return m < 10 ? "0" + String.valueOf(m) : String.valueOf(m);
        	}
            } 
        }
	
	//取得日期
	public int getDay(String date){
	    if(date == null || date.indexOf("/") == -1)//今天
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	    else{
		String[] ymd = date.split("/");
		if(ymd == null || ymd.length < 3)
		    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		else
		    return Integer.parseInt(ymd[2]);
	    }
	}
	
	private String getThisMonth(){
	    Calendar cal = Calendar.getInstance();
	    int m = cal.get(Calendar.MONTH) + 1;
	    return m < 10 ? "0" + String.valueOf(m) : String.valueOf(m);
	}
	
	public boolean isBusying(String yearMonth){
	    return soBonusDAO.getSoBonusLogCount(yearMonth) > 0;
	}
	
	public long saveSoBonusLog(String yearMonth, String user){
	    return soBonusDAO.saveSoBonusLog(yearMonth, user);
	}
	
	public void updateSoBonusLog(long id, String status){
	    soBonusDAO.updateSoBonusLog(id, status);
	}
}
