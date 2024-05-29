/***
 *	檔案：vat-message.js
 *	說明：訊息對話框控制
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */

//*** vat & vat.message 宣告
	if (typeof vat === 'undefined'){vat = function(){};}
	vat.message = { top : 160, left : 0, width : 100, height : 25,
									tagName : "#vatMessage",						/* 跟後端(ceap)整合用的元素名稱 */
									inDesignStudio : false, 
									notCompleted 	 : true,							/* 是否已經執行過 complete  */
									order : "", 
									model : "",													/* ALERT, CONFIRM, PROMPT				*/
									topic : "", 
									title : "",	
									c_y_c : "NOTHING",									/* NOTHING, CLOSE, HANDLER    	*/
									c_y_f :	"",
									c_y_h : "",
									c_n_c : "NOTHING",									/* NOTHING, CLOSE, HANDLER    	*/
									c_n_f	: "",
									c_n_h : "",
									reply : ""	 												/* 是否有要有回傳, 及預設的內容 */
								};
  vat.message.bind = {	currentItemIndex : 0,
  											name  : new Array(),
												value : new Array(),
												index : 0,
												tab	  : new Array()
										 };
	vat.message.dialog = { top : 200, left : 200, height : 100, width : 200 };
	vat.message.debug = { vat : false, 
												developer : false,
												user : true
											};

/*************************************************
 *	功能：訊息初始話設定
 *  說明：不管幾個 form, 每次 submit 時, 每一個 form 的 onsubmit 都會被呼叫, 
 *    		所以如果每個 form 的 onsubmit 都有回拋訊息, 都會顯示出來
 */	
vat.message.complete = function(){
	var msg, x1 = document.getElementsByName(vat.message.tagName);
	vat.message.notCompleted = false;
	if (x1[0]){
		//*** 先隱藏此元素
		x1[0].style.display = "none";
		msg = x1[0].value;
		x1[0].setAttribute("value", vat.message.bind.merge());
		if (typeof msg == "string" && msg.length > 1){
			if (msg == '[binding]'){
				vat.message.inDesignStudio = true;									//*** 在 Studio 中無法取得 vat.message
			}else{			
	
				vat.message.decode(msg);														//*** 再判斷是否有要解碼的訊息
			}
		}else{
			//*** 無訊息內容
		}
	}else{
		vat.debug("developer", "無訊息變數 vatMessage 設定");
	}
};

vat.message.decode = function(pMsg){
	var msg = pMsg;
	var cmd, cmdTagS, cmdTagE;
	while(msg.length > 0){
		// vat.debug("tester", "未解析的剩餘訊息內容為:" + msg);
		if ((cmdTagS = msg.indexOf("<")) != -1){
			// vat.debug("tester", "命令標籤為起始位置(cmdTagS)為：" + cmdTagS);
			cmdTagE = msg.indexOf(">", cmdTagS + 1);
			if (cmdTagE != -1){
				// vat.debug("tester", "命令標籤為起始位置(cmdTagE)為：" + cmdTagE);
				vat.message.topic += msg.substring(0, cmdTagS);
				cmd = msg.substring(cmdTagS + 1, cmdTagE + 0);		//*** 不包含 "<" & ">"
				vat.message.cmdExec(cmd);
				msg = msg.substring(cmdTagE + 1, msg.length);
			}else{
				vat.debug("developer", "Missing end of command tag '>' in message:" + msg);
				msg = "";
			}
		}else{
			vat.message.topic += msg;
			msg = "";
		}
		//*** vat.debug("tester", "解析後訊息顯示的模式(model/topic)為：" + vat.message.model + "/" + vat.message.topic);
	}
	if (vat.message.topic.length > 0 && vat.message.model.length === 0){
		// vat.debug("tester", "有訊息沒有命令");
	}
};

