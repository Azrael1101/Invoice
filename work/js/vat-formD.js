/*** 
 *	檔案：vat-formD.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */
// 變數宣告 vat & vat.formD, vat.formD.page 
	if (typeof vat === "undefined"){vat = function(){};}
	vat.formD =	{page	: new Array(), current : 0};
		
vat.formD.pageCreate = function vatFormD_pageCreate(pnId){
		pnId = (typeof pnId == "undefined") ? vat.formD.page.length+1 : pnId; 
		vat.formD.page[pnId] = { top : 0, left : 0, width : 0, height : 0, 
			laylout		: "grid",		// grid 
			binding		:	"ajax",		// ajax
			lines 		: 12,	/* 預設每頁行數 */ 
			itemName	: new Array(),
			itemDesc	: new Array(), /*[{x: 1}, {}, {}]*/
			firstIndex: 1,
			lastIndex : 0,
			dataCount : 0,
			hasInsert : false,
			hasChange : false,			
			canDataAppend : true,
			canDataModify : true,													
			canDataDelete : true
		};
};

vat.formD.itemVerify = function vatFormD_itemVerify(poDesc){
	var vbResult=false;
	if(typeof(poDesc) === "object"){
		try{
			// poDesc.vName    = poDesc.vName		? poDesc.vName		: ""; 		
			poDesc.type     = poDesc.type     ? poDesc.type.toUpperCase() : "TEXT";
			poDesc.mode     = poDesc.mode     ? poDesc.mode.toUpperCase() : "";	//** Hidden, Readonly, Password
			poDesc.mask     = poDesc.mask     ? poDesc.mask  		: "";
			poDesc.desc     = poDesc.desc     ? poDesc.desc  		: "";			
			poDesc.init     = poDesc.init     ? poDesc.init  		: "";
			poDesc.maxLen   = poDesc.maxLen   ? poDesc.maxLen		: 20;                                                                    
			poDesc.size     = poDesc.size     ? poDesc.size  		: 8; 
			poDesc.src		  = poDesc.src	  	? poDesc.src	 		: "";
			poDesc.io			  = poDesc.io	  		? poDesc.io		 		: 0;
			poDesc.classX   = poDesc.classX   ? poDesc.classX		: "defaultField";
			poDesc.picture 	= poDesc.picture 	? poDesc.picture 	: "";
			poDesc.onchange = poDesc.onchange ? " onchange='"+poDesc.onchange+"' " : "";
			poDesc.eChange 	= poDesc.eChange 	? " onchange='"+poDesc.eChange+"' " : "";
			poDesc.eClick 	= poDesc.eClick 	? " onclick='"+poDesc.eClick+"' " : "";			
			poDesc.eFocus   = poDesc.eFocus   ? " onfocus='"+poDesc.eFocus+"' " : "";
			poDesc.eBlur    = poDesc.eBlur    ? " onblur='"+poDesc.eBlur+"' " : "";			
			poDesc.eValid 	= poDesc.eValid 	? poDesc.eValid : "";			
			switch(poDesc.type){
				case "ROWID"	: poDesc.mode = "HIDDEN";	 	break;
				case "MSG"  	: poDesc.mode = "HIDDEN";	 	break;
				case "DEL"  	: poDesc.mode = "READONLY"; break;
				case "IDX"  	: poDesc.mode = "READONLY"; break;
				case "SELECT" : break;
				case "NUMB" 	:
				case "NUMM" 	: break;
			}
			switch(poDesc.mode){                                                                                                                                                         
				case "HIDDEN"  : poDesc.hType="HIDDEN";    poDesc.mode=""; break;
				case "PASSWORD": poDesc.hType="PASSWORD";  poDesc.mode=""; break;
				case "READONLY": poDesc.hType=poDesc.type;                 break;				
				default:         poDesc.hType=poDesc.type; poDesc.mode=""; break;
			}
			/*
			for (i=0; i < psName.length; i++){
				if (typeof(psName[i]) !== "string"){
					psName[i] = "";
				}
			}
			*/
			vbResult = true;
			// alert("caller:"+this.caller);
		}catch(e){                                                                                                                                                                
			vat.debug("developer", "表格的欄位設定錯誤:"+e);                                                                                                                               
		}
	}else{		
		vat.debug("developer", "傳入的參數型態("+typeof(poDesc)+")錯誤, 必須是個實字物件");
	}
	return vbResult;
};



