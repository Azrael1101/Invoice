/***
 *	檔案：頁籤畫面切換控制
 *	說明：vat-tab.js
 *
 *	建立：Mac 
 *	修改：
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 *
 */

/*************************************************
 * 說明：vat & vat.tab 變數宣告
 */
	if (typeof vat == 'undefined') {
		vat = function() {};
	}
	vat.tab = { top : 160, left : 0, width : 100, height : 25, 
							delay : 50,              						/* 延遲微秒數	default: 500			*/
							axisTagId 		: "vatTabSpan", 			/* 頁籤最左上角軸心元素 ID      */
							whichTagName 	: "#vatTab"						/* 跟後端(ceap)整合用的元素名稱 */
						};
  vat.tab.btn = {};						
	vat.tab.btn.saved =	{ beforeId 		 : "", 	/* 之前按鈕的 id																		*/
												beforeImgSrc : "",	/* 目前按鈕的圖案路徑																*/
											 	currentId 	 : "",	/* 目前按鈕的 id, 與 whichTagName: #vatTab 的值同步 */
											 	currentIndex :  0,  /* 目前按鈕在 list 中的索引													*/
											 	timerId : 0 };
	vat.tab.btn.list = new Array();
	vat.tab.div = {};
	vat.tab.div.saved = { beforeId : "" };
	vat.tab.div.list = new Array();		
	

/*************************************************
 *	功能：標籤按鈕設定
 *  範例： vat.tab.button(<自訂按鈕 id >, <滑鼠指到時的描述>, <自訂區塊 id >, 
 *												<預設影像檔路徑>, <顯示影像檔路徑>, <預設是否顯示>)
 *	說明：(1) pBtnId 加入 list
 *        (2) 判斷是否為隱藏頁
 *			  (3) check 是否為顯示頁籤: (是/高亮度)img.src = pSrc2 
 * 																			 			 saved.beforeId = pBtnId 
 *                                       			 saved.beforeSrc = pSrc1
 *                                  (否/低亮度)img.src = pSrc1
 */
vat.tab.button = function(pBtnId, pDesc, pDivId, pSrc1, pSrc2, pHideTagName, pHandler){
	var btnSpan = document.getElementById(vat.tab.axisTagId);
	if (btnSpan){
		var isDisplay = "inline"; //*** 預設為顯示
		if (pHideTagName){	//*** 有傳入控制隱藏的 pHideTagName, 才判斷此頁籤按鈕預設是否為隱藏
			if (pHideTagName == "none"){
				isDisplay = "none";
			}else if(pHideTagName == "inline"){
				isDisplay = "inline";
			}else{
				var x1 = document.getElementsByName(pHideTagName);
				if (x1 && x1.length > 0){
					if (! x1[0].checked) isDisplay = "none";	//*** 控制元素一定要是 <INPUT CHECKBOX> 才有效
				}
			}
		}
		vat.tab.div.list.push(pDivId);
		vat.tab.btn.list.push(pBtnId);	//*** 必須先加入 id 到 btn.list 才能判斷是否為目前的顯示頁籤
		var btnImg = document.createElement('<img>');
	  btnImg.id  = pBtnId;
	  btnImg.alt = pDesc;
		btnImg.border = 0;	  
		btnImg.onmouseout = function(){vat.tab.btn.timeoutClear()};
		btnImg.onclick = function(x){vat.tab.btn.timeoutSet(this.id, pDivId, pSrc2, x, pHandler)};
	  btnImg.style.cursor = "hand";
		btnImg.style.display = vat.message.isDesignStudio() ? "inline" : isDisplay;	
														//*** 使用 style.display:none 可讓隱藏的 btn 不佔元素空間
		vat.debug("tester", " 是否顯示 <isDisplay> :" + isDisplay + " / getCurrentId():" + vat.tab.btn.getCurrentId());
		if (isDisplay != "none" &&	//*** 如果不是隱藏才需要判斷是否為目前顯示頁
			  vat.tab.btn.getCurrentId()== pBtnId ){
			btnImg.src = pSrc2;	//*** 高亮度頁籤按鈕圖
			vat.tab.btn.saved.beforeId = pBtnId;
			vat.tab.btn.saved.beforeImgSrc = pSrc1;
		}else{
		  btnImg.src = pSrc1;	//*** 低亮度頁籤按鈕圖
		}
	  btnImg.width = vat.tab.width;
	  btnImg.height = vat.tab.height;		
		btnSpan.appendChild(btnImg);
	}else{
		vat.debug("developer", "Please create <SPAN id='vatTabSpan'> before call vat.tab.button()");
	}
}