vat.message.cmdExec = function(pCmd){
	var cmd, txt, pos;
	if ((pos = pCmd.indexOf(":")) != -1){
		cmd = pCmd.substring(0, pos);									//*** 帶值命令或變數
		txt = pCmd.substring(pos + 1);
	}else{
		cmd = pCmd;																		//*** 不帶值命令
		txt = "";
	}
	vat.debug("tester", "解析整合後的命令為：" + cmd + "/參數：" + txt);
	switch(cmd){
		case 'CMD-CLEAR':
			vat.message.topic = "";
			break;
		case 'CMD-ALERT':	
			vat.message.model = "ALERT";
			break;
		case 'CMD-EXEC':	
			vat.message.model = "EXEC";
			if (txt) vat.jr(txt);
			break;
		case 'CMD-RELOAD':	
			vat.message.model = "RELOAD"  //RELOAD.ALERT.CLOSE
			winParent = window.opener.document;
			winParent.getElementById("vatGridRefreshImg#"+txt).click();
			/*
			if (typeof winParent.getElementById === "object"){
				vatGridRefreshBtn = winParent.getElementById("vatGridRefreshImg#"+txt);
				if (vatGridRefreshBtn !== null)
					if (typeof vatGridRefreshBtn.onclick === "object")
						vatGridRefreshBtn.click();
					else
						eval(vatGridRefreshBtn.onclick);	
			}
			*/				
			break;
		case 'CMD-CONFIRMREPORT':	
			vat.message.model = "CONFIRMREPORT";
			break;
		case 'CMD-CONFIRM':	
			vat.message.model = "CONFIRM";
			break;
		case 'CMD-PROMPT':
			vat.message.model = "PROMPT";
			break;
		case 'CMD-YES':
			vat.message.c_y_c  = txt;
			break;
		case 'CMD-Y-F':
			vat.message.c_y_f  = txt;
			break;
		case 'CMD-Y-H':
			vat.message.c_y_h  = txt;
			break;			
		case 'CMD-NO' :
			vat.message.c_n_c  = txt;
			break;
		case 'CMD-N-F':
			vat.message.c_n_f  = txt;
			break;
		case 'CMD-N-H':
			vat.message.c_n_h  = txt;
			break;
		case 'REPLY':
			vat.message.reply = txt;
			break;			
		case 'TITLE'  :
			vat.message.title = "<" + txt + ">";	
			break;
		case 'TAB'		:
			// vat.debug("tester", "標籤預設：" + txt);
			if ((pos = txt.indexOf("-")) != -1){
				vat.message.bind.tab.setValue(txt.substring(0, pos), txt.substring(pos + 1));
			}else{
				vat.debug("developer", "預設頁籤的命令有誤：" + txt);
			}
			break;
		case 'VAR'		:
			vat.debug("tester", "標籤預設：" + txt);
			if ((pos = txt.indexOf("-")) != -1){
				vat.message.bind.setValue(txt.substring(0, pos), txt.substring(pos + 1));
			}else{
				vat.debug("developer", "預設頁籤的命令有誤：" + txt);
			}
			break;			
		default:
			vat.debug("developer", "錯誤或無法解晰的命令：" + cmd + "/參數：" + txt);
			break;
	}
};

vat.message.bind.getValue = function(pName, pIni){
	var ok = false, retValue = pIni;
	for (var i=0; i < vat.message.bind.name.length; i++){
		if (pName == vat.message.bind.name[i]){
			retValue = vat.message.bind.value[vat.message.bind.index = i];
			ok = true;
			break;
		}
	}
	return retValue;
};

vat.message.bind.setValue = function(pName, pValue){
	var ok = false;
	for (var i=0; i < vat.message.bind.name.length; i++){
		if (pName == vat.message.bind.name[i]){
			vat.message.bind.value[vat.message.bind.index = i] = pValue;
			ok = true;
			break;
		}
	}
	if (! ok){
		vat.message.bind.name.push(pName);
		vat.message.bind.value.push(pValue);
	}
	vat.message.bind.save();	
};

vat.message.bind.tab.getValue = function(pIdx){
	var tabValue;
	if (typeof vat.message.bind.tab == 'undefined'){
		if (vat.message.notCompleted){
			vat.message.complete();												//*** 相容於未先呼叫 vat.message.complete() 者
		}	
		if (vat.message.isDesignStudio()){
		}else{
			vat.debug("developer", "呼叫 vat.message.bind.tab.getValue() 前未先使用 vat.message.complete() 取得預設值");
		}
	}
	if (pIdx < vat.message.bind.tab.length){
		tabValue = vat.message.bind.tab[pIdx];
	}else{
		tabValue = 0;
		if (vat.message.isDesignStudio()){
		}else{
			vat.debug("tester", "此頁籤編號(" + pIdx + ")有誤或沒有預設的值");
		}
	}
	return tabValue;
};

vat.message.bind.tab.setValue = function(pIdx, pValue) {
	if (isFinite(pIdx)){
		vat.message.bind.tab[parseInt(pIdx)] = pValue;
	}else{
		vat.debug("developer", "預設的頁籤編號(" + pIdx + ")或位置(" + pValue + ")有誤");
	}
};

vat.message.bind.merge = function(){
	var msg = "<CMD-CLEAR>";
	for (var i=0; i < vat.message.bind.tab.length; i++){
		if (typeof vat.message.bind.tab[i] != 'undefined'){
			msg += ("<TAB:" + i.toString() + "-" + vat.message.bind.tab[i] + ">");
		}
	}
	for (i=0; i < vat.message.bind.name.length; i++){
		if (typeof vat.message.bind.name[i] != 'undefined'){
			msg += ("<VAR:" + vat.message.bind.name[i] + "-" + vat.message.bind.value[i] + ">");
		}
	}
	return (msg + "<REPLY:" + vat.message.reply + ">");
};

