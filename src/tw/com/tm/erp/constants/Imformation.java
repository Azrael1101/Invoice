package tw.com.tm.erp.constants;

public enum Imformation {
    EANCODE("國際碼"),
    REPLENISH_BARCODE("補條碼"),
    AUTO_REPLENISH("自動補貨"),
    AUTO_REPLENISH_BASIC_PARAMETER("自動補貨基本參數");
    
    private String description;
    
    Imformation(String description) {        
	this.description = description;    
    }    
    
    public String getDescription() {        
	return description;    
    }
    
    /**
     * 執行初始化
     * @return
     */
    public String getActionInitial(){
	return new StringBuffer("執行").append(description).append("初始化失敗，原因：").toString();
    }
    
    /**
     * 初始化
     * @return
     */
    public String getServiceInitial(){
	return new StringBuffer(description).append("初始化失敗，原因：").toString();
    }
    
    /**
     * 執行查詢初始化
     * @return
     */
    public String getActionSearchInitial(){
	return new StringBuffer("執行").append(description).append("查詢初始化失敗，原因：").toString();
    }
    
    /**
     * 查詢初始化
     * @return
     */
    public String getServiceSearchInitial(){
	return new StringBuffer(description).append("查詢初始化失敗，原因：").toString();
    }
    
    /**
     * 載入
     * @return
     */
    public String getServiceAJAXPageData(){
	return new StringBuffer("載入頁面顯示").append(description).append("明細發生錯誤，原因：").toString();
    }
    
    /**
     * 查詢載入
     * @return
     */
    public String getServiceAJAXSearchPageData(){
	return new StringBuffer("載入頁面顯示的").append(description).append("查詢發生錯誤，原因：").toString();
    }
    
    /**
     * 更新明細
     * @return
     */
    public String getServiceUpdateAJAXPageLinesData(){
	return new StringBuffer("更新").append(description).append("明細資料發生錯誤，原因：").toString();
    }
    
    
    /**
     * 執行流程
     * @return
     */
    public String getActionExecuteProcess(){
	return new StringBuffer("執行").append(description).append("流程發生錯誤，原因：").toString();
    }
    
    /**
     * 啟流程
     * @return
     */
    public String getServiceStartProcess(){
	return new StringBuffer(description).append("流程啟動失敗，原因：").toString();
    }
    
    /**
     * 執行送出
     * @return
     */
    public String getActionPerformTransaction(){
	return new StringBuffer("執行").append(description).append("存檔失敗，原因：").toString();
    }
    
    /**
     * 背景送出
     * @return
     */
    public String getActionPerformTransactionForBackGround(){
	return new StringBuffer("執行").append(description).append("背景存檔失敗，原因：").toString();
    }
    
    /**
     * 檢核
     * @return
     */
    public String getActionError(){
	return new StringBuffer("執行").append(description).append("檢核失敗，原因：").toString();
    }
    
    /**
     * 完成工作任務
     * @return
     */
    public String getServiceCompleteAssignment(){
	return new StringBuffer("完成").append(description).append("工作任務失敗，原因：").toString();
    }
    
    /**
     * 匯入
     * @return
     */
    public String getServiceImport(){
	return new StringBuffer(description).append("明細匯入時發生錯誤，原因：").toString();
    }
}
