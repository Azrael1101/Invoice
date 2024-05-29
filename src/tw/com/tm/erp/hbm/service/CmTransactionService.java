package tw.com.tm.erp.hbm.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.CmTransactionView;
import tw.com.tm.erp.hbm.bean.FPGoods;
import tw.com.tm.erp.hbm.dao.CmD8TransactionDAO;
import tw.com.tm.erp.hbm.dao.FPGoodsDAO;

public class CmTransactionService {
    private static final Log log = LogFactory.getLog(CmTransactionService.class);
    
    private CmD8TransactionDAO cmD8TransactionDAO;
    private FPGoodsDAO fPGoodsDAO;
    
    
    public void setFPGoodsDAO(tw.com.tm.erp.hbm.dao.FPGoodsDAO goodsDAO) {
        fPGoodsDAO = goodsDAO;
    }

    public void setCmD8TransactionDAO(CmD8TransactionDAO cmD8TransactionDAO) {
        this.cmD8TransactionDAO = cmD8TransactionDAO;
    }
    
    /**
     * delete轉至SQL SERVER FPGoods
     * @param parameterMap
     * @throws Exception
     */
    public int deleteFPGoods(Map parameterMap, FPGoods fPGoods)throws Exception{
	// 刪除 sqlserver 商品主檔
	FPGoodsDAO fPGoodsDAO = (FPGoodsDAO) SpringUtils.getApplicationContext().getBean("fPGoodsDAO");
	return fPGoodsDAO.deleteFPGoods(parameterMap, fPGoods);
    }
    
    /**
     * delete轉至SQL SERVER CmD8Transaction
     * @param parameterMap
     * @throws Exception
     */
    public void deleteCmTransaction(Map parameterMap, String day)throws Exception{
	// 刪除 sqlserver cm交易檔
	cmD8TransactionDAO.deleteCmD8Transaction(parameterMap, day);
    }
    
    /**
     * 轉至SQL SERVER CmD8Transaction
     * @param parameterMap
     * @throws Exception 
     */
    public void executeFPGoods(Map parameterMap, FPGoods goods)throws Exception{

	// 撈 oracle 取得cm交易檔  to sqlserver
	FPGoodsDAO fPGoodsDAO = (FPGoodsDAO) SpringUtils.getApplicationContext().getBean("fPGoodsDAO");
	fPGoodsDAO.executeFPGoods(parameterMap, goods);
    }
    
    /**
     * 轉至SQL SERVER CmD8Transaction
     * @param parameterMap
     * @throws Exception 
     */
    public void executeCmTransaction(Map parameterMap, CmTransactionView cmTransactionView)throws Exception{

	// 撈 oracle 取得cm交易檔  to sqlserver
	cmD8TransactionDAO.executeCmD8Transaction(parameterMap, cmTransactionView);
    }
}
