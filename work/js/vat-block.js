/***
 *	檔案：vat-block.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */
// 變數宣告 vat & vat.block, vat.block.page
	if (typeof vat === "undefined"){vat = function(){};}

vat.block =	{};
vat.block.$box = [];
vat.block.$current = 0;
vat.block.$submitMaskId = "";

// bean
vat.bean = function vatBean(psProperty, pbFast){
	function nest(poBean, psProperty){
		var vsProperty, vxResult;
		if (poBean !== null)
			if (typeof poBean[psProperty] !== "undefined"){
				vxResult = poBean[psProperty];
			}else{
				for (vsProperty in poBean){
					if (typeof poBean[vsProperty] === "object"){
						vxResult = nest(poBean[vsProperty], psProperty);
						if (typeof vxResult !== "undefined") break;
					}
				}
			}
		return vxResult;
	}
	if(!arguments.length){
		return vat.bean.$box;
	}else{
		if (typeof pbFast === "boolean" &&
				typeof vat.bean.$box.vatBeanOther === "object" &&
				typeof vat.bean.$box.vatBeanOther.form === "object"){
			var voResult = nest(vat.bean.$box.vatBeanOther.form, psProperty);
			if (typeof voResult === "undefined")
				return nest(vat.bean.$box, psProperty);
			else
				return voResult;
		}else
			return nest(vat.bean.$box, psProperty);
	}
};
vat.bean.$box = {};
vat.bean.$ready = false;
vat.bean.set = function vatBean_set(psProperty, pxValue){
	if(psProperty !== null){
		vat.bean.$box[psProperty] = pxValue;
	}
};


vat.bean.nest = function vatBean_nest(poBean, poEvent){
	function nest(poBean){
		for (var vsProperty in poBean){
			if (typeof poBean[vsProperty] === "string"){
				if (typeof poEvent === "function"){
					poBean[vsProperty] = poEvent(poBean[vsProperty]);
				}else{
					poBean[vsProperty] = poEvent;
				}
			}else if (typeof poBean[vsProperty] === "object" && poBean !== null){
				nest(poBean[vsProperty]);
			}
		}
	}
	nest(poBean);
}

vat.bean.sets = function vatBean_sets(poBean){
	if (typeof poBean === "object" && poBean !== null){
		vat.bean.nest(poBean, function(poThat){return poThat;});
		for (var property in poBean){
			if (typeof vat.bean.$box[property] === "undefined")
				vat.bean.$box[property] = (poBean[property] instanceof Array ? [] : {});
			vat.$extend(vat.bean.$box[property], poBean[property]);
		}
	}
};

vat.bean.toString = function vatBean_toString(psBeanName, poBean){
	var vsData = psBeanName, vsItem = "", voItem;
	function next(poBean, pf_call){
		for (var property in poBean)
			if (typeof poBean[property] === "object")
				next(poBean);
			else
				merge(property, poBean[property]);
	}
	function merge(psProperty, pxValue, pbComma){
		// 加上轉換控制碼
		vsData += ("'" + psProperty + "':'" + vat.utils.escape(pxValue) + "'" + (pbComma ? "," : ""));
	}
	if (poBean instanceof Array){
		vsData += "{";
		for(i=0; i < poBean.length; i++){
			voItem = document.getElementById(poBean[i]);
			merge(voItem.name, voItem.value, i < poBean.length);
		}
		vsData += "}";
	}else{
		vsData += vat.utils.escape(JSON.stringify(poBean, function (key, value) {
    						if (typeof value === "number" && !isFinite(value)) {
        					return String(value);
    						}if (typeof value === "string"){
    							// if (value.indexOf('"') !== -1)
    							return value;
    						}
								return value;
							}));
	}
	return vsData;
};
vat.bean.init = function vatBean_init(psService, poOpt){
	vat.block.submit(psService, vat.$extend(vat.$default(poOpt, {}), {asyn: false, id: 'vat.bean.init'}));
};

// block
vat.block.getCurrent = function(){
	return vat.block.$current;
}

vat.block.submit = function vatBlock_submit(psService, poOpt){
	var st = new Date(), voNowBean, voBean, vsProcessString, vsMaskId = "";
	function message(poMsg){
		function exec(poExec){
			if (typeof poExec === "object" && typeof poExec.cmd === "string"){
				switch(poExec.cmd){
					case "WIN-CLOSE" :
						window.top.close();
						return false;
					case "HANDLER" :
						executeCommandHandler(poExec.parameters[0], poExec.parameters[1]);
						break;
					case "FUNCTION" :
						vat.$jsr(poExec.parameters[0], {that:poExec.parameters[1]});
						break;
				}
			}
		}
		if (typeof poMsg === "object" && typeof poMsg.type === "string"){	// 有回傳訊息才處理
			if (poMsg.before)	exec(poMsg.before);
			switch(poMsg.type){
				case "ALERT":
					window.alert(poMsg.message);
					exec(poMsg.ok);
					break;
				case "CONFIRM":
					vbReply = window.confirm(poMsg.message);
					if (vbReply) exec(poMsg.ok);
								  else exec(poMsg.cancel);
					break;
				case "PICKER":
					var winParent = window.opener.document;
					winParent.getElementById("vatGridRefreshImg#"+txt).click();
					exec(poMsg.ok);
					break;
			}
		}
	}
	function openMask(psId, psSrc){
		if (document.getElementById(psId) === null){
			var	voMaskDiv = document.createElement("DIV");
		  voMaskDiv.id  = psId;
			voMaskDiv.style.position = "absolute";
			voMaskDiv.style.top = 0;
			voMaskDiv.style.left = 0;
			// voMaskDiv.style.zIndex = 0;
		  voMaskDiv.width = 1024;
		  voMaskDiv.height = 768;
		  // voMaskDiv.style.display = "none";
			var voMaskImg = document.createElement("IMG");
			voMaskImg.id  = psId + "IMG"
			voMaskImg.style.position = "relative";
			voMaskImg.src = psSrc;
			voMaskImg.style.top = 16;
			voMaskImg.style.left = 18;
	  	voMaskImg.width = 30;
	  	voMaskImg.height = 30;
	  	voMaskImg.style.visibility = "hidden";
			voMaskDiv.appendChild(voMaskImg);
			document.body.appendChild(voMaskDiv);
		}
		vat.block.popupStart(psId, 0, 0);
		return psId;
	}
	function closeMask(){
		var vnTimerId = window.setInterval(function(){
											window.clearInterval(vnTimerId);
											vat.block.popupClose();
										}, 100);
		var et = new Date();
		vat.bean.set("submitTime", et.valueOf() - st.valueOf());
	}
	// start submit process
	voBean = {};
	voNowBean = vat.bean();
	if (typeof voNowBean.vatBeanPicker !== "object") voNowBean.vatBeanPicker = {};
	if (poOpt){
		if (poOpt && poOpt.id !== 'vat.bean.init') openMask("vatBlockUI", "./images/loading.gif");
		if (poOpt.saved)
			for (var i=0; i<vat.block.$box.length; i++)
				if (typeof vat.block.$box[i] === "object" && vat.block.$box[i].layout === "grid:ajax")
					vat.block.pageSearch(i); // 儲存最後的選擇
 		if (poOpt.picker === true && typeof poOpt.blockId === "number"){
			voNowBean.vatBeanPicker.timeScope = vat.block.$box[poOpt.blockId].timeScope;
			voNowBean.vatBeanPicker.searchKey = vat.block.$box[poOpt.blockId].searchKey;
		}
	}
	if (typeof psService === "function"){
		vsProcessString = psService();
	}else{
		vsProcessString = psService;
	}
	if (poOpt){
		if (poOpt.bind  === true) voBean.vatBeanFormBind = vat.item.$bind.toBean();
		if (poOpt.link  === true) voBean.vatBeanFormLink = vat.item.$link.toBean();
		if (poOpt.other === true && typeof voNowBean.vatBeanOther !== "undefined")
			voBean.vatBeanOther  = voNowBean.vatBeanOther;
		if (poOpt.picker === true)
			voBean.vatBeanPicker = voNowBean.vatBeanPicker;
		vsProcessString += vat.bean.toString("&vatBean=", voBean);
	}
	vat.ajax.XHRequest(
		{ sId: vat.$default(poOpt.id, "vat.block.submit"),
			asyn: (poOpt ? (typeof poOpt.asyn == "boolean" ? poOpt.asyn : true) : true),
			post: vsProcessString,
			lose: function vatAjaxSubmitLose(){
							if (poOpt & typeof poOpt.funcFailure === "function") poOpt.funcFailure();

 								if (poOpt && poOpt.isPicker === true){
									var voBtn = document.getElementsByName("#vatPickerId");
									if (voBtn !== null && typeof window.opener !== "undefined"){
										var winParent = window.opener.document;
										// var voPicker = winParent.getElementById(voBtn[0].value);
										var voPickerClick = winParent.getElementById(voBtn[0].value+"#pick");	// check
										if (voPickerClick !== null){
											voNowBean = vat.bean();
											if (typeof voBean.vatBeanPicker !== "undefined"){
												vat.$extend(voNowBean.vatBeanPicker, voBean.vatBeanPicker);
												voPickerClick.setAttribute("value", vat.bean.toString("", voNowBean.vatBeanPicker));
											}
											voPickerClick.click();
											if (typeof poOpt.close == "boolean" ? poOpt.close : true)	window.close();
										}
									}
								}

							if (poOpt && poOpt.id !== 'vat.bean.init') closeMask();
						},
			find: function vatAjaxSubmitFind(oXHR){
 							var vsBean, boBean, voMsg;
 							vsBean = vat.ajax.getValue("vatBean", oXHR.responseText);
 							if (typeof vsBean === "string" && vsBean.length > 1){
								voBean = eval('('+vsBean+')');
 								// voBean = JSON.parse(vsBean);
 								vat.bean.sets(voBean);
								if (poOpt & typeof poOpt.funcBeforeMessage === "function")
									poOpt.funcBeforeMessage();
								voMsg = vat.bean("vatMessage");
 								if (typeof voMsg === "object"){
 									vat.bean().vatBeanOther.vatMessage = "";
 									message(voMsg);
 								}
 								if (poOpt && poOpt.id !== 'vat.bean.init') closeMask();
 								if (poOpt && poOpt.isPicker === true){
									var voBtn = document.getElementsByName("#vatPickerId");
									if (voBtn !== null && typeof window.opener !== "undefined"){
										var winParent = window.opener.document;
										// var voPicker = winParent.getElementById(voBtn[0].value);
										var voPickerClick = winParent.getElementById(voBtn[0].value+"#pick");	// check
										if (voPickerClick !== null){
											voNowBean = vat.bean();
											if (typeof voBean.vatBeanPicker !== "undefined"){
												vat.$extend(voNowBean.vatBeanPicker, voBean.vatBeanPicker);
												voPickerClick.setAttribute("value", vat.bean.toString("", voNowBean.vatBeanPicker));
											}
											voPickerClick.click();
											if (typeof poOpt.close == "boolean" ? poOpt.close : true)	window.close();
										}
									}
								}
								if (poOpt && typeof poOpt.funcSuccess === "function") poOpt.funcSuccess();
 							}else
 								if (poOpt && poOpt.id !== 'vat.bean.init') closeMask();
						}
		});
};
// 不知道做什麼用, 給誰呼叫
vat.block.picker = function vatBlock_picker(psService, poOpt){
	for (var i=0; i<vat.block.$box.length; i++)	vat.block.pageSearch(i); // 儲存最後的選擇
									if (poOpt & typeof poOpt.funcSuccess === "function") poOpt.funcSuccess();
	var vsProcessString = vat.$jsr(psService, {defaultResult:""});
}

vat.block.create = function vatBlock_create(pnId, poOpt){
	var vnPtr, vnExtendEnd, voRow, voNow;
	pnId = (typeof pnId === "undefined") ? vat.block.$box.length+1 : pnId;
	if (typeof vat.block.$box[pnId] !== "object"){
		vat.block.$box[pnId] = {top : 0, left : 0, width : 0, height : 0,
			itemName	: new Array(),
			itemDesc	: new Array(),
			firstIndex: 1,
			lastIndex : 0,
			dataCount : 0,
			saveAudit	: false,	// *** 重要, grid 第一次 save 的時候要鎖住, 等 binding 完畢後才能再次更新, 否則會重複新增
			bindAudit : false,
			loadAudit : false,
			hasModify : false,
			hasInsert : false,
			hasChange : false
		};
	}
	if(typeof poOpt === "object"){
		if (typeof poOpt.extend === "number"){
			var vnPtr, voRow, vsProperty, voItems, voItem, i, x1, x2, x3;
			for(vnPtr=1; vnPtr < poOpt.extend; vnPtr++){
				voRow = vat.$copy(poOpt.rows[1]);
				for (i=0; i < voRow.cols.length; i++){
					voItems = voRow.cols[i].items;
					for (j=0; j < voItems.length; j++){
						voItem = voItems[j];
						for (vsProperty in voItem){
							if (typeof voItem[vsProperty] === "string"){
								x1 = voItem[vsProperty].indexOf("[", 0);
								if (x1 != -1){
									x2 = voItem[vsProperty].indexOf("]",0);
									x3 = parseInt(voItem[vsProperty].substring(x1+1, x2));
									voItem[vsProperty] = voItem[vsProperty].substring(0, x1 - 1) + "[" + vnPtr + "]";
								}
							}
						}
					}
				}
				poOpt.rows.push(voRow);
			}
		}
		voNow = new Date();
		vat.$extend(vat.block.$box[pnId], poOpt);
		vat.block.$box[pnId].layout = vat.$default(poOpt.layout, "free"); // free, grid:ajax, grid:bean
		vat.block.$box[pnId].binder = vat.$default(poOpt.binder, "ajax");
		vat.block.$box[pnId].lines  = vat.$default(poOpt.pageSize, 12); // 預設每頁行數
		vat.block.$box[pnId].canGridDelete	=	poOpt.canGridDelete ? true : false;
		vat.block.$box[pnId].canGridAppend	=	poOpt.canGridAppend ? true : false;
		vat.block.$box[pnId].canGridModify	=	poOpt.canGridModify ? true : false;
		vat.block.$box[pnId].timeScope			= voNow.getTime().toString();
		vat.block.$box[pnId].selectionType	= vat.$default(poOpt.selectionType, "NONE");
		vat.block.$box[pnId].indexType			= vat.$default(poOpt.indexType, "NONE");
		vat.block.$box[pnId].searchKey			= vat.$default(poOpt.searchKey, []);
		vat.block.$box[pnId].gridData 			= [];
		switch(vat.block.$box[pnId].layout){
		case "grid:ajax":
			break;
		default:
			vat.block.layout(pnId, !(typeof poOpt.generate === "boolean" && !poOpt.generate));
			break;
		}
	}
};

vat.block.getGridData = function vatBlock_getGridData(pnId){
	return vat.block.$box[pnId].gridData;
}

vat.block.getValue = function vatBlock_getValue(pnId, psProperty){
	if (typeof psProperty === "string" && vat.block.$box[pnId][psProperty] !== "undefined")
		return vat.block.$box[pnId][psProperty];
};

vat.block.layout = function vatBlock_layout(pnId, pbGenerate){
	var vsHtml="", vnCount = 0, voItem, vsId;
	function cols(paCols, pnDeep, pfcall){
		var j, k;
		if(paCols instanceof Array){
			for(j=0; j<paCols.length; j++){
				vsHtml = "<td "+vat.$default(paCols[j].td, paCols[j].col_style, " ")+">";
				if (pbGenerate)	document.write(vsHtml);
				if(paCols[j].items instanceof Array){
					for (k=0; k<paCols[j].items.length; k++){
						pfcall.call(paCols[j].items[k], pnDeep, vnCount++);
					}
				}else if(typeof paCols[j].items === "object"){
         	pfcall.call(paCols.items, pnDeep, vnCount++);
				}
				vsHtml = "</td>";
				if (pbGenerate)	document.write(vsHtml);
			}
		}else if(typeof paCols === "object"){
			vsHtml = "<td>";
			if (pbGenerate)	document.write(vsHtml);
			if (paCols.items instanceof Array){
				for (k=0; k<paCols.items.length; k++){
						pfcall.call(paCols.items[k], pnDeep, vnCount++);
				}
			}else if(typeof paCols.items === "object"){
				pfcall.call(paCols.items, pnDeep, vnCount++);
			}else{
				pfcall.call(paCols, pnDeep, vnCount++);
			}
			vsHtml = "</td>";
			if (pbGenerate)	document.write(vsHtml);
		}
	}
	function rows(paRows, pnDeep, pfcall){
		for(var i=0; i<paRows.length; i++){
			if (typeof paRows[i] !== "undefined"){
				if (pbGenerate) document.write("<tr"+vat.$default(paRows[i].tr, paRows[i].row_style, " ")+">");
				cols(paRows[i].cols, i, pfcall);
				if (pbGenerate)	document.write("</tr>");
			}
		}
	}
	function block(paBlock){
		if(typeof paBlock === "object"){
			vsId = vat.$default(vat.block.$box[pnId].id, "");
			vat.tabm.createDivision(vsId);
			vsHtml = "<table "+ vat.$default(paBlock.table, "") + ">";
			if (paBlock.title){
				vsHtml += "<tr><td align='left' rowspan='1' style='background-color: #990000;' valign='center' colspan='30'>"+
					 				"<span class='brownTableTitle'>"+paBlock.title+"</span></td></tr>";
			}
			if (pbGenerate)	document.write(vsHtml);
			vsHtml = "";
			if(paBlock.rows instanceof Array){
				rows(paBlock.rows, 0, function(pnDeep, pnCol){
					var vsItem;
					if (typeof this == "object"){
						if (this.rows){
							block(this);
						}else{
							this.HTML_id = this.name // vat.item.IdMake(pnId, pnDeep, pnCol);
							this.HTML_name = this.name;
							vat.item.verify(this);
							vsItem = vat.item.outHTML(this,	{blockId:pnId, row:pnDeep, col:pnCol, generate:pbGenerate});
						}
					}else{
						vsItem = this;	//*** text
					}
				});
			}
			vsHtml = "</table></div>";
			if (pbGenerate)	{
				document.write(vsHtml);
				voDiv = document.getElementById(vsId);
				// if (voDiv !== null)	vat.drag.init(voDiv);
			}
		}
	}
	// setTimeout
	// vat.$jsr(vat.block.$box[pnId].beginService);
	block(vat.block.$box[pnId]);
	// vat.$jsr(vat.block.$box[pnId].closeService);
};
vat.block.column = function vatBlock_column(psItemName, poOpt){
	var voItem = document.getElementById(psItemName+"#0");
	if (voItem !== null){
		if (poOpt && typeof poOpt.view === "string"){
			if (poOpt.view === "none") vat.item.setGridStyleByName(psItemName, "display", "none");
			voItem.view = poOpt.view;
		}
	}
}


