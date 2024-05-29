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
 * 說明：vat & vat.tabm 變數宣告
 */
	if (typeof vat == 'undefined') {
		vat = function() {};
	}
	vat.tabm = { currentTabIndex :  0 };
	vat.tabm.$box = new Array();


/*************************************************
 *	功能：標籤及按鈕設定
 * 屬性：public
 * 範例：vat.tabm.createTab(<頁籤編號>,<軸心元素>)
 *			vat.tabm.createButton(<頁籤編號>,<按鈕ＩＤ>,<描述>,<區塊ＩＤ>,<低亮圖檔路徑>,<按鈕時圖檔路徑>, <顯示控制>, <其他控制>)
 *			vat.tabm.createDivision(<頁籤編號>,<區塊ＩＤ>)
 * 說明：(1) pBtnId 加入 list
 *      (2) 判斷是否為隱藏頁
 *			(3) check 是否為顯示頁籤: (是/高亮度) img.src = pSrc2 
 * 																	 			 saved.beforeId = pBtnId 
 *                                   			 saved.beforeSrc = pSrc1
 *                               (否/低亮度)img.src = pSrc1
 */
vat.tabm.createTab = function(pTabId, pAxis, pAlign, pFloat, pOpt){
	vat.tabm.$box[pTabId] = {width				: 975,			/* option: tab width 	-- need, if floatMode is R 				*/
													height			: 600,			/* option: tab height -- never use, reserve							*/
													btnWidth		: 100,			/* option: btn width 																		*/
													btnHeight		: 25,				/* option: btn height 																	*/													
													btnShiftTop	: 0,				/* option: btn shift-top  															*/
													btnShiftLeft: 0,				/* option: btn shift-left 															*/
													divShiftTop	: 1,				/* option: div shift-top																*/
													divShiftLeft: 1,				/* option: div shift-left 															*/
													delay 			: 50,  	    /* option: 延遲微秒數	default: 500												*/
													axisTagId 	: (pAxis  ? pAxis  : "vatTabSpan"),	/* 頁籤最左上角軸心元素 ID      		*/
													alignMode		: (pAlign ? pAlign : "H"),					/* 按鈕排列的方式 (H, V)					*/
													floatMode		: (pFloat ? pFloat :  ""),					/* 指定按鈕的左右配置	(L, R)			*/													
													divTop			: 0,				/* internal use, for position calculate 								*/
													divLeft			: 0,				/* internal use, for position calculate 								*/
													divWidth    : 0,				/* internal use, for position calculate 								*/
													beforeId 		: "", 			/* 之前按鈕的 id (btn)																		*/
													beforeImgSrc: "",				/* 目前按鈕的圖案路徑																			*/
												 	currentId 	: "",				/* 目前按鈕的 id, 與 vat.message.bind.tab[]的值同步				*/
												 	currentIndex:  0,  			/* 目前按鈕在 list 中的索引																*/
													beforeDivId	: "",				/* 之前區塊的 id (div)	　																	*/
												 	timerId 		:  0,
												 	actionAudit : false,													
												 	btnList			: new Array(),
													divList			: new Array()};
	if (pOpt){
		if (pOpt.width 				&& isFinite(pOpt.width			 ))	vat.tabm.$box[pTabId].width 				= pOpt.width;
		if (pOpt.btnWidth 		&& isFinite(pOpt.btnWidth		 ))	vat.tabm.$box[pTabId].btnWidth 		= pOpt.btnWidth;		
		if (pOpt.btnHeight 		&& isFinite(pOpt.btnHeight	 ))	vat.tabm.$box[pTabId].btnHeight 		= pOpt.btnHeight;		
		if (pOpt.btnShiftTop  && isFinite(pOpt.btnShiftTop ))	vat.tabm.$box[pTabId].btnShiftTop  = pOpt.btnShiftTop;
		if (pOpt.btnShiftLeft && isFinite(pOpt.btnShiftLeft))	vat.tabm.$box[pTabId].btnShiftLeft = pOpt.btnShiftLeft;
		if (pOpt.divShiftTop  && isFinite(pOpt.divShiftTop ))	vat.tabm.$box[pTabId].divShiftTop  = pOpt.divShiftTop;
		if (pOpt.divShiftLeft && isFinite(pOpt.divShiftLeft))	vat.tabm.$box[pTabId].divShiftLeft = pOpt.divShiftLeft;
		if (pOpt.delay 				&& isFinite(pOpt.delay			 ))	vat.tabm.$box[pTabId].delay 				= pOpt.delay;
	}
	document.write("<div id='vatTab'><table cellspacing='0' style='height: 35px;width:100%;' border='0' cellpadding='0'><tr style='width: 100%'><td align='left' rowspan='1' valign='center' colspan='8' nowrap><span id='vatTabSpan'></span></td></tr></table></div>");
	/*
	var voHTML_table = document.createElement('<table>'), 
			voHTML_tr = document.createElement('<tr>'),
			voHTML_td = document.createElement('<td>');
		
			voHTML_table.id = 'tab1';
			voHTML_tr.id = 'tr1';
			voHTML_td.id = 'td1';
			document.body.appendChild(voHTML_table);
			document.body.appendChild(voHTML_tr);
			document.body.appendChild(voHTML_td);

	*/	
}



