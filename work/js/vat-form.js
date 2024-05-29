/***
 *	檔案：vat-form.js 
 *	說明：預設處理函數
 *	修改：Mac
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */

	//*** 變數宣告 vat & vat.form  
	if (typeof vat == 'undefined') {
		vat = function() {};
	}
	vat.form =	{ top : 0, left : 0, width : 0, height : 0, line : 0, 
								beginDivTag		: "vatBeginDiv",		/* 預設第一個 DIV 的 ID 							*/
								CeapInputMark	: "#",							/* 預設 Ceap 系統中可輸入的元素代號 	*/
								currentId			: "",
								
								obj 					: "", 
								backgroundObj	: false
							};
	vat.form.item =	{	currentIndex	:  0,
										current				:  0,						/* 當已經 focus 後, 再把 currentIndex 放到 current */
										currentCheck	: -1,						/* 儲存判斷是否已經可以正確 focus 的欄位 */
										stepMode			: "cycle",			/* cycle, stay, first, last */
										step					:  1,
										canfocus			: true,
										result				: new Array(),	/* 存放每個 form detail 裡面最後一行及最後一欄的元素名稱 */
										list 					: new Array()		/* 存放這個 div 內所有需要控制的輸入元素 Obj */
									};
	vat.form.itemStack = new Array();									/* for popupDiv(), 存放 vat.form.item.list */
	vat.form.formStack = new Array();									/* for popupDiv(), 存放 vat.form.currentId */

/*************************************************
 *	功能：form.item
 *  說明：
 *    		
 */
vat.form.item.createList = function(pDivId){
  var alltags, voItems;
	vat.form.currentId = pDivId;
	vat.form.obj = document.getElementById(vat.form.currentId);
	if (vat.form.obj) {
		vat.form.item.list = new Array();
		voItems = vat.form.obj.getElementsByTagName("FORM");
		if (typeof voItems.length === "number" && voItems.length > 0){
			alltags = vat.form.obj.getElementsByTagName("FORM")[0].elements;
			vat.form.item.appendList(alltags);
			if (vat.form.item.list.length > 0){
				vat.form.item.currentIndex = parseInt(vat.message.bind.getValue("current.item", 0));	//*** 第一個輸入項
				vat.form.item.current 		 = vat.form.item.currentIndex;
				vat.form.item.move(0);
			}
		}	
	}
};

vat.form.item.appendList = function(pTags){
	var debug = false, e1, e2;
	var s1, s2, s2name, x1, x2, x3, x4, x5 , s3name ;
	s2 = (s1 = -1);
	for (i=0; i< pTags.length; i++){
		// readOnly && disabled 都要放在 focus 陣列裡面,  
		if (pTags[i].name && (pTags[i].tagName == "INPUT" || pTags[i].tagName == "SELECT" || pTags[i].tagName == "TEXTAREA") &&
				pTags[i].type != 'hidden' &&
	  		pTags[i].style.display != "none" &&
	  		pTags[i].style.visibility != "hidden" &&
	  		pTags[i].name != "#vatMessage" &&
				vat.form.CeapInputMark == pTags[i].name.substr(0, 1)){
			pTags[i].focusSaved = pTags[i].onfocus; 
			pTags[i].onfocus = function(){vat.form.item.setFocus(this.id) };
			pTags[i].blurSaved = pTags[i].onblur;
			pTags[i].onblur  = function(){ vat.form.item.setBlur(this.id) };
			vat.form.item.list.push(pTags[i]);
			pTags[i].itemIndex = vat.form.item.list.length; 
			x1 = pTags[i].name.indexOf("[", 0);
			if (x1 != -1){
				x2 = pTags[i].name.indexOf("]",0);
				x3 = parseInt(pTags[i].name.substring(x1+1, x2));							//*** subscript
				x4 = pTags[i].name.substring(0, x1);													//*** name
				x5 = pTags[i].name.substring(x2 + 1);													//*** name
				if (x3 == s1){												//*** 同一行
					s2name = x4;												//      記住名稱
					s3name = x5;
				}else if(x3 > s1){										//*** 下一行
					s1 = x3; 														//      記住目前在哪一行
					s2 = s1;														//      記住前一行
					if (s2 > vat.form.line) vat.form.line = s2;
				}else{																//*** 換不同 data table
					vat.form.item.result.push(s2name + "[" + s2.toString() + "]" + s3name);
					s2 = (s1 = -1);
				}
			}
		}
	}
	if (s2 > -1){
		vat.form.item.result.push(s2name + "[" + s2.toString() + "]" + s3name);
		if (s2 > vat.form.line) vat.form.line = s2;
	}
}

