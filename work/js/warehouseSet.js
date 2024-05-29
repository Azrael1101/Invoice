vat.debug.disable();

var vnB_Warehouse = 21;

function warehouseSetBlock(){
  	
		//vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		//vat.tabm.createDivision("vatDetailDiv3");
		kweWarehouseDetail();
		//vat.tabm.endDivision();
		
		
}
 
      
// 自訂事件
function assignOriginalQtyToArrival() {
  // alert("eeeeeee");
    if (confirm("是否確定執行『自訂事件』？")) {
        if (vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "WAIT_OUT") {
            vat.block.submit(function () {
                return "process_object_name=imMovementMainService" + "&process_object_method_name=updateOriginalQtyToArrival";
            }, {
                other: true,
                picker: false,
                funcSuccess: function () {
                    vat.block.pageRefresh(vnB_Detail);
                }
            });
        } else {
            alert("狀態必須在待轉出，方可使用本功能");
        }
    }
}






function kweWarehouseDetail(){
  
	//var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    
    vat.item.make(vnB_Warehouse, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_Warehouse, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
    vat.item.make(vnB_Warehouse, "warehouseCode"            , {type:"TEXT" , size: '30%',  desc:"庫別代碼" ,mode:"READONLY"});
	vat.item.make(vnB_Warehouse, "warehouseName"            , {type:"TEXT" , size: '30%',  desc:"庫別名稱" ,mode:"READONLY"});
	//vat.item.make(vnB_Warehouse, "warehouseId"            , {type:"TEXT" ,  desc:"庫別Id" });
	vat.item.make(vnB_Warehouse, "brandCode"            , {type:"TEXT" ,  desc:"品牌" ,mode:"READONLY"});
	vat.item.make(vnB_Warehouse, "lineId"                   , {type:"HIDDEN"  ,                     desc:"lineId"       });
	
	

	vat.block.pageLayout(vnB_Warehouse, {
														id                  : "vatWarehouseDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        loadBeforeAjxService: "loadBeforeAjxService("+vnB_Warehouse+")",
								                        eventService        : "selectAllWarehouses()",
														//loadSuccessAfter    : "loadSuccessAfterAdDetail()",	
														saveBeforeAjxService: "saveBeforeAjxWarehouseService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "21"
														});	
														

	vat.block.pageDataLoad(vnB_Warehouse, vnCurrentPage = 1);															
}

function checkIfCost(elem){
  alert(elem.id);
}


// 自訂事件
function selectAllWarehouses() {
    
    if (confirm("是否確定執行『全選』？")) {
        if (vat.item.getValueByName("#F.status") == "SAVE") {
            vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
            vat.block.submit(function () {
               return "process_object_name=buPurchaseServiceSub" + "&process_object_method_name=updateAllHouses";
            }, {
                other: true,
                picker: false,
                funcSuccess: function () {
                    vat.block.pageRefresh(vnB_Warehouse);
                }
            });
        } else {
            alert("狀態必須在暫存，方可使用本功能");
        }
    }
}


// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

// 建立新資料按鈕	
function loadSuccessAfterAdDetail(){
    var menuName;
    for(var i=1;i<=10;i++){
    //alert(document.getElementById("vatF#B8A#Y"+i+"#X6").value.length);
     if(document.getElementById("vatF#B8A#Y"+i+"#X5").value.length==0){
       /*
       var menuId = document.getElementById("menuId#"+i);
       var lineId = document.getElementById("lineId#"+i);
       var indexNo = document.getElementById("indexNo#"+i);
       var enable = document.getElementById("enable#"+i);
       
       var url = document.getElementById("url#"+i);
       var brandCode = document.getElementById("brandCode#"+i);
       var cost = document.getElementById("cost#"+i);
       var categoryCode = document.getElementById("categoryCode#"+i);
       var wareHouseCode = document.getElementById("wareHouseCode#"+i);
       menuId.style.backgroundColor = "lightblue";
       lineId.style.backgroundColor = "lightblue";
       indexNo.style.backgroundColor = "lightblue";
       enable.style.backgroundColor = "lightblue";
       url.style.backgroundColor = "lightblue";
       brandCode.style.backgroundColor = "lightblue";
       cost.style.backgroundColor = "lightblue";
       categoryCode.style.backgroundColor = "lightblue";
       wareHouseCode.style.backgroundColor = "lightblue";
       */
     //alert(document.getElementById("vatF#B8A#Y"+i+"#X6").value);
      // if(document.getElementById("vatF#B8A#Y"+i+"#X6").value==""||document.getElementById("vatF#B8A#Y"+i+"#X6").value==null){
         document.getElementById("vatF#B8A#Y"+i+"#X4").style.color="#FF0000";
         //vat.item.setAttributeByName("vatF#B8A#Y"+i+"#X2", "readOnly", true);
         
       //}
     }else{
        document.getElementById("vatF#B8A#Y"+i+"#X4").style.color="#000000";
        //vat.item.setAttributeByName("vatF#B8A#Y"+i+"#X2", "readOnly", false); 
        
     }
    }
}

// 送出的返回
function createRefreshForm(){
        vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
	refreshForm("");
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}


function saveBeforeAjxService(div) {
	var processString = "";
	//alert("headId"+ vat.item.getValueByName("#F.headId")+"categoryCode="+ vat.item.getValueByName("categoryCode")+"loginEmployeeCode"+vat.bean().vatBeanOther.loginEmployeeCode+"&status=" + vat.item.getValueByName("#F.status") );
    alert("saveBeforeAjxService:"+div);
    
    if(div==21){
    processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesDataAdDetail" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    }else{
	processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}
	//alert(processString);
	return processString;

}

function saveBeforeAjxWarehouseService() {
	               //alert("eeeeeeee");
    processString = "process_object_name=buPurchaseServiceSub&process_object_method_name=updateAJAXPageLinesDataWarehouse" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    
	return processString;

}
//
function kweloadBeforeAjxService(){
	var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
	                    "&applicant=" + vat.item.getValueByName("#F.applicant");
	return processString;		
}
/*
function loadBeforeAjxService(div){
	alert("After loadBeforeAjxService:"+div);
	
	var processString = "";
	
	if (div == vnB_vat7){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXAttachment" + 
			            "&parentHeadId=" + vat.item.getValueByName("#F.headId")  + 
		                "&parentOrderType=" + vat.item.getValueByName("#F.orderTypeCode") +
		                "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value + 
		                "&ownerType=creator";
	}else if(div == 8){
	    alert("fffffffff");
	    processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageDataAddetail" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
	}else{
	   
		processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
    }
	                
	return processString;
}
*/



// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}


/* 報表列印 */
function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=buPurchaseService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
   if("AFTER_SUBMIT"==type) refreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
}