vat.block.columnViewChange = function vatBlock_columnViewChange(pnId, psName, psMode){
	var voTr = document.getElementById(vat.block.pageTrIdMake(pnId, 0));
	var voCols, vnPtr = 0, vnMax = voTr.childNodes.length;
	while (vnPtr < vnMax){
		voCols = voTr.childNodes(vnPtr);
	 	if (voCols.itemName === psName){
	 		voCols.view = (typeof psMode === "string" ? psMode : "none");
	 		return vnPtr;
	 	}
	 	vnPtr++;
	}
	return -1;
}


vat.block.columnTurn = function vatBlock_columnTurn(poTr, pnNow, pnStep, psStat){
	var voCols, vnNow = parseInt(pnNow);
	var vnMax = poTr.childNodes.length;
	var vnMin = pnStep < 0 ? 0 : -1;
	while(vnNow < vnMax && vnNow > vnMin){
		voCols = poTr.childNodes(vnNow);
		if (voCols.view === "shift" || voCols.view === ""){
			vat.item.setGridStyleByName(voCols.itemName, "display", psStat);
			return vnNow;
		}
		vnNow += pnStep;
	}
	return -1;
};
vat.block.columnRight = function vatBlock_columnRight(pnId){
	var voLeft 	= document.getElementById(vat.block.columnHereIdMake(pnId));
	var voRight = document.getElementById(vat.block.columnHereIdMake(pnId)+"End");
	var voTr  	= document.getElementById(vat.block.pageTrIdMake(pnId, 0));
	var vnLeft, vnRight;
	if (voLeft !== null & voRight !== null && voTr !== null &&
		 (vnRight = vat.block.columnTurn(voTr, parseInt(voRight.value)+1, +1, "inline")) !== -1){
		voRight.value = vnRight;
		if ((vnLeft = vat.block.columnTurn(voTr, voLeft.value, +1, "none")) !== -1)
			voLeft.value = vnLeft+1;
	}
};
vat.block.columnLeft = function vatBlock_columnLeft(pnId){
	var voLeft 	= document.getElementById(vat.block.columnHereIdMake(pnId));
	var voRight = document.getElementById(vat.block.columnHereIdMake(pnId)+"End");
	var voTr  	= document.getElementById(vat.block.pageTrIdMake(pnId, 0));
	var vnLeft, vnRight;
	if (voLeft !== null & voRight !== null && voTr !== null &&
		 (vnLeft = vat.block.columnTurn(voTr, parseInt(voLeft.value)-1, -1, "inline")) !== -1){
		voLeft.value = vnLeft;
		if ((vnRight = 	vat.block.columnTurn(voTr, voRight.value, -1, "none")) !== -1)
			voRight.value = vnRight-1;
	}
};
vat.block.setPickAll = function vatBlock_setPickAll(pnId, pxValue){
	var	vsResult, voItem = document.getElementById('pickAll#'+pnId);
	if (voItem !== null){
		vsResult = vat.item.getValue(voItem);
		if (typeof pxValue === "string" && (pxValue === "Y" || pxValue === "N")){
			vat.item.setValue(voItem, pxValue);
		}
		return vsResult;
	}
};

vat.block.lineCx = function vatBlock_lineCx(poThat, pbState){
	if (pbState){
		//poThat.style.border = "4";
		voItems = poThat.getElementsByTagName("TD");
		for (var i=0; i<voItems.length; i++){
			voItems[i].bcx_color = voItems[i].style.backgroundColor;
			voItems[i].style.backgroundColor = "#33FFCC";
			voItem = voItems[i].childNodes;
			for (var j=0; j<voItem.length; j++){
				if (typeof voItem[j].nodeType === "number" && voItem[j].nodeType === 1){
					voItem[j].bcx_color = voItem[j].style.backgroundColor;
					voItem[j].style.backgroundColor = "#33FFCC";
				}
			}
		}
	}else{
		//poThat.style.border = "2";
		voItems = poThat.getElementsByTagName("TD");
		for (var i=0; i<voItems.length; i++){
			if (voItems[i].bcx_color){
				voItems[i].style.backgroundColor = voItems[i].bcx_color;
				voItems[i].removeAttribute("bcx_color");
				voItem = voItems[i].childNodes;
				for (var j=0; j<voItem.length; j++){
					if (typeof voItem[j].nodeType === "number" && voItem[j].nodeType === 1){
						voItem[j].style.backgroundColor = voItem[j].bcx_color;
						voItem[j].removeAttribute("bcx_color");
					}
				}
			}
		}
	}
};

vat.block.pageLayout = function vatBlock_pageLayout(pnId, poOpt){
	var i, j, vnRow, vnCol, vsTH, vsTB1, vsData, vsShift, vsColor, vsDivId, vsPost;
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	vat.block.create(pnId, vat.$extend(vat.$default(poOpt,{}), {layout:"grid:ajax"}));
	vsDivId = vat.tabm.createDivision(vat.$default(poOpt.id, " "));
	vsTB1 =	"<table class='defaultNone' border='0' cellpadding='3' cellspacing='0'>"+
	       	"<tr style='height:300px'>"+
	       	"<td align='left' rowspan='1' valign='center' colspan='1'>"+
				 	"<table class='default'     border='0' cellspacing='2' cellpadding='2'>"+
					"<tr><td width='100%' colspan='"+vat.block.$box[pnId].itemDesc.length+"'><table width='100%'><tr>"+
					"<td align='left' align='left'>"+
					"<IMG src='images/v2/icon_b01.gif'  alt='新增空白頁' align='middle' style='CURSOR:Hand' id='"+vat.block.pageIdMake(pnId)+"BtnAdd' onclick='vat.block.pageAdd("+pnId+")'>&nbsp&nbsp&nbsp"+
					"<IMG src='images/v2/icon_b02.gif'  alt='翻到第一頁' align='middle' style='CURSOR:Hand' onclick='vat.block.pageTop("+pnId+")'>&nbsp"+
					"<IMG src='images/v2/icon_b03.gif'  alt='向前翻一頁' align='middle' style='CURSOR:Hand' onclick='vat.block.pageBackward("+pnId+")'>&nbsp"+"&nbsp&nbsp&nbsp共有資料"+
					"<INPUT id='"+vat.block.pageCountIdMake(pnId)+"' type='TEXT' size='1' style='text-align:right;vertical-align:middle;border:0;' value='0' READONLY>&nbsp頁,&nbsp目前顯示第"+
					"<INPUT id='"+vat.block.pageThereIdMake(pnId)+"' type='TEXT' size='1' style='text-align:right;vertical-align:middle;border:0;' value='1'         >&nbsp頁&nbsp&nbsp&nbsp"+
					"<IMG src='images/v2/icon_b05.gif'  alt='前往指定頁' align='middle' style='CURSOR:Hand' id='vatGridSearchImg#"+pnId+"' onclick='vat.block.pageSearch("+pnId+")'  >&nbsp"+
					"<IMG src='images/v2/icon_b06.gif'  alt='往後翻一頁' align='middle' style='CURSOR:Hand' onclick='vat.block.pageForward("+pnId+")'>&nbsp"+
					"<IMG src='images/v2/icon_b07.gif'	alt='翻到最後頁' align='middle' style='CURSOR:Hand' onclick='vat.block.pageBottom("+pnId+")'>&nbsp"+
					"</td><td align='right'>"+
					"<IMG src='images/24x24/Left.png' id='"+ vat.block.columnHereIdMake(pnId) +"BtnLeft' alt='欄位向前' align='middle' style='CURSOR:Hand;display:none' onclick='vat.block.columnLeft("+pnId+")' >&nbsp"+
					"<INPUT id='"+vat.block.columnHereIdMake(pnId) +"' 		type='HIDDEN' size='6' value='1' style='text-align:right;vertical-align:middle;border:0;'>&nbsp"+
					"<INPUT id='"+vat.block.columnHereIdMake(pnId) +"End' type='HIDDEN' size='6' value='6' style='text-align:right;vertical-align:middle;border:0;'>&nbsp"+
					"<IMG src='images/24x24/Right.png' id='"+ vat.block.columnHereIdMake(pnId) +"BtnRight' alt='欄位向後' align='middle' style='CURSOR:Hand;display:none' onclick='vat.block.columnRight("+pnId+")' >&nbsp"+
					"<IMG id='vatGridRefreshImg#"+pnId+"' src='images/24x24/Refresh.png'  alt='頁面重新整理' align='middle' style='CURSOR:Hand' onclick='vat.block.pageRefresh("+pnId+")' >&nbsp"+
					"<IMG id='vatGridEventImg#"+pnId+"' 	src='images/24x24/Edit.png'  		alt='"+vat.$default(vat.block.$box[pnId].eventDescription, "自訂事件")+"'	align='middle' style='CURSOR:Hand' onclick='vat.block.eventService("+pnId+")' >&nbsp"+
					"</td>"+
					"</tr></table></td></tr>";
	document.write(vsTB1);
	vsTH = 	"<tr id='"+vat.block.pageTrIdMake(pnId, 0)+"' style='width: 100%'>";
	document.write(vsTH);
	var voDiv, voHead, voItem, vbNotSetFreezeLeft = true;
	for(j=0; j<vat.block.$box[pnId].itemDesc.length; j++){
		vsTH = "<th class='brownTableTitle' id='"+vat.block.$box[pnId].itemName[j]+"#0'";
		if (vat.block.$box[pnId].itemDesc[j].HTML_type === "HIDDEN" ||
				vat.block.$box[pnId].itemDesc[j].view === "shift") vsTH += " style='display:none' ";
		vsTH += ">" +	vat.block.$box[pnId].itemDesc[j].desc;
		if (j === 0 && (typeof poOpt.pickAllService === "string" ||
										typeof poOpt.pickAllService === "function")){
			vsTH += "<INPUT id='pickAll#"+pnId+"' type='CHECKBOX'>";
		}
		vsTH +=	"<span id='"+vat.block.$box[pnId].itemName[j]+"#0DIV' style='display:none'>" + vat.block.$box[pnId].itemDesc[j].desc +	"</span></th>";
		document.write(vsTH);
		if (j === 0 && (typeof poOpt.pickAllService === "string" ||
										typeof poOpt.pickAllService === "function")){
			voItem = document.getElementById('pickAll#'+pnId);
			if (voItem !== null){
				// 給 picker 用的 PickAll 選項, 會帶入 timescope, isAllclick, searchkey 加入 otherbean
				vat.$addEvent(voItem, "click", function(x){
					var vsPost;
					vsPost = (typeof poOpt.pickAllService === "function" ? poOpt.pickAllService() : poOpt.pickAllService);
					vsPost += "&vatIsAllClick="+(event.srcElement.checked ? "Y" : "N");
					if (typeof poOpt.blockId !== "undefined"){
						vat.bean().vatBeanOther.timeScope = vat.block.$box[poOpt.blockId].timeScope;
						vat.bean().vatBeanOther.searchKey = vat.block.$box[poOpt.blockId].searchKey;
					}
					vat.bean().vatBeanOther.isAllClick = event.srcElement.checked ? "Y" : "N";
					vat.block.submit(vsPost, {bind: true, other:true, picker:true, funcSuccess:function(){ vat.block.pageRefresh(pnId); /*alert('test xbox ok!!!'+vsPost);*/ }});
				});
			}
		}
		voHead = document.getElementById(vat.block.$box[pnId].itemName[j]+"#0");
		if (voHead !== null){
			voHead.view = vat.block.$box[pnId].itemDesc[j].view;
			voHead.itemName = vat.block.$box[pnId].itemName[j];
			if (voHead.view === ""){
				if (vbNotSetFreezeLeft){
					vbNotSetFreezeLeft = false;
					voLeft = document.getElementById(vat.block.columnHereIdMake(pnId));
					if (voLeft !== null) voLeft.value = j;
					voBtn = document.getElementById(vat.block.columnHereIdMake(pnId)+'BtnLeft');
					if (voBtn !== null) voBtn.style.display = "inline";
					voBtn = document.getElementById(vat.block.columnHereIdMake(pnId)+'BtnRight');
					if (voBtn !== null) voBtn.style.display = "inline";
				}
				voRight = document.getElementById(vat.block.columnHereIdMake(pnId)+"End");
				if (voRight !== null) voRight.value = j;
			}
			// 調整 shift & fixed 的狀態
			vat.$addEvent(voHead, "dblclick", function(poThat){
				if (poThat.view === "shift" || poThat.view === ""){
					vat.item.setStyleByName(poThat.id, "backgroundColor", "green");
					poThat.view = "fixed";
				}else{
					vat.item.setStyleByName(poThat.id, "backgroundColor", "red");
					poThat.view = "";
				}
			});
		}

		voDiv = document.getElementById(vat.block.$box[pnId].itemName[j]+"#0DIV");
		if (voDiv !== null){
			vPos = vat.utils.findPos(voDiv);
			voDiv.style.left = vPos[1];
			voDiv.style.top  = vPos[0];
			voDiv.style.position = "relative";
			voDiv.style.zIndex = 2;
			vat.drag.init(voDiv);
		}else
			alert('err');
	}
	vsTH = "</tr>";
	document.write(vsTH);

	vsShift = false;
	for(i=0; i<vat.block.$box[pnId].lines; i++){
		vnRow = i + 1;
		if (vsShift){
			vsShift = false;
			vsColor = "#efefef";
		}else{
			vsShift = true;
			vsColor = "#dedede";
		}
		vsData = "<tr id='"+vat.block.pageTrIdMake(pnId, vnRow)+"' name='"+vat.block.pageTrNameMake(pnId, vnRow)+"' style='background-color:"+vsColor+"'>";
		document.write(vsData);
		if (typeof poOpt.indicate === "function" || (typeof poOpt.indicate === "boolean" && poOpt.indicate)){
			voTr = document.getElementById(vat.block.pageTrIdMake(pnId, vnRow));
			if (typeof voTr === "object" && voTr !== null){
				vat.$addEvent(voTr, "mouseover", function(e){vat.block.lineCx(e,  true);});
				vat.$addEvent(voTr, "mouseout" , function(e){vat.block.lineCx(e, false);});
				if (typeof poOpt.indicate === "function"){
					vat.$addEvent(voTr, "dblclick" , function(e){
						vat.block.lineCx(e, false);
						poOpt.indicate();
						for (i=0; i<vat.form.item.list.length; i++){
							if (typeof vat.form.item.list[i].style.backgroundColor === "string"){
								if (typeof vat.form.item.list[i].HTML_backgroundColor === "string")
									vat.form.item.list[i].style.backgroundColor = vat.form.item.list[i].HTML_backgroundColor
								else
									vat.form.item.list[i].style.removeAttribute("backgroundColor");
							}
						}
						// if (typeof event.srcElement.blur === "function") event.srcElement.blur();
					});
				}				
			}
			
		}
		
		if(poOpt.cursor==="Y"){
			
			voTr = document.getElementById(vat.block.pageTrIdMake(pnId, vnRow));
			vat.$addEvent(voTr, "click" , function(e){
						
			if(color==""){
				//alert(color);
			}else{
				vat.block.lineCx(color,  false);
			}			
			cursor = vat.item.getGridLine();			
			vat.block.lineCx(e,  true);			
			color = e;						
			ptr = cursor+1;
			
			});
		}

		for(j=0; j<vat.block.$box[pnId].itemDesc.length; j++){
			vnCol = j + 1;
			//*** td
			vsData = "<td id='"+vat.block.$box[pnId].itemName[j]+"#"+vnRow+"' name='"+vat.block.pageTrNameMake(pnId, vnRow)+"' rowspan='1' valign='center' colspan='1' align='center' style='background-color:"+vsColor+"; ";
				if (vat.block.$box[pnId].itemDesc[j].HTML_type === "HIDDEN" ||
						vat.block.$box[pnId].itemDesc[j].view === "shift") vsData += " display:none; ";
				if (vat.block.$box[pnId].itemDesc[j].type === "IDX")	vsData += " padding=0; ";	/* width='20px;'*/
				vsData += "'>";
			document.write(vsData);
			//*** item
			vat.block.$box[pnId].itemDesc[j].HTML_id = vat.item.IdMake(pnId, vnRow, vnCol);
			vat.block.$box[pnId].itemDesc[j].HTML_name = vat.item.nameMake(vat.block.$box[pnId].itemName[j], vnRow);
			vat.block.$box[pnId].itemDesc[j].HTML_backgroundColor = vsColor;
			vsItem = vat.item.outHTML(vat.block.$box[pnId].itemDesc[j],
																{blockId:pnId, modifiable: vat.block.$box[pnId].canGridModify,
																 color:vsColor, row:vnRow, col:vnCol, grid:true, generate:true});
			vsData = "</td>";
			document.write(vsData);
		}
		vsData = "</tr>";
		document.write(vsData);
	}
	vsTB9 = "</table></td></tr></table></div>";
	document.write(vsTB9);
	vat.block.canGridAppend(pnId);		// 最後確認按鈕是否要出現
	if (poOpt && typeof poOpt.gridOverflow === "string"){
		var voDiv = document.getElementById(vsDivId);
		if (voDiv !== null){
			voDiv.style.overflow = poOpt.gridOverflow;
		}
	}
};