vat.form.originalRestore = function(){
  var i;
	if (vat.form.item.list){
		for (i=0; i < vat.form.item.list.length; i++){
			if (typeof vat.form.item.list[i].original == "string" && 
					typeof vat.form.item.list[i].replace == "function"){
				vat.form.item.list[i].value = vat.form.item.list[i].original.replace(/,*/g, "");
			}else{
				vat.form.item.list[i].value = vat.form.item.list[i].original;				
			}				
		}	
	}
}

vat.form.item.appendResult = function(pItemId){
	vat.form.item.result.push(pItemId);
}

vat.form.item.disableAll = function(){
	var i;
	if (vat.form.item.list instanceof Array) {
		for (i=0; i < vat.form.item.list.length; i++) {
			vat.form.item.list[i].disabledSave = vat.form.item.list[i].disabled;
			vat.form.item.list[i].readOnlySave = vat.form.item.list[i].readOnly;
			vat.form.item.list[i].disabled = true;
		}
	}
}
vat.form.item.renewDisableAll = function(){
	var i;
	if (vat.form.item.list instanceof Array) {
		for (i=0; i < vat.form.item.list.length; i++) {
			if (vat.form.item.list[i].disabledSave){
				vat.form.item.list[i].disabled = vat.form.item.list[i].disabledSave;  
				vat.form.item.list[i].removeAttribute("disabledSave");
			}else	
				vat.form.item.list[i].disabled = false;
		}
	}
}

vat.form.item.enableAll = function(){
	var i;
	if (vat.form.item.list instanceof Array){
		for (i=0; i < vat.form.item.list.length; i++) {
			vat.form.item.list[i].disabled = false;
		}
	}
}

vat.form.item.disable = function(pId){
	var i, obj, alltags;
	obj = document.getElementById(pId);
	if (obj){
		// alltags = obj.getElementsByTagName("INPUT");
		alltags = obj.all;
		for (i=0; i< alltags.length; i++){
			alltags[i].disabledSave = alltags[i].disabled;
			alltags[i].readOnlySave = alltags[i].readOnly;
			alltags[i].readOnly = true;
			alltags[i].disabled = true;
		}
	}
}

vat.form.item.enable = function(pId){
	var i, obj, alltags;
	obj = document.getElementById(pId);
	if (obj){
		alltags = obj.all;
		for (i=0; i< alltags.length; i++){
			saveBgColor = obj.getAttribute("saveBgColor");
			if (saveBgColor)
				obj.style.background = saveBgColor;			  	
			alltags[i].readOnly = false;
		  alltags[i].disabled = false;
			if(alltags[i].type == 'checkbox'){
				bonclick = obj.getAttribute("bonclick");
				if (bonclick){
					alltags[i].onclick = bonclick;
					if (alltags[i].indeterminate)
						alltags[i].indeterminate = false;
				}
			}
		}
	}
}

vat.form.item.enableByTagName = function(pId, pTag){
	var i, tagObj, alltags;
	pTag = pTag ? pTag : 'div';
	tagObj = document.getElementsByTagName(pTag);
	for(i=0; i < tagObj.length; i++){
		obj = tagObj[i];
		if (obj.id == pId){
			alltags = obj.all;
			for (j=0; j < alltags.length; j++){
				saveBgColor = obj.getAttribute("saveBgColor");
				if (saveBgColor)
					obj.style.background = saveBgColor;			  	
				alltags[j].readOnly = false;
			  alltags[j].disabled = false;
			}
		}
	}	
}