vat.tabm.createButton = function(pTabId, pBtnId, pDesc, pDivId, pSrc1, pSrc2, pHideTagName, pHandler){
	var btnImg, btnSpan;
	if (typeof vat.tabm.$box[pTabId] == 'undefined'){
		vat.tabm.createTab(pTabId);
	}
	btnSpan = document.getElementById(vat.tabm.$box[pTabId].axisTagId);
	if (btnSpan){
		var isDisplay = "inline"; 														//*** 預設為顯示
		if (pHideTagName){																		//*** 有傳入控制隱藏的 pHideTagName, 才判斷預設是否為隱藏
			if (pHideTagName == "none"){
				isDisplay = "none";																//*** 使用 style.display:none 可讓隱藏的 btn 不佔元素空間
			}else if(pHideTagName == "inline"){
				isDisplay = "inline";
			}else{
				var x1 = document.getElementsByName(pHideTagName);
				if (x1 && x1.length > 0){
					if (! x1[0].checked) isDisplay = "none";				//*** 控制元素一定要是 <INPUT CHECKBOX> 才有效
				}
			}
		}
		vat.tabm.$box[pTabId].divList.push(pDivId);
		vat.tabm.$box[pTabId].btnList.push(pBtnId);	//*** 必須先加入 id 到 btn.list 才能判斷是否為目前的顯示頁籤
		btnImg = document.createElement('img');
	  btnImg.id  = pBtnId;
	  btnImg.alt = pDesc;
		btnImg.border = 0;
		// btnImg.onmouseout = function(){vat.tabm.$box.timeoutClear(pTabId)};
		btnImg.onclick = function(x){vat.tabm.$box.timeoutSet(pTabId, this.id, pDivId, pSrc2, 0, pHandler)};
	  btnImg.style.cursor = "hand";
		btnImg.style.display = vat.debug("isCeapDesignStudio") ? "inline" : isDisplay;
		if (vat.tabm.$box[pTabId].alignMode == "V"){
			btnImg.style.position = "absolute";
			btnImg.style.top = (vat.tabm.$box[pTabId].btnShiftTop + vat.tabm.$box[pTabId].btnHeight * (vat.tabm.$box[pTabId].btnList.length - 1));
			if (vat.tabm.$box[pTabId].floatMode == "R"){
				btnImg.style.left = vat.tabm.$box[pTabId].width - vat.tabm.$box[pTabId].btnWidth - vat.tabm.$box[pTabId].divShiftLeft;
			}else{
				btnImg.style.left = vat.tabm.$box[pTabId].btnShiftLeft;
			}	
			if (btnImg.style.display == "none"){
				btnImg.style.zIndex = -1;
			}else{	
				btnImg.style.zIndex = 1;
			}
		}
		if (isDisplay != "none" && vat.tabm.$box.getCurrentId(pTabId)== pBtnId ){	//*** 如果不是隱藏才需要判斷是否為目前顯示頁
			btnImg.src = pSrc2;	//*** 高亮度頁籤按鈕圖
			vat.tabm.$box[pTabId].beforeId = pBtnId;
			vat.tabm.$box[pTabId].beforeImgSrc = pSrc1;
		}else{
		  btnImg.src = pSrc1;	//*** 低亮度頁籤按鈕圖
		}
	  btnImg.width = vat.tabm.$box[pTabId].btnWidth;
	  btnImg.height = vat.tabm.$box[pTabId].btnHeight;		
		btnSpan.appendChild(btnImg);
	}else{
		vat.debug("developer", "Please create <SPAN id='vatTabSpan'> before call vat.tabm.button()");
	}
}