/*
 find : ajax request 成功後會被呼叫
 lose : ajax request 失敗後會被呼叫, 沒有設定會判斷是否有訊息

vat.block.pageAdd()

	vat.block.pageDataSave()
	01.saveBeforeAjxService   (需回傳存檔功能的url 字串)
	02.--- 存檔作業 --- 					call vat.block.submit
	03.saveSuccessBefore
		vat.block.pageRefresh()

			vat.block.pageDataLoad()
			04.loadBeforeAjxService (需回傳載入功能的 url 字串)
			05.--- 載入作業 ---					call vat.block.submit
 		 	06.loadSuccessBefore
			07.funcSuccess

				vat.block.pageDataBind()
				08.bindSuccessBefore
				09.--- 繫結作業	---
				10.bindSuccessAfter or .bindFailureAfter

			11.loadSuccessAfter or .loadFailureAfter
			12.funcSuccessAfter

	13.saveSuccessAfter or .saveFailureAfter

	PS: 如果任何一個 Method 內有再呼叫 vat.block.submit 或 vat.ajax.XHRequest(不建議), 所有的執行順序將重新起算
*/
vat.block.pageDataFlow = function vatBlock_pageDataFlow(pnId, psFlag){
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		switch(psFlag){
		case "save:begin":	// 頁面資料存檔開始
			vat.block.$box[pnId].saveAudit = true;
			break;
		case "save:success":// 頁面資料存檔成功
				break;
		case "load:begin":	// 頁面資料開始載入
			vat.block.$box[pnId].loadAudit = true;
			break;
		case "func:success":// 頁面資料存檔成功
			break;
		case "bind:begin":	// 頁面資料開始繫結
			vat.block.$box[pnId].bindAudit = true;
			break;
		case "bind:close":	// 頁面資料完成繫結
			vat.block.$box[pnId].bindAudit = false;
			break;
		case "load:close":	// 頁面重新載入後, 相關的狀態就解除了
			vat.block.$box[pnId].hasInsert = false;
			vat.block.$box[pnId].loadAudit = false;
			vat.block.$box[pnId].bindAudit = false;
			break;
		case "load:closeAfter":// 頁面資料存檔成功
			break;
		case "load:stop":		// 頁面停止載入後, 所有的狀態也會解除
			vat.block.$box[pnId].hasInsert = false;
			vat.block.$box[pnId].saveAudit = false;	// contrl for multi-save
			vat.block.$box[pnId].bindAudit = false;
			vat.block.$box[pnId].loadAudit = false;
			break;
		case "save:close":	// 頁面資料存檔成功
			vat.block.$box[pnId].saveAudit = false;
			break;
		case "save:stop":		// 頁面資料存檔停止
			vat.block.$box[pnId].saveAudit = false;
			vat.debug("user", "【資料存檔錯誤】data grid id:" + pnId);
			break;
		default:
			vat.debug("vat", "error type");
			break;
		}
	}
};
vat.block.pageDataFlowStopAll = function vatBlock_pageDataFlowStopAll(){
	for (var i=0; i<vat.block.$box.length; i++)	vat.block.pageDataFlow(i, "load:stop");
};
vat.block.pageDataSave = function vatBlock_pageDataSave(pnId, poOpt){
	function pageDataBack(pnId){
		var i, j, vnRow, vnCol, voItem
		var vnFirstIndex, vnGridIndex, vaGridData = new Array();
		pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
		if (typeof(vat.block.$box[pnId]) != "undefined"){
			voItem = vat.item.getGridByAxis(pnId, 1, 1);
			if (voItem){
				vnFirstIndex = vat.item.getGridLine(voItem.name);						// 第一個元素的行號
				if (vnFirstIndex == -1){	//   如果有載入過資料, 就不會是 -1
					vat.debug("developer", "【資料載入錯誤】元素名稱: "+voItem.name);
				}else{
					vaGridData = new Array(vat.block.$box[pnId].lines);
					vnGridIndex = vnFirstIndex;
					for(i=0; i<vat.block.$box[pnId].lines; i++){
						vaGridData[vnGridIndex] = new Array(vat.block.$box[pnId].itemDesc.length) ;
						for(j=0; j<vat.block.$box[pnId].itemDesc.length; j++){
							vnRow = i + 1;
							vnCol = j + 1;
							voItem=vat.item.getGridByAxis(pnId, vnRow, vnCol);
							vaGridData[vnGridIndex][j] = vat.item.getValue(voItem);
						}
						vnGridIndex++;
					}
				}
			}else{
				vat.debug("developer", "【頁面元素不在】element id:data grid id:"+pnId);
			}
		}
		return vaGridData;
	}
	var vaGridData, vsGridReturn, vnFirstIndex, voItem, vsPost, vsP1, vsP2, vbResult = false;
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (vat.block.$box[pnId].saveAudit || vat.block.$box[pnId].bindAudit){
		// 未完成繫結或正在儲存中 unbinding or saving....
	}else{
		vat.block.pageDataFlow(pnId, "save:begin");
		vaGridData = pageDataBack(pnId);
		if (vaGridData.length > 0){
			voItem = vat.item.getGridByAxis(pnId, 1, 1);
			vnFirstIndex = vat.item.getGridLine(voItem.name);
			vsGridReturn = vat.utils.twoDArrayToStr(vaGridData, vnFirstIndex);
 			vsPost = vat.$jsr(vat.block.$box[pnId].saveBeforeAjxService, {defaultResult:""})+
 								"&gridRowCount="+vat.block.$box[pnId].lines+
								"&gridLineFirstIndex="+vnFirstIndex+"&gridData="+vsGridReturn;
			if (vat.block.$box[pnId].searchKey && vat.block.$box[pnId].searchKey instanceof Array && vat.block.$box[pnId].searchKey.length > 0)
				vsPost += (vat.item.$bind.toUrlString()+ "&timeScope="+vat.block.$box[pnId].timeScope+"&searchKey="+vat.block.$box[pnId].searchKey.toString());
			if (vat.block.$box[pnId].indexType && vat.block.$box[pnId].indexType !== "NONE")
				vsPost += "&indexType="+vat.block.$box[pnId].indexType;
			if (vat.block.$box[pnId].selectionType && vat.block.$box[pnId].selectionType !== "NONE")
				vsPost += "&selectionType="+vat.block.$box[pnId].selectionType;
				vat.ajax.XHRequest(
				{  sId: "vat.block.pageDataSave",
					post: vsPost,
					asyn: (poOpt ? (typeof poOpt.asyn == "boolean" ? poOpt.asyn : true) : true),
					lose: function vatBlock_pageDataSaveLose(){
									vat.block.pageDataFlow(pnId, "save:stop");	// contrl for multi-save
									vat.$jsr(poOpt && poOpt.saveFailureAfter ? poOpt.saveFailureAfter : vat.block.$box[pnId].saveFailureAfter);
								},
					find: function vatBlock_pageDataSaveFind(oXHR){
									vat.block.pageDataFlow(pnId, "save:success");
									vat.$jsr(poOpt && poOpt.saveSuccessBefore ? poOpt.saveSuccessBefore : vat.block.$box[pnId].saveSuccessBefore);
									if (poOpt && typeof poOpt.refresh === "boolean" && !poOpt.refersh){
										// 除非特別設定, 否則預設都會重新顯示
										vat.block.pageDataFlow(pnId, "save:close");
									}else{
										vat.block.pageRefresh(pnId,	vat.$extend(poOpt, {
											loadSuccessAfterForCloseSave: function(){
													vat.block.pageDataFlow(pnId, "save:close");	// contrl for multi-save
													vat.$jsr(poOpt && poOpt.saveSuccessAfter ? poOpt.saveSuccessAfter : vat.block.$box[pnId].saveSuccessAfter);
													// pageRefresh() 內會先執行 funcSuccess, 所以不用重複執行
												},
											loadFailureAfterForCloseSave: function(){
													vat.block.pageDataFlow(pnId, "save:close");
												}
										}));
									}
								}
				});
			vbResult = true;
		}else{
			vat.block.pageDataFlow(pnId, "save:stop");	// contrl for multi-save
			vat.$jsr(poOpt && poOpt.saveFailureAfter ? poOpt.saveFailureAfter : vat.block.$box[pnId].saveFailureAfter);
		}
	}
	return vbResult;
};

vat.block.pageDataLoad = function vatBlock_pageDataLoad(pnId, pnThisPageNo, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (vat.block.$box[pnId].loadAudit){
		// 正在下載中 loading....
		vat.$jsr(poOpt && poOpt.loadFailureAfterForCloseSave ? poOpt.loadFailureAfterForCloseSave : vat.block.$box[pnId].loadFailureAfterForCloseSave);
	}else{
		vat.block.pageDataFlow(pnId, "load:begin");
		var vsPost, vaGridNameArray = null, vaGridDataArray = new Array();
		vsPost = vat.$jsr(vat.block.$box[pnId].loadBeforeAjxService, {defaultResult:""})+"&startPage="+pnThisPageNo+"&pageSize="+vat.block.$box[pnId].lines;
		if (vat.block.$box[pnId].searchKey && vat.block.$box[pnId].searchKey instanceof Array && vat.block.$box[pnId].searchKey.length > 0)
			vsPost += (vat.item.$bind.toUrlString()+ "&timeScope="+vat.block.$box[pnId].timeScope+"&searchKey="+vat.block.$box[pnId].searchKey.toString());
		if (vat.block.$box[pnId].indexType && vat.block.$box[pnId].indexType !== "NONE")
			vsPost += "&indexType="+vat.block.$box[pnId].indexType;
		if (vat.block.$box[pnId].selectionType && vat.block.$box[pnId].selectionType !== "NONE")
			vsPost += "&selectionType="+vat.block.$box[pnId].selectionType;
		vat.ajax.XHRequest(
		 { sId: "vat.block.pageDataLoad",
			post: vsPost,
			asyn: (poOpt ? (typeof poOpt.asyn == "boolean" ? poOpt.asyn : true) : true),
			lose: function vatBlock_pageDataLoadLose(){
							vat.block.pageDataFlow(pnId, "load:stop");	// 回歸初始狀態
							vat.$jsr(poOpt && poOpt.loadFailureAfter ? poOpt.loadFailureAfter : vat.block.$box[pnId].loadFailureAfter);
						},
		 	find: function vatBlock_pageDataLoadFind(oXHR){
							vat.block.$box[pnId].firstIndex = parseInt(vat.ajax.getValue("gridLineFirstIndex", oXHR.responseText));
							vat.block.$box[pnId].lastIndex  = parseInt(vat.ajax.getValue("gridLineMaxIndex"  , oXHR.responseText));
							vat.block.$box[pnId].dataCount  = parseInt(vat.ajax.getValue("gridRowCount"      , oXHR.responseText));
							if (typeof vat.block.$box[pnId].dataCount  === "number" && vat.block.$box[pnId].dataCount >= 0 &&
									typeof vat.block.$box[pnId].firstIndex === "number" &&
									typeof vat.block.$box[pnId].lastIndex  === "number" && vat.block.$box[pnId].lastIndex > 0){
								vaGridNameArray = vat.utils.strToArray(vat.ajax.getValue("fieldNames", oXHR.responseText));
								vaGridDataArray = vat.utils.strTwoDArray(vat.ajax.getValue("gridData", oXHR.responseText),
																												 vat.block.$box[pnId].firstIndex, vaGridNameArray.length,
																												 vat.block.$box[pnId].dataCount);
								vat.block.$box[pnId].gridData = vaGridDataArray;
								var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
								if (voPageCount){
									if (!(poOpt && poOpt.noPageCount)){
										voPageCount.value = Math.ceil(vat.block.$box[pnId].lastIndex/vat.block.$box[pnId].lines);
									}
									var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
									if (voPageThere){
										voPageThere.value = pnThisPageNo;
										vat.block.pageDataFlow(pnId, "load:success");
										vat.$jsr(poOpt && poOpt.loadSuccessBefore ? poOpt.loadSuccessBefore : vat.block.$box[pnId].loadSuccessBefore);
										vat.block.pageDataFlow(pnId, "func:success");
										vat.$jsr(poOpt && poOpt.funcSuccess ? poOpt.funcSuccess : vat.block.$box[pnId].funcSuccess);
										if (poOpt && poOpt.noBind ? false : true){
											vat.block.pageDataBind(pnId, vat.block.$box[pnId].firstIndex, vat.block.$box[pnId].dataCount, vaGridDataArray, vaGridNameArray, poOpt);
										}
									}else{
										vat.debug("vat", "【頁面元素不在】元素代號:"+vat.block.pageThereIdMake(pnId));
									}
								}else{
									vat.debug("vat", "【頁面元素不在】元素代號:"+vat.block.pageCountIdMake(pnId));
								}
								vat.block.pageDataFlow(pnId, "load:close");	// 回歸初始狀態; 頁面 reload, 就解除新增的狀態
								vat.$jsr(poOpt && poOpt.loadSuccessAfter ? poOpt.loadSuccessAfter : vat.block.$box[pnId].loadSuccessAfter);
								vat.block.pageDataFlow(pnId, "load:closeAfter");
								vat.$jsr(poOpt && poOpt.loadSuccessAfterForCloseSave ? poOpt.loadSuccessAfterForCloseSave : vat.block.$box[pnId].loadSuccessAfterForCloseSave);
								vat.$jsr(poOpt && poOpt.funcSuccessAfter ? poOpt.funcSuccessAfter : vat.block.$box[pnId].funcSuccessAfter);
							}else{
								vat.block.pageDataFlow(pnId, "load:stop");	// 回歸初始狀態
								vat.$jsr(poOpt && poOpt.loadFailureAfter ? poOpt.loadFailureAfter : vat.block.$box[pnId].loadFailureAfter);
								vat.debug("developer", "【後端回傳錯誤】gridRowCount, firstIndex 或 lastIndex 不正確");
							}
						}});
	}
	return vaGridDataArray;
};

vat.block.getGridObject = function(pnId){
	return vat.block.$box[pnId];
}

vat.block.pageDataBind = function vatBlock_pageDataBind(pnId, pnGridDataIndex, pnGridDataCount, paGridData, paGridName, poOpt){
	var voItem, vxValue, vbReadOnly, vbReadOnlyNew, vbReadOnlyOld;
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	vat.block.pageDataFlow(pnId, "bind:begin");	// 不控制繫結 binding....
	vat.$jsr(poOpt && poOpt.bindSuccessBefore ? poOpt.bindSuccessBefore : vat.block.$box[pnId].bindSuccessBefore);
	if (typeof(vat.block.$box[pnId]) !== "undefined" && typeof pnGridDataIndex === "number" && typeof pnGridDataCount === "number"){
		if (!vat.block.$box[pnId].canGridModify){
			voItem = document.getElementById("vatFD{2}pageThere");
			if (voItem){
				try{
					voItem.blur();
					voItem.focus();
				}catch(ex){
				}	
			}	
		}		
		for(var i=0, vnRow; i<vat.block.$box[pnId].lines; i++){
			for(var j=0, vnCol; j<vat.block.$box[pnId].itemDesc.length; j++){
				vnRow = i + 1;
				vnCol = j + 1;
				voItem = vat.item.getGridByAxis(pnId, vnRow, vnCol);
				vxValue = "";
				if (voItem){
					if ((pnGridDataCount === 0) ||																						// 如果無傳回筆數,
							(i >= pnGridDataCount)){																							// 或筆數不滿一頁
						vxValue = vat.item.getDefaultValue(vat.block.$box[pnId].itemDesc[j]);		// 填入初始值的空白行
						vbReadOnly = vat.block.$box[pnId].canGridAppend ? false : true;					// 空白處的唯讀屬性
					}else{
						if (paGridData instanceof Array && // 檢核傳回的資料是否為陣列內容
								paGridData[pnGridDataIndex] && j < paGridData[pnGridDataIndex].length){
							vxValue = paGridData[pnGridDataIndex][j];		// 並以傳回的'資料名稱'陣列數控制回填內容
							vbReadOnly = vat.block.$box[pnId].canGridModify ? false : true;				// 資料欄的唯讀屬性
						}else{
							vxValue = vat.item.getDefaultValue(vat.block.$box[pnId].itemDesc[j]);	// 回傳資料不足, 用初始值補滿空缺
							vbReadOnly = vat.block.$box[pnId].canGridAppend ? false : true;				// 空白處的唯讀屬性
						}
					}
					//*** start of dynamic change readOnly for canGridModify/canGridAppend check
						if (vbReadOnly){
							vbReadOnlyNew = true;
						}else{
							if (typeof voItem.mode_readOnly === "boolean"){
								vbReadOnlyNew = voItem.mode_readOnly;
							}else if (typeof voItem.mode === "string" && voItem.mode === "READONLY"){
								vbReadOnlyNew = true;
							}else{
								vbReadOnlyNew = false;
							}
						}
						vbReadOnlyOld = typeof voItem.readOnly === "boolean" ? voItem.readOnly : false;
						if (vbReadOnlyNew !== vbReadOnlyOld){
							vat.item.setReadOnlyByObject(voItem, vbReadOnlyNew, true);
						}
						// end of dynamic change readOnly
						vat.item.setValue(voItem, vxValue, {mode : "pageDataBind"});
					}else{
						vat.debug("developer", "【頁面元素不在】element id:" + vat.item.IdMake(pnId, vnRow, vnCol));
					}
			}
			pnGridDataIndex++;
		}
		vat.block.pageDataFlow(pnId, "bind:close");
		vat.$jsr(poOpt && poOpt.bindSuccessAfter ? poOpt.bindSuccessAfter : vat.block.$box[pnId].bindSuccessAfter);
	}else{
		vat.block.pageDataFlow(pnId, "bind:stop");
		vat.$jsr(poOpt && poOpt.bindFailureAfter ? poOpt.bindFailureAfter : vat.block.$box[pnId].bindFailureAfter);
		vat.debug("developer", "【傳入參數錯誤】data grid id:" + pnId + " 的相關設定不存在");
	}
};