vat.formD.pageLayout = function vatFormD_pageLayout(pnId, paOpt){
	var i, j, vsData, vsShift, vsColor;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	try{
		vat.formD.page[pnId].lines    = paOpt.pageSize ? paOpt.pageSize : 12;
		vat.formD.page[pnId].ajxDelAll= paOpt.deleteAllAjxService  ? paOpt.deleteAllAjxService  : "";			
		vat.formD.page[pnId].ajxDelAY = paOpt.deleteAllSuccess     ? paOpt.deleteAllSuccess     : "";
		vat.formD.page[pnId].ajxDelAN = paOpt.deleteAllFailure     ? paOpt.deleteAllFailure     : "";			                                                                                                                                                                      
		vat.formD.page[pnId].ajxLoadB = paOpt.loadBeforeAjxService ? paOpt.loadBeforeAjxService : "";			
		vat.formD.page[pnId].ajxLoadAY= paOpt.loadSuccessAfter     ? paOpt.loadSuccessAfter     : "";
		vat.formD.page[pnId].ajxLoadAN= paOpt.loadFailureAfter     ? paOpt.loadFailureAfter     : "";
		vat.formD.page[pnId].ajxSaveB = paOpt.saveBeforeAjxService ? paOpt.saveBeforeAjxService : "";
		vat.formD.page[pnId].ajxSaveAY= paOpt.saveSuccessAfter     ? paOpt.saveSuccessAfter     : "";
		vat.formD.page[pnId].ajxSaveAN= paOpt.saveFailureAfter     ? paOpt.saveFailureAfter     : "";						
		vat.formD.page[pnId].ajxAddB  = paOpt.appendBeforeService  ? paOpt.appendBeforeService  : "";
		vat.formD.page[pnId].ajxAddA  = paOpt.appendAfterService   ? paOpt.appendAfterService   : "";
		vat.formD.page[pnId].pageUpT  = paOpt.pageUpTopService	   ? paOpt.pageUpTopService     : "";		
		vat.formD.page[pnId].pageUpB  = paOpt.pageUpBeforeService  ? paOpt.pageUpBeforeService  : "";		
		vat.formD.page[pnId].pageUpA  = paOpt.pageUpAfterService   ? paOpt.pageUpAfterService   : "";
		vat.formD.page[pnId].pageDnB  = paOpt.pageDnBeforeService  ? paOpt.pageDnBeforeService  : "";		
		vat.formD.page[pnId].pageDnA  = paOpt.pageDnAfterService   ? paOpt.pageDnAfterService   : "";
		vat.formD.page[pnId].pageDnE  = paOpt.pageDnBottomService  ? paOpt.pageDnBottomService  : "";		
		vat.formD.page[pnId].lineDelB = paOpt.deleteBeforeService  ? paOpt.deleteBeforeService  : "";
		vat.formD.page[pnId].lineDelA = paOpt.deleteAfterService   ? paOpt.deleteAfterService   : "";
		vat.formD.page[pnId].eventSrv	= paOpt.eventService		     ? paOpt.eventService 		    : "";
		vat.formD.page[pnId].eventSA  = paOpt.eventSuccessAfter    ? paOpt.eventSuccessAfter    : "";
		vat.formD.page[pnId].eventFA  = paOpt.eventFailureAfter    ? paOpt.eventFailureAfter    : "";						
		vat.formD.page[pnId].canDataDelete	=	paOpt.canDataDelete ? true : false;
		vat.formD.page[pnId].canDataAppend 	=	paOpt.canDataAppend ? true : false;
		vat.formD.page[pnId].canDataModify 	=	paOpt.canDataModify ? true : false;
 	}catch(e){                                                                                                                                                                
		vat.debug("developer", "錯誤的設定參數:"+e);                                                                                                                               
	}
			/*	"<IMG src='images/24x24/Save.png'     align='middle' style='CURSOR:Hand' onclick='vat.formD.pageSave("+pnId+")'    >&nbsp"+					
					"<IMG src='images/24x24/Cancel.png'   align='middle' style='CURSOR:Hand' onclick='vat.formD.pageCancel("+pnId+")'  >&nbsp"+
					"<IMG src='images/24x24/Remove.png'   align='middle' style='CURSOR:Hand' onclick='vat.formD.pageRemove("+pnId+")'  >&nbsp"+					
					"<IMG src='images/24x24/Search.png'   align='middle' style='CURSOR:Hand' onclick='vat.formD.pageSearch("+pnId+")'  >&nbsp"+
					"<IMG src='images/import.png'	height='26' width='26' alt='把 EXCEL 資料匯入到表格' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageImport("+pnId+")'>&nbsp"+
					"<IMG src='images/export.png' height='26' widht='26' alt='把表格匯出成 EXCEL 檔案' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageExport("+pnId+")'>&nbsp"+
					"<INPUT id='"+vat.formD.talkMsgIdMake(pnId)  +"' type='TEXT' style='vertical-align:middle;border:0;display:none' value=''>&nbsp"+
					"<INPUT id='"+vat.formD.talkAnsIdMake(pnId)  +"' type='FILE' size='20' maxlength='100' style='vertical-align:middle;display:none;'>&nbsp"+
					"<INPUT id='"+vat.formD.talkFileIdMake(pnId) +"' type='FILE' size='20' maxlength='100' style='vertical-align:middle;display:none;' value='c:\\export.xls'>&nbsp"+									
					"<INPUT id='"+vat.formD.talkBtnOkIdMake(pnId)+"' type='BUTTON' value='確定' size='1' style='vertical-align:middle;display:none;'>&nbsp"+
					"<INPUT id='"+vat.formD.talkBtnNoIdMake(pnId)+"' type='BUTTON' value='取消' size='1' style='vertical-align:middle;display:none;'>&nbsp"+
								
			*/
	vsTB1 =	"<table class='defaultNone' border='0' cellpadding='3' cellspacing='0'>"+
	       	"<tr style='height:300px'>"+
	       	"<td align='left' rowspan='1' valign='center' colspan='1'>"+
				 	"<table class='default'     border='0' cellspacing='2' cellpadding='2'>"+
					"<tr><td width='100%' colspan='"+vat.formD.page[pnId].itemDesc.length+"'><table width='100%'><tr>"+
					"<td align='left' align='left'>"+
					"<IMG src='images/24x24/Add.png'      alt='新增空白頁' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageAdd("+pnId+")'>&nbsp&nbsp&nbsp"+
					"<IMG src='images/24x24/Home.png'     alt='翻到第一頁' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageTop("+pnId+")'>&nbsp"+										
					"<IMG src='images/24x24/Backward.png' alt='向前翻一頁' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageBackward("+pnId+")'>&nbsp"+							
					"<IMG src='images/24x24/Forward.png'  alt='往後翻一頁' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageForward("+pnId+")' >&nbsp"+
					"<IMG src='images/24x24/Book.png'  alt='翻到最後一頁' align='middle' style='CURSOR:Hand' onclick='vat.formD.pageBottom("+pnId+")'>&nbsp"+					
					"&nbsp&nbsp&nbsp共有資料"+
					"<INPUT id='"+vat.formD.pageCountIdMake(pnId)+"' type='TEXT' size='1' style='text-align:right;vertical-align:middle;border:0;' value='0' READONLY>&nbsp頁,&nbsp目前顯示第"+						
					"<INPUT id='"+vat.formD.pageHereIdMake(pnId) +"' type='TEXT' size='1' style='text-align:right;vertical-align:middle;border:0;' value='1'         >&nbsp頁&nbsp&nbsp&nbsp"+
					"<IMG src='images/24x24/Search.png'   align='middle' style='CURSOR:Hand' onclick='vat.formD.pageSearch("+pnId+")'  >&nbsp"+					
					"</td><td align='right'>"+
					"<IMG src='images/24x24/Refresh.png'  alt='頁面重新整理' align='middle' style='CURSOR:Hand' onclick='vat.formD.eventService("+pnId+")' >&nbsp"+					
					"</td>"+
					"</tr></table></td></tr>";
	vsTH = 	"<tr style='width: 100%'>";
	for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
		if (vat.formD.page[pnId].itemDesc[j].hType != "HIDDEN"){
			vsTH += "<th class='brownTableTitle'>" + vat.formD.page[pnId].itemDesc[j].desc + "</th>";
		}	
	}
	vsTH += "</tr>";
	vsData = "";
	vsShift = false;
	for(i=0; i<vat.formD.page[pnId].lines; i++){
		if (vsShift){
			vsShift = false;
			vsColor = " background-color: #efefef;";
		}else{
			vsShift = true;
			vsColor = " background-color: #dedede;";			
		}
		vsData += "<tr id='"+vat.formD.pageTrIdMake(pnId, i)+"' style='"+vsColor+"'>";
		for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
			vsItemHTML 	 = vat.formD.page[pnId].itemDesc[j].hType;  
			vsItemMode   = vat.formD.page[pnId].itemDesc[j].mode;   
			vsItemMaxlen = vat.formD.page[pnId].itemDesc[j].maxLen; 
			vsItemSize	 = vat.formD.page[pnId].itemDesc[j].size;   
			vsItemClass  = vat.formD.page[pnId].itemDesc[j].classX; 
			vsItemValue  = vat.formD.page[pnId].itemDesc[j].value; 
			vsItemStyle  = "";
			vsItemevent  = vat.formD.page[pnId].itemDesc[j].eventX; 
			vsItemChange = vat.formD.page[pnId].itemDesc[j].onchange;
			vsEventFocus = vat.formD.page[pnId].itemDesc[j].eFocus; 
			vsEventBlur	 = vat.formD.page[pnId].itemDesc[j].eBlur; 
//		vsItemName	 = vat.formD.page[pnId].itemDesc[j].itemName;
			vsItemSrc		 = vat.formD.page[pnId].itemDesc[j].src;
			//*** td 
			vsData += "<td rowspan='1' valign='center' colspan='1' align='center' ";
			switch (vsItemHTML){
			case "HIDDEN":
				vsData += " style='display:none' "; break;
			}
			if (vat.formD.page[pnId].itemDesc[j].type == "IDX"){
				vsData += "  style='padding=0;'";    /* width='20px;'*/
			}
			vsData += ">";
			//*** item
			switch (vat.formD.page[pnId].itemDesc[j].type){
			case "IDX":
				vsItem = "<INPUT id='"+vat.formD.itemIdMake(pnId, i, j)+"' name='idx' size='1' maxlength='25' style='padding:0;border:0;"+vsColor+"' READONLY>";
				vsItem +=	 "<IMG id='"+vat.formD.pageTrImgIdMake(pnId, i)+"' src='images/16x16/Document.png' align='middle' style='visibility:hidden;padding=0;'>";				
				break;
			case "DEL":
				vsItem = "<IMG id='"+vat.formD.itemIdMake(pnId, i, j)+"' name='del' src='images/16x16/trash-empty.png'"+
									 			" onclick='vat.formD.lineDeleteClick("+pnId+","+i+", this)' alt='刪除此筆資料'";
				if (! vat.formD.page[pnId].canDataDelete){
					vsItem += " style='visibility:hidden' ";
				}					 			
				vsItem +=	">";
				break;
			case "ROWID":
				vsItem = "<INPUT id='"+vat.formD.itemIdMake(pnId, i, j)+"' name='idx' size='25' type='HIDDEN' READONLY>";
				break;
			case "SELECT":
				vsInit = vat.formD.page[pnId].itemDesc[j].init; 
				if (vsInit &&	vsInit instanceof Array &&	vsInit.length >= 3){
					vsItem = "<SELECT id='"+vat.formD.itemIdMake(pnId, i, j)+"' size='1'"+
													" name='"+vat.formD.itemNameMake(i, vat.formD.page[pnId].itemName[j])+"'>";
					if (vsInit[1] && vsInit[1] instanceof Array){
						for(k=0;k<vsInit[1].length;k++){
							vsItem += "<option value="+vsInit[2][k];
							if (vsInit[0][1] == vsInit[2][k]){
								// vsItem += " selected ";
							}
							vsItem += ">"+vsInit[1][k]+"</option>";
						}
					}
					vsItem += "</SELECT>";
				}						 
				break;
			case "BUTTON":
				vsItem = "<INPUT id='"+vat.formD.itemIdMake(pnId, i, j)+"' value='"+vsItemValue+"'"+
										" class='"+vsItemClass+"' "+
										" type='"+vsItemHTML+"'"+" size='"+vsItemSize+"' maxlength='"+vsItemMaxlen+"'"+
										" name='"+vat.formD.itemNameMake(i, vat.formD.page[pnId].itemName[j])+"' "+
										" src='"+vsItemSrc+"' "+
										vat.formD.page[pnId].itemDesc[j].eBlur+
										vat.formD.page[pnId].itemDesc[j].eFocus+
										vat.formD.page[pnId].itemDesc[j].eClick+
										vat.formD.page[pnId].itemDesc[j].eChange+										
										vsItemChange+">";
				break;			
			case "IMAGE":
				vsItem = "<INPUT id='"+vat.formD.itemIdMake(pnId, i, j)+"' value='"+vsItemValue+"'"+
										" class='"+vsItemClass+"' "+vsItemMode+" style='"+vsItemStyle+"'"+
										" type='"+vsItemHTML+"'"+" size='"+vsItemSize+"' maxlength='"+vsItemMaxlen+"'"+
										" name='"+vat.formD.itemNameMake(i, vat.formD.page[pnId].itemName[j])+"' "+
										" src='"+vsItemSrc+"'"+
										vsItemChange+">";
				break;				
			default:
				if (vsItemMode =="READONLY"){
					vsItemStyle += "border:0;"+vsColor;
				}
				switch (vat.formD.page[pnId].itemDesc[j].type){
				case "NUMB":
				case "NUMM":
					vsItemStyle += " text-align:right; ";
					break;
				default:
					vsItemStyle += " text-align:left; ";
					break;
				}							
				vsItem = "<INPUT id='"+vat.formD.itemIdMake(pnId, i, j)+"' value='"+vsItemValue+"'"+
										" class='"+vsItemClass+"' "+vsItemMode+" style='"+vsItemStyle+"'"+
										" type='"+vsItemHTML+"'"+" size='"+vsItemSize+"' maxlength='"+vsItemMaxlen+"'"+
										" name='"+vat.formD.itemNameMake(i, vat.formD.page[pnId].itemName[j])+"' "+
										vsItemChange+vsEventFocus+vsEventBlur+">";
				// if (i < 1)	vat.debug("vat", i+":"+vsItem);
				break;	
			}
			vsData += (vsItem + "</td>");
		} 
		vsData += "</tr>";
	}
	vsTB9 = "</table></td></tr></table>";
	document.write(vsTB1 + vsTH + vsData + vsTB9);
};