/*************************************************
 *	功能：標籤區塊設定及目前區塊顯示
 *  範例：vat.tab.createDivision(<自訂區塊 id >);
 *	說明：標籤區塊設定的時候會判斷如果是目前區塊則顯示
 *        PS** 會自動重新計算頁面位置 
 */
vat.tab.createDivision = function (pDivId){
	var isDisplay
	if (vat.tab.div.getCurrentId() == pDivId){
		isDisplay = "inline";
		vat.tab.div.saved.beforeId = pDivId
	}else{
		isDisplay = "none";
	}
	vat.tab.div.positionCalc();
	var x1 ='<DIV id="' + pDivId + '" style = "display:'+ isDisplay +
					'; position:absolute; top:' + vat.tab.top + '; left:' + vat.tab.left + '; zIndex:-1;' +
					 '">';
	document.write(x1);
}	


 
/*************************************************
 * 功能：所有頁籤設定後的完成動作, 檢查頁籤的有效狀況
 * 說明：在 iframe onload=vatComplete() 動作中觸發, 不需使用者呼叫
 *       並會同步 whichTagName 的內容 (#vatTab)
 */
vat.tab.complete = function(){
	if (vat.tab.btn.saved.currentId === ""){	//*** 檢查是否已經初始化
		if (typeof vat.message != 'undefined' && vat.message.isDesignStudio()){
			//***
		}else{
			//vat.debug("developer", "Never use vat.tab.button() & vat.tab.createDivision()");
		}
	}else{
		if (vat.tab.btn.list instanceof Array && vat.tab.btn.list.length > 0){
			//*** 有效頁檢查
			vat.tab.btn.saved.currentIndex = vat.utils.find(vat.tab.btn.saved.currentId, vat.tab.btn.list);
			if (! vat.tab.btn.saved.currentIndex){	//*** 如果 currentId 不是有效頁籤, 則重設定為第一頁
				vat.tab.btn.saved.currentId = vat.tab.btn.list[(vat.tab.btn.saved.currentIndex = 0)];
			}
			//*** 隱藏頁檢查
			var x3 = document.getElementById(vat.tab.btn.saved.currentId);
			if (! x3){	// 如果目前的頁籤是隱藏的, 那就從第一頁開始
				vat.tab.btn.saved.currentId = vat.tab.btn.list[(vat.tab.btn.saved.currentIndex = 0)];
			}else{			// 如果目前的頁籤是隱藏的, 更換為找到的第一個非隱藏頁籤
				if (x3.style.display == "none"){
					for (i in vat.tab.btn.list)
						if (document.getElementById(vat.tab.btn.list[i]).style.display != "none")	break;
							vat.tab.btn.saved.currentId = vat.tab.btn.list[(vat.tab.btn.saved.currentIndex = i)];
				}
			}
			// 設定跟後端 CEAP Binding (#vatTab) 的內容跟 currentId 同步
			vat.tab.whichTagSave();
		}else{
			vat.debug("vat", "呼叫 vat.tab.complete() 之前必須在 vat.tab.btn.list 建立一個頁籤按鈕 id");
		}
		/*
		*/
	}
}

