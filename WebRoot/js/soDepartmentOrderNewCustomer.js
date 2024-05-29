 /*** 
 *	檔案: buCustomer.js
 *	說明: 類別代號,抽成率維護
 */
 

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;


function outlineBlock(){ 
 	formInitial();
  	headerInitial();

	//doFormAccessControl();
}
			
//初始化
function formInitial(){
 	
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
       {  
          brandCode  			: document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          category		      	: document.forms[0]["#category"      	 ].value, 
          customerCode	      	: document.forms[0]["#customerCode"    	 ].value,      	  
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	 //    approvalResult		: "approvalResult",
	 //    approvalComment		: "approvalComment",       
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        }; 
          vat.bean.init(  
             function(){
                    return "process_object_name=buCustomerModAction&process_object_method_name=performPosInitial"; 
         },{
             other: true
        } 
        );
  }

}


function headerInitial(){ 
var allVipType = vat.bean("allVipType"); //會員類別
var allShop = vat.bean("allShop"); //會員類別
var allCustomerTypeCode = [["", false, true], ["1-自然人","2-法人 "], ["1", "2"]];//類型
var allGender = [["", false, true], ["男","女"], ["M", "F"]];
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"客戶資料維護作業", 
		rows:[{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 		    {items:[{name:"#F.brandCode", type:"TEXT",   bind:"brandCode",mode:"ReadOnly"}]}
	 		    ]},
	 	{row_style:"", cols:[
				{items:[{name:"#L.chineseName", type:"LABEL", value:"會員姓名<font color='red'>*</font>"}]},
				{items:[{name:"#F.chineseName", type:"TEXT", bind:"chineseName" ,size:40}]}
				]},
		{row_style:"", cols:[
				{items:[{name:"#L.mobilePhone", type:"LABEL", value:"行動電話<font color='blue'>#</font>"}]},
				{items:[{name:"#F.mobilePhone", type:"TEXT", bind:"mobilePhone",size:25,maxLen:"40" }]}
				]},
	  	{row_style:"", cols:[
				{items:[{name:"#L.customerCode", type:"LABEL", value:"會員編號<font color='red'>*</font>"}]},
				{items:[{name:"#F.customerCode", type:"TEXT", bind:"customerCode",size:25,eChange:"onChangeCustomerCode('customerCode')"},
						{name:"#L.enable", type:"LABEL", value:"啟用?"},
						{name:"#F.enable", type:"XBOX", bind:"enable" }]}
				]},
		{row_style:"", cols:[
				{items:[{name:"#L.identityCode", type:"LABEL", value:"身份證明代碼<font color='red'>*</font>"}]},
				{items:[{name:"#F.identityCode", type:"TEXT", bind:"identityCode",size:40,eChange:"onChangeCustomerCode('identityCode')"},
						{name:"#F.addressBookId", type:"TEXT", bind:"addressBookId",mode:"hidden" },
						{name:"#F.headId", type:"TEXT", bind:"headId",mode:"hidden" }]}

				]},
		{row_style:"", cols:[
				{items:[{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'}],td:" colSpan=2"}
				]}	,
		{row_style:"", cols:[
				{items:[{name:"#L.vipTypeCode", type:"LABEL" , value:"會員類別<font color='red'>*</font>"}]},
	 		    {items:[{name:"#F.vipTypeCode", type:"SELECT",   bind:"vipTypeCode",init:allVipType}]}
				]},

		{row_style:"", cols:[
	 		    {items:[{name:"#L.category07", type:"LABEL", value:"申請櫃別<font color='red'>*</font>"}]},
				{items:[{name:"#F.category07", type:"SELECT", bind:"category07",init:allShop},
						 {name:"#F.loginEmployeeCode", type:"TEXT",   bind:"loginEmployeeCode",mode:"HIDDEN"}]}
				]},
		{row_style:"", cols:[
				{items:[{name:"#L.vipStartDate", type:"LABEL", value:"起始日<font color='red'>*</font>"}]},
				{items:[{name:"#F.vipStartDate", type:"DATE", bind:"vipStartDate" }]}
				]},	
		{row_style:"", cols:[
				{items:[{name:"#L.vipEndDate", type:"LABEL", value:"到期日<font color='red'>*</font>"}]},
				{items:[{name:"#F.vipEndDate", type:"DATE", bind:"vipEndDate" }]}
				]},	
				{row_style:"", cols:[
				{items:[{name:"#L.applicationDate", type:"LABEL", value:"申請日"}]},
				{items:[{name:"#F.applicationDate", type:"DATE", bind:"applicationDate" }]}
				]}		 
			], 	
		 
		beginService:"",
		closeService:""			
	});
}


// 送出的返回
function createRefreshForm(){
	//window.close();
	var customerResult = vat.item.getValueByName("#F.customerCode");
	window.returnValue = customerResult;
	window.close();
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.close();
   }
}

// 送出,暫存按鈕
function doSubmit(formAction){
vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
//alert(vat.item.getValueByName("#F.headId"));

	var alertMessage ="送出後將會新增會員並關閉視窗，是否繼續?";
	if(confirm(alertMessage)){		
		vat.block.submit
		(function(){
			return "process_object_name=buCustomerModAction" + "&process_object_method_name=performPosTransaction";
		},
		{bind:true, link:true, other:true});
		//window.close();
	}
}


function onChangeCustomerCode(changeData){

	if((vat.item.getValueByName("#F.customerCode")!=="")){
		var processString = "process_object_name=buCustomerModService&process_object_method_name=getAJAXFormDataByCustomer"
							+"&brandCode="  + vat.item.getValueByName("#F.brandCode")
							+"&customerCode="  + vat.item.getValueByName("#F.customerCode").replace(/^\s+|\s+$/, '').toUpperCase()
							+"&identityCode="  + vat.item.getValueByName("#F.identityCode")
							+"&changeData="  + changeData;
		vat.ajax.startRequest(processString,  function()
		{ 
			if (vat.ajax.handleState())
			{
				if(vat.ajax.getValue("orderType", vat.ajax.xmlHttp.responseText)==="old")
				{
				
					alert("此會員編號已被使用,請確認新建會員編號");
					vat.item.setValueByName("#F.customerCode", vat.ajax.getValue("customerCode", ""));

				}
				else if(vat.ajax.getValue("orderType", vat.ajax.xmlHttp.responseText)!=="old" && (changeData === 'customerCode')){
					vat.item.setValueByName("#F.customerCode", vat.item.getValueByName("#F.customerCode").replace(/^\s+|\s+$/, '').toUpperCase());
					vat.item.setValueByName("#F.identityCode", vat.item.getValueByName("#F.customerCode").replace(/^\s+|\s+$/, '').toUpperCase());
				}
	  		}
		});
	}
}