function reconfirmImmovement(orderTypeCode,orderNo){
		if(confirm('是否要列印請採驗')){	
			url = "http://10.1.98.161:8080/crystal/t2/pu1209.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
	
	function reconfirmImmovement1(orderTypeCode,orderNo){
		if(confirm('是否要列報修單')){	
			url1 = "http://10.1.98.161:8080/crystal/t2/IRD.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url1,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url1;
	}
	
	function reconfirmImmovement2(orderTypeCode,orderNo){
		if(confirm('是否要列印需求單')){	
			url2 = "http://10.1.98.161:8080/crystal/t2/IRQ.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url2,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url2;
	}



function getEmployeeRole(vsEmployee) {

    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeRole&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
            
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F.role", vat.ajax.getValue("EMPLOYEE_ROLE", vat.ajax.xmlHttp.responseText));
                    vat.item.setValueByName("#F.COST", vat.ajax.getValue("COST_CONTROL", vat.ajax.xmlHttp.responseText));
                    vat.item.setValueByName("#F.WAREHOUSE", vat.ajax.getValue("WAREHOUSE_CONTROL", vat.ajax.xmlHttp.responseText));
                    
                } else {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    alert("查無此員工代號");
                }
            }
        });
    }
}

function getOriginalWareHouseData(roleCode){

//alert("frfr:"+roleCode);
var getEmpCode = "";
var getBoseeCode = "";
  
  if(vat.item.getValueByName("#F.applicant")!=""){
      getEmpCode = vat.item.getValueByName("#F.applicant");
    if(roleCode!=""){
       if(vat.item.getValueByName("#F.depManager")==""){
          alert("請先輸入部門主管工號");
       }else{
          getBoseeCode = vat.item.getValueByName("#F.depManager");
       }
    }
  }else{
    alert("請先輸入申請人工號");
  }
	//alert(getBoseeCode);
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=buPurchaseServiceSub"+
                    "&process_object_method_name=executeCopyOriWarehouse"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + getEmpCode +
                    "&bossCode=" + getBoseeCode +
                    "&headId=" + vat.item.getValueByName("#F.headId"),
            
        find: function getOriginalDeliverySuccess(oXHR){
        	
            doPageRefreshWareHouse();
       }  
   }); 
}



function doPageRefreshWareHouse(){

    vat.block.pageRefresh(vnB_Warehouse);
}

function saveSuccessAfter() {
  
}

function kwePageLoadSuccess(){
}