vat.tab.flash = function(){
	var i, tabDiv;
	for (i in vat.tab.div.list){
		if (i != vat.tab.btn.saved.currentIndex){
			var tabDiv  = document.getElementById(vat.tab.div.list[i]);
			if (tabDiv){
				tabDiv.style.position = "absolute";
				tabDiv.style.top  = vat.tab.top;
				tabDiv.style.left	= vat.tab.left;
				tabDiv.style.zIndex = -1;
				tabDiv.style.display = "none";
			}
		}	
	}
	if (vat.tab.btn.saved.currentId === ""){	//*** 檢查是否已經初始化
	}else{
		tabDiv = document.getElementById(vat.tab.btn.saved.currentId);
		if (tabDiv && tabDiv.getAttribute("onclick")){
			tabDiv.onclick(0);
		}
	}	
}

vat.tab.btn.getCurrentId = function(){
	if (vat.tab.btn.saved.currentId === ""){	//*** 檢查是否已經載入過 whichTagName 的內容
		vat.tab.whichTagLoad();
	}
	return vat.tab.btn.saved.currentId;
}

vat.tab.div.getCurrentId = function(){
	vat.tab.btn.saved.currentIndex = vat.utils.find(vat.tab.btn.saved.currentId, vat.tab.btn.list);
	return vat.tab.div.list[vat.tab.btn.saved.currentIndex];
}



/*************************************************
 * 功能：將 whichTagName: #vatTab 的內容載入目前按鈕 (currentId)
 */
vat.tab.whichTagLoad = function(){
	var x1 = document.getElementsByName(vat.tab.whichTagName);
	if (typeof vat.message != 'undefined' && vat.message.notCompleted){
		vat.message.complete();																		//*** 為了相容於上一版
	}	
	if (x1 && x1.length > 0){	//*** 取得跟後端 CEAP Binding 的目前在哪個頁籤的值
		var x2 = x1[0].value;
		if (x2){	//*** 在 submit 過後已經產生值 or 有預設則帶入作為目前顯示頁籤 id 
			vat.tab.btn.saved.currentId = x2;
		}else{		//*** 有此 TagName 但是還沒有 .value 產生, 通常是第一次載入; 預設為第一個頁籤
			if (vat.tab.btn.list instanceof Array && vat.tab.btn.list.length > 0){
				vat.tab.btn.saved.currentId = vat.tab.btn.list[(vat.tab.btn.saved.currentIndex = 0)];
			}else{
				vat.debug("vat", "呼叫 vat.tab.whichTagLoad 之前必須在 vat.tab.btn.list 建立一個頁籤按鈕 id");
			}
		}
	}else{	//*** 不管是沒有設定或設錯 Binding 的元素, 將 whichTagName 設定為空白, 視為不 Binding
		if (typeof vat.message != 'undefined' && vat.message.isDesignStudio()){
			//*** 
			if (vat.tab.btn.list instanceof Array && vat.tab.btn.list.length > 0){
				vat.tab.btn.saved.currentId = vat.tab.btn.list[(vat.tab.btn.saved.currentIndex = 0)];
			}	
		}else{
			vat.debug("developer", "No Binding HTML TAG for Tab control <INPUT name='" + vat.tab.whichTagName + "'>");
		}
		vat.tab.whichTagName = "";	
	}		
}



/*************************************************
 * 功能：將目前按鈕 (currentId) 載入 whichTagName: #vatTab 的內容
 * 說明：設定跟後端 CEAP Binding 的目前在哪個頁籤的值
 */
vat.tab.whichTagSave = function(){
	if (vat.tab.whichTagName !== ""){
		var x1 = document.getElementsByName(vat.tab.whichTagName);
		if (x1[0]){
			x1[0].setAttribute("value", vat.tab.btn.saved.currentId);
		}
	}
}



/*************************************************
 * 功能：頁簽按鈕延遲設定及清除 (利用 Timeout 延遲)
 */
vat.tab.btn.timeoutSet = function(pBtnIdNow, pX1, pX2, pX3, pX4){
	vat.tab.btn.saved.currentId = pBtnIdNow;
	vat.tab.btn.saved.timerId = setTimeout(function(){vat.tab.change(pX1, pX2, pX4)}, pX3?pX3:vat.tab.delay);
}

vat.tab.btn.timeoutClear = function(){
	clearTimeout(vat.tab.btn.saved.timerId);
}	