// grid 頁面操作
vat.block.pageBackward = function vatBlock_pageBackward(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		// vat.$jsr(vat.block.$box[pnId].pageUpBeforeService);
		vat.block.pageDataSave(pnId, vat.$extend(poOpt,
		{	saveSuccessBefore: function vatBlock_pageBackwardSuccessBefore(){
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
				if (voPageThere){
					var vnPageThere = eval(voPageThere.value);
					if (voPageThere.value > 1){
						voPageThere.value--;
					}else{
						if (vat.$jsr(vat.block.$box[pnId].pageUpTopService, {defaultResult:true})){
							vat.debug("user", "【已經是第一頁】");
						}
					}
					// vat.$jsr(vat.block.$box[pnId].pageUpAfterService);
				}else{
					vat.debug("developer", "【頁面元素不在】元素代號:"+vat.block.pageThereIdMake(pnId));
				}},
			saveFailureAfter: function vatBlock_pageBackwardFailure(){
				vat.debug("user", "【資料存檔錯誤】");
			}
		}));
	};
};

vat.block.pageForward = function vatBlock_pageForward(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		// vat.$jsr(vat.block.$box[pnId].pageDnBeforeService);
		vat.block.pageDataSave(pnId, vat.$extend(poOpt,
		{	saveSuccessBefore: function vatBlock_pageForwardSuccessBefore(){
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
				var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
				if (voPageThere && voPageCount){
					var vnPageThere  = eval( voPageThere.value);
					var vnPageCount = eval(voPageCount.value);
					if (vnPageThere < vnPageCount){
						voPageThere.value++;
					}else{
						if (vat.$jsr(vat.block.$box[pnId].pageDnBottomService, {defaultResult:true})){
							vat.debug("user", "【已經是最後一頁】");
						}
					}
					//vat.$jsr(vat.block.$box[pnId].pageDnAfterService);
				}else{
					vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId)+" or "+vat.block.pageCountIdMake(pnId));
				}},
			saveFailureAfter: function vatBlock_pageForwardFailure(){
				vat.debug("user", "【資料存檔錯誤】, block id:" + pnId);
			}
		}));
	}
};

vat.block.pageSearch = function vatBlock_pageSearch(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		vat.block.pageDataFlow(pnId, "load:stop");
		vat.block.pageDataSave(pnId, vat.$extend(poOpt,
		{	saveSuccessBefore: function vatBlock_pageSearchSuccessBefore(){
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
				var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
				if (voPageThere && voPageCount){
					voPageThere.value  = Math.max(1, Math.min(eval(voPageThere.value), eval(voPageCount.value)));
				}else{
					vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId)+" or "+vat.block.pageCountIdMake(pnId));
				}},
			saveFailureAfter: function vatBlock_pageSearchFailure(){
				vat.debug("user", "【資料存檔失敗】, block id:" + pnId);
			}
		}));
	}
};

vat.block.pageRefresh = function vatBlock_pageRefresh(pnId, poOpt){ // 注意, 跟其他的函數不同處理
	pnId = (typeof pnId === "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) !== "undefined"){
		var voPageThere  = document.getElementById(vat.block.pageThereIdMake(pnId));
		var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
		if (voPageThere && voPageCount){
			voPageThere.value  = Math.max(1, Math.min(eval(voPageThere.value), eval(voPageCount.value)));
			vat.block.pageDataLoad(pnId, voPageThere.value, poOpt);	// 此函數會被頁面儲存呼叫, 所以載入部分小心修改
		}else{
			vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId)+" or "+vat.block.pageCountIdMake(pnId));
		}
	}
};

vat.block.pageTop = function vatBlock_pageTop(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		vat.block.pageDataSave(pnId, vat.$extend(poOpt,
		{	saveSuccessBefore: function vatBlock_pageTopSuccessBefore(){
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
				var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
				if (voPageThere && voPageCount){
					voPageThere.value  = 1;
				}else{
					vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId)+" or "+vat.block.pageCountIdMake(pnId));
				}},
			saveFailureAfter: function vatBlock_pageTopFailure(){
				vat.debug("user", "【資料存檔失敗】, block id:" + pnId);
			}
		}));
	}
};

vat.block.pageBottom = function vatBlock_pageBottom(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		vat.block.pageDataSave(pnId, vat.$extend(poOpt,
		{	saveSuccessBefore: function vatBlock_pageBottomSuccessBefore(){
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
				var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
				if (voPageThere && voPageCount){
					voPageThere.value  = voPageCount.value;
				}else{
					vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId)+" or "+vat.block.pageCountIdMake(pnId));
				}},
			saveFailureAfter: function vatBlock_pageBackwardFailure(){
				vat.debug("user", "【資料存檔失敗】, block id:" + pnId);
			}
		}));
	}
};

vat.block.pageAdd = function vatBlock_pageAdd(pnId, poOpt){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined" && vat.block.$box[pnId].canGridAppend){
		if (vat.$jsr(vat.block.$box[pnId].appendBeforeService, {defaultResult:true})){
			vat.block.pageDataSave(pnId, vat.$extend(poOpt,
			{	noPageCount : true,
				saveSuccessBefore: function vatBlock_pageAddSaveSuccessBefore(e){
					vat.block.$box[pnId].hasInsert  = true;
					vat.block.$box[pnId].firstIndex = parseInt(vat.block.$box[pnId].firstIndex) + parseInt(vat.block.$box[pnId].lines);
					vat.block.$box[pnId].lastIndex  = parseInt(vat.block.$box[pnId].lastIndex ) + parseInt(vat.block.$box[pnId].lines);
					vat.block.$box[pnId].dataCount  = 0;
					var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
					if (voPageCount){
						voPageCount.value++;
						var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
						if (voPageThere){
							voPageThere.value = voPageCount.value;
						}else{
							vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageThereIdMake(pnId));
						}
					}else{
						vat.debug("developer", "【頁面元素不在】元素代號："+vat.block.pageCountIdMake(pnId));
					}},
				saveSuccessAfter: function vatBlock_pageAddSaveSuccessAfter(e){
					vat.$jsr(poOpt && poOpt.appendAfterService ? poOpt.appendAfterService : vat.block.$box[pnId].appendAfterService);
				},
				saveFailureAfter: function vatBlock_pageAddSaveFailure(){
					vat.debug("user", "【資料存檔失敗】, block id:" + pnId);
				}
			}));
		}else{
			// process for failure of (append before service)
		}
	}else{
		// process for cannot grid append
	}
};

vat.block.pageSave = function vatBlock_pageSave(pnId, poOpt){
	vat.block.pageDataSave(pnId, poOpt);
}
vat.block.pageCancel = function(pnId){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
};
vat.block.pageRemove = function(pnId){
	pnId = (typeof pnId == "undefined") ? vat.block.$current : pnId;
};

vat.block.pageChange = function vatBlock_pageChange(pnId){
	var vsPageList = "";
	if ((typeof pnId === 'number') && (vat.block.page instanceof Array)){
		if (typeof vat.block.$box[pnId] === 'object'){
			vat.block.$current = pnId;
		}else{
			for(var i=0; i<vat.block.$box.length; i++){
				if (typeof vat.block.$box[i] === "object"){
					vsPageList +=  i + (i < vat.block.$box.length-1 ? "," : "");
				}
			}
			vat.debug("developer", "系統錯誤, 查無此 block 代號:"+pnId+", 目前有效的代號有包含("+vsPageList+")");
		}
	}
	return vat.block.$current;
};

vat.block.pageCount = function vatBlock_pageCount(pnId, pnNewCount){
	if (typeof pnId === "number"){
		var voPageCount = document.getElementById(vat.block.pageCountIdMake(pnId));
		if (typeof voPageCount === "object"){
			var vnPageCount = voPageCount.value;
			if (typeof pnNewCount === "number")	voPageCount.value = pnNewCount;
			return vnPageCount;
		}else{
			vat.debug('developer', "系統錯誤, 找不到儲存【頁面總數】的元素:"+vat.block.pageCountIdMake(pnId));
		}
	}else{
		vat.debug('developer', "語法錯誤, 呼叫 vat.block.pageCount() 沒有傳入 block id");
	}
};

vat.block.pageThere = function vatBlock_pageThere(pnId, pnNewHere){
	if (typeof pnId === "number"){
		var voPageThere = document.getElementById(vat.block.pageThereIdMake(pnId));
		if (typeof voPageThere === "object"){
			var vnPageThere = voPageThere.value;
			if (typeof pnNewHere === "number") voPageThere.value = pnNewHere;
			return vnPageThere;
		}else{
			vat.debug('developer', "系統錯誤, 找不到儲存【目前頁碼】的元素:"+vat.block.pageThereIdMake(pnId));
		}
	}else{
		vat.debug('developer', "語法錯誤, 呼叫 vat.block.pageThere() 沒有傳入 block id");
	}
}

vat.block.pageRecount = function vatBlock_pageRecount(pnId){
	vat.block.pageCount(pnId, Math.ceil(vat.block.$box[pnId].lastIndex/vat.block.$box[pnId].lines));
}

// line
vat.block.lineEnhanceClick = function vatBlock_lineEnhanceClick(pnId, pnLineIndex, poThat){
	var voItem = document.getElementById(vat.block.pageTrIdMake(pnId, pnLineIndex));
	if (voItem){
		if (poThat.value == "1"){
			poThat.setAttribute("value", "0");
			var saveBgColor = voItem.style.getAttribute("saveBgColor");
			if (saveBgColor){
				voItem.style.setAttribute("backgroundColor", saveBgColor);
			}
		}else{
			poThat.setAttribute("value", "1");
			voItem.style.setAttribute("saveBgColor", voItem.style.getAttribute("backgroundColor"));
			voItem.style.backgroundColor = "#FF00FF";
		}
	}
};

vat.block.lineEnhance = function vatBlock_lineEnhance(pnId, pnLineIndex, poThat){
	var voItem = document.getElementById(vat.block.pageTrIdMake(pnId, pnLineIndex));
	if (voItem){
		if (poThat.value == "1"){
			voItem.style.setAttribute("saveBgColor", voItem.style.getAttribute("background"));
			voItem.style.background = "#FF00FF";
		}else{
			if (voItem.style.getAttribute("saveBgColor")){
				voItem.style.setAttribute("background", voItem.style.getAttribute("saveBgColor"));
			}else{
				voItem.style.removeAttribute("background");
			}
		}
	}
};

vat.msg = {
	deleteSrc 	: "images/v2/icon_b11.gif",
	deleteAlt 	: "刪除此筆資料",
	undeleteSrc : "images/v2/icon_b11s.gif",
	undeleteAlt : "恢復此筆資料"

}
vat.block.lineDeleteClick = function vatBlock_lineDeleteClick(pnId, poThat){
	if (vat.block.$box[pnId].canGridDelete){
		if (vat.$jsr(vat.block.$box[pnId].deleteBeforeService, {defaultResult:true})){
			if (poThat && poThat.value === "1"){
				poThat.setAttribute("value", "0");
				poThat.src = vat.msg.deleteSrc;
				poThat.alt = vat.msg.deleteAlt;
			}else{
				poThat.setAttribute("value", "1");
				poThat.src = vat.msg.undeleteSrc;
				poThat.alt = vat.msg.undeleteAlt;
			}
			vat.$jsr(vat.block.$box[pnId].deleteAfterService);
		}
	}
};

vat.block.lineDeleteState = function vatBlock_lineDelete(pnId, poThat){
	if (poThat){
		if (poThat.value && poThat.value == "1"){
			poThat.src = vat.msg.undeleteSrc;
			poThat.alt = vat.msg.undeleteAlt;
		}else{
			poThat.value = "0";
			poThat.src = vat.msg.deleteSrc;
			poThat.alt = vat.msg.deleteAlt;
			poThat.onmouseover = function (){};
			poThat.onmouseout	= function (){};
		}
	}
};

vat.block.lineMove = function vatBlock_lineMove(psItemId, pnStep){
	var x1, x2, x3, xx, obj;
	x1 = psItemId.indexOf("A#Y", 0);
	if (x1 != -1){
		x2 = psItemId.indexOf("#X",0);
		x3 = parseInt(psItemId.substring(x1+3, x2));
		xx = 	psItemId.substring(0, x1+3)+																//*** before [ name
					Math.max(0, x3+pnStep) +
					psItemId.substring(x2);
		/*  	Math.max(0, Math.min(vat.block.$box[pnId].lines, x3+pnStep))+		line subscript, 先不檢查超過太大		*/
																	//***  after ] name
		obj = document.getElementById(xx);
		if (obj && (psItemId != xx)){
			obj.focus();
		}
	}
};


vat.block.$popup = {
	enable : false,
	prior : [],	// 儲存先前的的區塊物件
	count : 0,
	stack	: []
};

vat.block.$heap = {
	$box : 	[],
	$len : 	[],
	$put : 	function vatBlock_heapPut(poTarget){
						vat.block.$heap.$box.push(poTarget);
						if (typeof vat.block.$heap.$len[poTarget.id] !== "number" ||
											 vat.block.$heap.$len[poTarget.id] === 0){
							vat.block.$heap.$len[poTarget.id] = 1;
							return true;	// 第一次建立, 應該要打開遮罩
						}else{
							vat.block.$heap.$len[poTarget.id]++;
							return false;
						}
					},
	$out :	function vatBlock_heapOut(){
						if (vat.block.$heap.$box.length > 0){
							var voTarget = vat.block.$heap.$box.pop();
							if (typeof vat.block.$heap.$len[voTarget.id] === "number"){
								if (vat.block.$heap.$len[voTarget.id] === 1){
									vat.block.$heap.$len[voTarget.id] = 0;
									return voTarget;
								}else if (vat.block.$heap.$len[voTarget.id] > 1){
									vat.block.$heap.$len[poTarget.id]--;
								}
							}
						}
						return false;
					}
};

vat.block.popupCount = function vatBlock_popupCount(){
	return vat.block.$popup.prior.length-1;
}

vat.block.popupStart = function vatBlock_popupStart(psId, pnTop, pnLeft, psBeforeId){
	var voAfter = document.getElementById(psId);
	if (typeof voAfter === "object" && voAfter !== null){
		var voBefore = document.getElementById(psBeforeId ? psBeforeId : vat.form.currentId);
		if (typeof voBefore === "object" && voBefore !== null){	// 將現有的區塊(或表單)儲存並轉成背景-background
			if (vat.block.$heap.$put(voBefore)){
				// voBefore.style.visibility = "hidden";
				// voBefore.style.zIndex = 99;
				if (voBefore.filters.length > 0){
					voBefore.filters[0].enabled = true;
					voBefore.filters[0].opacity = 0.50
				}

			// vat.block.$popup.prior.push(voBefore);
			// vat.form.item.disableAll();
			// vat.form.itemStack.push(vat.form.item.list);
			// vat.form.formStack.push(vat.form.currentId);
			}
		}
		if (arguments.length >= 3){	// 開始處理彈出新的區塊-foreground
			voAfter.style.position = "absolute";
			voAfter.style.top  	= pnTop;
			voAfter.style.left	= pnLeft;
			// voAfter.style.visibility = "visible";
			// voAfter.style.zIndex = 0;
		}
		voAfter.setCapture(false);
		vat.form.currentId = psId;
		// vat.form.item.createList(psId);
		// Drag.init(voAfter);
	}
};

vat.block.popupClose = function vatBlock_popupClose(psId){
	// if (vat.block.$popup.prior.length > 0){
		// var voAfter = vat.block.$popup.prior.pop();
	var voAfter = vat.block.$heap.$out();
	if (voAfter){
		var voBefore = document.getElementById(psId ? psId : vat.form.currentId);
		if (typeof voBefore === "object" && voBefore !== null){
			// voBefore.style.visibility = "hidden";
			// voBefore.style.zIndex = 99;
			voBefore.releaseCapture();
		}
		if (typeof voAfter === "object" && voAfter !== null){
			// voAfter.style.visibility = "visible";
			// voAfter.style.zIndex = 0;
			if (voAfter.filters.length > 0){
				voAfter.filters[0].enabled = false;
			}
			vat.form.currentId = voAfter.id;
		}
		// *** 需要先回存欄位, 再 Enable
		// vat.form.item.list = vat.form.itemStack.pop();
		// vat.form.currentId = vat.form.formStack.pop();
		// vat.form.item.renewDisableAll(); // 要重新思考, 避免頁面上欄位 diaable 後, 原先屬性遺失
		// vat.form.item.currentIndex = vat.message.bind.getValue("current.item", 0);
		// vat.form.item.move(0);
	}
};



// event
vat.block.eventService = function vatBlock_eventService(pnId){
	pnId = (typeof pnId === "undefined") ? vat.block.$current : pnId;
	if (typeof(vat.block.$box[pnId]) != "undefined"){
		vat.$jsr(vat.block.$box[pnId].eventService);
	}
};

vat.block.ready = function vatBlock_ready(pnId){
	if (typeof vat.block === "object" && vat.block.$box instanceof Array)
		return true;
	else
		return false;
};

vat.block.canGridDelete = function vatBlock_canGridDelete(pnId, pbValue){
	var vbResult;
	if (typeof vat.block === "object" && vat.block.$box instanceof Array && typeof pnId === "number"){
		vbResult = vat.block.$box[pnId].canGridDelete;
		if (typeof pbValue === "boolean")
			vat.block.$box[pnId].canGridDelete = pbValue;
	}
	return 	vbResult;
};