vat.message.bind.save = function(){
	var x1 = document.getElementsByName(vat.message.tagName);
	var msg;
	if (x1[0]){
		msg = x1[0].value;
		x1[0].setAttribute("value", vat.message.bind.merge());
	}
};

//** 訊息顯示及對話
vat.message.display = function vatMessageDisplay(){
	var cmd, c_f, c_h, winParent, vatGridRefreshBtn;
	if (vat.message.topic.length > 0){	// 有回傳訊息才處理
		switch(vat.message.model){
			case 'ALERT':
				window.alert(vat.message.title + vat.message.topic);
				cmd = vat.message.c_y_c;
				c_f = vat.message.c_y_f;
				c_h = vat.message.c_y_h;						
				break;
			case 'CONFIRM':
				vat.message.reply = window.confirm(vat.message.title + vat.message.topic);
				if (vat.message.reply){
					cmd = vat.message.c_y_c;
					c_f = vat.message.c_y_f;
					c_h = vat.message.c_y_h;
				}else{
					cmd = vat.message.c_n_c;
					c_f = vat.message.c_n_f;
					c_h = vat.message.c_n_h;
				}
				break;
			case 'CONFIRMREPORT':
				vat.message.reply = window.confirm(vat.message.title + vat.message.topic + ",確認是否列印單據?");
				if (vat.message.reply){
					var reportUrl = document.getElementsByName("reportUrl")[0].value;
					window.open(reportUrl,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
				}
				cmd = vat.message.c_y_c;
				c_f = vat.message.c_y_f;
				c_h = vat.message.c_y_h;					
				break;
			case 'RELOAD':
				winParent = window.parent;
				if (typeof winParent.getElementById === "function"){
					vatGridRefreshBtn = winParent.getElementById("vatGridRefreshImg#"+vat.message.c_y_c);
					if (vatGridRefreshBtn !== null)
						vatGridRefreshBtn.onclick();
				}	
				break;				
			case 'PROMPT':
				break;
		}
		switch(cmd){
			case "WIN-CLOSE" :
				window.top.close();
				return false;	//
			case "WIN-CLOSE-AFTER-RUN" :
				if (vat.block && typeof vat.block.submit === "function"){
					var voBtn = document.getElementsByName("#vatPickerId");
					if (voBtn !== null && typeof window.opener !== "undefined"){
						var winParent = window.opener.document;
						var voPickerClick = winParent.getElementById(voBtn[0].value+"#pick");	// check										 
						if (voPickerClick !== null){
							voPickerClick.click();
						}
					}					
				}	
				window.top.close();
				return false;	//
			case "CALL-HANDLER" :
				vat.message.bind.save();
				executeCommandHandler(c_f, c_h);
				break;
			default:
				break;
		}	
	}
};

vat.message.isDesignStudio = function(){
	return vat.message.inDesignStudio;
};

vat.debug = function(psLevel, psMsg){
	if (psLevel === "user" && vat.message.debug.user){
		window.alert("訊息通知：" + psMsg);
	}else if (psLevel === "developer" && vat.message.debug.developer){
		window.alert("訊息通知："+psMsg+",\n\n caller:"+vat.callerName(vat.debug.caller));
	}else if (psLevel === "vat" && vat.message.debug.vat){
		if (!window.confirm(psMsg+"\n\n caller:"+vat.callerName(vat.debug.caller)+", continue?", "yes")){
			window.alert("source from:"+vat.debug.caller.toString());
		}
	}
};

vat.debug.enable = function(psLevel){
	if (psLevel === "developer"){
		vat.message.debug.developer = true;
	}else if (psLevel === "vat"){
		vat.message.debug.vat = true;
	}else{
		vat.message.debug.developer = true;
		vat.message.debug.vat = true;			
	}	
};

vat.debug.disable = function(psLevel){
	if (psLevel === "developer"){
		vat.message.debug.developer = false;
	}else if (psLevel === "vat"){
		vat.message.debug.vat = false;
	}else{
		vat.message.debug.developer = false;
		vat.message.debug.vat = false;			
	}
};

vat.debug.developer = function(){
	return vat.message.debug.developer;
};

vat.callerName = function vatCallerName(psCaller){
	var vsCaller = psCaller.toString();
	return vsCaller.substring(vsCaller.indexOf("func", 0)+8, vsCaller.indexOf("(", 0))+"()";
};