vat.formD.pageDataBind = function vatFormD_pageDataBind(pnId, pnGridDataIndex, pnGridDataCount, paGridData, paGridName){
	var i, j, vxDataObj, vxDataValue;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	pnGridDataIndex = eval(pnGridDataIndex);
	pnGridDataCount = eval(pnGridDataCount);
	if (typeof(vat.formD.page[pnId]) != "undefined"){	
		vsShift = false;	
		for (i=0; i<vat.formD.page[pnId].lines; i++){
			if (vsShift){
				vsShift = false;
				vsColor = "#efefef";
			}else{
				vsShift = true;
				vsColor = "#dedede";			
			}
			for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
				vxDataObj=vat.formD.itemGetById(pnId, i, j);
				if (vxDataObj){
					if ((pnGridDataCount === 0) ||	//*** 如果無傳回筆數, 新增一空白頁或 
							(i >= pnGridDataCount)){		//*** 如果筆數不滿一頁, 填入空白行
						if (vat.formD.page[pnId].canDataAppend){
							vbDataModifiable = true;
							vxDataValue = vat.formD.itemInit(pnId, j);				//*** 設定為初始值
						}else{
							vbDataModifiable = false;
						}
					}else{
						if (paGridData instanceof Array && //*** 檢核傳回的資料是否為陣列內容
								paGridData[pnGridDataIndex] && j < paGridData[pnGridDataIndex].length){
							if (vat.formD.page[pnId].canDataModify){
								vbDataModifiable = true;
							}else{
								vbDataModifiable = false;								
							}	
							vxDataValue = paGridData[pnGridDataIndex][j];	//*** 並以傳回的'資料名稱'陣列數控制回填內容
						}else{
							// vat.debug("developer", "DataGrid ID:"+vat.formD.itemIdMake(pnId, i, j)+", row:"+pnGridDataIndex+" data lose, count:"+pnGridDataCount);						
							if (vat.formD.page[pnId].canDataAppend){
								vbDataModifiable = true;							
								vxDataValue = vat.formD.itemInit(pnId, j);			//*** 設定為初始值
							}else{
								vbDataModifiable = false;							
							}
						}	
					}
					if ("READONLY" == vat.formD.page[pnId].itemDesc[j].mode){
					}else{
						vxDataObj.readOnly = false;					
						if (vbDataModifiable){
							vxDataObj.removeAttribute("backgroundColor");
							vxDataObj.removeAttribute("border");
						}else{
							vxDataObj.style.border = 0;
							vxDataObj.style.backgroundColor = vsColor;
							vxDataObj.readOnly = true;
						}
					}
					if ((paGridName instanceof Array) && (j < paGridName.length)){
						vxDataObj.setAttribute("name", vat.formD.itemNameMake(pnGridDataIndex, paGridName[j]));
					}else{
						vxDataObj.setAttribute("name", vat.formD.itemNameMake(pnGridDataIndex, vat.formD.page[pnId].itemName[j]));
					}					
					//if (i < 1) vat.debug("vat", "row"+i+", col:"+j+", type:"+vat.formD.page[pnId].itemDesc[j].type);
					if (vxDataValue !== null){				// modify for excel import
						switch (vat.formD.page[pnId].itemDesc[j].type){
						case "DEL":
							vxDataObj.value = vxDataValue;
							vat.formD.lineDeleteState(pnId, i, vxDataObj);
							break;
						case "MSG":
							vxDataObj.value = vxDataValue;
							var vxLineObj = document.getElementById(vat.formD.pageTrImgIdMake(pnId, i));
							if (vxDataValue && vxDataValue.length > 0){
								vxLineObj.alt = vxDataValue;
								vxLineObj.src = "images/16x16/Chat Bubble.png";
								vxLineObj.style.visibility = "visible";
							}else{
								vxLineObj.style.visibility = "hidden";
							}	
							break;
						case "CHECKBOX":
							if ("1" == vxDataValue){
								vxDataObj.checked = true;
							}else{
								vxDataObj.checked = false;
							}
							break;
						case "BUTTON":
							break;	
						case "SELECT":
							var vbHasSelected = false;
							var vnDefaultIndex = 0;
							for (var k=0; k<vxDataObj.options.length; k++){
								if (vxDataObj.options[k].value == vat.formD.page[pnId].itemDesc[j].init[0][2]){
									vnDefaultIndex = k;
								}
								if (vxDataObj.options[k].value == vxDataValue){
									vxDataObj.options[k].selected = true;
									vbHasSelected = true;
								}else{
									vxDataObj.options[k].selected = false;
								}
							}
							if (!vbHasSelected){
								vxDataObj.options[vnDefaultIndex].selected = true;
							}
							// vat.debug("vat", "row:"+i+", j:"+j+" value:"+vxDataValue+", index:"+vxDataObj.selectedIndex+", has Select:"+vbHasSelected);
							break;						
						case "IMG":
							break;
						case "ROWID":
							vxDataObj.value = vxDataValue;
							vxLineObj = document.getElementById(vat.formD.pageTrImgIdMake(pnId, i));
							// alert(vat.formD.pageTrImgIdMake(pnId, i));
							if (vxLineObj){
								vxLineObj.rowid = vxDataValue;
								vxLineObj.onclick = function(){alert("Row Id:"+this.rowid)};
							}
							break;													
						default:
							switch (vat.formD.page[pnId].itemDesc[j].type){
							case "NUMM":
								vxDataObj.original = vxDataValue;	
								vxDataObj.value = vat.formD.itemFormatNumm(vxDataValue);
								break;
							default:
								vxDataObj.value = vxDataValue;
								break;
							}							
							vxDataObj.setAttribute("template", vat.formD.page[pnId].itemDesc[j].mask);
							vxDataObj.setAttribute("datatype", vat.formD.page[pnId].itemDesc[j].type);					
							break;
						}
					}	
				}else{
					vat.debug("developer", "DataGrid ID:"+vat.formD.itemIdMake(pnId, i, j)+" HTML element not found");
				}
			}
			pnGridDataIndex++;
		}
	}else{
		vat.debug("developer", "指定的資料表格 (Grid Data) ID:"+pnId+", 相關設定不存在");
	}	
};