vat.block.canGridAppend = function vatBlock_canGridAppend(pnId, pbValue){
	var vbResult;
	if (typeof pnId === "number" && typeof vat.block === "object" && vat.block.$box instanceof Array){
		vbResult = vat.block.$box[pnId].canGridAppend;
		if (typeof pbValue === "boolean")
			vat.block.$box[pnId].canGridAppend = pbValue;

		var voBtn = document.getElementById(vat.block.pageIdMake(pnId)+"BtnAdd");
		if (voBtn !== null){
			voBtn.style.display = vat.block.$box[pnId].canGridAppend ? "inline" : "none";
		}
	}
	return 	vbResult;
};


vat.block.canGridModify = function vatBlock_canGridModify(pnId, pbCanModify, pbCanDelete, pbCanAppend){
	var vbResult, vnId, voIds = new Array();
	if (typeof pnId === "number"){
		voIds.push(pnId);
	}else if (pnId instanceof Array){
		voIds = pnId;
	}else if (typeof pnId === "object"){
		for (var k in pnId)	voIds.push(pnId[k]);
	}else if (typeof pnId === "undefined"){
		for (var j=0; j < vat.block.$box.length; j++)
			if (typeof vat.block.$box[j] === "object") voIds.push(j);
	}
	if (typeof vat.block === "object" && vat.block.$box instanceof Array){
		for (var i=0; i < voIds.length; i++){
			vnId = voIds[i];
			if (typeof vnId === "number" && typeof vat.block.$box[vnId] === "object"){
				if (typeof pbCanModify === "boolean")	vat.block.$box[vnId].canGridModify = pbCanModify;
																				 else vat.block.$box[vnId].canGridModify = true;
				if (typeof pbCanDelete === "boolean") vat.block.$box[vnId].canGridDelete = pbCanDelete;
																				 else vat.block.$box[vnId].canGridDelete = true;
				vat.block.canGridAppend(vnId, typeof pbCanAppend === "boolean" ? pbCanAppend : true);

			}
		}
	}
};


/******************************************************************
 *  說明：各種 ID, NAME 的檢查或組合產生
 */
vat.block.pageId = function vatBlock_pageId(pnId){
	return (typeof pnId === "undefined") ? vat.block.$current : pnId;
};
vat.block.pageIdVerify = function vatBlock_pageIdVerify(pnId){
	if (typeof(pnId) === "number"){
		return true;
	}else{
		vat.debug("developer", "傳入的參數型態("+typeof(pnId)+")錯誤, <pnPageId> 必須是整數");
		return false;
	}
};



/******************************************************************
 *  說明：基本操作
 */



// item
vat.item = function(){};
vat.item.$box = [];
vat.item.$bind = [];
vat.item.$link = [];
vat.item.$ceap = [];

vat.item.verify = function vatItem_verify(poDesc){
	var vbResult=false;
	if(typeof(poDesc) === "object"){
		try{
			poDesc.vName    = poDesc.vName		? poDesc.vName		: "";
			poDesc.type     = poDesc.type     ? poDesc.type.toUpperCase() : "TEXT";
			poDesc.mode     = poDesc.mode     ? poDesc.mode.toUpperCase() : "";	//** Hidden, Readonly, Password
			poDesc.mask     = poDesc.mask     ? poDesc.mask  		: "";
			poDesc.desc     = poDesc.desc     ? poDesc.desc  		: "";
			poDesc.init     = poDesc.init     ? poDesc.init  		: "";
			poDesc.maxLen   = poDesc.maxLen   ? poDesc.maxLen		: 20;
			poDesc.size     = poDesc.size     ? poDesc.size  		: 8;
			poDesc.src		  = poDesc.src	  	? poDesc.src	 		: "";
			poDesc.io			  = poDesc.io	  		? poDesc.io		 		: 0;
	//	poDesc.dec			= poDesc.dec			? poDesc.dec			: 2;
			poDesc.className= poDesc.className? poDesc.className		: "defaultField";
			poDesc.picture 	= poDesc.picture 	? poDesc.picture 	: "";
			poDesc.style		= poDesc.style		? poDesc.style		: "";
			switch(poDesc.type){
				case "ROWID"	: poDesc.mode = "HIDDEN";	 	break;
				case "MSG"  	: poDesc.mode = "HIDDEN";	 	break;
				case "DEL"  	: poDesc.view = "fixed"; 		break;
				case "IDX"  	: poDesc.mode = "READONLY"; break;
				case "SELECT" : break;
				case "NUMB" 	:
				case "NUMM" 	: poDesc.HTML_type="TEXT"			; break;
				case "XBOX" 	: poDesc.HTML_type="CHECKBOX"	; break;
				default				: poDesc.HTML_type=poDesc.type; break;
			}
			switch(poDesc.mode){
				case "HIDDEN"  : poDesc.HTML_type="HIDDEN";    poDesc.mode=""; break;
				case "PASSWORD": poDesc.HTML_type="PASSWORD";  poDesc.mode=""; break;
				case "READONLY":
				default:
			}
			if (poDesc.HTML_type === "HIDDEN") poDesc.view = "none";
			vbResult = true;
		}catch(e){
			vat.debug("developer", "表格的欄位設定錯誤:"+e);
		}
	}else{
		vat.debug("developer", "傳入的參數型態("+typeof(poDesc)+")錯誤, 必須是個實字物件");
	}
	return vbResult;
};

vat.item.altMouseOver = function vatItem_altMouseOver(poThis){
	var voThat = poThis;
	voThat.timerId = setTimeout(function(){
		var voItem = document.getElementById(poThis.id.substring(0, poThis.id.indexOf("IMG")));
		if (typeof voItem.value === "string" && voItem.value.length > 0){
			var voItemImg = document.getElementById(poThis.id);
			voItemImg.alt = voItem.value;
			/*
			var voItemDiv = document.getElementById(poThis.id + "ALTDIV");
			voItemDiv.innerHTML = "說明:<&nbsp><&nbsp>" + voItem.value + "<&nbsp>";
			voItemDiv.style.backgroundColor = "#C0C0C0";
			voItemDiv.style.position = "absolute";
			voItemDiv.style.zIndex = 101;
			voItemDiv.style.overflow = "visible";
			voItemDiv.style.display = "inline";
			*/
		}
	}, 50);

}

vat.item.altMouseOut = function vatItem_altMouseOut(poThis){
	var voThat = poThis;
	clearTimeout(voThat.timerId);
	voThat.timerId = false;
	var voItemDiv = document.getElementById(poThis.id + "ALTDIV");
	voItemDiv.style.display = "none";
}

vat.item.outHTML = function vatItem_outHTML(poItem, poOpt){
	var vsItem = "", vxResult;
	if (vat.item.setDefaultValueByBind(poItem)){	// 從預設值或bean內取得bind的結果
		var	vsInit, vnRow, vnCol, vnId, voItem,
				vsItemStyle = poItem.style, vsItemChecked = "", vsItemStyleTextAlign = "left";
		// vnRow = vat.$default(poOpt.row, 0);
		// vnCol = vat.$default(poOpt.col, 0);
		vnId = vat.$default(poOpt.blockId, 0);
		vat.item.bindBuild(poItem);
		switch (poItem.type){
			case "RBOX":
				vsItem  = "<label for='"+poItem.HTML_id+"'>";
				vsItem += "<input  id='"+poItem.HTML_id+"' name='"+poItem.HTML_name+"' type='RADIO'>";
				vsItem += "</label>";
				break;
			case "NBOX":
			case "XBOX":
			case "CHECKBOX":
				vsItemChecked = poItem.value == "Y" || poItem.value == "1" ? "checked" : "";
			case "NUMM":
			case "NUMB":
				vsItemStyleTextAlign = "right";
			case "TEXT":
				vsItem = "<INPUT id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+vsItemChecked+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+
										" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' alt='"+poItem.value+"'>";
				if (poItem.alter === true){
					vsItem +=	"&nbsp<INPUT id='"+poItem.HTML_id+"IMG'"+" type='IMAGE'"+
										" onmouseover='vatItem_altMouseOver(this)' onmouseout='vatItem_altMouseOut(this)' onclick='return false; // 預設不動作' "+
										" name='"+poItem.HTML_name+"IMG'"+
										" src='images/v2/icon_b07.gif' width='12'>" +
										" <DIV id='"+poItem.HTML_id+"IMGALTDIV' style='display:none'></DIV>"
				}

				break;
			case "SELECT":
				vsInit = (poItem.init instanceof Array &&	poItem.init.length >= 3) ? vat.$arrayCopy(poItem.init) : [["","",true], [], []];
				if (vsInit[0][2]){
						vsInit[1].unshift("<請選擇>");
						vsInit[2].unshift("");
				}
				vsItem = "<SELECT id='"+poItem.HTML_id+"' size='1' style='"+vsItemStyle+"'"+" name='"+poItem.HTML_name+"'>";
				if (vsInit[1] && vsInit[1] instanceof Array){
					for(k=0;k<vsInit[1].length;k++){
						vsItem += "<option value='"+vsInit[2][k]+"'";
						if (vsInit[0][1] == vsInit[2][k] || poItem.value === vsInit[2][k]){
							vsItem += " selected ";
						}
						vsItem += ">"+vsInit[1][k]+"</option>";
					}
				}
				vsItem += "</SELECT>";
				break;
			case "TIME":
				//vsInit = (poItem.init instanceof Array &&	poItem.init.length >= 3) ? vat.$arrayCopy(poItem.init) : [["","",true], [], []];
				vsInit =[
        			["", "", true],
        			["08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00"],
        			["08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00"]
    			];
				if (vsInit[0][2]){
						vsInit[1].unshift("<請選擇>");
						vsInit[2].unshift("");
				}
				vsItem = "<SELECT id='"+poItem.HTML_id+"' size='1' style='"+vsItemStyle+"'"+" name='"+poItem.HTML_name+"'>";
				if (vsInit[1] && vsInit[1] instanceof Array){
					for(k=0;k<vsInit[1].length;k++){
						vsItem += "<option value='"+vsInit[2][k]+"'";
						if (vsInit[0][1] == vsInit[2][k] || poItem.value === vsInit[2][k]){
							vsItem += " selected ";
						}
						vsItem += ">"+vsInit[1][k]+"</option>";
					}
				}
				vsItem += "</SELECT>";
				break;
			case "BUTTON":
				vsItem = "<INPUT id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" type='"+poItem.HTML_type+"'"+
										" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+

										" src='"+poItem.src+"'>";
				break;
			case "ALT":
				vsItem = "<INPUT id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='IMAGE'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" onmouseover='vatItem_altMouseOver(this)' onmouseout='vatItem_altMouseOut(this)'"+
										" name='"+poItem.HTML_id+"'"+
										" src='images/v2/icon_b07.gif' width='12' height='15'>" +
										" <DIV id='"+poItem.HTML_id+"ALTDIV' style='display:none'>1111111111111<br>1333331<cr>14444444111111"+poItem.value+"</DIV>"
				break;
			case "IMAGE":	// for line
				vsItem = "<INPUT id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_id+"'' "+
										" src='"+poItem.src+"'"+">";
				break;
			case "OPEN":
			case "PICKER":
			case "IMG":
				if (poItem.type === "PICKER" || (poItem.type === "IMG" && poItem.serviceAfterPick)){
					vsItem = "<INPUT id='"+poItem.HTML_id+"#pick' style='display:none;'>";
					document.write(vsItem);
					
				}
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+">";
				break;
			case "IMGNUM":
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+" height='60' width='60' >";
				break;
		   case "IMG1":
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+" height='45' width='100' >";
				break;
			case "IMGPAY":
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+" height='100' width='85' >";
				break;
			case "IMGLOGO":
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+" height='120' width='180s' >";
				break;
			case "IMG0":
				vsItem = "<IMG id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"' "+
										" src='"+poItem.src+"'"+" height='50' width='180' >";
				break;					
			case "DATE":
				vsItem = "<input class='defaultField' id='"+poItem.HTML_id+"' name='"+poItem.HTML_name+"' readonly value='"+poItem.value+"' size='10' type='TEXT'>";
				vsItem += "<img align='TOP' id='"+poItem.HTML_id+"_button' name='"+poItem.HTML_name+"_button' src='images/calbtn.gif'>";
				vsItem += "<script language='JavaScript'>Calendar.setup({inputField:'"+poItem.HTML_id+
									"',ifFormat:'yyyy/M/d',button:'"+poItem.HTML_id+"_button',timeFormat : '12',weekNumbers: true,showsTime : false,dateStatusFunc: ourDateStatusFunc});</script>"
				break;
			case "RADIO":
				vsInit = (poItem.init instanceof Array &&	poItem.init.length >= 3) ? poItem.init : [["","", false], [], []];
				vsItem = "";
				if (vsInit[1] && vsInit[1] instanceof Array){
					for(k=0; k<vsInit[1].length; k++){
						vsItem += "<label for='"+poItem.HTML_id+"'>";
						vsItem += "<input  id='"+poItem.HTML_id+"#"+k+"' value="+poItem.init[2][k]+"";
						vsItem += (vsInit[0][1] == vsInit[2][k] ? " CHECKED " : "");
						vsItem +=" name='"+poItem.HTML_id+"' type='RADIO'>"+vsInit[1][k];
						vsItem += "</label>"
					}
				}
				break;
			case "LABEL":
				vsItem = "<SPAN id='"+poItem.HTML_id+"'"+
										" class='"+poItem.className+"' "+" style='"+vsItemStyle+"'"+
										" name='"+poItem.HTML_name+"'>"+poItem.value+"</SAPN>";
				break;
			case "TEXTAREA":
				if (typeof poItem.row === "number" && typeof poItem.col === "number"){
					vsItem = "<TEXTAREA id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" rows='"+poItem.row+"' "+
										" cols='"+poItem.col+"' "+
										" name='"+poItem.HTML_name+"'>"+poItem.value+"</TEXTAREA>";
				}
				break;
			case "DEL":		// Grid 選用, 用來刪除資料
				vsItem = "<IMG id='"+poItem.HTML_id+"' src='"+vat.msg.deleteSrc+"'"+" name='"+poItem.HTML_name+"' "+
									 		" onclick='vat.block.lineDeleteClick("+vnId+", this);' alt='刪除此筆資料'";
				if (! vat.block.$box[vnId].canGridDelete){
					vsItem += " style='visibility:hidden' ";
				}
				vsItem +=	">";
				break;
			case "IDX":		// Grid	必須, 用來顯示序號 (Hibernate 自動排序)
				vsItem = "<INPUT id='"+poItem.HTML_id+"' size='1' maxlength='25' style='padding:0;border:0;' name='"+poItem.HTML_name+"' "+" READONLY>";
				vsItem +=	 "<IMG id='"+poItem.HTML_id+"IDXIMG' src='images/16x16/Document.png' align='middle' style='visibility:hidden;padding=0;'>";
				break;
			case "ROWID":	// Grid 必需, 用來判斷是否為新增的 line
			default:
				vsItem = "<INPUT id='"+poItem.HTML_id+"' value='"+poItem.value+"'"+
										" class='"+poItem.className+"' "+poItem.mode+" style='"+vsItemStyle+"'"+
										" type='"+poItem.HTML_type+"'"+
										" size='"+poItem.size+"' maxlength='"+poItem.maxLen+"'"+
										" name='"+poItem.HTML_name+"'>";
				break;
		}
		if (poOpt.generate){
			document.write(vsItem);
			voItem = document.getElementById(poItem.HTML_id);
			if (voItem !== null && typeof voItem.setAttribute !== "undefined"){
				if (typeof poItem.className === "string") voItem.setAttribute("className", poItem.className);
				if (typeof poItem.init !== "undefined") voItem.setAttribute("init", poItem.init);
				if (typeof poItem.bind === "string") voItem.setAttribute("bind", poItem.bind);
				if (typeof poItem.mask === "string") voItem.setAttribute("template", poItem.mask);
				if (typeof poItem.mode === "string") voItem.setAttribute("mode", poItem.mode);
				if (typeof poItem.dec	 === "number") voItem.setAttribute("dec" , poItem.dec);
				if (typeof poItem.HTML_backgroundColor === "string") voItem.setAttribute("HTML_backgroundColor" , poItem.HTML_backgroundColor);
				if (typeof voItem.src  !== "undefined") voItem.default_src = voItem.src;	// backup src path for every image type
				if (typeof poItem.type === "string"){
					voItem.setAttribute("datatype", poItem.type);
					switch(poItem.type){
					case "TEXT":
						if (typeof poItem.suggest === "string"){
							var isOpera = -1;
							document.write("<SPAN id=" + poItem.HTML_id + "suggestBox" + "></SPAN>");
							var voSpan = document.getElementById(poItem.HTML_id + "suggestBox");
							voItem.suggestSpanId = voSpan.id;
							voItem.lineMin = voItem.linePointer = 1;
							voItem.lineMax = 5;
							voItem.suggestShowTime = poItem.suggestShowTime ? poItem.suggestShowTime : 300;
							voSpan.style.backgroundColor = "#C0C0C0";
							voSpan.style.position = "absolute";
							voSpan.style.zIndex = 101;
							voSpan.style.overflow = "visible";
							voSpan.style.display = "none";
							voSpan.onmouseout		=	function vatItemSuggestStartTimeout(){
																			var voThat = this;
																			voThat.timerId = setTimeout(function(){
																																		voThat.style.display = "none";
																																		vatItemSuggestEraseTimeout();
																																	}, voThat.suggestShowTime);
																		};
							voSpan.onmouseover	=	function vatItemSuggestEraseTimeout(){
																			var voThat = this;
																			clearTimeout(voThat.timerId);
																			voThat.timerId = false;
																		};
							vat.$addEvent(voItem, vat.browser.opera ? "keypress" : "keydown", function(e){
								function vatItemSuggestLineMove(pnWay){
									var voLine, vnId;
									if (voItem.linePointer >= voItem.lineMin){
										vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
										(voLine = document.getElementById(vnId)).onmouseout();
									}
									vnId = voItem.suggestSpanId + "div" + (voItem.linePointer + pnWay);
									(voLine = document.getElementById(vnId)).onmouseover();
								}
								var voSpan = document.getElementById(voItem.suggestSpanId);
								var vnId, voLine, inkey = event.keyCode;
								if (inkey === 13){
									vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
									voLine = document.getElementById(vnId);
									if (voLine)	voItem.value = voLine.innerHTML;
									if (voItem.linePointer >= voItem.lineMin){
										vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
										voLine = document.getElementById(vnId);
										if (voLine) voLine.onmouseout();
									}
									voItem.linePointer = 0;
								}else if (inkey == 38){	//** up
									if (voItem.linePointer > voItem.lineMin) vatItemSuggestLineMove(-1);
								}else if (inkey == 40){	//** down
									if (voItem.linePointer < voItem.lineMax) vatItemSuggestLineMove(+1);
								}else{
									vsPost = poItem.suggest + "&voItemValue=" + voItem.value + String.fromCharCode(inkey);
									vat.block.submit(vsPost, {id: 'vat.bean.init', funcSuccess:function(){
										var voRetLines = vat.bean().lines;
										var vsGetNow = voItem.value; // + String.fromCharCode(inkey);
										var voPos = vat.utils.getAbsolutePos(voItem);
										voSpan.style.top 	= voPos[1] + voItem.offsetHeight;
										voSpan.style.left = voPos[0];
										// voRetLines instanceof Array
										(function(){
											var voLine;
											var vnLinePtr=0;
											for(var i=voItem.lineMin; i<=voItem.lineMax; i++){
												voLine = document.getElementById(voItem.suggestSpanId + "div" + i);
												if (!voLine){
													voLine = document.createElement('<DIV>');
													voLine.sn = i;
													voLine.id = voItem.suggestSpanId + "div" + voLine.sn;
													voLine.onmouseover =	function(){
																									if (voItem.linePointer >= voItem.lineMin){
																										vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
																										(voLine = document.getElementById(vnId)).onmouseout();
																									}
																									this.style.background = "#000040";
																									this.style.color = "white";
																									this.style.cursor = "pointer";
																									voItem.linePointer = this.sn;
																								};
													voLine.onmouseout	=		function(){
																									this.style.background = "#C0C0C0";
																									this.style.color = "";
																									this.style.cursor = "";
																								};
													voLine.onmousedown	=	function(){
																									vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
																									voLine = document.getElementById(vnId);
																									if (voLine)	voItem.value = voLine.innerHTML;
																									if (voItem.linePointer >= voItem.lineMin){
																										vnId = voItem.suggestSpanId + "div" + voItem.linePointer;
																										voLine = document.getElementById(vnId);
																										if (voLine) voLine.onmouseout();
																									}
																									voItem.linePointer = 0;
																								};
													voSpan.appendChild(voLine);
												}else{
													voLine.onmouseout();
												}
												// voLine.innerHTML = (vsGetNow + "000000000000000").substr(0, 14) + i.toString();

												if (vnLinePtr < voRetLines.length){
													voLine.innerHTML = voRetLines[vnLinePtr];
													vnLinePtr++;
												}
											};
										})();
										voItem.linePointer = 0;
										voSpan.style.display = "inline";
									}});
								}
							});
							vat.$addEvent(voItem, "blur", function(x){
								var voSpan = document.getElementById(voItem.suggestSpanId);
								voSpan.style.display = "none";
								// if (this.obj.useTimeout) vat.item.startTimeout();
							});
						}
						break;
					case "NUMM":
						voItem.setAttribute("original", voItem.getAttribute("value"));
						voItem.setAttribute("value", vat.item.formatNumm(voItem.getAttribute("value"), voItem.getAttribute("dec")));
						break;
					case "NBOX":
						voItem.style.visibility = "hidden";
					case "XBOX":
						break;
					case "RBOX":
						vat.$addEvent(voItem, "click", function(x){
							// start
								var vsName, voItem, vnPtr;
								vnPtr = parseInt(x.id.substring(x.id.indexOf("#Y", 0)+2, x.id.indexOf("#X", 0)));
								for(var i=1; i < 25; i++){
									// 取得 <RBOX> 物件, 命名方式是: 欄位名稱 + # + Y 座標 + # + X 座標
										vsName = x.id.substr(0, x.id.indexOf("#Y", 0)+2) + i + x.id.substr(x.id.indexOf("#X", 0));
										voItem = document.getElementById(vsName);
										if (typeof voItem === "object" && voItem != null){
											if (vnPtr !== i)
												voItem.checked = false;
										}else{
											break;
									}
								}
							// end
							vsPost = poOpt.pickAllService+"&vatIsClick="+(event.srcElement.checked ? "Y" : "N");
							// add timescope, isAllclick, searchkey to otherbean
							vat.bean().vatBeanOther.timeScope = vat.block.$box[poOpt.blockId].timeScope;
							vat.bean().vatBeanOther.searchKey = vat.block.$box[poOpt.blockId].searchKey;
							vat.bean().vatBeanOther.isClick = event.srcElement.checked ? "Y" : "N";
							vat.block.submit(vsPost, {bind: true, other:true, picker:true, funcSuccess:function(){ vat.block.pageRefresh(poOpt.blockId); /*alert('test xbox ok!!!'+vsPost);*/ }});
							});
						break;
					case "IMG":
					case "PICKER":
						if (poItem.serviceAfterPick){
							var voAfterPickBtn = document.getElementById(voItem.id + "#pick");
							if (voAfterPickBtn !== null)
								vat.$addEvent(voAfterPickBtn, "click", function(){
											var voAfterPickBtn = document.getElementById(voItem.id + "#pick");
											if (voAfterPickBtn !== null && voAfterPickBtn.value){
												if (typeof poItem.beanName === "string"){
													if (typeof vat.bean().vatBeanPicker !== "object") vat.bean().vatBeanPicker = {};
													voResult = eval('('+vat.utils.unescape(voAfterPickBtn.value)+')');
													vat.$extend(vat.bean().vatBeanPicker, voResult);
												}else
													vat.bean().vatBeanPicker = eval('('+vat.utils.unescape(voAfterPickBtn.value)+')');
											}
											var vnTimerId = window.setInterval(function(){
												window.clearInterval(vnTimerId);
												window.focus();
												vat.block.pageDataFlowStopAll();
												vat.$jsr(poItem.serviceAfterPick, {that:voItem});
											}, vat.$default(poItem.timeAfterPick, 1));
									});
						}
						if (typeof poItem.openMode === "string"){
							var	vnTop  	= vat.$default(poItem.top, 			0),
									vnLeft 	= vat.$default(poItem.left, 		0),
									vnWidth = vat.$default(poItem.width, 1024),
									vnHeight= vat.$default(poItem.height, 768);
							switch(poItem.openMode){
								case "showModalDialog":
									vat.$addEvent(voItem, "click", function(){
										if (vat.$jsr(poItem.serviceBeforePick, {that:voItem, defaultResult:true})){
											var vsPost = poItem.service+"?vatPickerId="+ vat.utils.escapeURL(voItem.id);
													vsPost += vat.$jsr(poItem.servicePassData, {that:voItem, defaultResult:""});
											window.showModalDialog(vsPost, '', "dialogHeight:"+vnHeight+"px;dialogWidth:"+vnWidth+"px;dialogTop:"+vnTop+"px;dialogLeft:"+vnLeft+"px;status:no;");
										}
									});
									break;
								case "open":
									vat.$addEvent(voItem, "click", function(){
										if (vat.$jsr(poItem.serviceBeforePick, {that:voItem, defaultResult:true})){
											var vsPost = poItem.service+"?vatPickerId="+ vat.utils.escapeURL(voItem.id);
													vsPost += vat.$jsr(poItem.servicePassData, {that:voItem, defaultResult:""});
											window.open(vsPost, '', "left="+vnLeft+", right="+vnTop+", width="+vnWidth+", height="+vnHeight+", menubar=yes, status=yes, resizable=yes, location=yes");
										}
									});
									break;
							}
						}
						break;
					case "DATE":
					case "TIME":
					case "SELECT":
						if (poItem.HTML_type === "HIDDEN"){
							voItem.style.display = "none";
						}
						break;
					case "IDX":
						/* 如果將滑鼠移到 IDX, 則顯示 row id
						voItem.onclick = function(){alert("Row Id:"+this.rowid)};
						voLine = document.getElementById(vat.block.pageTrImgIdMake(pnId, i));
						if (vxLineObj){
							vxLineObj.rowid = pxValue;
							vxLineObj.onclick =
						}
						*/
						break;
					}
				}
				voItem.style.textAlign = vsItemStyleTextAlign;
				vaEvent = {onchange:"change", eChange: "change", eValid:"change", eClick: "click", eFocus:"focus", eBlur:"blur", eMouseIn:"mouseover", eMouseOut:"mouseout"};
				for (vsType in vaEvent)
					if (typeof poItem[vsType] !== "undefined" && poItem[vsType] !== null)
						vat.$addEvent(voItem, vaEvent[vsType], poItem[vsType]);
				if (typeof poItem.type === "string" && poItem.type === "RADIO"){
					var voItem1 = document.getElementById(poItem.HTML_id+"#1");
					if (voItem1 !== null){
						for (vsType in vaEvent)
							if (typeof poItem[vsType] !== "undefined" && poItem[vsType] !== null)
								vat.$addEvent(voItem1, vaEvent[vsType], poItem[vsType]);
					}
				}
				if (poItem.mode === "READONLY" || (! vat.$default(poOpt.modifiable, true))){
					vat.item.setReadOnlyByObject(voItem, true);
				}else if (poItem.mode === "HIDDEN"){
					voItem.style.display = "none";
				}
				// "shift"
				voItem.setAttribute("view", (typeof poItem.view === "string" ? poItem.view : ""));
				if (voItem.view === "shift"){
					// voItem.style.display = "none";
				}
			}
		}
	}
	return vsItem;
};

