/***
 *	檔案：預設處理函數 
 *	說明：vat-popup.js
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


var vatInputList;
var vatPopupId;
var vatPopupObj;
var vatBackgroundId;
var vatBackgroundObj;
var vatCeapInputMark = "#";
var vatInputEndMarkCeap = ".";
var vatPopupState;

var vatStackDivInputList = new Array();
var vatStackDivPopupId   = new Array();


function vatDivStackPush() {
	vatStackDivInputList.push(vatInputList);
	vatStackDivPopupId.push(vatPopupId);
}

function vatDivStackPop(pObj) {
	vatInputList = vatStackDivInputList.pop();
	vatPopupId   = vatStackDivPopupId.pop();
	vatPopupObj  = document.getElementById(vatPopupId);
}


function vatDivInputListCreate(pId){
  var alltags;
	vatPopupId  = pId;							
	vatPopupObj = document.getElementById(vatPopupId);	
	if (vatPopupObj) {
		vatInputList = new Array();
		alltags = vatPopupObj.getElementsByTagName("INPUT");
		for (i=0; i< alltags.length; i++){
			if (alltags[i].name && 
					alltags[i].type != 'hidden' &&
		  		alltags[i].readOnly != true &&
		  		alltags[i].disabled != true &&
					vatCeapInputMark == alltags[i].name.substr(0, 1)){
			//	alltags[i].tabIndex = vatInputList.length + 1;
					vatInputList.push(alltags[i]);
					/*
						 alert("總數: " + vatInputList.length.toString() + ", " + 
						 			 "序號: " + (vatInputList.length-1).toString() + ", " +
									 "名稱: " + vatInputList[vatInputList.length-1].name + ", " +
									 "唯讀: " + vatInputList[vatInputList.length-1].readOnly + ", " +
									 "停用: " + vatInputList[vatInputList.length-1].disabled + ", " +
									 "型態: " + vatInputList[vatInputList.length-1].type );
					*/			
			} else {
				/*
				if (alltags[i].tabIndex)
					alltags[i].removeAttribute("tabIndex");
				*/	
			}
			
		}
		// 第一個輸入項：vatInputList[0]
		// 最末個輸入項：vatInputList[vatInputList.length-1]
		// vatSetInputFocus();
	}
}


function vatSetInputFocus() {
	if (vatInputList.length > 0 &&
			vatInputList[0].type != 'hidden' &&
		  vatInputList[0].readOnly != true &&
			vatInputList[0].disabled != true) {
		if (vatStackDivPopupId.length > 0){			// 非第零層	
		} else {																// 第零層
		}
		vatInputList[0].focus();							
   	vatInputList[0].select();				
		// alert("第" + vatStackDivPopupId.length + "層" + vatInputList[0].id);		
	}		
}


function vatDivPresentInit(pId){
	vatDivInputListCreate(pId);
}



function vatDivInputDisableAll() {
	var i;
	if (vatInputList instanceof Array) {
		for (i=0; i < vatInputList.length; i++) {
			vatInputList[i].disabled = true;
		}
	}
}

function vatDivInputEnableAll() {
	var i;
	if (vatInputList instanceof Array){
		for (i=0; i < vatInputList.length; i++) {
			vatInputList[i].disabled = false;
		}
	}
}




function vatDivPopup(pPopupId, popTop, popLeft) {
	var popObj  = document.getElementById(pPopupId);
	var backObj, alltags;
	if (popObj){
		// *** backgruond process
		backObj = vatPopupObj;
		if (backObj){
			backObj.filters[0].enabled = true;
			backObj.filters[0].opacity = 0.50 
			vatBackgroundObj = backObj;			
		}
		vatDivInputDisableAll();				
		vatDivStackPush();								// save vatPopupId, vatInputList
		// *** foreground process
		popObj.setCapture(false);					// initial new div for popup
		popObj.style.visibility = "visible";
		if (arguments.length >= 3){
			popObj.style.position = "absolute";
			popObj.style.top  = popTop;
			popObj.style.left	= popLeft;
		}
		vatDivInputListCreate(pPopupId);	// create new Input list		
		// Drag.init(popObj);
	}
}

function vatElementRenew() {
	if (vatPopupObj){
		vatPopupObj.style.visibility = "hidden";
		vatPopupObj.releaseCapture();
		if (vatBackgroundObj){
			vatBackgroundObj.filters[0].enabled = false;			
		}
		vatDivStackPop();						// *** 需要先回存欄位, 再 Enable		
		vatDivInputEnableAll();
		vatSetInputFocus();
	}
}

