<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.oreilly.servlet.MultipartRequest" %>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.GnFile"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.utils.AjaxUtils"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%
GnFileDAO gnFileDAO = (GnFileDAO) SpringUtils.getApplicationContext().getBean("gnFileDAO");
BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
GnFile bean = new GnFile();
List<BuCommonPhraseLine> filePath = baseDAO.findByProperty(
					"BuCommonPhraseLine",
					new String[] { "id.buCommonPhraseHead.headCode","id.lineCode" },
					new Object[] { "SystemConfig","AttchedFilePath" }, "indexNo");
					
					
					
String path = filePath.get(0).getAttribute1();
System.out.println("path="+path);
Long headId=Long.parseLong(request.getParameter("headId"));
String orderType=request.getParameter("orderType");
String ownerType = request.getParameter("ownerType");
String applyType = "";

String strHeadId="";
String saveName="";

if(strHeadId.length()<8){
    int len = strHeadId.length();
    for(int i=1;i<8-len;i++){
    	strHeadId+="0";
    }
 }
 strHeadId+=headId;
					
// 宣告將上傳之檔案放置到伺服器的d:\目錄中
    // 宣告限制上傳之檔案大小為 50 MB
    String saveDirectory =path+orderType+"\\"+strHeadId+"\\";
    int    maxPostSize = 50 * 1024 * 1024 ;
    // 宣告上傳檔案名稱
    String strFileName = null;
    // 宣告上傳檔案型態
    String strContentType = null;
    // 宣告上傳類型
    String type = null;
    
    // 宣告敘述上傳檔案內容敘述
    String strDescription = null;
    // 為Big5或MS950則支援中文檔名
    String enCoding = "utf-8";
   	//員工工號
   	String empCode = null;
   						
	File dir = new File(saveDirectory);
	if(!dir.exists()){
		dir.mkdirs();
	}

%>

<%
	
    // 產一個新的MultipartRequest 的物件，multi
    MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize, enCoding);
    //  取得所有上傳之檔案輸入型態名稱及敘述
    strFileName = multi.getFilesystemName("filePath");
    strContentType = multi.getContentType("filePath");
    strDescription = multi.getParameter("description");
    empCode		   = multi.getParameter("loginEmployeeCode");
    applyType	   = multi.getParameter("applyType");
    
    //orderType	   = multi.getParameter("orderType");
   // headId	   	   = Long.parseLong(multi.getParameter("headId"));
    
    String param ="jsp/AdRequestFileUpload.jsp?loginEmployeeCode="+empCode+"&orderType="+orderType+"&headId="+headId;
    if(null==strFileName||strFileName.length()==0){
    	param+="&errorMsg=must choose a file";
    	response.sendRedirect(param);
    	System.out.println(param);    	
    }else if(null==strDescription||strDescription.length()==0){
    	response.sendRedirect(param+"&errorMsg=enter the description");
    }else{
    	
    	UUID uuid = UUID.randomUUID();
    	saveName=uuid+"_"+strFileName; 
    	List<GnFile> gnFiles = baseDAO.findByProperty(
					"GnFile",
					new String[] { "parentHeadId" , "ownerType"},
					new Object[] { headId,ownerType }, "indexNo");
					
		Long indexNo = gnFiles.size()+1L;
  		File attachment = new File(saveDirectory+strFileName);
    	
    	if(attachment.exists()){
    		int startIndex = attachment.getName().lastIndexOf(46);
   			int endIndex = attachment.getName().length();
   			bean.setContentType(attachment.getName().substring(startIndex,endIndex));
    		bean.setContentSize(attachment.length());
    		attachment.renameTo(new File(saveDirectory+saveName));
    	}
    	
    	
    	bean.setParentHeadId(headId);
    	bean.setFileName(strFileName);
    	bean.setDescription(strDescription);
    	bean.setType(type);
    	bean.setLastUpdateDate(new Date());
    	bean.setLastUpdatedBy(empCode);
    	bean.setCreatedBy(empCode);
    	bean.setCreationDate(new Date());
    	bean.setParentOrderType(orderType);
    	bean.setPhysicalName(saveName);
    	bean.setPhysicalPath(saveDirectory);
    	bean.setIndexNo(indexNo);
    	bean.setOwnerType(ownerType);
    	bean.setType(applyType);
    	bean.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE); //"0"
    	gnFileDAO.saveOrUpdate(bean);
    }
    
    out.print("檔案名稱為："+ strFileName+"  \n檔案型態為："+  strContentType +
              "\n檔案的敘述："+strDescription +"\n工號為:"+empCode+"\nheadId:"+headId+"\n單別為:"+orderType+"<br>");
    
 %>


 <script type="text/javascript">
window.opener.afterUpload();
window.close();
</script>