/*************************************************
 *	功能：標籤區塊設定及目前區塊顯示
 *  範例：vat.tabm.createDivision(<自訂區塊 id >);
 *	說明：標籤區塊設定的時候會判斷如果是目前區塊則顯示
 *        PS** 會自動重新計算頁面位置 
 */
vat.tabm.createDivision = function (psDivId){
	var x0, x0d='', x0w='', x0l='', x0t='', vnGroupNo, vnBtnNo, vbBtnActive = false; 
	(function findDiv(){
		var vnGroupIdx, vnDivIdx
		vnGroupNo = vnDivNo = -1;
		for(vnGroupIdx = 0; vnGroupIdx < vat.tabm.$box.length; vnGroupIdx++)
			if (typeof vat.tabm.$box[vnGroupIdx] !== 'undefined')
				for(vnDivIdx = 0; vnDivIdx < vat.tabm.$box[vnGroupIdx].divList.length; vnDivIdx++)
					if (vat.tabm.$box[vnGroupIdx].divList[vnDivIdx] == psDivId){
						vnGroupNo = vnGroupIdx, vnBtnNo = vnDivIdx;
						break;
					}
	})();	// 找出 psDivId 在哪一個標籤群組中	
	if (vnGroupNo >= 0 && vnGroupNo < vat.tabm.$box.length){
		vat.tabm.positionCalc(vnGroupNo);
		if (typeof vat.tabm.$box[vnGroupNo].btnList[vnBtnNo] === "string"){
			voBtn = document.getElementById(vat.tabm.$box[vnGroupNo].btnList[vnBtnNo]);
			if (voBtn.style.display !== "none") vbBtnActive = true;
		}
		if (vat.tabm.getCurrentId(vnGroupNo) == psDivId && vbBtnActive){
			x0d = 'display:inline;';
			vat.tabm.$box[vnGroupNo].beforeDivId = psDivId
		}else{
			x0d = 'display:none;z-Index:-1;';
		}
		if (vat.tabm.$box[vnGroupNo].alignMode == "V")
			if (vat.tabm.$box[vnGroupNo].floatMode == "R")
				x0w=('width:'+vat.tabm.$box[vnGroupNo].divWidth+';');
		x0t=('top:' +vat.tabm.$box[vnGroupNo].divTop+';');
		x0l=('left:'+vat.tabm.$box[vnGroupNo].divLeft+';');
		x0 ='<DIV id="'+psDivId+'" style="'+x0d+'position:absolute;'+x0t+x0l+x0w+'">';
	}else{
		x0 ="<DIV id='"+psDivId+"'>";
	}		
	document.write(x0);
	return psDivId;
}
	
vat.tabm.endDivision = function (){
	x0 = "</DIV>";
	document.write(x0);
}

vat.tabm.findDiv = function(pDivId){
	var IdxTab, IdxDiv, vnTabGroupNo = -1;
	for(IdxTab = 0; IdxTab < vat.tabm.$box.length; IdxTab++){
		if (typeof vat.tabm.$box[IdxTab] == 'undefined'){
		}else{
			for(IdxDiv = 0; IdxDiv < vat.tabm.$box[IdxTab].divList.length; IdxDiv++){
				if (vat.tabm.$box[IdxTab].divList[IdxDiv] == pDivId){
					vnTabGroupNo = IdxTab;
					break;
				}
			}
		}
	}
	return vnTabGroupNo;
}


