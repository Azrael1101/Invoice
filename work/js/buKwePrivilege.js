/*** 
 *	檔案: buKwePrivilege.js
 *	說明：  Kwe權限維護作業
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	kweDetail();
	//doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
			loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
			currentRecordNumber 	: 0,
			lastRecordNumber    	: 0
        };
        /*
	   	vat.bean.init(	
	  		function(){
				return "process_object_name=siGroupAction&process_object_method_name=performInitial"; 
	    	},
	    	{other: true}
	    );
	    */
  }	
}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id : "vatBlock_Button", 
			generate : true,
			table : "cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title : "", 
			rows : [  
				 {
				 	row_style : "", 
				 	cols : [
					 	{
					 		items : [
					 	 		{name : "#B.exit", type : "IMG", value : "離開", src : "./images/button_exit.gif", eClick : "closeWindows('CONFIRM')"},
					 	 		{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.submit", type : "IMG", value : "送出", src : "./images/button_submit.gif", eClick : "doSubmit('SUBMIT')"}
					 	   ],
					 	   td : "style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
					 	}
					]
				 }
	  		], 	 
			beginService : "",
			closeService : ""			
		}
	);
}

function headerInitial(){
	vat.block.create(
		vnB_Header,
		{
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2' style='width:25%'",
			title : "KWE權限維護作業",
			rows : [
				{
					row_style : "", 
					cols : [
						{items:[{name:"#L.applicant", type:"LABEL", value:"申請人<font color='red'>*</font>"}]},
						{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY"}]}
					]
				},
				{
					row_style: "", 
					cols : [
						{items:[{name:"#L.note", type:"LABEL" , value:"<font color='red'>*</font>為必填欄位，請務必填寫。"}],td:" colSpan=4"}
					]
				}		
			], 	
			beginService:"",
			closeService:""		
		});
}

function doAfterPickerEmployee(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.applicant", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.applicantName", vat.bean().vatBeanPicker.result[0].chineseName);
    	
    	//將選擇帳號的權限帶出
    	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	}
}

function kweDetail(){//KWE權限申請單
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    vat.item.make(vnB_Detail, "menuId"                   , {type:"hidden"  ,                     desc:"menuId"       });
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
	vat.item.make(vnB_Detail, "name"            , {type:"TEXT" , size: '98%',  desc:"名稱", mode:"READONLY" });
	vat.item.make(vnB_Detail, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vnB_Detail",
														pageSize            : 50,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
													    //blockId             : "3"
														});	
														
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);											
}

function loadBeforeAjxService(){
	var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
	                    "&applicant=" + vat.item.getValueByName("#F.applicant");
	return processString;		
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		alert("您輸入的條件查無資料！");
	}
}

function saveBeforeAjxService(){
	var processString = '';
	processString = "process_object_name=siGroupMenuService&process_object_method_name=saveAJAXPageLinesData" + 
	  			"&brandCode=" + vat.bean().vatBeanOther.brandCode +
		        "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
		        "&applicant=" + vat.item.getValueByName("#F.applicant");
	return processString;
}

function saveSuccessAfter() {
	//do nothing
}

// 離開按鈕按下
function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType) isExit = confirm("是否確認離開?");
	if(isExit) window.top.close();
}

// 送出按鈕
function doSubmit(formAction){
/*
	var applicant = vat.item.getValueByName("#F.applicant");
	if(applicant == null || applicant == ''){
		alert('請輸入申請人!');
		return;
	}
*/
	if(confirm("是否確定送出?")){
		vat.block.submit(
			function(){
				return "process_object_name=siGroupAction" + "&process_object_method_name=performKwePrivilegeTransaction";
			},
			{bind : true, link : true, other : true}
		);
	}
}