vat.form.item.readonlyByTagName = function(pId, pTag){
	var i, tagObj, alltags;
	pTag = pTag ? pTag : 'div';
	tagObj = document.getElementsByTagName(pTag);
	for(i=0; i < tagObj.length; i++){
		obj = tagObj[i];
		if (obj.id == pId){
			alltags = obj.all;
			for (j=0; j < alltags.length; j++){
				saveBgColor = obj.getAttribute("saveBgColor");
				if (saveBgColor)
					obj.style.background = saveBgColor;			  	
				alltags[j].readOnly = true;
			  //alltags[j].disabled = true;
			}
		}
	}	
}

vat.form.item.enableByName = function(pName){
	var i, j, obj, nameObj, Obj, alltags;
	nameObj = document.getElementsByName(pName);
	if (nameObj){
		alert('test 1:' + pName + '/' + nameObj.length);
		for(i=0; i < nameObj.length; i++){
			obj = nameObj[i];
			if (obj){
				alert('test 2:' + obj.id + '/' + obj.length);			
				alltags = obj.all;
				for (j=0; j < alltags.length; j++){
	  			alltags[j].readOnly = false;
			  	alltags[j].disabled = false;
					saveBgColor = obj.getAttribute("saveBgColor");
					if (saveBgColor)
						obj.style.background = saveBgColor;			  	
				}
			}
		}
	}
}

vat.form.item.readonly = function(pId){
	var i, obj, alltags;
	obj = document.getElementById(pId);
	if (obj){
		alltags = obj.getElementsByTagName("INPUT");
		for (i=0; i< alltags.length; i++){
			alltags[i].setAttribute("saveBgColor", obj.style.background);
			alltags[i].style.background ="#EBEBEB";
			alltags[i].readOnly = true;			
			if(alltags[i].type == 'image')
				alltags[i].disabled = true;						
			if(alltags[i].type == 'checkbox'){
				alltags[i].bchecked = alltags[i].checked;
				alltags[i].bonclick = alltags[i].onclick;
				if (alltags[i].checked) alltags[i].indeterminate = true;
				alltags[i].onclick  = function(){return false};
			}
		}
		alltags = obj.getElementsByTagName("IMG");
		for (i=0; i< alltags.length; i++){
			alltags[i].readOnly = true;
			alltags[i].disabled = true;
		}
		alltags = obj.getElementsByTagName("SELECT");
		for (i=0; i< alltags.length; i++){
			alltags[i].readOnly = true;
			alltags[i].disabled = true;
		}
		alltags = obj.getElementsByTagName("BUTTON");
		for (i=0; i< alltags.length; i++){
			alltags[i].readOnly = true;
			alltags[i].disabled = true;
		}
	}
}

vat.form.item.hidden = function(pId){
	var i, obj;
	obj = document.getElementById(pId);
	if (obj){
		obj.style.visibility = "hidden";
	}
}


vat.form.item.visible = function(pId){
	var i, obj;
	obj = document.getElementById(pId);
	if (obj){
		obj.style.visibility = "visible";
	}
}

vat.form.item.none = function(pId){
	var i, obj;
	obj = document.getElementById(pId);
	if (obj){
		obj.style.display = "none";
	}
}


vat.form.item.inline = function(pId){
	var i, obj;
	obj = document.getElementById(pId);
	if (obj){
		obj.style.display = "inline";
	}
}

vat.form.item.ajaxTest = function (pSelfId){
	vat.item.currentId = pSelfId;
	vat.ajax.startRequest();
	if (xmlHttp) {
   	// do request
	}	else {
   	alert("您的瀏覽器不支援這個Ajax程式的功能");
	}
}

vat.form.item.setBlur = function vatForm_itemSetBlur(pItemId){
	var obj, saveBgColor;
	obj = document.getElementById(pItemId);
	if (obj){
		saveBgColor = obj.getAttribute("saveBgColor");
		obj.style.backgroundColor = saveBgColor;
		if (typeof obj.original === "string"){
			if (typeof obj.datatype === "string" && obj.datatype === "NUMM"){
				obj.original = obj.value.replace(/,*/g, "");
			}else{
				obj.original = obj.value;
			}
			if (vat){
				/*
				if (vat.item && typeof vat.item.formatNumm === "function"){	
					obj.value = vat.item.formatNumm(obj.value, obj.dec);
				}else if (vat.formD && typeof vat.formD.itemFormatNumm === "function"){
					obj.value = vat.formD.itemFormatNumm(obj.value, obj.dec);
				}
				*/
			}
		}		
		if (typeof vat.jr == 'function' && obj.blurSaved){
			vat.jr(obj.blurSaved);
		}
	}
}


