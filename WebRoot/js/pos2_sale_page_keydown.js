/**
 * 
 */
var request_bean;
var mapping_config;
//initial
function onloadInitial(){
	//get mapping key function config
	console.log("onloadInitial");
	// event_trigger 觸發事件
	// objectId 觸發綁定物件
	// execute_function 物件觸發後執行的事件
	event_register_for_input("","","");
	// function_parameters 
	
	//add event listener
	console.log("event listener");
	window.addEventListener('keydown', do_on_keydown , false);
	
	//new request bean
	console.log("initial_bean");                                                                                                                                                                                
	request_bean = new reqBean(
		new transaction(null,null,null,null,null, null, null, null,null, null , null , null , null , null,null,null,null,null,null,null,null,null,null),
		new posTransaction(null,null,null,null,null, null, null, null,null, null , null , null , null , null,null,null,null,null,null,null,null,null,null,null), 
		new log(null,null,null) );
	
	//do init
	request_bean.posTransaction.action = "init";
	set_obj_value();
	//execute event function call
	call_key_mapping_function("/pos_2.0_web/TransactionController");
}

//add event listener            //
function event_register_for_input(trigger_object_id,trigger_event,execute_function){
	var obj = document.getElementById(trigger_object_id);
	obj.addEventListener(trigger_event, execute_function , false);
}

//register for keydown event mapping key and function
function do_on_keydown(e){
	console.log(e.key);
	switch(e.key){
		case "Enter" :
			//alert("insertNewDataByBar");
			insertNewDataByBar();
		break;
		
		case "q":
			
		break;
		
		case "Alt":
			alert("請不要按下 alt 鍵")
		break;
	}
}

function executefunction(){
	set_obj_value();
	call_key_mapping_function();
}

function set_obj_value(){
	//check pos transaction status
	//check transaction status
	//set value
	if(request_bean.posTransaction.action === "init"){
		console.log("set_obj_value init");
		request_bean.posTransaction.action = "init"
		//set value
		//ordertypecode headid loginbrandcoe loginemployeecode
		const orderTypeCode = document.getElementById("orderTypeCode");
		request_bean.posTransaction.orderTypeCode = orderTypeCode.value;
		const headId = document.getElementById("headId");
		request_bean.posTransaction.headId = headId.value;
		const brandCode = document.getElementById("brandCode");
		request_bean.posTransaction.brandCode = brandCode.value;
		const superintendentCode = document.getElementById("superintendentCode");
		request_bean.posTransaction.superintendentCode = superintendentCode.value;
		
	}else if(request_bean.posTransaction.action === "execute_discount"){
		console.log("set_obj_value execute_discount");
		request_bean.posTransaction.action = "execute_discount"
		//set value
		const barcode = document.getElementById("barcode");
		const total_count = document.getElementById("total_count");
		const total = document.getElementById("total");
		request_bean.posTransaction.totalActualSalesAmount = total_count.value;
		request_bean.posTransaction.discount = barcode.value;
		request_bean.posTransaction.total = total.value;
	}
}

function set_obj_value_by_response(rtnResponse){
	//check pos transaction status
	//check transaction status
	//set value
	var obj = JSON.parse(rtnResponse);
	const orderTypeCode = document.getElementById("orderTypeCode");
	orderTypeCode.value = obj.posTransaction.orderNo;
	
	const brandCode = document.getElementById("brandCode");
	brandCode.value = obj.posTransaction.brandCode;
	
	const superintendentCode = document.getElementById("superintendentCode");
	superintendentCode.value = obj.posTransaction.superintendentCode;
	
	const currencyCode = document.getElementById("currencyCode");
	currencyCode.value = obj.posTransaction.currencyCode;
	
	const discountRate = document.getElementById("discountRate");
	discountRate.value = obj.posTransaction.discountRate;
	
	const exportExchangeRate = document.getElementById("exportExchangeRate");
	exportExchangeRate.value = obj.posTransaction.exportExchangeRate;
	
	const salesOrderDate = document.getElementById("salesOrderDate");
	salesOrderDate.value = obj.posTransaction.salesOrderDate;
	
	
	const status = document.getElementById("status");
	status.value = obj.posTransaction.status;
	
	const schedule = document.getElementById("schedule");
	schedule.value = "99";//obj.posTransaction.schedule;
	
	const orderNo = document.getElementById("orderNo");
	orderNo.value = obj.posTransaction.soSalesOrderHead.orderNo;
	
	const headId = document.getElementById("headId");
	headId.value = obj.posTransaction.soSalesOrderHead.headId;
	
	const createdBy = document.getElementById("createdBy");
	createdBy.value = obj.posTransaction.soSalesOrderHead.createdBy;
	
	console.log(obj)
	
}

//call back update value
function call_key_mapping_function(function_call){
	console.log("call_key_mapping_function send ajax to "+function_call);
	// Create an XMLHttpRequest object
	const xhttp = new XMLHttpRequest();
	
	// Define a callback function                                                               
	xhttp.onload = function() {
		key_mapping_callback_function(xhttp.responseText);
	}
	
	// Send a request for controller 
	xhttp.open("POST", function_call, true);
	var data = JSON.stringify(request_bean);
	
	console.log(request_bean);
	xhttp.send(data);
}

//call back update value
function key_mapping_callback_function(rtnResponse){
	console.log("key_mapping_callback_function");
	set_obj_value_by_response(rtnResponse);
	
	
	//execute_discount();
}

function execute_discount(){
	//邏輯
	const discount = document.getElementById("discount");
	discount.innerHTML=barcode.value;
	total.innerHTML=total.innerHTML-barcode.value;
	
}

function openDiv(url){
	var divContainer = document.getElementById("container");
	var iframeElement = document.createElement("iframe");
	iframeElement.src = url;
	iframeElement.style.width = "100%";
	iframeElement.style.height = "100%";
	if(divContainer.hasChildNodes){
		divContainer.removeChild();
	}
	divContainer.appendChild(iframeElement);
}