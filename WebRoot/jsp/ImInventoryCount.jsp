<%
/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : ImInventoryCount.jsp
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Jun 9, 2010		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.User"%>
<%@page import="java.util.*"%>
<%
try{
	// 取得登入品牌
	String brandCode = "";
	String employeeCode = "";
	if (request.getSession().getAttribute("userObj")!=null){
		User userObj = (User)request.getSession().getAttribute("userObj");
		brandCode = userObj.getBrandCode();
		employeeCode = userObj.getEmployeeCode();
	}else{
		brandCode = "T2";
	}
	
	// 取得庫別資料
	ImWarehouseService imWarehouseService = (ImWarehouseService)SpringUtils.getApplicationContext().getBean("imWarehouseService");
	List<ImWarehouse> imWarehouse =  imWarehouseService.findByBrandCode(brandCode);
	// 大類
	ImItemCategoryService imItemCategoryService = (ImItemCategoryService)SpringUtils.getApplicationContext().getBean("imItemCategoryService");
	List<ImItemCategory> allImItemCategory01 = imItemCategoryService.findByCategoryType(brandCode, ImItemCategoryDAO.CATEGORY01, "ASC");
	// 中類
	List<ImItemCategory> allImItemCategory02 = imItemCategoryService.findByCategoryType(brandCode, ImItemCategoryDAO.CATEGORY02, "ASC");
	// 品牌
	List<ImItemCategory> allImItemBrand = imItemCategoryService.findByCategoryType(brandCode, ImItemCategoryDAO.ITEM_BRAND, "ASC");

	String inventoryCountsId = null;
	if (inventoryCountsId == null){
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
		List<BuCommonPhraseLine> list = buCommonPhraseService.getCommonPhraseLinesById("InventoryCountsId",false);

		for (BuCommonPhraseLine line : list) {
			if(line.getId().getLineCode().equals((String)request.getParameter("inventoryCountsId"))){
				inventoryCountsId = line.getAttribute1();
				break;
			}
		}
	}
	String isAllow = request.getParameter("isAllow") != null ? (String)request.getParameter("isAllow"): "";
%>
<html>
<link href="../css/erp.css" rel="stylesheet" type="text/css">
<head>
<title>盤點報表列印作業</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href='../css/calendar-system.css' rel='stylesheet'>
<link href='../css/default.css' rel='stylesheet'>
<link href='../css/erp.css' rel='stylesheet'>
<script type="text/javascript" src="../js/json2.js"></script>
<script type="text/javascript" src="../js/vat.js"></script>
<script type="text/javascript" src="../js/vat-message.js"></script>
<script type="text/javascript" src="../js/vat-utils.js"></script>
<script type="text/javascript" src="../js/vat-tab-m.js"></script>
<script type="text/javascript" src="../js/vat-ajax.js"></script>
<script type="text/javascript" src="../js/vat-form.js"></script>
<script type="text/javascript" src="../js/vat-block.js"></script>
<script type="text/javascript" src="../js/default.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script>
<script type="text/javascript" src="../js/calendar-setup.js"></script>
<script type="text/javascript" src="../js/calendar-zh.js"></script>
<script type="text/javascript" src="../js/calendar-zh_TW.js"></script>
<script type="text/javascript" src="../js/DateExpFunction.js"></script>
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.blockUI.js"></script>
<script>

function doDownload(formAction) {
    var date = document.main.date.value;
    if (date == '') {
        alert("請輸入庫存日期");
        return;
    }
    var doSelect = false;
    selectOption = document.getElementById('warehouseCode');
    for (var i = 0; i < selectOption.options.length; i++) {
        if (selectOption.options[i].selected) {
            doSelect = true;
            break;
        }
    }
    if (!doSelect) {
        alert("請選擇庫別！");
        return;
    }
    var alertMessage = "是否確定下載?";
    if (confirm(alertMessage)) {

        var form = document.main;
        msgDiv = document.getElementById("message");
        msgDiv.innerHTML = "庫別："+getSelected('warehouseCode')+ "，日期："+date+"，庫存下載";
        $.blockUI({
            message: $('#domMessage'),
            overlayCSS: { // 遮罩的css設定
                backgroundColor: '#eee'
            },
            css: { // 遮罩訊息的css設定
                border : '3px solid #aaa',
                width: '30%',
                left : '35%',
                backgroundColor : 'white',
                opacity : '0.9' //透明度，值在0~1之間
            }
        });
        $.ajax({
            url: "ImInventoryCountCompare.jsp",
            dataType: "html",
            data: $(form).serialize(),
            type: "POST",
            aynsc: false,
            success: function (data) {
                if (data.indexOf('complete') > -1) alert('下載完成！');
                else alert(data.substring(data.indexOf('error:') + 6));
                $.unblockUI();
            }, error: function (data, status, e) {
                alert("Error:" + data);
                $.unblockUI();
            }
        });
    }
}

function randomWord(randomFlag, min, max){
    var str = "",
        range = min,
        arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

    if(randomFlag){
        range = Math.round(Math.random() * (max-min)) + min;
    }
    for(var i=0; i<range; i++){
        pos = Math.round(Math.random() * (arr.length-1));
        str += arr[pos];
    }
    return str;
}

//盤點差異表列印
function doExport() {

    var alertMessage = "是否確定列印?";
    var warehouseCode = getSelected('warehouseCode');
    var category01 = getSelected('category01');
	var category02 = getSelected('category02');
	var itemBrand = getSelected('itemBrand');
	var randomNo = randomWord(true,10,10);
	var url = "http://10.1.94.161:8080/crystal/t2/IM0122.rpt?cryp="+randomNo+"&prompt0=<%=brandCode%>&prompt1=<%=inventoryCountsId%>&prompt2="+warehouseCode+"&prompt3="+category01+"&prompt4="+category02+"&prompt5="+itemBrand;
    if (confirm(alertMessage)) {
        window.open (url, "盤點差異表列印", "toolbar=no, menubar=no, status=no, location=yes, resizable=yes")
    }
}

//盤點條碼列印
function doBarCodePrint() {

    var alertMessage = "是否確定列印?";
    var warehouseCode = getSelected('warehouseCode');
    var lotNo1 = document.main.lotNo1.value;
    var lotNo2 = document.main.lotNo2.value;
    var randomNo = randomWord(true,10,10);
	var url = "http://10.1.94.161:8080/crystal/t2/IM0124.rpt?cryp="+randomNo+"&prompt0=<%=brandCode%>&prompt1=<%=inventoryCountsId%>&prompt2=&prompt3="+warehouseCode+"&prompt4="+lotNo1+"&prompt5="+lotNo2;
    if (confirm(alertMessage)) {
        window.open (url, "盤點條碼列印", "toolbar=no, menubar=no, status=no, location=yes, resizable=yes")
    }
}

//盤點清單
function doListPrint() {

    var alertMessage = "是否確定列印?";
    var warehouseCode = getSelected('warehouseCode');
    var lotNo1 = document.main.lotNo1.value;
    var lotNo2 = document.main.lotNo2.value;
    var randomNo = randomWord(true,10,10);
	var url = "http://10.1.94.161:8080/crystal/t2/IM0120.rpt?cryp="+randomNo+"&prompt0=<%=brandCode%>&prompt1=<%=inventoryCountsId%>&prompt2=&prompt3="+warehouseCode+"&prompt4="+lotNo1+"&prompt5="+lotNo2;
    if (confirm(alertMessage)) {
        window.open (url, "盤點清單", "toolbar=no, menubar=no, status=no, location=yes, resizable=yes")
    }
}

//盤點表
function doInventoryPrint() {

    var alertMessage = "是否確定列印?";
    var warehouseCode = getSelected('warehouseCode');
    var lotNo1 = document.main.lotNo1.value;
    var lotNo2 = document.main.lotNo2.value;
    var randomNo = randomWord(true,10,10);
	var url = "http://10.1.94.161:8080/crystal/t2/IM0123.rpt?cryp="+randomNo+"&prompt0=<%=brandCode%>&prompt1=<%=inventoryCountsId%>&prompt2="+lotNo1+"&prompt3="+lotNo2+"&prompt4="+warehouseCode+"&prompt5=";
    if (confirm(alertMessage)) {
        window.open (url, "盤點表", "toolbar=no, menubar=no, status=no, location=yes, resizable=yes")
    }
}

// 以逗號分割所選的資料
function getSelected(id) {
    var selected = "";
    selector = document.getElementById(id);
    for (var i = 0; i < selector.options.length; i++) {
        if (selector.options[i].selected && selector.options[i].value !== '' && selector.options[i].value.indexOf("#")== -1) {
        	if(selected == "")
            	selected += selector.options[i].value ;
            else
            	selected += "," + selector.options[i].value ;
        }
    }
    return selected;
}

function getNowDate() {
    var Today = new Date();
    document.main.date.value = Today.toFormat('yyyymmdd');
    var isAllow = '<%=isAllow%>';
    if (isAllow == "N"){
    	document.main.download.disabled = true;
    	document.main.reportExport.disabled = true;
    	document.main.clearRecord.disabled = true;
    	document.main.lockRecord.disabled = true;
    	document.main.unLockRecord.disabled = true;
    }
    else{
    	document.main.download.disabled = false;
    	document.main.reportExport.disabled = false;
    	document.main.clearRecord.disabled = false;
    	document.main.lockRecord.disabled = false;
    	document.main.unLockRecord.disabled = false;
    }
}

//清除選取
function doClear(id){
    var selector = document.getElementById(id);
    for (var i = 0; i < selector.options.length; i++) {
        selector.options[i].selected = false;
    }
}

//選取Select Multiple的option不用加上ctrl鍵
var arrOldValues;

function DeselectAllList(CONTROL) {
    for (var i = 0; i < CONTROL.length; i++) {
        CONTROL.options[i].selected = false;
    }
}

function FillListValues(CONTROL) {
    var arrNewValues;
    var intNewPos;
    var strTemp = GetSelectValues(CONTROL);
    arrNewValues = strTemp.split(",");
    for (var i = 0; i < arrNewValues.length - 1; i++) {
        if (arrNewValues[i] == 1) {
            intNewPos = i;
        }
    }

    for (var i = 0; i < arrOldValues.length - 1; i++) {
        if (arrOldValues[i] == 1 && i != intNewPos) {
            CONTROL.options[i].selected = true;
        }
        else if (arrOldValues[i] == 0 && i != intNewPos) {
            CONTROL.options[i].selected = false;
        }

        if (arrOldValues[intNewPos] == 1) {
            CONTROL.options[intNewPos].selected = false;
        }
        else {
            CONTROL.options[intNewPos].selected = true;
        }
    }
}

function GetSelectValues(CONTROL) {
    var strTemp = "";
    for (var i = 0; i < CONTROL.length; i++) {
        if (CONTROL.options[i].selected == true) {
            strTemp += "1,";
        }
        else {
            strTemp += "0,";
        }
    }
    return strTemp;
}

function GetCurrentListValues(CONTROL) {
    var strValues = "";
    strValues = GetSelectValues(CONTROL);
    arrOldValues = strValues.split(",")
}

//清除盤點紀錄
function doClearRecord() {
    if (document.main.lotNo1.value == "" || document.main.lotNo2.value == "") {
        alert("請輸入盤點批號區間！");
        return;
    }
    var dataString = "&employeeCode=<%=employeeCode%>&brandCode=<%=brandCode%>&countsId=<%=inventoryCountsId%>&countsLotNoS=" + document.main.lotNo1.value + "&countsLotNoE=" + document.main.lotNo2.value;
    $.blockUI({
        message: $('#domMessage1'),
        overlayCSS: { // 遮罩的css設定
            backgroundColor: '#eee'
        },
        css: { // 遮罩訊息的css設定
            border: '3px solid #aaa',
            width: '30%',
            left: '35%',
            backgroundColor: 'white',
            opacity: '0.9' //透明度，值在0~1之間
        }
    });
    $.ajax({
        url: "AjaxQuery.jsp?process_object_name=imInventoryCountsService&process_object_method_name=executeListDataClean" + dataString,
        dataType: "html",
        type: "POST",
        aynsc: false,
        success: function (data) {
            if (data.indexOf("SUCCESS") >= -1) alert("清除盤點紀錄完成！");
            else {
                index1 = data.indexOf("msg") + 4;
                data2 = data.substring(index1);
                index2 = data2.indexOf(">");
                alert(data2.substring(0, index2));
            }
            $.unblockUI();
        },
        error: function (data, status, e) {
            alert("Error:" + data);
            $.unblockUI();
        }
    });
}

//鎖住盤點清單
function doLockRecord() {
    var doSelect = false;
    selectOption = document.getElementById('warehouseCode');
    for (var i = 0; i < selectOption.options.length; i++) {
        if (selectOption.options[i].selected) {
            doSelect = true;
            break;
        }
    }
    if (!doSelect && (document.main.lotNo1.value == "" && document.main.lotNo2.value == "")) {
        alert("請輸入庫別或是盤點批號區間！");
        return;
    }
    var warehouseCode = getSelected('warehouseCode');
    var dataString = "&employeeCode=<%=employeeCode%>&brandCode=<%=brandCode%>&countsId=<%=inventoryCountsId%>&countsLotNoS=" + document.main.lotNo1.value + "&countsLotNoE=" + document.main.lotNo2.value + "&warehouseCode=" + warehouseCode;
    $.blockUI({
        message: $('#domMessage2'),
        overlayCSS: { // 遮罩的css設定
            backgroundColor: '#eee'
        },
        css: { // 遮罩訊息的css設定
            border: '3px solid #aaa',
            width: '30%',
            left: '35%',
            backgroundColor: 'white',
            opacity: '0.9' //透明度，值在0~1之間
        }
    });
    $.ajax({
        url: "AjaxQuery.jsp?process_object_name=imInventoryCountsService&process_object_method_name=executeListDataFinish" + dataString,
        dataType: "html",
        type: "POST",
        aynsc: false,
        success: function (data) {
            if (data.indexOf("SUCCESS") >= -1) alert("已鎖住盤點清單！");
            else {
                index1 = data.indexOf("msg") + 4;
                data2 = data.substring(index1);
                index2 = data2.indexOf(">");
                alert(data2.substring(0, index2));
            }
            $.unblockUI();
        },
        error: function (data, status, e) {
            alert("Error:" + data);
            $.unblockUI();
        }
    });
}

//解鎖盤點清單
function doUnLockRecord() {
    var doSelect = false;
    selectOption = document.getElementById('warehouseCode');
    for (var i = 0; i < selectOption.options.length; i++) {
        if (selectOption.options[i].selected) {
            doSelect = true;
            break;
        }
    }
    if (!doSelect && (document.main.lotNo1.value == "" && document.main.lotNo2.value == "")) {
        alert("請輸入庫別或是盤點批號區間！");
        return;
    }
    var warehouseCode = getSelected('warehouseCode');
    var dataString = "&employeeCode=<%=employeeCode%>&brandCode=<%=brandCode%>&countsId=<%=inventoryCountsId%>&countsLotNoS=" + document.main.lotNo1.value + "&countsLotNoE=" + document.main.lotNo2.value + "&warehouseCode=" + warehouseCode;
    $.blockUI({
        message: $('#domMessage2'),
        overlayCSS: { // 遮罩的css設定
            backgroundColor: '#eee'
        },
        css: { // 遮罩訊息的css設定
            border: '3px solid #aaa',
            width: '30%',
            left: '35%',
            backgroundColor: 'white',
            opacity: '0.9' //透明度，值在0~1之間
        }
    });
    $.ajax({
        url: "AjaxQuery.jsp?process_object_name=imInventoryCountsService&process_object_method_name=executeListDataCounting" + dataString,
        dataType: "html",
        type: "POST",
        aynsc: false,
        success: function (data) {
            if (data.indexOf("SUCCESS") >= -1) alert("已解鎖盤點清單！");
            else {
                index1 = data.indexOf("msg") + 4;
                data2 = data.substring(index1);
                index2 = data2.indexOf(">");
                alert(data2.substring(0, index2));
            }
            $.unblockUI();
        },
        error: function (data, status, e) {
            alert("Error:" + data);
            $.unblockUI();
        }
    });
}
</script>
<style type="text/css">
</style>
</head>
<body onload='getNowDate()'>
<form accept-charset='UTF-8' id='main' name='main' method='POST'>
  <input id='actionType' value='submit' name='actionType' type='HIDDEN'>
  <input id='inventoryCountsId' name='inventoryCountsId' type='HIDDEN' value='<%=inventoryCountsId %>'>
  <input id='brandCode' name='brandCode' type='HIDDEN' value='<%=brandCode %>'>
  <input name='pageStyle' type='HIDDEN' value='no_popup'>
  <table align="center" class=MsoNormalTable border=0 cellspacing=1 cellpadding=0 width="100%" style='width:100.0%;mso-cellspacing:.7pt;background:white;mso-padding-alt: 1.5pt 1.5pt 1.5pt 1.5pt'>
    <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>
      <td colspan=9 style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; background:#990000;padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'> <b><span style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:新細明體;mso-ascii-font-family:Arial;mso-hansi-font-family:Arial;mso-bidi-font-family:Arial;color:white;mso-font-kerning:0pt'>盤點報表列印作業</span></b> </p></td>
    </tr>
    <tr style='mso-yfti-irow:1'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>盤點代號</p></td>
      <td colSpan=8 style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'><%=inventoryCountsId %></p></td>
    </tr>
    <tr style='mso-yfti-irow:2'>
      <td rowSpan=8 style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>庫別</p></td>
      <td rowSpan=8 style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <select id='warehouseCode' name="warehouseCode" multiple='multiple' size='15' onMouseDown="GetCurrentListValues(this);" onchange="FillListValues(this);" >
            <option value="">全部</option>
            <%for(int i=0;i<imWarehouse.size();i++){
				ImWarehouse imWarehouseBean = imWarehouse.get(i);
			%>
            <option value="<%=imWarehouseBean.getWarehouseCode() %>"><%=imWarehouseBean.getWarehouseCode()%>-<%=imWarehouseBean.getWarehouseName()%></option>
            <%} %>
          </select>
        </p>
		<input type='button' value='清除選取' onclick="doClear('warehouseCode')">
      </td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'> 庫存日期 </p></td>
      <td colspan='5'style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input type="text" id='date' name="date" size="10" maxLength="8">
          <img align='TOP' id='date_id' name='date_id' src='../images/calbtn.gif'>
          <script language='JavaScript'>
				    Calendar.setup({
				        inputField: 'date',
				        ifFormat: 'yyyyMMdd',
				        button: 'date_id',
				        timeFormat: '12',
				        weekNumbers: true,
				        showsTime: false,
 				        dateStatusFunc: ourDateStatusFunc
				    });
		  </script>
        </p>
      </td>
      <td align='left' style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'>
        <p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name="download" type='button' value='庫存下載' onclick='doDownload("callStoreProceduce")'>
        </p>
      </td>
    </tr>
    <tr style='mso-yfti-irow:3'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>大類</p></td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <select id='category01' name="category01" multiple='multiple' size='10' onMouseDown="GetCurrentListValues(this);" onchange="FillListValues(this);">
            <option value="#1">全部</option>
            <%for(int i=0;i<allImItemCategory01.size();i++){
				ImItemCategory imItemCategory = allImItemCategory01.get(i);
			%>
            <option value="<%=imItemCategory.getId().getCategoryCode() %>"><%=imItemCategory.getId().getCategoryCode()%>-<%=imItemCategory.getCategoryName() %></option>
            <%} %>
          </select>
        </p>
        <input type='button' value='清除選取' onclick="doClear('category01')">
      </td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>中類</p></td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <select id="category02" name="category02" multiple='multiple' size='10' onMouseDown="GetCurrentListValues(this);" onchange="FillListValues(this);">
            <option value="#2">全部</option>
            <%for(int i=0;i<allImItemCategory02.size();i++){
				ImItemCategory imItemCategory = allImItemCategory02.get(i);
			%>
            <option value="<%=imItemCategory.getId().getCategoryCode() %>"><%=imItemCategory.getId().getCategoryCode()%>-<%=imItemCategory.getCategoryName() %></option>
            <%} %>
          </select>
        </p>
        <input type='button' value='清除選取' onclick="doClear('category02')">
      </td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'> 品牌 </p></td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <select id="itemBrand" name="itemBrand" multiple='multiple' size='10' onMouseDown="GetCurrentListValues(this);" onchange="FillListValues(this);">
            <option value="#3">全部</option>
            <%for(int i=0;i<allImItemBrand.size();i++){
				ImItemCategory imItemCategory = allImItemBrand.get(i);
			%>
            <option value="<%=imItemCategory.getId().getCategoryCode() %>"><%=imItemCategory.getId().getCategoryCode()%>-<%=imItemCategory.getCategoryName() %></option>
            <%} %>
          </select>
        </p>
        <input type='button' value='清除選取' onclick="doClear('itemBrand')">
      </td>
      <td align='left' style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name='reportExport' type='button' value='盤點差異表' onclick='doExport()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:4'>
      <td rowspan='6' colspan='1' style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'> 盤點批號 </p></td>
      <td rowspan='6' colspan='5' style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input type='text' id='lotNo1' name='lotNo1' value=''>
          &nbsp;～&nbsp;
          <input type='text' name='lotNo2' name='lotNo2' value=''>
        </p></td>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input type='button' value='盤點條碼' onclick='doBarCodePrint()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:5'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input type='button' value='盤點清單' onclick='doListPrint()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:6'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input type='button' value='盤點表' onclick='doInventoryPrint()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:7'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name='clearRecord' type='button' value='清除盤點紀錄' onclick='doClearRecord()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:8'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name='lockRecord' type='button' value='鎖住盤點清單' onclick='doLockRecord()'>
        </p></td>
    </tr>
    <tr style='mso-yfti-irow:9'>
      <td style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name='unLockRecord' type='button' value='解鎖盤點清單' onclick='doUnLockRecord()'>
        </p></td>
    </tr>
  </table>
  <%
	}catch(Exception ex){
		ex.printStackTrace();
	}
%>
</form>
<div id="domMessage" style="display:none;">
    <table>
    	<tr>
      		<td align="center" valign="middle">
        		<font size=4><b>資料下載中，請稍後...</b></font>
        		<div id="message"></div>
      		</td>
    	</tr>
    </table>
</div>
<div id="domMessage1" style="display:none;">
    <table>
    	<tr>
      		<td align="center" valign="middle">
        		<font size=4><b>清除盤點記錄，請稍後...</b></font>
        		<div id="message"></div>
      		</td>
    	</tr>
    </table>
</div>
<div id="domMessage2" style="display:none;">
    <table>
    	<tr>
      		<td align="center" valign="middle">
        		<font size=4><b>鎖住盤點清單中，請稍後...</b></font>
        		<div id="message"></div>
      		</td>
    	</tr>
    </table>
</div>
<form name=form2>
  <iframe id=action name=action width=0 height=0 TITLE="ACTIONFRAME" scrolling="auto" src="blank.html" frameborder=0></iframe>
</form>
</body>
</html>