vat.form.item.setFocus = function(pItemId){
	var voItem, saveBgColor, color, saveOnfocus;
	// event.cancelBubble = false;
	voItem = document.getElementById(pItemId);
	if (voItem){
			if (typeof voItem.bcx_color === "string")
				voItem.setAttribute("saveBgColor", voItem.bcx_color);
			else
				voItem.setAttribute("saveBgColor", voItem.style.backgroundColor);
			
			voItem.style.backgroundColor = "#CDF9E8";
			if (voItem.original){
				voItem.value = voItem.original;
			}
			vat.form.item.currentMatch(pItemId);
			vat.form.item.current = vat.form.item.currentIndex;
			if (voItem.focusSaved && typeof vat.jr == 'function'){
				vat.jr(voItem.focusSaved);
			}
			if (voItem.tagName == "INPUT" || voItem.tagName == "TEXTAREA") voItem.select();
	}		
}


vat.form.item.currentMatch = function(pItemId){
	var ok = false, i;
	for (i=0; i < vat.form.item.list.length; i++){
		if (pItemId == vat.form.item.list[i].id){
			if (vat.form.item.currentIndex != i){
				vat.form.item.currentIndex = i;
				vat.message.bind.setValue("current.item", vat.form.item.currentIndex);
			}	
			break;
		}
	}
}


vat.form.item.move = function(pnStep){
	function checkItemCorrect(pnIndex, pnSign){
		var vnSign = pnSign ? 1 : -1;
		if (vbCircuit){
			if(pnIndex < vat.form.item.currentIndex-1){
				// next
			}else if (pnIndex === vat.form.item.currentIndex-1){
				try{ 
					var voItemA = document.getElementById(vat.form.item.list[pnIndex].id);
					voItemA.blur();
					voItemA.focus();
				}catch(ex){
					return ; // must test
				}
				return ;
			}else if(pnIndex >= vat.form.item.currentIndex || 
				pnIndex >= vat.form.item.list.length-1){
				return ; // 已經往下找過一個循環
			}	
		}else{ 
			if (pnIndex < vat.form.item.list.length){
			}else{
				vbCircuit = true;
				pnIndex = 0;
			}	 
		}
		var voItem = document.getElementById(vat.form.item.list[pnIndex].id);
		if (voItem){
			if (voItem.readOnly){ 
				checkItemCorrect(pnIndex + vnSign, vnSign);
			}else{	
				try{
					voItem.focus();
					// if (voItem.tagName == "INPUT" || voItem.tagName == "TEXTAREA") voItem.select();
					vat.message.bind.setValue("current.item", vat.form.item.currentIndex);
				}catch(ex){
					checkItemCorrect(pnIndex + vnSign, vnSign);
				}
			}				
		}	
	}	
	var vbCircuit = false;
	if (vat.form.item.list instanceof Array && vat.form.item.list.length > 0){
		pnStep = (vat.form.item.step = pnStep ? pnStep : vat.form.item.step);
		vat.form.item.currentIndex = ((vat.form.item.currentIndex + pnStep + vat.form.item.list.length) % vat.form.item.list.length)
		checkItemCorrect(vat.form.item.currentIndex, pnStep);
	}	
}