/*******************************
	功能：頁面切換控制
	範例：vat.tab.change("vatItemDiv", "images/tab_item_data_light.gif");
	說明
*/
 	vat.tab.change = function(pDivIdNew, pBtnSrcNew, pHandler){
	var btnNow = document.getElementById(vat.tab.btn.saved.currentId);
	var btnOld = document.getElementById(vat.tab.btn.saved.beforeId);
	var divOld = document.getElementById(vat.tab.div.saved.beforeId);	
	if (divOld){																		//*** 隱藏之前的頁籤區塊內容
		divOld.style.zIndex = -1;
		// divOld.style.visibility = "hidden";
		divOld.style.display = "none";
	}
	if (btnOld){																		//*** 恢復之前的頁籤按鈕圖案
		btnOld.src = vat.tab.btn.saved.beforeImgSrc;
	}
	vat.tab.btn.saved.beforeImgSrc = btnNow.src;		//*** 儲存目前的頁籤按鈕圖案
	btnNow.src = pBtnSrcNew;												//*** 更換目前的頁籤按鈕圖案
	vat.tab.popup(pDivIdNew);
	vat.tab.btn.saved.beforeId = btnNow.id;					//*** 儲存目前的頁籤按鈕 ID (Button)
	vat.tab.div.saved.beforeId = pDivIdNew;					//*** 儲存目前的頁籤區塊 ID (DIV)	
  //*** 功能：同時更新網頁(HTML)上的頁籤控制變數, 以便同步後端 ceap 的變數 (#vatTab)
	vat.tab.whichTagSave();
	//*** 透過 submit 呼叫 Ceap Handler, 必須放在最後
	if (pHandler){
		if (typeof vat.message != 'undefined' && vat.message.isDesignStudio()){
			//***
		}else{
			if (typeof pHandler == "string"){
				eval(pHandler);
			}else{
				executeCommandHandler(pHandler.formid, pHandler.handlerid);
			}	
		}	
	}
}



/*******************************
	功能：標籤頁面顯示
	範例：vat.tab.popup("vatItemDiv", top, left);
	說明：PS** 會自動重新計算頁面位置
*/
vat.tab.popup = function (pDivId, pTabTop, pTabLeft) {
	var tabDiv  = document.getElementById(pDivId);
	if (tabDiv){
		if (arguments.length >= 3){
			tabDiv.style.position = "absolute";			
			tabDiv.style.top  = pTabTop;
			tabDiv.style.left	= pTabLeft;
		}	else {
			vat.tab.div.positionCalc();
			tabDiv.style.top  = vat.tab.top;
		}
		//*** 將 z 軸維度往前移動, 並將元素產生
		//*** 若只是隱藏可用 tabDiv.style.visibility = "visible"; 也許速度較快, 但是要測試 tab 使用時會不會有問題
		tabDiv.style.zIndex = 1;
		tabDiv.style.display = "inline";	
	}
}


vat.tab.div.positionCalc = function() {
	var pos = vat.utils.findPos(document.getElementById(vat.tab.axisTagId));
	// vat.tab.left = pos[0];
	vat.tab.top  = pos[1] + 20;
}



/*************************************************
 * 功能：外部呼叫啟動顯示/隱藏頁籤按鈕
 * 說明：除啟動顯示或隱藏按鈕外, 同時如果是顯示則會自動設定為目前的頁籤
 *           											   如果是隱藏則會移動到第一個有效頁籤
 *       並會將目前的頁籤同步到外部 Binding 的元素內 (#vatTab)
 */
vat.tab.displayToggle = function (pBtnId, pHide){
	pHide = pHide ? 'inline' : 'none';
	var x1 = document.getElementById(pBtnId)
	if (x1){
		x1.style.display = pHide;
		vat.tab.btn.saved.currentId = pBtnId;
	}else{
		vat.debug("developer", "incorrect id:" + pBtnId + " ex: vat.tab.button(tabBtnId, ...)");
	}
	vat.tab.complete();
	document.getElementById(vat.tab.btn.saved.currentId).onclick(0);
}
