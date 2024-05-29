/***
 *	檔案：輸入 Ajax 控制 
 *	說明：vat-ajax.js
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */

/*************************************************
 * 說明：vat & vat.tab 變數宣告
 */
 	if (typeof vat == 'undefined') {
		vat = function() {};
	}
	vat.ajax = {};
	vat.ajax.xmlHttp = {};
	vat.ajax.RequestQueue = {};
	vat.ajax.serviceID = "";
	vat.ajax.RESPONSE_TAG_START = "<";
	vat.ajax.RESPONSE_TAG_END = ">";


/*************************************************
 * 說明：vat.ajx.adv 2.0
 			  1.多重呼叫: 非共用ajax物件
 			  2.同步非同步控制
 			  3.請求成功或失敗的作業控制 find(), lose(), fail()
 			  4.回傳值: 資料欄位: 表頭, 表身(相容於舊格式), 控制命令, 訊息
 			  	{ControlCommand, name:value, ...} + DataGridString  
 			  	name, value, type, updated, picture, 
 */
vat.ajax.XHRequestCreate = function vatAjaxXHRequestCreate(){
	return new ActiveXObject("Microsoft.xmlHttp");
//	return new XMLHttpRequest();  
	
	/*
	if (typeof xmlHttpRequest != "undefined"){				// 如果可以取得 xmlHttpRequest
		return new xmlHttpRequest();  									// Mozilla、Firefox、Safari 
	}else if (window.ActiveXObject){									// 如果可以取得 ActiveXObject
		var vaVersions = ["MSXML2.XMLHttp.6.0", "MSXML2.XMLHttp.3.0"];
		for (var i=0; i < vaVersions.length; i++){
			try{	
				return ActiveXObject(vaVersions[i]);				// try Internet Explorer ver
			} catch (oError){
			}
		}
	}
	*/	
};
vat.ajax.XHRequest = function vatAjaxXHRequest(poReq){
  if (typeof poReq != "undefined"){
  	poReq.priority = poReq.priority ? poReq.priority : 8;
  	poReq.sId  = poReq.sId  ? poReq.sId  : "";  	
  	poReq.type = poReq.type ? poReq.type : "POST";
  	poReq.url  = poReq.url  ? poReq.url  : "./jsp/AjaxQuery.jsp"; 
  	poReq.asyn = typeof poReq.asyn == "boolean" ? poReq.asyn : true;
  	poReq.post = poReq.post ? poReq.post : ""; 
  	poReq.find = typeof poReq.find !== "undefined" ? poReq.find : function vatAjax_XHR_find(){};
  	poReq.lose = typeof poReq.lose !== "undefined" ? poReq.lose : function vatAjax_XHR_lose(){};
  	poReq.fail = typeof poReq.fail !== "undefined" ? poReq.fail : function vatAjax_XHR_fail(){};
 	 	var oXHR = vat.ajax.XHRequestCreate();					 	// 建立非同步請求物件
	 	oXHR.onreadystatechange = function(){							// 設定callback 函式
 	 		 	if (oXHR.readyState == 4){										// 測試狀態是否請求完成
					if (oXHR.status == 200){ 										// 如果伺服端回應 OK
						if (oXHR.responseText.indexOf(vat.ajax.RESPONSE_TAG_START+"status=Success"+vat.ajax.RESPONSE_TAG_END) != -1){
							if (typeof poReq.find === "function"){
								poReq.find(oXHR);	// 後端有取回資料
							}	
						}else{
							if (typeof poReq.lose === "function"){	
								poReq.lose(oXHR);											// 後端無取回資料
							}else{
								var vsResponseStatus 	= vat.ajax.getValue("status", oXHR.responseText);
								var vsResponseMsg 		= vat.ajax.getValue("msg"		, oXHR.responseText);		
								if("Error" === vsResponseStatus && typeof vsResponseMsg === "string"){
									vat.debug("user", vsResponseMsg);
								}else{
									vat.debug("user", "【後端回傳錯誤】");
								}
							}	
						}	
					}else{																			// 如果伺服端回應不 OK
						if (typeof poReq.fail === "function"){
							poReq.fail(oXHR);
						}	
					}
 	 			}
 	 		};
		oXHR.open(poReq.type, poReq.url, poReq.asyn);				// 開啟連結
		oXHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;");
		oXHR.send(poReq.post);  														// 傳送請求參數
	}
};


/*************************************************
 *	功能：設定 Ajax 初始化
 *  說明：
 *    		
 */	 
vat.ajax.createRequest = function(){
	if (window.xmlHttpRequest){ 															// 如果可以取得 xmlHttpRequest
		vat.ajax.xmlHttp = new xmlHttpRequest();  							// Mozilla、Firefox、Safari 
	}else{ 
		if (window.ActiveXObject) 															// 如果可以取得 ActiveXObject
			vat.ajax.xmlHttp = new ActiveXObject("Microsoft.xmlHttp");	// Internet Explorer
	}
}


vat.ajax.createQueryString = function(){
	var queryString = "";
	return queryString;
}