vat.form.keydown = function(){
	var i, ret = false;
	if (event.altKey){
		event.returnValue = false;	// disable ALT key
		vat.debug("user", "請不要按下 alt 鍵");
	}else{
		switch(event.keyCode){
		case 13:	/* enter */
			event.returnValue = false;
			if (event.shiftKey){
				vat.form.item.move(-1);
			}else{
				if (vat.form.item.result.length > 0 && (vat.form.item.list instanceof Array && vat.form.item.list.length > 0)){
					for (i=0; i<vat.form.item.result.length; i++){
						if (typeof HTML_backgroundColor === "string"){
						}
						if (vat.form.item.list[vat.form.item.currentIndex].name === vat.form.item.result[i]){
							ret = true;
						}
					}
				}
				for (i=0; i<vat.form.item.list.length; i++){
					if (typeof vat.form.item.list[i].style.backgroundColor === "string"){
						if (typeof vat.form.item.list[i].datatype === "string" && vat.form.item.list[i].datatype !== "DATE"){
							if (vat.form.item.list[i].readOnly && typeof vat.form.item.list[i].HTML_backgroundColor === "string") 
									vat.form.item.list[i].style.backgroundColor = vat.form.item.list[i].HTML_backgroundColor;  
							else 
								vat.form.item.list[i].style.removeAttribute("backgroundColor");
						}
					}	
				}
				if (ret){
					//** currentIndex + 1, 是因為 ceap 到最後再按 enter 會加一行(submit), 所以先加一再存起來, reload 後就會到下一個欄位
					vat.message.bind.setValue("current.item", vat.form.item.currentIndex + 1);
				}else{
					vat.form.item.move(+1);
				}
			}	
			event.returnValue = ret;
			break;
		case 8:	
			if (typeof event.srcElement.id !== "string")
				event.returnValue = false;
			break;
		case 38:	/* line up */
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){
				vat.formD.lineMove(event.srcElement.id, -1);
			}
			break;
		case 40:	/* line down */
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){
				vat.formD.lineMove(event.srcElement.id, +1);			
			}	
			break;						
		case 33:	/* page backward */
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){		
				vat.formD.pageBackward();
			}	
			break;						
		case 34:	/* page forward */
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){			
				vat.formD.pageForward();
			}	
			break;
		case 9:	/* tab disable */
			event.returnValue = false;
			break;
		case 27:	/* esc disable */
			event.returnValue = false;
			break;
		default:
			break;			
		}
	}
}

vat.form.keypress = function(){
	var vsType = event.srcElement.getAttribute("datatype");
	if (typeof vsType === "string"){
		switch(vsType){
		case "NUMB":	// '0'~'9', backspace, del
		case "NUMM":
			if (event.keyCode > 57 && event.keyCode !== 190){ 
				event.returnValue = false; 
			}	
			break;
		default:
			var vsMark = event.srcElement.getAttribute("template");
			if (typeof vsMark == "string"){	
				// Get start position of selection in INPUT element
				if(document.selection){	// IE black magic
					var selStart=Math.abs(document.selection.createRange().moveStart("character",-1000000));
				}else if(typeof(this.field.selectionStart)!="undefined"){
					var selStart=this.field.selectionStart;
					if(selStart==2147483647){
						selStart=0;
					}
				}
				vsToken = vsMark.charAt(selStart);
				if (event.KeyCode === 8 && selStart == 0){
					event.returnValue = false;
				}else	if (typeof(vsToken) == "string"){
					switch(vsToken){
					case "9":
						if(event.keyCode <= 57 && event.keyCode >= 48){
							// '0'~'9' 
						}else{
							if(event.keyCode !== 190){
								event.returnValue = false;	// non or del, backspace
							}
						}
						break;				
					case "A":
						if (event.keyCode >= 97 && event.keyCode <= 122){
							event.keyCode -= 32;	// 'a'~'z'
						}else if(event.keyCode >= 65 && event.keyCode <= 90){
							// 'A'~'Z'
						}else{
							if(event.keyCode !== 190){
								event.returnValue = false;	// non or del, backspace
							}
						}
						break;												
					case "C":
						if (event.keyCode >= 97 && event.keyCode <= 122){
							event.keyCode -= 32;	// 'a'~'z'
						}else if(event.keyCode >= 65 && event.keyCode <= 90){
							// 'A'~'Z'
						}else if(event.keyCode <= 57 && event.keyCode >= 48){
							// '0'~'9' 
						}else{
							if(event.keyCode !== 190){
								event.returnValue = false;	// non or del, backspace
							}
						}
						break;
					case "X":
						break;						
					default:
						break;	
					}
				}
			}
			break;
		}
	}		
}