vat.item.make = function vatItem_make(pnId, psName, poDesc){
	if (vat.block.pageIdVerify(pnId) && vat.item.verify(poDesc)){
		if (typeof(vat.block.$box[pnId]) === "undefined"){
			vat.block.create(pnId);
		}
		if (typeof(psName) === "string"){
			vat.block.$box[pnId].itemName.push(psName);
		}else{
			vat.debug("developer", "傳入的參數型態("+typeof(psName)+")錯誤, <psName> 必須是字串");
		}
		vat.block.$box[pnId].itemDesc.push(poDesc);
	}
};


// 下拉式選單
vat.item.IsSelect = function vatItem_IsSelect(pxObj){
	if (pxObj !== null && pxObj.options !== null){
		return true;
	}
	return false;
};
vat.item.SelectGetValue = function vatItem_SelectGetValue(psName){
	var vxObjByName = document.getElementsByName(psName);
	var vsResult = "";
	if (vxObjByName && vxObjByName[0]){
		vxObjSelect = vxObjByName[0];
		if (vat.item.IsSelect(vxObjSelect)){
			for(var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
				if (vxObjSelect.options[i].selected){
					vsResult = vxObjSelect.options[i].text;
					break;
				}
			}
		}
	}
	return vsResult;
};
vat.item.SelectGetIndex = function vatItem_SelectGetIndex(psName){
	var vxObjByName = document.getElementsByName(psName);
	var vsResult = "";
	if (vxObjByName && vxObjByName[0]){
		vxObjSelect = vxObjByName[0];
		if (vat.item.IsSelect(vxObjSelect)){
			for(var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
				if (vxObjSelect.options[i].selected){
					vsResult = i;
					break;
				}
			}
		}
	}
	return vsResult;
};
vat.item.formatNumm = function vatItem_formatNumm(psNumb, pnDec){
	function formatNumber(psInt){
		if (psInt.length <= 3) {
			return psInt;
		}else{
			return formatNumber(psInt.substr(0, psInt.length-3))+","+psInt.substr(psInt.length-3);
		}
	}
	if (typeof psNumb == "string" && psNumb.length > 0 && typeof psNumb.replace == "function"){
		var vsSign = "", vnInteger, vsDecimal;
		if (psNumb.substr(0, 1) === "-"){
			vsSign = psNumb.substr(0, 1);
			psNumb = psNumb.substr(1);
		}
		vnInteger = parseInt(psNumb.replace(/,*/g, ""));
		if (typeof pnDec === "number"){
			vnPos = psNumb.indexOf(".");
			if (vnPos != -1 && pnDec > 0){
				vsDecimal = psNumb.substr(vnPos, pnDec + 1);
			}else{
				vsDecimal = "";
			}
		}else{
			if ((vnPos = psNumb.indexOf(".")) != -1 && vnPos !== psNumb.length - 1){
				vsDecimal =  psNumb.substr(vnPos);
			}else{
				vsDecimal =  "";
			}
		}
		return vsSign+formatNumber(vnInteger.toString())+vsDecimal;
	}
	return psNumb;
};




/*
*/
(function vatItem_convert_define(){
	vat.item.bindBuild = function vatItem_bindBuild(poItem){
		if (poItem !== null){
			if (typeof poItem.type === "string" && poItem.type !== "LABEL"){
				var voItem, vsId = poItem.HTML_id;
			  if (typeof poItem.ceap === "string"){
					voItem = document.getElementsByName(poItem.ceap);
					if (voItem) vsId = voItem[0].id;
					vat.item.$ceap.push(poItem.ceap);
	  		}
				if (typeof poItem.back === "boolean" && !poItem.back){
					vat.item.$link.push({key : poItem.bind, id : vsId});
				}else{
					vat.item.$bind.push({key : poItem.bind, id : vsId});
				}
			}
		}
	};
	vat.item.bindAll = function vatItem_bindAll(){
		var vsData = "", vsItem = "", voItem;
		for(var i=0; i<vat.item.$bind.length; i++){
			voItem = document.getElementById(vat.item.$bind[i].id);
			if (voItem !== null)
				vat.item.setDefaultValueByBind(voItem);
		}
		for(var i=0; i<vat.item.$link.length; i++){
			voItem = document.getElementById(vat.item.$link[i].id);
			if (voItem !== null)
				vat.item.setDefaultValueByBind(voItem);
		}
		return true;
	};
	// 將頁面上有設定 binding 的欄位編碼資料後轉成字串, 供換頁時(vat.block.pageDataLoad & vat.block.pageDataSave) ajax 呼叫時傳輸用
	vat.item.$bind.toUrlString = function vatItem_bind2UrlString(){
		var vsData = "", vsItem = "", voItem, vsKey = "";
		for(var i=0; i<vat.item.$bind.length; i++){
			vsKey = vat.item.$bind[i].id ? vat.item.$bind[i].id : vat.item.$bind[i].key;
			voItem = document.getElementById(vsKey);
			if (voItem !== null)
				vsItem = "&" + vat.item.$bind[i].key + "=" + vat.utils.escape(vat.item.getValue(voItem)) + ""; // 需先轉換特殊符號
			else
				vat.debug("developer", "找不到頁面上的元素, id : "+vsKey);
			vsData += vsItem;
		}
		vsData += "}";
		return vsData;
	};
	/* 暫無使用
	vat.item.$bind.toString = function vatItem_bind2string(){
		var vsData = "vatBeanFormBind={", vsItem = "", voItem, vsKey = "";
		for(var i=0; i<vat.item.$bind.length; i++){
			vsKey = vat.item.$bind[i].id ? vat.item.$bind[i].id : vat.item.$bind[i].key;
			voItem = document.getElementById(vsKey);
			vsItem = "'" + vat.item.$bind[i].key + "':'" + vat.item.getValue(voItem) + "'";
			vsData += (vsItem + ((i < vat.item.$bind.length-1) ? "," : ""));
		}
		vsData += "}";
		return vsData;
	}; */
	//
	vat.item.$bind.toBean = function vatItem_bind2bean(){
		var voBean = {}, vsItem = "", voItem, vsKey = "";
		for(var i=0; i<vat.item.$bind.length; i++){
			vsKey = vat.item.$bind[i].id ? vat.item.$bind[i].id : vat.item.$bind[i].key;
			voItem = document.getElementById(vsKey);
			if (voItem && typeof vat.item.$bind[i].key !== "undefined")
				voBean[vat.item.$bind[i].key] = vat.item.getValue(voItem); // 處理特殊符號, 不等 vatBean 轉 JSON 時再轉換
		}
		return voBean;
	};
	/* 暫無使用
	vat.item.$link.toString = function vatItem_link2string(){
		var vsData = "vatBeanFormLink={", vsItem = "", voItem, vsKey = "";
		for(var i=0; i<vat.item.$link.length; i++){
			vsKey = vat.item.$link[i].id ? vat.item.$link[i].id : vat.item.$link[i].key;
			voItem = document.getElementById(vsKey);
			vsItem = "'" + vat.item.$link[i].key + "':'" + vat.item.getValue(voItem) + "'";
			vsData += (vsItem + ((i < vat.item.$link.length-1) ? "," : ""));
		}
		vsData += "}";
		return vsData;
	}; */
	vat.item.$link.toBean = function vatItem_link2bean(){
		var voBean = {}, vsItem = "", voItem, vsKey = "";
		for(var i=0; i<vat.item.$link.length; i++){
			vsKey = vat.item.$link[i].id ? vat.item.$link[i].id : vat.item.$link[i].key;
			voItem = document.getElementById(vsKey);
			if (voItem) voBean[vat.item.$link[i].key] = vat.item.getValue(voItem); // 處理特殊符號, 不等 vatBean 轉 JSON 時再轉換
		}
		return voBean;
	};
	/* 暫無使用
	vat.item.$ceap.toString = function vatItem_ceap2string(){
		var vsData = "vatBeanFormCeap={", vsItem = "", voItem;
		for(var i=0; i<vat.item.$link.length; i++){
			voItem = document.getElementsByName(vat.item.$ceap[i]);
			vsItem = "'" + voItem[0].name + "':'" + vat.item.getValue(voItem[0]) + "'";
			vsData += (vsItem + ((i < vat.item.$ceap.length-1) ? "," : ""));
		}
		vsData += "}";
		return vsData;
	};*/
})();