vat.formD.pageDataLoad = function vatFormD_pageDataLoad(pnId, pnThisPageNo, pxOpt){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	var vaGridNameArray = null, vaGridDataArray = new Array();
	var vsProcessString = vat.jr(vat.formD.page[pnId].ajxLoadB, "")+"&startPage="+pnThisPageNo+"&pageSize="+vat.formD.page[pnId].lines;
	vat.ajax.XHRequest(
	 { sId: "vat.formD.pageDataLoad()/"+vat.callerName(vat.formD.pageDataLoad.caller)+"()",
		post: vsProcessString,	 	
		asyn: (pxOpt ? (typeof pxOpt.asyn == "boolean" ? pxOpt.asyn : true) : true), 	 
		lose: function vatFormD_pageDataLoadAjxRequestLose(){vat.jr(vat.formD.page[pnId].ajxLoadAN)}, 	 	   
	 	find: function vatFormD_pageDataLoadAjxRequestFind(oXHR){ 
						vat.formD.page[pnId].firstIndex = parseInt(vat.ajax.getValue("gridLineFirstIndex", oXHR.responseText));
						vat.formD.page[pnId].lastIndex  = parseInt(vat.ajax.getValue("gridLineMaxIndex"  , oXHR.responseText));
						vat.formD.page[pnId].dataCount  = parseInt(vat.ajax.getValue("gridRowCount"      , oXHR.responseText));
						vaGridNameArray = vat.utils.strToArray(vat.ajax.getValue("fieldNames", oXHR.responseText));
						vaGridDataArray = vat.utils.strTwoDArray(vat.ajax.getValue("gridData", oXHR.responseText), 
																										 vat.formD.page[pnId].firstIndex, vaGridNameArray.length, 
																										 vat.formD.page[pnId].dataCount);
						/*  vat.debug("vat", "Index:"+vat.formD.page[pnId].firstIndex+"/Count:"+vat.formD.page[pnId].dataCount+
										 							 "/Total:"+vat.formD.page[pnId].lastIndex+
																	 "/responseText:"+vat.ajax.xmlHttp.responseText);	*/
						var xTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
						if (xTotalObj){
							xTotalObj.value = Math.ceil(vat.formD.page[pnId].lastIndex/vat.formD.page[pnId].lines);
							var vxPageHereObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
							if (vxPageHereObj){
							}else{
								vat.debug('developer', "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" HTML element not found");
							}
						}else{
							vat.debug('developer', "DataGrid ID:"+vat.formD.pageCountIdMake(pnId)+" HTML element not found");
						}
						var vbIsDataBind = true;
						if (pxOpt){
							vbIsDataBind = pxOpt.noBind ? false : true;
							if (pxOpt.funcSuccess){
								pxOpt.funcSuccess();
							}
						}
						if (vbIsDataBind){
							vat.formD.pageDataBind(pnId, vat.formD.page[pnId].firstIndex, vat.formD.page[pnId].dataCount, vaGridDataArray, vaGridNameArray);
						}							
						vat.jr(vat.formD.page[pnId].ajxLoadAY);
					}});
 	// 頁面重新 load 進來, 新增的狀態就解除了
		vat.formD.page[pnId].hasInsert  = false;
		return vaGridDataArray;
};


vat.formD.pageDataSave = function vatFormD_pageDataSave(pnId, pxOpt){
	var vsP1, vsP2, vbResult = false;
	var vaGridDataArray, vsReturnGridData;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;	
	vaGridDataArray = vat.formD.pageDataBack(pnId);
	if (vaGridDataArray.length > 0){
		vxDataObj = vat.formD.itemGetById(pnId, 0, 0);
		vnGridLineFirstIndex = vat.formD.itemIndexNo(vxDataObj.name);
		vsReturnGridData = vat.utils.twoDArrayToStr(vaGridDataArray, vnGridLineFirstIndex);
				vat.ajax.XHRequest(
				{  sId: "vat.formD.pageDataSave()/"+vat.callerName(vat.formD.pageDataSave.caller)+"()",
					post: vat.jr(vat.formD.page[pnId].ajxSaveB, "")+
								"&gridRowCount="+vat.formD.page[pnId].lines+
			  				"&gridLineFirstIndex="+vnGridLineFirstIndex+
			  				"&gridData="+vsReturnGridData,
					asyn: (pxOpt ? (typeof pxOpt.asyn == "boolean" ? pxOpt.asyn : true) : true),			
					lose: function vatFormD_pageDataSaveAjxRequestLose(){ vat.debug("user", "資料無法存檔");}, 	 	   
				 	find: function vatFormD_pageDataSaveAjxRequestFind(oXHR){ 				
									if (pxOpt){
										if (pxOpt.funcSuccess){
											pxOpt.funcSuccess();
										}	
									}
									vsP1 = vat.formD.page[pnId].ajxSaveAY.indexOf("(", 0);
									vsP2 = vat.formD.page[pnId].ajxSaveAY.indexOf(")", 0);
									vat.jr(vat.formD.page[pnId].ajxSaveAY);
								}
				});
		vbResult = true;		
	}			 
	return vbResult;
};
	


vat.formD.pageDataBack = function vatFormD_pageDataBack(pnId){
	var i, j, vxDataObj
	var vnGridLineFirstIndex, vnGridDataIndex, vaGridDataArray = new Array();	
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vxDataObj = vat.formD.itemGetById(pnId, 0, 0); 
		if (vxDataObj){
			vnGridLineFirstIndex = vat.formD.itemIndexNo(vxDataObj.name);						// 第一個元素的行號
			if (vnGridLineFirstIndex == -1){																				//   如果有載入過資料, 就不會是 -1
				vat.debug("developer", "formD layout define error: "+vxDataObj.name);
			}else{
				vaGridDataArray = new Array(vat.formD.page[pnId].lines);
				vnGridDataIndex = vnGridLineFirstIndex;
				for (i=0; i<vat.formD.page[pnId].lines; i++){
					vaGridDataArray[vnGridDataIndex] = new Array(vat.formD.page[pnId].itemDesc.length) ;	
					for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
						vxDataObj=vat.formD.itemGetById(pnId, i, j);
						if (vxDataObj){
							switch (vat.formD.page[pnId].itemDesc[j].type){						
							case 'CHECKBOX':
								if (vxDataObj.checked){
									vaGridDataArray[vnGridDataIndex][j] = "1";
								}else{
									vaGridDataArray[vnGridDataIndex][j] = "0";
								}							
								break;
							case "SELECT":
								for (var k=0; k<vxDataObj.options.length; k++){
									if (vxDataObj.options[k].selected) {
										vaGridDataArray[vnGridDataIndex][j] = vxDataObj.options[k].value;
										break;
									}
								}
								break;
							case "NUMM":
								vsOriginal = vxDataObj.getAttribute("original");
								if (typeof vsOriginal == "string"){
									if (typeof vsOriginal.replace == "function"){
										vaGridDataArray[vnGridDataIndex][j] = vsOriginal.replace(",", "");
									}else{
										vaGridDataArray[vnGridDataIndex][j] = vsOriginal;
									}	
								}else{
									vaGridDataArray[vnGridDataIndex][j] = vxDataObj.getAttribute("value");
								}	
							default:
								vaGridDataArray[vnGridDataIndex][j] = vxDataObj.getAttribute("value");
							}
						}
					}
					vnGridDataIndex++;
				}
			}
		}else{
			vat.debug("developer", "找不到 Page 元素");
		}
	}
	return vaGridDataArray;
}

		