vat.form.isSpecialKey = function(charCode){
	return (
		(
			newChar == null &&
			charCode != 8 &&
			charCode != 46
		) ||
		charCode == 9		|| // tab
		charCode == 13	|| // enter
		charCode == 16	|| // shift
		charCode == 17	|| // ctrl
		charCode == 18	|| // alt
		charCode == 20	|| // caps lock
		charCode == 27	|| // escape
		charCode == 33	|| // page up
		charCode == 34	|| // page down
		charCode == 35	|| // home
		charCode == 36	|| // end
		charCode == 37	|| // left arrow
		charCode == 38	|| // up arrow
		charCode == 39	|| // right arrow
		charCode == 40	|| // down arrow
		charCode == 45	|| // insert
		charCode == 144	|| // num lock
		charCode > 256 		 // Safari strange bug
	);
};
/*************************************************
 *	功能：Form
 *  說明：
 *    		
 */
vat.form.complete = function(pnId){
	var voForm, vnId;
	if (pnId) vat.form.beginDivTag = pnId;
	vat.form.item.createList(vat.form.beginDivTag);	
	voForm = document.getElementById(vat.form.beginDivTag);
	if (voForm){
		voForm.onkeypress = function(){vat.form.keypress()};
		voForm.onkeydown  = function(){vat.form.keydown()};
		/*
		window.onbeforeunload = function(){
		 if (confirm("Are you sure?")){
		 	window.opener=null;
		 	window.open('','_self');
		 	window.close(); 
		 }else{
		 	event.returnValue = 'Check?'
		 } 
		};
		*/
		voForm.style.height = Math.max(680, 320 + vat.form.line * 32);
		if (typeof vat.block === "object" && vat.block.$box instanceof Array){
			for (vnId = 0; vnId < vat.block.$box.length; vnId++){
				if (vat.block.$box[vnId] && typeof vat.block.$box[vnId].closeService === "function")
					vat.$jst(vat.block.$box[vnId].closeService);
			}
		}
	}		
}


vat.form.closed = function (){
}

vat.form.popupDiv = function(psId, pnTop, pnLeft, pbSaved) {
	var voCurrentForm;
	var voNewForm = document.getElementById(psId);
	if (voNewForm){
		voCurrentForm = document.getElementById(vat.form.currentId);
		if (voCurrentForm){
			// *** backgruond process
			voCurrentForm.filters[0].enabled = true;
			voCurrentForm.filters[0].opacity = 0.50 
			vat.form.backgroundObj = voCurrentForm;
			//vat.form.item.disableAll();
			vat.form.itemStack.push(vat.form.item.list);	// save vat.form.item.list
			vat.form.formStack.push(vat.form.currentId);	// save vat.form.currentId	
		}
		// *** foreground process
		voNewForm.setCapture(false);					// initial new div for popup
		voNewForm.style.visibility = "visible";
		if (arguments.length >= 3){						// 如果需要定座標
			voNewForm.style.position = "absolute";
			voNewForm.style.top  = pnTop;
			voNewForm.style.left = pnLeft;
		}
		vat.form.item.createList(psId);	// create new Input list		
		// Drag.init(voNewForm);
	}
}


vat.form.renewDiv = function(){
	var voCurrentForm = document.getElementById(vat.form.currentId);
	if (voCurrentForm){
		voCurrentForm.style.visibility = "hidden";
		voCurrentForm.releaseCapture();
		if (vat.form.backgroundObj){
			vat.form.backgroundObj.filters[0].enabled = false;			
		}
		// *** 需要先回存欄位, 再 Enable		
		vat.form.item.list = vat.form.itemStack.pop();
		vat.form.currentId = vat.form.formStack.pop();
		// vat.form.item.renewDisableAll(); // 要重新思考, 避免頁面上欄位 diaable 後, 原先屬性遺失
		vat.form.item.currentIndex = vat.message.bind.getValue("current.item", 0);
		vat.form.item.move(0);
	}
}