/*
*/
(function vatItem_get_define(){
	vat.item.getDefaultValue = function vatItem_getDefaultValue(poItem){
		var vsResult, vxDefalt;
		if (typeof poItem === "object"){
			switch (typeof poItem.init){
			case "undefined":
				if (typeof poItem.datatype === "string"){
					switch (poItem.datatype){
					case "NUMM":
					case "NUMB":
						vxDefault = "0";
						break;
					case "XBOX":
					case "CHECKBOX":
						vxDefault = "N";
						break;
					default:
						vxDefault = "";
						break;
					}
				}
				return vxDefault;
				break;
			case "function":
				return poItem.init();
				break;
			case "object":
				if (poItem instanceof Array && poItem.length == 3){
					return poItem.init[0][1];
				}else
					return "";
				break;
			default:
				return poItem.init;
			}
		}else
			vat.debug("developer", "vat.item");
	};
	vat.item.getGridLine = function vatItem_getGridLine(psName){
		var vnPtrLeft, vnPtrRight, vnLineNo;
		if (typeof psName === "undefined"){
			psName = event.srcElement.name;
			// psName = vat.form.item.list[vat.form.item.current].name;
			// if (psName.indexOf("[", 0) === -1) psName = event.srcElement.name;
		}else if(typeof psName === "object" && typeof psName.name !== "undefined"){
			psName = psName.name;
		}
		vnPtrLeft = psName.indexOf("[", 0);
		if (vnPtrLeft !== -1){
			vnPtrRight = psName.indexOf("]", 0);
			vnLineNo = parseInt(psName.substring(vnPtrLeft+1, vnPtrRight));	//*** subscript
			return vnLineNo;
		}else{
			return -1;
		}
	};
	vat.item.getGridByAxis = function vatItem_getGridByAxis(pnId, pnAxisY, pnAxisX){
		return document.getElementById(vat.item.IdMake(pnId, pnAxisY, pnAxisX));
	};
	vat.item.getGridValueByName = function vatItem_getGridValueByName(psName, pnLine){
		var vsName, voItem;
		if (typeof psName === "undefined"){
			vsName = vat.form.item.list[vat.form.item.current].name;
		}else{
			if (typeof pnLine === "undefined") pnLine = vat.item.getGridLine();
			vsName = vat.item.nameMake(psName, pnLine);
		}
		voItem = document.getElementsByName(vsName);
		return vat.item.getValue(voItem[0]);
	};
	vat.item.getValueByName = function vatItem_getValueByName(psName){
		var voItem;
		voItem = document.getElementsByName(psName);
		return vat.item.getValue(voItem[0]);
	}
	vat.item.getValueById = function vatItem_getValueById(psId){
		var voItem;
		if(!arguments.length){	// psId == null, get current
			voItem = vat.form.item.list[vat.form.item.current];
			if (voItem !== "object") voItem = event.srcElement;
		}else{
			voItem = document.getElementById(psId);
		}
		return vat.item.getValue(voItem);
	};
	vat.item.getValue = function vatItem_getValue(poItem){	// get value by object
		var vxRet = "", vsType = "";
		if (poItem !== null && typeof poItem === "object"){
			vsType = typeof poItem.datatype === "string" ? poItem.datatype : poItem.tagName;
			switch (vsType){
				case "NUMM":
				case "NUMB":
				case "TEXTAREA":
				case "TEXT":
					if (vsType === "NUMM"){
						vxRet = (poItem.original = poItem.value.replace(/,*/g, ""));	// for NUMM
					}else{
						vxRet = poItem.getAttribute("value");	// for TEXT
					}
					break;
				case "RADIO":
				case "XBOX":
				case "CHECKBOX":
					if (poItem.init instanceof Array && poItem.init.length >= 3){
						vxRet = poItem.checked ? poItem.init[2][0] : poItem.init[2][1];
					}else if (typeof poItem.value === "boolean")
						vxRet = poItem.checked ? true : false;
					else if (typeof poItem.value === "string")
						vxRet = poItem.checked ? "Y" : "N";
					else if (typeof poItem.value === "number")
						vxRet = poItem.checked ? 1 : 0;
					break;
				case "SELECT":
					var vaRet = [];
					for(var k=0; k<poItem.options.length; k++){
						if (poItem.options[k].selected) {
							vaRet.push(poItem.options[k].value);
							break;
						}
					}
					if (vaRet.length === 1){
						vxRet = vaRet[0];
					}else if (vaRet.length > 1){
						vxRet = vaRet;
					}
					break;
				case "TIME":
					var vaRet = [];
					for(var k=0; k<poItem.options.length; k++){
						if (poItem.options[k].selected) {
							vaRet.push(poItem.options[k].value);
							break;
						}
					}
					if (vaRet.length === 1){
						vxRet = vaRet[0];
					}else if (vaRet.length > 1){
						vxRet = vaRet;
					}
					break;
				default:
				 	vxRet = poItem.getAttribute("value");
			}
		}
		return vxRet;
	};
	vat.item.getCurrent = function vatItem_getCurrent(psId){
		var voItem;
		if(typeof psId === "undefined" || psId === "#current#"){
			voItem = vat.form.item.list[vat.form.item.current];
			if (voItem !== "object") voItem = event.srcElement;
			return voItem;
		}else{
			return undefined;
		}
	}
})();


/*
	vat.item.bindBuild = function vatItem_bindBuild(poItem){
		if (poItem !== null){
			if (typeof poItem.type === "string" && poItem.type !== "LABEL"){
				var voItem, vsId = poItem.HTML_id;
			  if (typeof poItem.ceap === "string"){
					voItem = document.getElementsByName(poItem.ceap);
					if (voItem) vsId = voItem[0].id;
					vat.item.$ceap.push(poItem.ceap);
	  		}
				if (typeof poItem.back === "boolean" && !poItem.back){
					vat.item.$link.push({key : poItem.bind, id : vsId});
				}else{
					vat.item.$bind.push({key : poItem.bind, id : vsId});
				}
			}
		}
	};
*/
(function vatItem_set_define(){
	vat.item.setDefaultValueByBind = function vatItem_setDefaultValueByBind(poItem){
		if (poItem !== null){
			var vxValue;
			if (typeof poItem.bind === "string"){
				var vxBind = vat.bean(poItem.bind, true);
				if(typeof vxBind === "function"){	// bind 的內容是 function
					 vxValue = vxBind();
				}else{
					var x1, x2, x3;
					if(vxBind instanceof Array && (x1 = poItem.name.indexOf("[")) > 0){
						// bind 的內容是個 array, for layout:"grid:bean" 使用
						x2 = poItem.name.indexOf("]", x1);
						x3 = parseInt(poItem.name.substring(x1+1, x2));
						if (x3 <= vxBind.length) vxValue = vxBind[x3];
					}else if (vxBind !== undefined)
						vxValue = vxBind;
					else
						vxValue = vat.item.getDefaultValue(poItem);	// 如果沒有 bind 的內容, 取得初始值
				}
			}else if (typeof poItem.value === "undefined"){
				vxValue = vat.item.getDefaultValue(poItem);	// 初始值
			}else{
				vxValue = poItem.value;
			}
			if (poItem.datatype === undefined) poItem.datatype = poItem.type;
			if (typeof poItem.nodeType === "number" && poItem.nodeType === 1)
				vat.item.setValue(poItem, vxValue);
			else
				poItem.value = vxValue;
			return true;
		}else
			return false;
	};
	vat.item.setGridValueByAxis = function vatItem_setGridValueByAxis(pnId, pnY, pnX, pxValue){
		pnId = (typeof pnId === "undefined") ? vat.block.$current : pnId;
		vat.item.setValueById(vat.item.IdMake(pnId, pnY, pnX), pxValue);
	};
	vat.item.setGridValueByName = function vatItem_setGridValueByName(psName, pnLine, pxValue){
		var vsName, voItem;
		if (typeof (voItem = vat.item.getCurrent(psName)) === "undefined"){
			if (typeof pnLine === "undefined") pnLine = vat.item.getGridLine();
			voItem = document.getElementsByName(vsName = vat.item.nameMake(psName, pnLine));
			if (typeof voItem.length === "number" && voItem.length > 0)
				voItem = voItem[0];
			else
				vat.debug("developer", "vat.item.gridBindByName(), 錯誤的名稱或行號, 導致無法取得 item 物件");
		}
		return vat.item.setValue(voItem, pxValue);
	};
	vat.item.setValueByName = function vatItem_setValueByName(psName, pxValue){
		var voItem
		if (typeof (voItem = vat.item.getCurrent(psName)) === "undefined"){
			voItem = document.getElementsByName(psName)[0];
		}
		return vat.item.setValue(voItem, pxValue);
	}
	vat.item.setValueById = function vatItem_setValueById(psId, pxValue){
		if (typeof (voItem = vat.item.getCurrent(psId)) === "undefined"){
			voItem = document.getElementById(psId);
		}
		return vat.item.setValue(voItem, pxValue);
	}

	// 注意: 如果是 pageLoad, setValue 會動態改變 readOnly & hidden 屬性
	vat.item.setValue = function vatItem_setValue(poItem, pxValue, poOpt){
		var vaYX, vxLineObj, vnId, vsType;
		var vbResult = false;
		if (pxValue !== null){// modify for excel import
			if (typeof poItem === "object" && poItem !== null){
				vsType = typeof poItem.datatype === "string" ? poItem.datatype : poItem.tagName;
				switch (vsType){
				case "NUMM":
					poItem.setAttribute("original", pxValue);
					pxValue = vat.item.formatNumm(pxValue, poItem.getAttribute("dec"));
				case "NUMB":
				case "TEXTAREA":
				case "ROWID":
				case "TEXT":
					poItem.setAttribute("value", pxValue);
					break;
				case "DEL":
					poItem.setAttribute("value", pxValue);
					vnId = (poOpt && poOpt.blockId) ? poOpt.blockId : vat.block.$current;
					vat.block.lineDeleteState(vnId, poItem);
					break;
				case "MSG":
					poItem.setAttribute("value", pxValue);
					(function(){
						var vsId = poItem.id.substring(0, poItem.id.indexOf("#X", 0)+2)+"1IDXIMG"
						var vxLineObj = document.getElementById(vsId);
						if (vxLineObj){
							if (pxValue && pxValue.length > 0){
								vxLineObj.alt = pxValue;
								vxLineObj.src = "images/16x16/Chat Bubble.png";
								vxLineObj.style.visibility = "visible";
							}else{
								vxLineObj.style.visibility = "hidden";
							}
						}
					})();
					break;
				case "XBOX":
				case "CHECK":
				case "CHECKBOX":
					if ("1" === pxValue || "Y" === pxValue){
						poItem.checked = true;
					}else{
						poItem.checked = false;
					}
					break;
				case "RBOX":
				case "RADIO":
					var vsId = (typeof poItem.id === "string" ? poItem.id : (typeof poItem.HTML_id === "string" ? poItem.HTML_id : ""));
					if (vsId !== ""){
						if ("1" === pxValue || "Y" === pxValue){
							var voItem0 = document.getElementById(vsId+"#0");
							if (voItem0 !== null) voItem0.value = pxValue;
						}else{
							var voItem1 = document.getElementById(vsId+"#1");
							if (voItem1 !== null) voItem1.value = pxValue;
						}
					}
					break;
				case "SELECT":
					var vbHasSelected = false;
					var vnDefaultIndex = 0;
					for(var k=0; k<poItem.options.length; k++){
						/* 從 init 內抓預設值
						if (poItem.options[k].value == vat.block.$box[pnId].itemDesc[j].init[0][2]){
							vnDefaultIndex = k;
						}	*/
						if (poItem.options[k].value == pxValue){
							poItem.options[k].selected = true;
							vbHasSelected = true;
						}else{
							poItem.options[k].selected = false;
						}
					}
					if (!vbHasSelected){
						poItem.options[vnDefaultIndex].selected = true;
					}
					break;
				case "TIME":
					var vbHasSelected = false;
					var vnDefaultIndex = 0;
					for(var k=0; k<poItem.options.length; k++){
						/* 從 init 內抓預設值
						if (poItem.options[k].value == vat.block.$box[pnId].itemDesc[j].init[0][2]){
							vnDefaultIndex = k;
						}	*/
						if (poItem.options[k].value == pxValue){
							poItem.options[k].selected = true;
							vbHasSelected = true;
						}else{
							poItem.options[k].selected = false;
						}
					}
					if (!vbHasSelected){
						poItem.options[vnDefaultIndex].selected = true;
					}
					break;
				case "IMG":
					if (poOpt && typeof poOpt.mode === "string" && poOpt.mode === "pageDataBind"){
						if (typeof pxValue === "string" && pxValue.length > 5){
							poItem.src = pxValue;
						}else if (typeof poItem.default_src === "string" && poItem.src !== poItem.default_src){
							poItem.src = "";
							poItem.src = poItem.default_src;
						}
					}
					break;
				case "BUTTON":
					if (typeof pxValue === "string"){
						poItem.src = pxValue;
					}
					break;
				case "IDX":
					poItem.value = pxValue;
					vxLineObj = document.getElementById(vat.block.pageTrImgIdMake(poItem.id));
					if (vxLineObj){
						vxLineObj.rowid = pxValue;
						vxLineObj.onclick = function(){alert("Row Id:"+this.rowid)};
					}
					break;
				default:
					poItem.setAttribute("value", pxValue);
					vat.debug("vat", "【沒有符合的型態, 啟用預設的給值方式】傳入的資料名稱:"+poItem.name+", 型態："+poItem.datatype+", 資料值:"+poItem.value);
					break;
				}
				vbResult = true;
			}
		}
		return vbResult;
	};
	vat.item.SelectBind = function vatItem_SelectBind(paDataSrc, poOpt){	//*** 不建議使用
		var voItems, voItem, vaSelect = [], vsPrompt = "<請選擇>";
		if (paDataSrc && paDataSrc instanceof Array && paDataSrc.length >= 2 &&
				paDataSrc[1] instanceof Array && paDataSrc[2] instanceof Array){
			if (typeof poOpt === "object"){
				if (typeof poOpt.itemName === "string")  paDataSrc[0][0] = poOpt.itemName;
				if (typeof poOpt.selected === "string")  paDataSrc[0][1] = poOpt.selected;
				if (typeof poOpt.isPrompt === "boolean") paDataSrc[0][2] = poOpt.isPrompt;
			}
			voItems = document.getElementsByName(paDataSrc[0][0]);
			if (voItems && voItems[0]){
				voItem = voItems[0];
				if (vat.item.IsSelect(voItem)){
					for(var i=(voItem.options.length - 1); i >= 0; i--) {
						voItem.options[i] = null;
					}
					if (paDataSrc[0][2]){
						if (paDataSrc[1][0] !== vsPrompt){
							paDataSrc[1].unshift(vsPrompt);
							paDataSrc[2].unshift("");
						}
					}else{
						if (paDataSrc[1][0] === vsPrompt){
							paDataSrc[1].shift(vsPrompt);
							paDataSrc[2].shift("");
						}
					}
					for(i=0; i<paDataSrc[1].length; i++){		//** option.text
						if (typeof paDataSrc[1][i] == "string" && paDataSrc[1][i].length > 0){
							voItem.options[voItem.options.length] = new Option(paDataSrc[1][i], paDataSrc[2][i], false, false);
						}
						if (paDataSrc[2][i] == paDataSrc[0][1]){
							voItem.selectedIndex = i;
						}
					}
				}else{
					vat.debug("developer", "從 "+vat.$caller(vat.item.SelectBind.caller)+"(), 更新的目標:"+paDataSrc[0][0]+", 不是一個<SELECT>元素");
				}
			}else{
				vat.debug("developer", "從 "+vat.$caller(vat.item.SelectBind.caller)+"(), 找不到被更新的 HTML 元素名稱"+paDataSrc[0][0]);
			}
		}else{
			vat.debug("developer", "傳入的參數錯誤");
		}
	};
	vat.item.setStyleByName = function vatItem_setStyleByName(psName, psProperty, psValue){
		var voItem;
		voItem = document.getElementById(psName);
		if (typeof voItem === "object" && voItem != null)
			if (voItem.tagName === "DIV" || voItem.tagName === "SPAN")
				for(var vsProperty in voItem)	voItem[vsProperty].style[psProperty] = psValue;
			else voItem.style[psProperty] = psValue;
	};
	vat.item.setGridStyleByName = function vatItem_setGridStyleByName(psName, psProperty, psValue){
		var vsName, voItem;
		for(var i=0; i < 100; i++){
			vsName = psName+"#"+i;
			voItem=document.getElementById(vsName)
			if (typeof voItem === "object" && voItem != null){
				if (voItem.tagName === "DIV" || voItem.tagName === "SPAN")
					for(var vsProperty in voItem)	voItem[vsProperty].style[psProperty] = psValue;
				else voItem.style[psProperty] = psValue;
			}else
				break;
		}
	};

	// 說明: 設定頁面元素的屬性
	//	1.pbMode 沒傳入或為 true 時, 只會對有 dataType 的元素有作用
	//	2.如果元素中有 tagName = DIV or SPAN 時, 只需一次 getElementsByTagName 就會包含所有階層的元素, 不用再往下找
	vat.item.setAttributeByObject = function vatItem_setAttributeByObject(poItem, psProperty, pxValue, pbFilter, pbMask){
		function setAttrubiteByObject(poItem){
			function updatePropertys(poItems){
				for (var j=0; j < poItems.length; j++) updateProperty(poItems[j]);
			}
			function updateProperty(poItem){
				if ((typeof poItem.datatype === "string" && poItem.datatype !== "IDX" && poItem.datatype !== "DEL" && poItem.datatype !== "ROWID") ||
					 (vbPass && (typeof poItems.nodeType === "number" && poItems.nodeType === 1))){
					if (psProperty === "readOnly"){
						if ((typeof pbMask === "boolean" && pbMask) && (poItem.datatype !== "DATE")){
							//*** start of dynamic change readOnly for canGridModify/canGridAppend check
							var vbReadOnlyNew, vbReadOnlyOld;
							if (pxValue){
								vbReadOnlyNew = true;
							}else{
								if (typeof poItem.mode_readOnly === "boolean"){
									vbReadOnlyNew = poItem.mode_readOnly;
								}else if (typeof poItem.mode === "string" && poItem.mode === "READONLY"){
									vbReadOnlyNew = true;
								}else{
									vbReadOnlyNew = false;
								}
							}
							vbReadOnlyOld = typeof poItem.readOnly === "boolean" ? poItem.readOnly : false;
							if (vbReadOnlyNew !== vbReadOnlyOld){
								vat.item.setReadOnlyByObject(poItem, vbReadOnlyNew, true);
							}
							// end of dynamic change readOnly
						}else
							vat.item.setReadOnlyByObject(poItem, pxValue);
					}else poItem[psProperty] = pxValue;
				}
			}	// 只需檢查一次, 其餘為內部函數呼叫確定為物件, 不用特別檢查
			if (typeof poItem === "object" && poItem != null){
				if (poItem.tagName === "DIV" || poItem.tagName === "SPAN" ||
						poItem.tagName === "TD" || poItem.tagName === "TR" ||
						poItem.tagName === "FORM" || poItem.tagName === "TABLE"){
					//subdivision(poItem.getElementByTagName("DIV"));
					//subdivision(poItem.getElementsByTagName("SPAN"));
					updatePropertys(poItem.getElementsByTagName("INPUT"));
					updatePropertys(poItem.getElementsByTagName("SELECT"));
					updatePropertys(poItem.getElementsByTagName("TIME"));
					updatePropertys(poItem.getElementsByTagName("TEXTAREA"));
					updatePropertys(poItem.getElementsByTagName("IMG"));
					updatePropertys(poItem.getElementsByTagName("BUTTON"));
				}else	updateProperty(poItem);
			}
		}
		function subdivision(poItems){
			for (var j=0; j < poItems.length; j++) setAttrubiteByObject(poItems[j]);
		}
		var vbPass = typeof pbFilter === "boolean" && !pbFilter ? true : false;
		setAttrubiteByObject(poItem);
	}
	vat.item.setAttributeByName = function vatItem_setAttributeByName(psName, psProperty, pxValue, pbFilter, pbMask){
		var voItem = document.getElementById(psName);
		vat.item.setAttributeByObject(voItem, psProperty, pxValue, pbFilter, pbMask);
	};
	vat.item.setGridAttributeByName = function vatItem_setGridAttributeByName(psName, psProperty, pxValue, pbFilter){
		var vsName, voCols;
		for(var i=1; i < 25; i++){
			vsName = psName+"#"+i;	// 取得 <TD> 物件, 命名方式是: 欄位名稱 + # + Y 座標
			voCols = document.getElementById(vsName)
			if (typeof voCols === "object" && voCols != null){
				// grid 的 backgroundColor 會在 vat.item.setReadOnlyByObject 中自動抓取內定值, 不需要再傳入
				vat.item.setAttributeByObject(voCols, psProperty, pxValue, pbFilter);
			}else{
				break;
			}
		}
	};

	vat.item.moveGridByName = function vatItem_moveGridByName(psFrom, psTo){
		var vsName, voCols, voRows, voCol_F, voCol_T, vnX1, vnPtr, i;
		voCols	= document.getElementById(psTo+"#0");
		if (voCols !== null && (voCols.nodeName === "TD" || voCols.nodeName === "TH")){
			voRows 	= voCols.parentNode.parentNode.getElementsByTagName("TR");
			for (i=0; i < voRows.length; i++){
				vnX1 = voRows[i].id.indexOf("TR#");
				if (vnX1 !== -1){
					vnPtr  = parseInt(voRows[i].id.substring(vnX1+3, voRows[i].id.length));
					voCol_F = document.getElementById(psFrom	+ "#" + vnPtr);
					voCol_T = document.getElementById(psTo		+ "#" + vnPtr);
					if (voCol_F !== null && voCol_T !== null)
						voCol_T.parentNode.insertBefore( voCol_F, voCol_T.nextSibling );
				}
			}
		}
	};


	/*
	*
	* 說明: 設定頁面元素唯讀(readOnly)的屬性
	*  1.HTML_backgroundColor 是預設的背景顏色, backgroundColor 會自動以內定屬性判定是否要改變, grid 欄位不需要再傳入分隔行屬性
	*  2.mode 是凡屬於 vat 宣告欄位的預設屬性(內容值包含 readOnly, hidden),
	*  3.mode_readOnly 是目前欄位的唯讀屬性值, 用遮罩的唯讀不會改變
	*  4.readOnly 是系統的原始屬性, vat.block.canGridModify(true/false) 則是設定遮罩屬性, 當目前欄位屬性是可讀寫時才受影響
	*/
	vat.item.$style_gridBColor1		= "#efefef";
	vat.item.$style_gridBColor2		= "#dedede";
	vat.item.$style_inputModifiable	= "#FFFFFF";
	vat.item.$style_dateReadOnly  	= "0px";
	vat.item.$style_dateModifiable	= "1px solid #999999";
	vat.item.setReadOnlyByObject = function vatItem_setReadOnlyByObject(poItem, pbValue, pbCanGridModifyMode){
		if (typeof poItem === "object" && poItem !== null){
			switch(poItem.tagName){
				case "TEXTAREA":
				case "INPUT":
					// 各種 datatype 特殊處理
					if (poItem.datatype === "RADIO"){	// 如果是 RADIO
						var voItems = document.getElementsByName(poItem.name);
						if (voItems !== null && voItems.length > 0){
							for(var i=0; i<voItems.length; i++){
								voItem = document.getElementById(voItems[i].id);
								if (voItem !== null){
									voItem.readOnly = pbValue;
									voItem.setAttribute("disabled", pbValue);
								}
							}
						}
					}else{
						if (poItem.datatype === "DATE"){	// 如果是日期格式, 那麼讓輸入按鈕消失
							var voDateBtn = document.getElementById(poItem.id + "_button");
							if (typeof voDateBtn === "object" && voDateBtn){
								if (pbValue) voDateBtn.style.display = "none";
												else voDateBtn.style.display = "inline";
							}
						}else{
							if (pbValue) poItem.style.border = vat.item.$style_dateReadOnly;	// 如果非日期格式, 讓外框消失
											else poItem.style.border = vat.item.$style_dateModifiable;
							if (pbValue)
								poItem.setAttribute("readOnly", pbValue);												// 非日期格式才設定
							else
								poItem.removeAttribute("readOnly");
							// poItem.disabled = pbValue;
						}
						// 其他屬性處理
						if(poItem.datatype === "IMAGE" || poItem.datatype === "SELECT"){
							if (pbValue)
								poItem.setAttribute("disabled", true);
							else
								poItem.removeAttribute("disabled");
						}else	if(poItem.datatype === "CHECKBOX" ||
										 poItem.datatype === "CHECK" 		||
										 poItem.datatype === "XBOX"){
							if (pbValue){
								poItem.setAttribute("disabled", true);
								// poItem.indeterminate = poItem.checked;
								poItem.bonclick = poItem.onclick;
								poItem.onclick  = function(){return false};
							}else{
								poItem.removeAttribute("disabled");
								// poItem.indeterminate = false;
								if (typeof poItem.bonclick !== "undefined")
									poItem.onclick = poItem.bonclick;
								else
									poItem.removeAttribute("onclick");
							}
						}else if(poItem.datatype === "PICKER"){
							var voPickBtn = document.getElementById(poItem.id + "#pick");
							if (voPickBtn !== null){
								voPickBtn.setAttribute("disabled", pbValue);
							}
						}
						// 背景處理
						if (pbValue || (poItem.datatype === "CHECKBOX" ||
														poItem.datatype === "CHECK" 		||
								 						poItem.type === "XBOX")){
							if (typeof poItem.HTML_backgroundColor === "string")	// grid, 改變為預設的背景顏色
								poItem.style.backgroundColor = poItem.HTML_backgroundColor;
						}else{
							/*
							if (typeof poItem.HTML_backgroundColor === "object" && poItem.HTML_backgroundColor !== null){
								poItem.style.background = poItem.style.background;
							}else
							*/
							poItem.style.background = vat.item.$style_inputModifiable;
						}
					}
					break;
				case "IMG":
				case "BUTTON":
				case "SELECT":
					poItem.setAttribute("readOnly", pbValue);
					if (pbValue){
						poItem.setAttribute("disabled", true);
						poItem.bonclick = poItem.onclick;
						poItem.bonchange = poItem.onchange;
						poItem.onclick  = function(){return false};
					}else{
						poItem.removeAttribute("disabled");
						if (poItem.bonclick) poItem.onclick = poItem.bonclick;
							else poItem.removeAttribute("onclick");
						if (poItem.bonchange) poItem.onchange = poItem.bonchange;
							else poItem.removeAttribute("onchange");
					}
					break;
				default:
					poItem.readOnly = pbValue;
					break;
			}
			if (!(typeof pbCanGridModifyMode === "boolean" && pbCanGridModifyMode)){
				poItem.setAttribute("mode_readOnly", poItem.readOnly);
			}
		}
	}
})();