/******************************************************************
 *  說明：Data Grid 頁面操作
 *    		
 */
vat.formD.pageBackward = function vatFormD_pageBackward(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.jr(vat.formD.page[pnId].pageUpB);		
		vat.formD.pageDataSave(pnId, 
		{	funcSuccess: function vatFormD_pageBackwardLoadSuccess(){
				var vxDataObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
				if (vxDataObj){			
					var vxDataValue = eval(vxDataObj.value);
					if (vxDataObj.value > 1){
						vxDataObj.value--;
					}else{
						if (vat.formD.page[pnId].pageUpT === "" || vat.jr(vat.formD.page[pnId].pageUpT)){
							vat.debug("user", "已經是第一頁");
						}	
					}
					vat.formD.pageDataLoad(pnId, vxDataObj.value);
					vat.jr(vat.formD.page[pnId].pageUpA);
				}else{
					vat.debug("developer", "無頁面控制 HTML 元素:"+vat.formD.pageHereIdMake(pnId));
				}}, 
			funcFailure: function vatFormD_pageBackwardLoadFailure(){
				vat.debug("user", "資料存檔失敗");
			}
		});	
	};
};


vat.formD.pageForward = function vatFormD_pageForward(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.jr(vat.formD.page[pnId].pageDnB);	
		vat.formD.pageDataSave(pnId, 
		{	funcSuccess: function vatFormD_pageForwardLoadSuccess(){
				var vxDataObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
				var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));				
				if (vxDataObj && vxTotalObj){
					var vxDataValue  = eval( vxDataObj.value);
					var vxTotalValue = eval(vxTotalObj.value);			
					if (vxDataValue < vxTotalValue){
						vxDataObj.value++;
					}else{
						if (vat.formD.page[pnId].pageDnE == "" || vat.jr(vat.formD.page[pnId].pageDnE)){					
							vat.debug("user", "已經是最後一頁");
						}	
					}
					vat.formD.pageDataLoad(pnId, vxDataObj.value);
					vat.jr(vat.formD.page[pnId].pageDnA);
				}else{
					vat.debug("developer", "無頁面控制 HTML 元素："+vat.formD.pageHereIdMake(pnId)+" or "+vat.formD.pageCountIdMake(pnId));
				}}, 
			funcFailure: function vatFormD_pageForwardLoadFailure(){
				vat.debug("user", "資料存檔失敗");
			}
		});	
	}	
};	



vat.formD.pageSearch = function vatFormD_pageSearch(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.formD.pageDataSave(pnId, 
		{	funcSuccess: function vatFormD_pageBackwardLoadSuccess(){
				var vxDataObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
				var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
				if (vxDataObj && vxTotalObj){
					vxDataObj.value  = Math.max(1, Math.min(eval(vxDataObj.value), eval(vxTotalObj.value)));
					vat.formD.pageDataLoad(pnId, vxDataObj.value);	
				}else{
					vat.debug("developer", "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" or "+vat.formD.pageCountIdMake(pnId)+" HTML element not found");
				}}, 
			funcFailure: function vatFormD_pageBackwardLoadFailure(){
				vat.debug("user", "資料存檔失敗");
			}
		});
	}	
};
vat.formD.pageTop = function vatFormD_pageTop(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.formD.pageDataSave(pnId, 
		{	funcSuccess: function vatFormD_pageBackwardLoadSuccess(){
				var vxDataObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
				var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
				if (vxDataObj && vxTotalObj){
					vxDataObj.value  = 1;
					vat.formD.pageDataLoad(pnId, vxDataObj.value);	
				}else{
					vat.debug("developer", "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" or "+vat.formD.pageCountIdMake(pnId)+" HTML element not found");
				}}, 
			funcFailure: function vatFormD_pageBackwardLoadFailure(){
				vat.debug("user", "資料存檔失敗");
			}
		});
	}	
};
vat.formD.pageBottom = function vatFormD_pageBottom(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.formD.pageDataSave(pnId, 
		{	funcSuccess: function vatFormD_pageBackwardLoadSuccess(){
				var vxDataObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
				var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
				if (vxDataObj && vxTotalObj){
					vxDataObj.value  = vxTotalObj.value;
					vat.formD.pageDataLoad(pnId, vxDataObj.value);	
				}else{
					vat.debug("developer", "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" or "+vat.formD.pageCountIdMake(pnId)+" HTML element not found");
				}}, 
			funcFailure: function vatFormD_pageBackwardLoadFailure(){
				vat.debug("user", "資料存檔失敗");
			}
		});
	}	
};