vat.ajax.startRequest = function vatAjaxStartRequest(pQueryString, pHandler, pServiceId){
	/*
  vat.ajax.vatServiceId = ( pServiceId ? pServiceId : "");
	vat.ajax.createRequest(); 																														// 建立非同步請求物件
  vat.ajax.xmlHttp.onreadystatechange = pHandler;																				// 設定callback函式
  vat.ajax.xmlHttp.open("POST", "./jsp/AjaxQuery.jsp", true);														// 開啟連結
	vat.ajax.xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	vat.ajax.xmlHttp.send(pQueryString);  																								// 傳送請求參數
	*/
	vat.ajax.XHRequest(
	{  sId: pServiceId ? pServiceId : "",
		post: pQueryString,
		asyn: true,			
		lose: function vatAjax_Lose(){
						// vat.$jsr(pHandler);
					}, 	 	   
		find: function vatAjax_Find(oXHR){
						vat.ajax.xmlHttp = oXHR;
						vat.$jsr(pHandler);
					}
	});
}


vat.ajax.handleState = function() {
	return true;
 	if (vat.ajax.xmlHttp.readyState == 4) { 												// 測試狀態是否請求完成
		if (vat.ajax.xmlHttp.status == 200) { 												// 如果伺服端回應 OK
			return true;
		} 																														// 如果伺服端回應不 OK
	}	
	return false;
}


vat.ajax.handleStateChange = function vatAjaxHandleStateChange(){
 	if (vat.ajax.xmlHttp.readyState == 4) { 												// 測試狀態是否請求完成
		if (vat.ajax.xmlHttp.status == 200) { 												// 如果伺服端回應 OK
			// vat.debug("developer", "伺服器回應" + vat.ajax.xmlHttp.responseText);
			return true;
		} else {																											// 如果伺服端回應不 OK
			vat.debug("developer", "no response from "+vat.ajax.vatServiceId+", readyState:"+
															vat.ajax.xmlHttp.readyState+" /status:" + vat.ajax.xmlHttp.status);
		}
	}	
}

/*

*/
vat.ajax.getValue = function vatAjaxGetValue(pName, pMsg, pOpt) {
	var msg = pMsg;
	var cmdValue = "", cmdError, cmdTagS, cmdTagB, cmdTagE;
	if (typeof(msg) === "string"){
		if ((cmdError = msg.indexOf('查無資料..')) === -1){
			if ((cmdTagS = msg.indexOf(vat.ajax.RESPONSE_TAG_START + pName + "=")) !== -1){
				cmdTagB = msg.indexOf("=", cmdTagS + pName.length);
				if (cmdTagB !== -1){
					cmdTagE  = msg.indexOf(vat.ajax.RESPONSE_TAG_END + vat.ajax.RESPONSE_TAG_START, cmdTagB);
					if (cmdTagE === -1){
						cmdTagE = msg.lastIndexOf(vat.ajax.RESPONSE_TAG_END);
					}	
					if (cmdTagE > cmdTagB) 
						cmdValue = msg.substring(cmdTagB + 1, cmdTagE + 0);		//*** 不包含 "<|" & "|>"
				}else{
					//vat.debug("developer", "vat.ajax.getValue():" + pName + 'value not found!');
				}		
			}else{ 
				//vat.debug("developer", "vat.ajax.getValue():" +pName + ' variable not found!');
			}	
		}	
	}		
	return cmdValue;
}

vat.ajax.getValueNoDot = function vatAjaxGetValueChangeNoDot(pName, pMsg) {
	var msg = pMsg;
	var cmdValue, cmdError, cmdTagS, cmdTagB, cmdTagE;
	cmdValue = "";
	if (typeof(msg) == "string"){
		if ((cmdError = msg.indexOf('查無資料..')) == -1){
			if ((cmdTagS = msg.indexOf(vat.ajax.RESPONSE_TAG_START + pName + "=")) != -1){
				cmdTagB = msg.indexOf("=", cmdTagS + pName.length);
				if (cmdTagB != -1){
					cmdTagE  = msg.indexOf(vat.ajax.RESPONSE_TAG_END, cmdTagB);
					cmdValue = msg.substring(cmdTagB + 1, cmdTagE + 0).split(".")[0];		//*** 不包含 "<|" & "|>"
					if(cmdValue.length <= 0){
						cmdValue = 0;
					}
					cmdValue = formatNum(cmdValue);
				}else{
					// vat.debug("developer", "vat.ajax.getValue():" + pName + 'value not found!');
				}		
			}else{ 
				// vat.debug("developer", "vat.ajax.getValue():" +pName + ' not found!');
			}	
		}	
	}		
	return cmdValue;
}
vat.ajax.found = function(pMsg) {
	if (pMsg.indexOf('查無資料..') != -1){
		return false;
	}else{
		return true;
	}
}

vat.ajax.response = function(psResponse){
	if (typeof vat.ajax.xmlHttp.responseText === "string"){
		var vsResponse = typeof psResponse === "string" ? psResponse : vat.ajax.xmlHttp.responseText;
		var vsStatus = vat.ajax.getValue("status", vsResponse),
				vsMsg 	 = vat.ajax.getValue("msg", vsResponse);		
		if(vsStatus === "Error"){
			return vsMsg;
		}else{
			return "" ;
		}
	}else{
		return "系統警告, 還沒執行過非同步傳輸(AJAX), 無任何【回傳訊息】";
	}	 
};

