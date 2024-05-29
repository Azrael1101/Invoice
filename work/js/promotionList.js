
/*** 
 *	檔案: promotionList.js
 *	說明：表單明細
 *	
 */
vat.debug.disable();
var afterSavePageProcess = "";
var headId;

function kweImBlock(){
  initParm();
  kweImModifyInitial();
 
  kweImHeader();
}

function initParm(){
	var queryString = window.location.search;
	//alert(queryString);
	if(queryString.indexOf('?')!=-1){
		if(queryString.indexOf('headId')!=-1){
			headId = queryString.substring(queryString.indexOf('headId')+7);
			//alert(headId);
		}
		
	}
};

function kweImModifyInitial(){ 
  
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : "T2",   	
  	     loginEmployeeCode    : "T96085",	  
  	     headId               : headId
	    };
     vat.bean.init(function(){
     	return "process_object_name=SoDepartmentOrderAction&process_object_method_name=performPromoteInitial"; 
   	    },{other: true});
  
 
}

function kweImHeader(){
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"滿額贈促銷活動清單", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull"      , type:"IMG"    ,value:"活動代碼",   src:"./images/departmentPos/promoteItem.png", eClick:'savePromotionCode("CT001")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull1"      , type:"IMG"    ,value:"活動代碼1",   src:"./images/departmentPos/promoteItem1.png", eClick:'savePromotionCode("SM0006")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull2"      , type:"IMG"    ,value:"活動代碼2",   src:"./images/departmentPos/promoteItem2.png", eClick:'savePromotionCode("SM0007")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull3"      , type:"IMG"    ,value:"活動代碼3",   src:"./images/departmentPos/promoteItem3.png", eClick:'savePromotionCode("NQONLY")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull4"      , type:"IMG"    ,value:"活動代碼4",   src:"./images/departmentPos/promoteItem4.png", eClick:'savePromotionCode("SM0009")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull5"      , type:"IMG"    ,value:"活動代碼5",   src:"./images/departmentPos/promoteItem5.png", eClick:'savePromotionCode("PD100")'}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#B.promotionFull6"      , type:"IMG"    ,value:"活動代碼6",   src:"./images/departmentPos/promoteItem6.png", eClick:'savePromotionCode("EC210310")'}]}
	 ]}
	 ], 	 
		beginService:"",
		closeService:""			
	});
	
}



function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}


function savePromotionCode(promotionCode){
	
	if(confirm("是否確定送出?")){
		if("" == headId){
			alert("ERROR");
		}else{
		  vat.bean().vatBeanOther.headId = headId;
		  vat.bean().vatBeanOther.promotionCode = promotionCode;
		 
		  vat.block.submit(function(){return "process_object_name=soDepartmentOrderService&process_object_method_name=updatePromotion";},{
	                    bind:true, link:true, other:true}  );
	      window.top.close();
		}
	}
}