vat.formD.pageRefresh = function vatFormD_pageRefresh(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		var vxDataObj  = document.getElementById(vat.formD.pageHereIdMake(pnId));
		var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
		if (vxDataObj && vxTotalObj){
			vxDataObj.value  = Math.max(1, Math.min(eval(vxDataObj.value), eval(vxTotalObj.value)));
			vat.formD.pageDataLoad(pnId, vxDataObj.value);
		}else{
			vat.debug("developer", "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" or "+
														 vat.formD.pageCountIdMake(pnId)+" HTML element not found");
		}
	}
};		


vat.formD.eventService = function vatFormD_eventService(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined"){
		vat.jr(vat.formD.page[pnId].eventSrv);
	}
};		



vat.formD.pageAdd = function vatFormD_pageAdd(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(vat.formD.page[pnId]) != "undefined" && vat.formD.page[pnId].canDataAppend){
		if (vat.jr(vat.formD.page[pnId].ajxAddB)){
			if (vat.formD.pageDataSave(pnId)){
				vat.formD.page[pnId].hasInsert  = true;
				vat.formD.page[pnId].firstIndex = parseInt(vat.formD.page[pnId].firstIndex) + parseInt(vat.formD.page[pnId].lines);
				vat.formD.page[pnId].lastIndex  = parseInt(vat.formD.page[pnId].lastIndex ) + parseInt(vat.formD.page[pnId].lines);
				vat.formD.page[pnId].dataCount  = 0;
				var vxTotalObj = document.getElementById(vat.formD.pageCountIdMake(pnId));
				if (vxTotalObj){
					vxTotalObj.value++;
					var vxPageHereObj = document.getElementById(vat.formD.pageHereIdMake(pnId));
					if (vxPageHereObj){
						vat.formD.pageDataLoad(pnId, (vxPageHereObj.value = vxTotalObj.value),
						{	funcSuccess: function vatFormD_pageAddSuccess(){
								vat.jr(vat.formD.page[pnId].ajxAddA);
							}
						});
					}else{
						vat.debug('developer', "DataGrid ID:"+vat.formD.pageHereIdMake(pnId)+" HTML element not found");
					}
				}else{
					vat.debug('developer', "DataGrid ID:"+vat.formD.pageCountIdMake(pnId)+" HTML element not found");
				}
			}
		}		
	}
};


vat.formD.pageSave = function vatFormD_pageSave(pnId){
	vat.formD.pageDataSave(pnId);
}
vat.formD.pageCancel = function(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
};
vat.formD.pageRemove = function(pnId){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
};

vat.formD.lineMove = function vatFormD_lineMove(psItemId, pnStep){
	var x1, x2, x3, xx, obj;
	x1 = psItemId.indexOf("A#Y", 0);
	if (x1 != -1){
		x2 = psItemId.indexOf("#X",0);
		x3 = parseInt(psItemId.substring(x1+3, x2));
		xx = 	psItemId.substring(0, x1+3)+																//*** before [ name
					Math.max(0, x3+pnStep) +
					psItemId.substring(x2);							
		/*  	Math.max(0, Math.min(vat.formD.page[pnId].lines, x3+pnStep))+		line subscript, 先不檢查超過太大		*/
																	//***  after ] name
		obj = document.getElementById(xx);
		if (obj && (psItemId != xx)){
			obj.focus();
		}	
	}				
};



vat.formD.lineEnhanceClick = function vatFormD_lineEnhanceClick(pnId, pnLineIndex, pxThisObj){
	var vxItemObj = document.getElementById(vat.formD.pageTrIdMake(pnId, pnLineIndex));
	if (vxItemObj){
		if (pxThisObj.value == "1"){
			pxThisObj.setAttribute("value", "0");
			var saveBgColor = vxItemObj.style.getAttribute("saveBgColor");
			if (saveBgColor){
				vxItemObj.style.setAttribute("backgroundColor", saveBgColor);
			}
		}else{
			pxThisObj.setAttribute("value", "1");
			vxItemObj.style.setAttribute("saveBgColor", vxItemObj.style.getAttribute("backgroundColor")); 
			vxItemObj.style.backgroundColor = "#FF00FF";
		}
	}			
};



vat.formD.lineEnhance = function vatFormD_lineEnhance(pnId, pnLineIndex, pxThisObj){
	var vxItemObj = document.getElementById(vat.formD.pageTrIdMake(pnId, pnLineIndex));
	if (vxItemObj){
		if (pxThisObj.value == "1"){
			vxItemObj.style.setAttribute("saveBgColor", vxItemObj.style.getAttribute("background")); 
			vxItemObj.style.background = "#FF00FF";
		}else{
			if (vxItemObj.style.getAttribute("saveBgColor")){
				vxItemObj.style.setAttribute("background", vxItemObj.style.getAttribute("saveBgColor"));
			}else{
				vxItemObj.style.removeAttribute("background");
			}
		}
	}			
};

vat.formD.lineDeleteClick = function vatFormD_lineDeleteClick(pnId, pnLineIndex, pxThisObj){
	var vxLineObj = document.getElementById(vat.formD.pageTrIdMake(pnId, pnLineIndex));
	if (pxThisObj && vat.formD.page[pnId].canDataDelete){
		if (vat.jr(vat.formD.page[pnId].lineDelB)){
			if (pxThisObj.value == "1"){
				pxThisObj.setAttribute("value", "0");
				pxThisObj.src = 'images/16x16/trash-empty.png';
				pxThisObj.alt = "刪除此筆資料";		
				//var saveBgColor = pxThisObj.style.getAttribute("saveDelBgColor");
				//if (saveBgColor){
				//	pxThisObj.style.setAttribute("backgroundColor", saveBgColor);
				//}else{
				//	pxThisObj.style.removeAttribute("backgroundColor");
				//}
			}else{
				pxThisObj.setAttribute("value", "1");
				pxThisObj.src = 'images/16x16/trash-full.png';
				pxThisObj.alt = "恢復此筆資料";			
				//pxThisObj.style.setAttribute("saveDelBgColor", pxThisObj.style.getAttribute("backgroundColor")); 
				//pxThisObj.style.backgroundColor = "#FF00FF";
			}
			vat.jr(vat.formD.page[pnId].lineDelA);
		}	
	}			
};



vat.formD.lineDeleteState = function vatFormD_lineDelete(pnId, pnLineIndex, pxThisObj){
	if (pxThisObj){
		if (pxThisObj.value && pxThisObj.value == "1"){
			// pxThisObj.style.backgroundColor = "#FF00FF";
			pxThisObj.src = 'images/16x16/trash-full.png';
			pxThisObj.alt = "恢復此筆資料";
		}else{
			pxThisObj.value = "0";
			pxThisObj.src = 'images/16x16/trash-empty.png';
			pxThisObj.onmouseover = function (){};
			pxThisObj.onmouseout	= function (){};
			// pxThisObj.style.backgroundColor = "#efefef";
			pxThisObj.alt = "刪除此筆資料";		
		}
	}			
};





/******************************************************************
 *  說明：各種 ID, NAME 的檢查或組合產生
 */