/*
*/
(function vatId_maker(){
	vat.block.pageIdMake = function vatBlock_pageIdMake(pnId){
		return "vatFD{"+vat.block.pageId(pnId)+"}";
	};
	vat.block.pageCountIdMake = function vatBlock_pageCountIdMake(pnId){
		return "vatFD{"+vat.block.pageId(pnId)+"}pageCount";
	};
	vat.block.pageThereIdMake = function vatBlock_pageThereIdMake(pnId){
		return "vatFD{"+vat.block.pageId(pnId)+"}pageThere";
	};

	vat.block.pageTrIdMake = function vatBlock_pageTrIdMake(pnId, pnAxisY){
		return "vatFD{"+vat.block.pageId(pnId)+"}TR#"+pnAxisY;
	};
	vat.block.pageTrNameMake = function vatBlock_pageTrNameMake(pnId, pnAxisY){
		return "vatFD{"+vat.block.pageId(pnId)+"}TR["+pnAxisY+"]";
	};
	vat.block.pageTrImgIdMake = function vatBlock_pageTrImgIdMake(pnId, pnAxisY){
		return "vatFD{"+vat.block.pageId(pnId)+"}TRIMG#"+pnAxisY;
	};
	vat.block.pageTrImgNameMake = function vatBlock_pageTrNameIdMake(pnId, pnAxisY){
		return "vatFD{"+vat.block.pageId(pnId)+"}TRIMG["+pnAxisY+"]";
	};
	vat.block.columnHereIdMake = function vatBlock_columnHereIdMake(pnId){
		return "vatFD{"+vat.block.pageId(pnId)+"}columnHere";
	};
	/*
	vat.block.talkMsgIdMake = function vatBlock_talkMsgIdMake(pnId){
		return "vatFD["+vat.block.pageId(pnId)+"]message";
	};
	vat.block.talkFileIdMake = function vatBlock_talkFileIdMake(pnId){
		return "vatFD["+vat.block.pageId(pnId)+"]talkFile";
	};
	vat.block.talkAnsIdMake = function vatBlock_talkAnsIdMake(pnId){
		return "vatFD["+vat.block.pageId(pnId)+"]talkAnswer";
	};
	vat.block.talkBtnOkIdMake = function vatBlock_talkBtnOkIdMake(pnId){
		return "vatFD["+vat.block.pageId(pnId)+"]buttonOk";
	};
	vat.block.talkBtnNoIdMake = function vatBlock_talkBtnNoIdMake(pnId){
		return "vatFD["+vat.block.pageId(pnId)+"]buttonNo";
	};
	*/
	vat.item.nameMake = function vatItem_nameMake(psItemName, pnLineIndex){
		return ("#form."+psItemName+"["+pnLineIndex+"]");
	};
	vat.item.IdMake = function vatItem_idMake(pnId, pnAxisY, pnAxisX){
		return "vatF#B"+pnId+"A#Y"+pnAxisY+"#X"+pnAxisX;
	};
})();



/**************************************************
 * dom-drag.js
 * 09.25.2001
 * www.youngpup.net
 **************************************************
 * 10.28.2001 - fixed minor bug where events
 * sometimes fired off the handle, not the root.
 **************************************************/
(function vatDrag(){
	vat.drag = {

    obj : null,

    init : function(o, oRoot, minX, maxX, minY, maxY, bSwapHorzRef, bSwapVertRef, fXMapper, fYMapper)
    {
        o.onmousedown    = vat.drag.start;

        o.hmode            = bSwapHorzRef ? false : true ;
        o.vmode            = bSwapVertRef ? false : true ;

        o.root = oRoot && oRoot != null ? oRoot : o ;

        if (o.hmode  && isNaN(parseInt(o.root.style.left  ))) o.root.style.left   = "0px";
        if (o.vmode  && isNaN(parseInt(o.root.style.top   ))) o.root.style.top    = "0px";
        if (!o.hmode && isNaN(parseInt(o.root.style.right ))) o.root.style.right  = "0px";
        if (!o.vmode && isNaN(parseInt(o.root.style.bottom))) o.root.style.bottom = "0px";
				// o.root.style.position = "absolute";

        o.minX    = typeof minX != 'undefined' ? minX : null;
        o.minY    = typeof minY != 'undefined' ? minY : null;
        o.maxX    = typeof maxX != 'undefined' ? maxX : null;
        o.maxY    = typeof maxY != 'undefined' ? maxY : null;

        o.xMapper = fXMapper ? fXMapper : null;
        o.yMapper = fYMapper ? fYMapper : null;

      //  o.root.ondragStart    = new Function();
      	o.root.ondragStart    = function(){ this.style.cursor = "hand";};
        o.root.ondragEnd    	= function(){ this.style.cursor = "auto";};
        o.root.ondrag        	= function(){ this.style.cursor = "move";};
    },

    start : function(e)
    {
        var o = vat.drag.obj = this;
        e = vat.drag.fixE(e);
        var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
        var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
        o.root.ondragStart(x, y);

        o.lastMouseX    = e.clientX;
        o.lastMouseY    = e.clientY;

        if (o.hmode) {
            if (o.minX != null)    o.minMouseX    = e.clientX - x + o.minX;
            if (o.maxX != null)    o.maxMouseX    = o.minMouseX + o.maxX - o.minX;
        } else {
            if (o.minX != null) o.maxMouseX = -o.minX + e.clientX + x;
            if (o.maxX != null) o.minMouseX = -o.maxX + e.clientX + x;
        }

        if (o.vmode) {
            if (o.minY != null)    o.minMouseY    = e.clientY - y + o.minY;
            if (o.maxY != null)    o.maxMouseY    = o.minMouseY + o.maxY - o.minY;
        } else {
            if (o.minY != null) o.maxMouseY = -o.minY + e.clientY + y;
            if (o.maxY != null) o.minMouseY = -o.maxY + e.clientY + y;
        }

        document.onmousemove    	= vat.drag.drag;
        document.onmouseup        = vat.drag.end;
        return false;
    },

    drag : function(e)
    {
        e = vat.drag.fixE(e);
        var o = vat.drag.obj;

        var ey    = e.clientY;
        var ex    = e.clientX;
        var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
        var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
        var nx, ny;

        if (o.minX != null) ex = o.hmode ? Math.max(ex, o.minMouseX) : Math.min(ex, o.maxMouseX);
        if (o.maxX != null) ex = o.hmode ? Math.min(ex, o.maxMouseX) : Math.max(ex, o.minMouseX);
        if (o.minY != null) ey = o.vmode ? Math.max(ey, o.minMouseY) : Math.min(ey, o.maxMouseY);
        if (o.maxY != null) ey = o.vmode ? Math.min(ey, o.maxMouseY) : Math.max(ey, o.minMouseY);

        nx = x + ((ex - o.lastMouseX) * (o.hmode ? 1 : -1));
        ny = y + ((ey - o.lastMouseY) * (o.vmode ? 1 : -1));

        if (o.xMapper)        	nx = o.xMapper(y)
        else if (o.yMapper)    	ny = o.yMapper(x)

        vat.drag.obj.root.style[o.hmode ? "left" : "right"] = nx + "px";
        vat.drag.obj.root.style[o.vmode ? "top" : "bottom"] = ny + "px";

        vat.drag.obj.lastMouseX    = ex;
        vat.drag.obj.lastMouseY    = ey;

        vat.drag.obj.root.ondrag(nx, ny);
        return false;
    },

    end : function()
    {
        document.onmousemove = null;
        document.onmouseup   = null;

        vat.drag.obj.root.ondragEnd(parseInt(vat.drag.obj.root.style[vat.drag.obj.hmode ? "left" : "right"]),
                              	parseInt(vat.drag.obj.root.style[vat.drag.obj.vmode ? "top" : "bottom"]));
        vat.drag.obj = null;
    },

    fixE : function(e)
    {
        if (typeof e == 'undefined') e = window.event;
        if (typeof e.layerX == 'undefined') e.layerX = e.offsetX;
        if (typeof e.layerY == 'undefined') e.layerY = e.offsetY;
        return e;
    }
	};
}());