/***
 *	檔案: wfApprovalResult.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vBrandCode           = "";
var vOrderTypeCode       = "";
var vOrderNo             = "";

function kweWfBlock(brandCode, orderTypeCode, orderNo, approver){
  vBrandCode     = brandCode;
  vOrderTypeCode = orderTypeCode;
  vOrderNo       = orderNo;
  vApprover      = approver;
  vat.tabm.createDivision("vatApprovalDiv");
  kweWfHeader(approver);
  kweWfHistory(brandCode, orderTypeCode, orderNo);
  getEmployeeName(approver);

  vat.tabm.endDivision();
}

// 更新全域變數
function refreshWfParameter( brandCode, orderTypeCode, orderNo ){
	vBrandCode     = brandCode;
  	vOrderTypeCode = orderTypeCode;
  	vOrderNo       = orderNo;
    getAssignee(brandCode, orderTypeCode, orderNo);
    vat.block.pageDataLoad(102, vnCurrentPage = 1);
}

function kweWfHeader(){
var resultSelect = [["", true, true], ["核准 ", "駁回"], [true, false]];
var vnB_WfHeader = 101;
var now          = new Date();

vat.block.create(vnB_WfHeader, {
	id: "vatBlock_WfHeader", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2' background:#E0E0E0;",
	rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.approver"         , type:"LABEL"    ,  value:"簽核人員"}]},
	 {items:[{name:"#F.approver"         , type:"TEXT"     ,  size:5, mode:"READONLY"},
	         {name:"#L.SLANT"            , type:"LABEL"    , value:"/"},
	         {name:"#F.assignee"         , type:"TEXT"     ,  size:8, mode:"READONLY"}]},
	 {items:[{name:"#L.approvalComment"  , type:"LABEL"    ,  value:"簽核意見"}], td:" rowSpan=4"},
	 {items:[{name:"#F.approvalComment"  , type:"TEXTAREA" ,  bind:"approvalComment" , row:4, col: 80}], td:" rowSpan=4"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.approvalDate"     , type:"LABEL"    ,  value:"簽核日期"}]},
	 {items:[{name:"#F.approvalDate"     , type:"LABEL"    ,  value:formatDate(now, "yyyy/MM/dd") , size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.approvalResult"   , type:"LABEL"    ,  value:"簽核決定"}]},
	 {items:[{name:"#F.approvalResult"   , type:"RADIO"    ,  bind:"approvalResult", value: true, size:12 ,mode:"", init:resultSelect}]}]}
	  ],
		beginService:"",
		closeService:""
	});
}

function kweWfHistory(){
  var vbCanGridDelete = false;
  var vbCanGridAppend = true;
  var vbCanGridModify = false;
  var vnB_WfHistory = 102;

    vat.item.make(vnB_WfHistory, "indexNo"          , {type:"IDX"  , size: 6, maxLen: 6, desc:"序號"          });
	vat.item.make(vnB_WfHistory, "activityName"     , {type:"TEXT" , size:12, maxLen:12, desc:"流程點名稱"	, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "approver"     	, {type:"TEXT" , size: 8, maxLen: 8, desc:"簽核人員"  	, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "approverName"     , {type:"TEXT" , size: 8, maxLen:10, desc:"簽核姓名"  	, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "approverPosition" , {type:"TEXT" , size: 8, maxLen:10, desc:"職稱"   		, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "result"           , {type:"TEXT" , size:12, maxLen:12, desc:"簽核結果"   	, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "approvalComment"  , {type:"TEXT" , size: 8, maxLen: 8, desc:"簽核意見"   	, alter:true, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "approvalDate"     , {type:"TEXT" , size:20, maxLen:20, desc:"簽核日期"   	, mode:"READONLY"});
	vat.item.make(vnB_WfHistory, "id"               , {type:"ROWID"});

	vat.block.pageLayout(vnB_WfHistory, {
														id: "vatApprovalHistoryDiv",
														pageSize: 10,
														selectionType       : "NONE",
														indexType           : "AUTO",
								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														loadBeforeAjxService: "loadWfBeforeAjxService()",
							  		  		          	loadSuccessAfter    : "",
														saveBeforeAjxService: "saveWFBeforeAjxService()",
														saveSuccessAfter    : ""
														});
	vat.block.pageDataLoad(vnB_WfHistory, vnCurrentPage = 1);
}

function loadWfBeforeAjxService(){
	var processString = "process_object_name=wfApprovalResultService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vBrandCode+
	                    "&orderTypeCode=" + vOrderTypeCode +
	                    "&orderNo=" + vOrderNo ;
	return processString;
}

function saveWFBeforeAjxService() {
	processString = "process_object_name=wfApprovalResultService"+
					"&process_object_method_name=updateAJAXPageData";
	return processString;
}

function getEmployeeName(approver){
	var vsProcessString = "process_sql_code=FindEmployeeChineseName&employeeCode=" + approver;

		vat.ajax.XHRequest(
		{ 	post: vsProcessString,
			find: function (oXHR){
				vat.item.setValueByName("#F.approver" , vat.ajax.getValue("CHINESE_NAME", oXHR.responseText));
			},
			fail: function (oXHR){
			    vat.item.setValueByName("#F.approver" , "查無簽核人員資料("+approver+")");
			}
		});
}

function getAssignee(brandCode, orderTypeCode, orderNo){
	var vsProcessString = "process_sql_code=findAssignee"+
	                      "&brandCode=" + brandCode+
	                      "&orderTypeCode=" + orderTypeCode +
	                      "&orderNo=" + orderNo ;

  		//alert(vsProcessString);
		vat.ajax.XHRequest(
		{ 	post: vsProcessString,
			find: function (oXHR){
				vat.item.setValueByName("#F.assignee" , vat.ajax.getValue("ASSIGNEE_NAME", oXHR.responseText));
			},
			fail: function (oXHR){
			    vat.item.setValueByName("#F.assignee" ,"");
			}
		});
}