vat.formD.pageId = function vatFormD_pageId(pnId){
	return (typeof pnId === "undefined") ? vat.formD.current : pnId;
};
vat.formD.pageIdVerify = function vatFormD_pageIdVerify(pnId){
	if (typeof(pnId) === "number"){
		return true;
	}else{
		vat.debug("developer", "傳入的參數型態("+typeof(pnId)+")錯誤, <pnPageId> 必須是整數");
		return false;
	}
};
vat.formD.pageCurrent = function(){
	return (vat.formD.current);
};
vat.formD.pageCountIdMake = function vatFormD_pageCountIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]pageCount";
};	
vat.formD.pageHereIdMake = function vatFormD_pageHereIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]pageHere";
};
vat.formD.pageTrIdMake = function vatFormD_pageTrIdMake(pnId, pnAxisY){
	return "vatFD["+vat.formD.pageId(pnId)+"]TR#"+pnAxisY;
};
vat.formD.pageTrDivIdMake = function vatFormD_pageTrDivIdMake(pnId, pnAxisY){
	return "vatFD["+vat.formD.pageId(pnId)+"]TRDIV#"+pnAxisY;
};
vat.formD.pageTrImgIdMake = function vatFormD_pageTrImgIdMake(pnId, pnAxisY){
	return "vatFD["+vat.formD.pageId(pnId)+"]TRIMG#"+pnAxisY;
};
vat.formD.itemNameMake = function vatFormD_itemNameMake(pnLineIndex, psItemName){
	return ("#form["+pnLineIndex+"]."+psItemName);
};
vat.formD.itemIdMake = function vatFormD_itemNameMake(pnId, pnAxisY, pnAxisX){
	return "vatFD["+vat.formD.pageId(pnId)+"]A#Y"+pnAxisY+"#X"+pnAxisX;
};
vat.formD.talkMsgIdMake = function vatFormD_talkMsgIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]message";
};
vat.formD.talkFileIdMake = function vatFormD_talkFileIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]talkFile";
};
vat.formD.talkAnsIdMake = function vatFormD_talkAnsIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]talkAnswer";
};
vat.formD.talkBtnOkIdMake = function vatFormD_talkBtnOkIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]buttonOk";
};
vat.formD.talkBtnNoIdMake = function vatFormD_talkBtnNoIdMake(pnId){
	return "vatFD["+vat.formD.pageId(pnId)+"]buttonNo";
};





/******************************************************************
 *  說明：基本操作
 */
vat.formD.pageCurrentChange = function vatFormD_pageCurrentChange(pnId){
	var vsPageList = "";
	if ((typeof(pnId) == 'number') && (vat.formD.page instanceof Array)){
		if (typeof(vat.formD.page[pnId]) == 'object'){ 
			vat.formD.current = pnId;
		}else{
			for (var i=0; i<vat.formD.page.length; i++){
				if (typeof(vat.formD.page[i]) == "object"){
					vsPageList +=  i + (i < vat.formD.page.length-1 ? "," : "");
				}
			}
			vat.debug("developer", "查無此表格代號: "+pnId+", 目前有效的表格代號有包含("+vsPageList+")");
		}	 
	}
	return (vat.formD.current);
};
vat.formD.pageMessage = function vatFormD_pageMessage(psMsg, psBgColor){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	var voMsgObj = document.getElementById(vat.formD.talkMsgIdMake(pnId));
	voMsgObj.value = psMsg;	
	voMsgObj.size = psMsg.length*2;
	if (typeof(psBgColor) == "string"){
		voMsgObj.style.backgroundColor = psBgColor;
	}
	voMsgObj.style.display  = "inline";
};
vat.formD.MessageOff = function vatFormD_messageOff(){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	var voMsgObj = document.getElementById(vat.formD.talkMsgIdMake(pnId));
	voMsgObj.value = "";	
	voMsgObj.style.backgroundColor = "";
	voMsgObj.style.display  = "none";
// voMsgObj.size = 1;	
	var voAnsObj = document.getElementById(vat.formD.talkFileIdMake(pnId));
	voAnsObj.style.display = "none";
	var voBtnOkObj = document.getElementById(vat.formD.talkBtnOkIdMake(pnId));
	voBtnOkObj.style.display = "none";
	var voBtnNoObj = document.getElementById(vat.formD.talkBtnNoIdMake(pnId));	
	voBtnNoObj.style.display = "none";
};
vat.formD.prompt = function vatFormD_prompt(psMsg, psType, pxEvent, psDefault){
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	var voMsgObj = document.getElementById(vat.formD.talkMsgIdMake(pnId));
	voMsgObj.value = psMsg;
	voMsgObj.style.display  = "inline";
	var voFileObj = document.getElementById(vat.formD.talkFileIdMake(pnId));
	var voAnsObj = document.getElementById(vat.formD.talkAnsIdMake(pnId));
	if (psType == "FILE"){
		voFileObj.style.display  = "inline";
	}else{
		voAnsObj.style.display   = "inline";
	}			
	if (typeof psDefault == "string"){
		voAnsObj.value = psDefault;
	}
	var voBtnOkObj = document.getElementById(vat.formD.talkBtnOkIdMake(pnId));
	voBtnOkObj.style.display = "inline";
	voBtnOkObj.onclick = function (){
		vat.formD.MessageOff();
		if (psType == "FILE"){
			vat.jr(pxEvent(voFileObj.value));
			voFileObj.style.display  = "none";			
		}else{
			vat.jr(pxEvent(voAnsObj.value));
			voAnsObj.style.display   = "none";			
		}
	};
	var voBtnNoObj = document.getElementById(vat.formD.talkBtnNoIdMake(pnId));
	voBtnNoObj.style.display  = "inline";
	voBtnNoObj.onclick = function (){vat.formD.MessageOff();};
};
vat.formD.r = function vatFormD_ret(px){
	return px;
};
vat.jr = function vatJsRun(psJsCode, pxResult){
	if ((typeof psJsCode == "string") && (psJsCode.length > 0)){
		return eval(psJsCode);
	}else{ 
		if (typeof psJsCode == "function"){
			return psJsCode();
		}else{
			if (typeof pxResult == "undefined"){
				return true;
			}else{
				return pxResult;
			}	
			
		}
	}
};

//*** item ***
vat.formD.item = function vatFormD_item(pnId, psName, poDesc){
	if (vat.formD.pageIdVerify(pnId) && vat.formD.itemVerify(poDesc)){
		if (typeof(vat.formD.page[pnId]) == "undefined"){
			vat.formD.pageCreate(pnId);
		}
		if (typeof(psName) === "string"){
			vat.formD.page[pnId].itemName.push(psName);
		}else{
			vat.debug("developer", "傳入的參數型態("+typeof(psName)+")錯誤, <psName> 必須是字串");
		}
		vat.formD.page[pnId].itemDesc.push(poDesc);
	}
};
vat.formD.itemInit = function vatFormD_itemInit(pnId, pnDataNameIndex){
	var vsResult = "";
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	if (typeof(pnDataNameIndex) == "number"){	
		switch (vat.formD.page[pnId].itemDesc[pnDataNameIndex].type){
		case "TEXT":
			vsResult = eval(vat.formD.page[pnId].itemDesc[pnDataNameIndex].init);
			if (vsResult == "undefined"){
				vsResult = "";
			}
			vsResult = "";
			break;
		case "IDX":
			vsResult = "";
			break;			
		case "ROWID":
			vsResult = "";
			break;
		}
	}
	return vsResult;
};
vat.formD.itemGetById = function vatFormD_itemGetById(pnId, pnAxisY, pnAxisX){
	return document.getElementById(vat.formD.itemIdMake(pnId, pnAxisY, pnAxisX));
};
vat.formD.itemAxisGet = function vatFormD_itemAxisGet(psItemId){
	var x1, x2, x3 = -1, x4 = -1;
	if (typeof psItemId == "undefined") {psItemId = event.srcElement.id;}
	x1 = psItemId.indexOf("A#Y");
	if (x1 !== -1){
		x2 = psItemId.indexOf("#X", x1);
		// alert("var:"+psItemId+","+x1 + "," + x2);
		if (x2 !== -1){
			x3 = parseInt(psItemId.substring(x1+3, x2));
			x4 = parseInt(psItemId.substring(x2+2));
		}	
	}
	return [x3, x4];		// y, x
};
vat.formD.itemCurrentValue = function vatFormD_itemCurrentValue(){
	var ret = '';
	var obj = document.getElementById(vat.form.item.list[vat.form.item.current].id);
	if (obj){
		 ret = obj.getAttribute("value"); 
	}
	return ret;
};