vat.tabm.positionCalc = function(pTabId) {
	var pos = vat.utils.findPos(document.getElementById(vat.tabm.$box[pTabId].axisTagId));
	// vat.debug("vat", "vat.tabm.positionCalc:" + pTabId + "/" + vat.tabm.$box[pTabId].axisTagId + "/pos:" + pos[0] + "/" + pos[1]);
	if (vat.tabm.$box[pTabId].alignMode == "V"){
		vat.tabm.$box[pTabId].divTop	= vat.tabm.$box[pTabId].divShiftTop;
		if (vat.tabm.$box[pTabId].floatMode == "R"){
			vat.tabm.$box[pTabId].divLeft = vat.tabm.$box[pTabId].divShiftLeft
			vat.tabm.$box[pTabId].divWidth = vat.tabm.$box[pTabId].width - vat.tabm.$box[pTabId].btnWidth - vat.tabm.$box[pTabId].divShiftLeft;
		}else{
			vat.tabm.$box[pTabId].divLeft = vat.tabm.$box[pTabId].divShiftLeft+vat.tabm.$box[pTabId].btnWidth;
		}
	}else{
		switch(vat.tabm.$box[pTabId].floatMode){
		case 'float':
			vat.tabm.$box[pTabId].divTop  = pos[1] + 15 + vat.tabm.$box[pTabId].divShiftTop;
			vat.tabm.$box[pTabId].divLeft = vat.tabm.$box[pTabId].divShiftLeft;		
			break;
		default:
			vat.tabm.$box[pTabId].divTop  = vat.tabm.$box[pTabId].divShiftTop + vat.tabm.$box[pTabId].btnHeight + 3;
			vat.tabm.$box[pTabId].divLeft = vat.tabm.$box[pTabId].divShiftLeft;
			break;
		}

		vat.tabm.$box[pTabId].divWidth = vat.tabm.$box[pTabId].width;
	}
	// vat.debug("vat", vat.tabm.$box[pTabId].divTop + "/" +	vat.tabm.$box[pTabId].divLeft + "/" + vat.tabm.$box[pTabId].divWidth);
} 


/*************************************************
 * 功能：所有頁籤設定後的完成動作, 檢查頁籤的有效狀況
 * 說明：必須在 iframe onload=vatComplete() 動作中觸發
 */
vat.tabm.complete = function(){
	var pTabId, tabDiv;
	for (pTabId = 0; pTabId < vat.tabm.$box.length; pTabId++){
		if (vat.tabm.$box[pTabId].currentId === ""){	//*** 檢查是否已經初始化
			if (!vat.debug("isCeapDesignStudio")){		//*** 不在開發環境				
				//vat.debug("developer", "Never used vat.tabm.createButton() or vat.tabm.createDivision() to create tab.");
			}
		}else{	
			if (vat.tabm.$box[pTabId].btnList instanceof Array && vat.tabm.$box[pTabId].btnList.length > 0){
				//*** 有效頁檢查
				vat.tabm.$box[pTabId].currentIndex = vat.utils.find(vat.tabm.$box[pTabId].currentId, vat.tabm.$box[pTabId].btnList);
				if (! vat.tabm.$box[pTabId].currentIndex){	//*** 如果 currentId 不是有效頁籤, 則重設定為第一頁
					vat.tabm.$box[pTabId].currentId = vat.tabm.$box[pTabId].btnList[(vat.tabm.$box[pTabId].currentIndex = 0)];
				}
				//*** 隱藏頁檢查
				var x3 = document.getElementById(vat.tabm.$box[pTabId].currentId);
				if (! x3){	// 如果目前的頁籤是隱藏的, 那就從第一頁開始
					vat.tabm.$box[pTabId].currentId = vat.tabm.$box[pTabId].btnList[(vat.tabm.$box[pTabId].currentIndex = 0)];
				}else{			// 如果目前的頁籤是隱藏的, 更換為找到的第一個非隱藏頁籤
					if (x3.style.display == "none"){
						for (i in vat.tabm.$box[pTabId].btnList)
							if (document.getElementById(vat.tabm.$box[pTabId].btnList[i]).style.display != "none") break;
						vat.tabm.$box[pTabId].currentId = vat.tabm.$box[pTabId].btnList[(vat.tabm.$box[pTabId].currentIndex = i)];
						tabDiv = document.getElementById(vat.tabm.$box[pTabId].currentId);
						if (tabDiv && tabDiv.getAttribute("onclick")){
							tabDiv.onclick(0);
						}
								
						vat.tabm.popup(vat.tabm.$box[pTabId].currentId);
					}
				}
				// 設定跟Ceap後端 vat.message.bind.tab[]的內容跟 currentId 同步
				vat.tabm.whichTagSave(pTabId);
			}else{
				vat.debug("vat", "呼叫 vat.tabm.complete() 之前必須在 vat.tabm.$box[pTabId].btnList 建立一個頁籤按鈕 id");
			}
		}
	}	
}

vat.tabm.flash = function(){
	var pTabId, i, tabDiv, tabBtn;
	for (pTabId = vat.tabm.$box.length-1; pTabId >= 0; pTabId--){
		for (i in vat.tabm.$box[pTabId].divList){
			var tabDiv  = document.getElementById(vat.tabm.$box[pTabId].divList[i]);
			/* 
				window.resize 的時候, IE 會 call onresize() 重新顯示兩次, 
				第一次的時候頁籤軸心元素會跟其他不含絕對座標的元素重新安排位置
				第二次的時候則顯示含有絕對座標的元素, 因此要依照頁籤軸心元素新的位置重新計算含有絕對座標的元素
			*/
			if (tabDiv){	
				vat.tabm.positionCalc(pTabId);
				tabDiv.style.position = "absolute";
				tabDiv.style.top  = vat.tabm.$box[pTabId].divTop;
				tabDiv.style.left	= vat.tabm.$box[pTabId].divLeft;
				tabDiv.style.zIndex = -1;
				tabDiv.style.display = "none";
			}
		}
		if (vat.tabm.$box[pTabId].currentId === ""){
			//***
		}else{
			tabDiv = document.getElementById(vat.tabm.$box[pTabId].currentId);
			if (tabDiv && tabDiv.getAttribute("onclick")){
				tabDiv.onclick(0);
			}
		}
		/*	don't need to re-calculate all button's position */	
	}
}

vat.tabm.$box.getCurrentId = function(pTabId){
	if (typeof vat.tabm.$box[pTabId] == 'undefined' || vat.tabm.$box[pTabId].currentId === ""){
		vat.tabm.whichTagLoad();						//*** 檢查是否已經載入過 vat.message.bind.tab[] 的內容
	}
	return vat.tabm.$box[pTabId].currentId;
}

vat.tabm.getCurrentId = function(pTabId){
	vat.tabm.$box[pTabId].currentIndex = vat.utils.find(vat.tabm.$box[pTabId].currentId, vat.tabm.$box[pTabId].btnList);
	return vat.tabm.$box[pTabId].divList[vat.tabm.$box[pTabId].currentIndex];
}



/*************************************************
 * 功能：將 vat.message.bind.tab[]: #vatTab 的內容載入目前按鈕 (currentId)
 */
vat.tabm.whichTagLoad = function(){
	var pTabId
	for (pTabId=0; pTabId < vat.tabm.$box.length; pTabId++){
		var x2 = vat.message.bind.tab.getValue(pTabId);
		if (x2){	//*** 在 submit 過後已經產生值 or 有預設則帶入作為目前顯示頁籤 id 
			vat.tabm.$box[pTabId].currentId = x2;
		}else{		//*** 有此 TagName 但是還沒有 .value 產生, 通常是第一次載入; 預設為第一個頁籤
			if (vat.tabm.$box[pTabId].btnList instanceof Array && vat.tabm.$box[pTabId].btnList.length > 0){
				vat.tabm.$box[pTabId].currentId = vat.tabm.$box[pTabId].btnList[(vat.tabm.$box[pTabId].currentIndex = 0)];
			}else{
				vat.debug("vat", "呼叫 vat.tabm.whichTagLoad 之前必須在 vat.tabm.$box[pTabId].btnList 建立一個頁籤按鈕 id");
			}
		}
	}	
}

/*************************************************
 * 功能：將目前按鈕 (currentId) 載入 vat.message.bind.tab[]: #vatTab 的內容
 * 說明：設定跟後端 CEAP Binding 的目前在哪個頁籤的值
 */
vat.tabm.whichTagSave = function(){
	var pTabId
	for (pTabId=0; pTabId < vat.tabm.$box.length; pTabId++){	
		vat.message.bind.tab.setValue(pTabId, vat.tabm.$box[pTabId].currentId);
	}
	vat.message.bind.save();
}



/*************************************************
 * 功能：頁簽按鈕延遲設定及清除 (利用 Timeout 延遲)
 */
vat.tabm.$box.timeoutSet = function(pnTabNo, pBtnIdNow, psDivId, psBtnSrc, pnDelay, pxHandler){
	if (!vat.tabm.$box[pnTabNo].actionAudit){
		vat.tabm.$box[pnTabNo].actionAudit = true; 
		vat.tabm.$box[pnTabNo].currentId = pBtnIdNow;
		vat.tabm.$box[pnTabNo].timerId = setTimeout(function(){
			clearTimeout(vat.tabm.$box[pnTabNo].timerId);
			vat.tabm.change(pnTabNo, psDivId, psBtnSrc, pxHandler);
			vat.tabm.$box[pnTabNo].actionAudit = false;
		}, 
		pnDelay ? pnDelay : vat.tabm.$box[pnTabNo].delay);
	}
}