vat.formD.itemDataGet = function vatFormD_itemDataGet(pnLineIndex, psItemName){
	var x0, x1, i, vbFound = false, vxResult = "";
  pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	x0 = vat.formD.itemNameMake(pnLineIndex, psItemName);
	for(i=0; i < vat.formD.page[pnId].lines ; i++){
   	for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
			vxDataObj=vat.formD.itemGetById(pnId, i, j);
			if (vxDataObj && vxDataObj.name == x0){
				switch (vat.formD.page[pnId].itemDesc[j].type){
				case "NUMM":
					vxResult = vxDataObj.getAttribute("original");
					alert(vxResult);
					break;
				default:
					vxResult = vxDataObj.getAttribute("value");
					break;
				}	
				vbFound = true;
				break;
			}
		}
	}
	if (! vbFound){
		vat.debug("developer","item data not vbFound:" +x0);
	}
  return vxResult;
};

vat.formD.itemDataBind = function vatFormD_itemDataBind(pnId, pnLineIndex, psItemName, pxValue){
	var vsT1, x1, i;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId;
	vsT1 = vat.formD.itemNameMake(pnLineIndex, psItemName);
	for(i=0; i<vat.formD.page[pnId].lines; i++){
		for(j=0; j<vat.formD.page[pnId].itemDesc.length; j++){
			vxDataObj=vat.formD.itemGetById(pnId, i, j);
			if (vxDataObj && vxDataObj.name == vsT1){
				switch (vat.formD.page[pnId].itemDesc[j].type){
				case "NUMM":
					vxDataObj.original = pxValue;	
					vxDataObj.value = vat.formD.itemFormatNumm(pxValue);
					break;
				default:
					vxDataObj.setAttribute("value", pxValue);
					break;
				}	
				break;
			}
		}		
	}	
};
vat.formD.itemIdDataBind = function vatFormD_itemIdDataBind(pnId, pnColIndex, pxValue){
	var vxDataObj, vaYX;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId; 
	vaYX = vat.formD.itemAxisGet(vat.form.item.list[vat.form.item.current].id);
	if (vaYX[0] !== -1){ 
			vxDataObj=vat.formD.itemGetById(pnId, vaYX[0], pnColIndex);
			if (vxDataObj){
				switch (vxDataObj.datatype){
				case "NUMM":
					vxDataObj.original = pxValue;	
					vxDataObj.value = vat.formD.itemFormatNumm(pxValue);
					break;
				default:
					vxDataObj.setAttribute("value", pxValue);
					break;
				}	
			}		
	}	
};
vat.formD.itemAxisDataBind = function vatFormD_itemAxisDataBind(pnId, pnY, pnX, pxValue){
	var vxDataObj;
	pnId = (typeof pnId == "undefined") ? vat.formD.current : pnId; 
	vxDataObj=vat.formD.itemGetById(pnId, pnY, pnX);
	if (vxDataObj){
		switch (vxDataObj.datatype){
		case "NUMM":
			vxDataObj.original = pxValue;	
			vxDataObj.value = vat.formD.itemFormatNumm(pxValue);
			break;
		default:
			vxDataObj.setAttribute("value", pxValue);
			break;
		}	
	}		
};
vat.formD.itemIndexNo = function vatFormD_itemIndexNo(psItemName){
	var x1, x2, x3;
	if (typeof psItemName == "undefined"){
		psItemName = vat.form.item.list[vat.form.item.current].name;
		// alert(psItemName);
	}
	x1 = psItemName.indexOf("[", 0);
	if (x1 != -1){
		x2 = psItemName.indexOf("]",0);
		x3 = parseInt(psItemName.substring(x1+1, x2));							//*** subscript
		return x3;
	}else{
		return -1;
	}
};
vat.formD.itemNUMB = function vatFormD_itemNUMB(){
	return 0;
};
vat.formD.itemTEXT = function(){
	return "";
};
vat.formD.itemIsSelect = function vatFormD_itemIsSelect(pxObj) {
	if (pxObj !== null && pxObj.options !== null){
		return true;
	}
	return false;
};
vat.formD.itemSelectGetValue = function vatFormD_itemSelectGetValue(psName) {
	var vxObjByName = document.getElementsByName(psName);
	var vsResult = "";
	if (vxObjByName && vxObjByName[0]){
		vxObjSelect = vxObjByName[0];
		if (vat.formD.itemIsSelect(vxObjSelect)){
			for (var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
				if (vxObjSelect.options[i].selected){
					vsResult = vxObjSelect.options[i].text;
					break;
				}
			}
		}else{
			vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 更新的目標:"+psName+", 不是一個<SELECT>元素");
		}
	}else{
		vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 找不到被更新的 HTML 元素名稱"+psName);			
	}
	return vsResult;
};
/****
	下拉式選單
*/
vat.formD.itemSelectBind = function vatFormD_itemSelectBind(paDataSrc) {
	var vxObjByName = document.getElementsByName(paDataSrc[0][0]);
	if (vxObjByName && vxObjByName[0]){
		vxObjSelect = vxObjByName[0];
		if (vat.formD.itemIsSelect(vxObjSelect)){
			for (var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
				vxObjSelect.options[i] = null;
			}
			if (paDataSrc[0][2]){
				paDataSrc[1].unshift("<請選擇>");
				paDataSrc[2].unshift("");
			}
			if (paDataSrc && paDataSrc instanceof Array && paDataSrc.length >= 2 && 
					paDataSrc[1] instanceof Array && paDataSrc[2] instanceof Array){
				for (i=0; i<paDataSrc[1].length; i++){		//** option.text
					if (typeof paDataSrc[1][i] == "string" && paDataSrc[1][i].length > 0){
						vxObjSelect.options[vxObjSelect.options.length] = new Option(paDataSrc[1][i], paDataSrc[2][i], false, false);
					}	
					if (paDataSrc[2][i] == paDataSrc[0][1]){
						// vxObjSelect.selected = true;
						vxObjSelect.selectedIndex = i;
					}
				}
			}			
		}else{
			vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectBind.caller)+"(), 更新的目標:"+paDataSrc[0][0]+", 不是一個<SELECT>元素");
		}
	}else{
		vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectBind.caller)+"(), 找不到被更新的 HTML 元素名稱"+paDataSrc[0][0]);			
	}
};
vat.formD.itemFormatNumm = function vatFormD_itemFormatNumm(psNumb) {
	function formatNumber(psInt){
		if (psInt.length <= 3) {
			return psInt;
		}else{
			return formatNumber(psInt.substr(0, psInt.length-3))+","+psInt.substr(psInt.length-3);
		}
	}
	if (typeof psNumb == "string" && psNumb.length > 0 && typeof psNumb.replace == "function"){
		var vnInteger = parseInt(psNumb.replace(",", ""));
		var vsDecimal = ((vnPos = psNumb.indexOf(".")) != -1) ? psNumb.substr(vnPos) : "";
		return formatNumber(vnInteger.toString())+vsDecimal;
	}
	return psNumb;
};