vat.tabm.$box.timeoutClear = function(pTabId){
	clearTimeout(vat.tabm.$box[pTabId].timerId);
}	



/*************************************************
	功能：頁面切換控制
	說明
*/
vat.tabm.change = function(pTabId, pDivIdNew, pBtnSrcNew, pHandler){
	var btnNow = document.getElementById(vat.tabm.$box[pTabId].currentId);
	var btnOld = document.getElementById(vat.tabm.$box[pTabId].beforeId);
	var divOld = document.getElementById(vat.tabm.$box[pTabId].beforeDivId);
	vat.tabm.currentTabIndex = pTabId;
	if (divOld){																							//*** 隱藏之前的頁籤區塊內容
		divOld.style.zIndex = -1;
		divOld.style.display = "none";													//*** 另一種方式 .visibility = "hidden";
	}
	if (btnOld){																							//*** 恢復之前的頁籤按鈕圖案
		btnOld.src = vat.tabm.$box[pTabId].beforeImgSrc;
	}
	vat.tabm.$box[pTabId].beforeImgSrc = btnNow.src;						//*** 儲存目前的頁籤按鈕圖案
	btnNow.src = pBtnSrcNew;																	//*** 更換目前的頁籤按鈕圖案
	vat.tabm.popup(pTabId, pDivIdNew);
	vat.tabm.$box[pTabId].beforeId = btnNow.id;								//*** 儲存目前的頁籤按鈕 ID (Button)
	vat.tabm.$box[pTabId].beforeDivId = pDivIdNew;							//*** 儲存目前的頁籤區塊 ID (DIV)
	vat.tabm.whichTagSave();																	//*** 同步網頁上及後端的頁籤控制變數
	if (pHandler){																						//*** 呼叫 Ceap Handler,  必須放最後
		if (vat.debug("isCeapDesignStudio")){
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
	說明：PS** 不會自動重新計算頁面位置
 */
vat.tabm.popup = function (pTabId, pDivId, pTabTop, pTabLeft) {
	var tabDiv  = document.getElementById(pDivId);
	if (tabDiv){
		/*
		if (arguments.length >= 4){
			tabDiv.style.position = "absolute";
			tabDiv.style.top  = pTabTop;
			tabDiv.style.left	= pTabLeft;
		}	
		*/
		//*** 將 z 軸維度往前移動, 並將隱藏的元素重新顯示出來
		//*** 若只是隱藏可用 style.visibility = "visible"; 也許速度較快, 但是要測試 tab 使用時會不會有問題
		tabDiv.style.zIndex = 1;
		tabDiv.style.display = "inline";	
	}
}





/*************************************************
 * 功能：外部呼叫啟動顯示/隱藏頁籤按鈕
 * 屬性：public
 * 說明：除啟動顯示或隱藏按鈕外, 如果是顯示(且沒有傳入控制參數)則會自動設定為目前的頁籤
 * 　　　　　　　　　　　　　　　　 如果是隱藏則會移動到第一個有效頁籤
 *      並會將目前的頁籤同步到外部 binding 的元素內 (#vatTab)
 *      另外也可控制是否執行 event
 */
vat.tabm.displayToggle = function(pnTabNo, psBtnId, pbIsHide, pbIsChangeNow, pbIsEventGo){
	if (typeof pnTabNo === "number" && typeof psBtnId === "string" &&
			typeof vat.tabm.$box[pnTabNo] === "object" && vat.tabm.$box[pnTabNo] !== null){ 
		var voBtn = document.getElementById(psBtnId)
		if (voBtn){
			voBtn.style.display = pbIsHide ? 'inline' : 'none';
			if (!(typeof pbIsChangeNow === "boolean" && !pbIsChangeNow)){
				vat.tabm.$box[pnTabNo].currentId = psBtnId;
			}
			vat.tabm.complete();
			if (!(typeof pbIsEventGo === "boolean" && !pbIsEventGo)){
				(document.getElementById(vat.tabm.$box[pnTabNo].currentId)).onclick(0);
			}			
		}else{
			vat.debug("developer", "incorrect id:" + psBtnId + " ex: vat.tabm.button(tabBtnId, ...)");
		}
	}
}
