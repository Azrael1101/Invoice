var popwin = null;
var count  = 0;

function ourDateStatusFunc(date, y, m, d)
{
	return false;
}

function blockPastFunc(date, y, m, d)
{
	nowTemp = new Date();
	now = new Date(nowTemp.getYear(), nowTemp.getMonth(), nowTemp.getDate());
	if(now.getTime() > date.getTime())
	{
    	return true;
	}
	else
	{
		return false;
	}
}

function formSubmit(formId, targetName)
{
	count = 0;
    document.forms[formId].target= targetName;

    if(targetName != '_self')
    {
    	setTimeout("formSubmitWait('"+formId+"','"+targetName+"')", 200);
    }
	else
	{
		formSubmitNoWait(formId);
	}
}

function formSubmitNoWait(formId)
{
	document.forms[formId].onsubmit();
    document.forms[formId].submit();
}

function formSubmitWait(formId, targetName)
{
	if(popwin.frames[targetName] != null)
	{
		formSubmitNoWait(formId);
	}
    else if(count<50)
	{
    	count++;
    	setTimeout("formSubmitWait('"+formId+"','"+targetName+"')", 200);
	}
}

function executeCommandHandler(formId, handlerId)
{
	var form = document.forms[formId];
	form.tbcn_command.value = handlerId;
	form.actionType.value='submit';
	formSubmit(formId, '_self');
	//20081204 shan add
	$.blockUI({ message: '<img src="./images/loading.gif" />' });
}	

function executeCommandHandlerNoBlock(formId, handlerId)
{
	var form = document.forms[formId];
	form.tbcn_command.value = handlerId;
	form.actionType.value='submit';
	formSubmit(formId, '_self');
}	


function selectTableAll(obj, formId, key)
{
	var myForm = eval('document.all.'+formId);
    for(var i=0; i<myForm.elements.length; i++)
    {
        if(myForm.elements[i].name == key)
        {
        	myForm.elements[i].checked = obj.checked;
        }
    }
}
function popwindow(link)
{
    popworklist = window.open(link, 'popworklist', 'width=800,height=430,left=100,top=100,status=1,scrollbars=1,resizable=1');
    popworklist.focus();
}
function popwindow(link, winName)
{
    popworklist = window.open(link, winName, 'width=800,height=430,left=100,top=100,status=1,scrollbars=1,resizable=1');
    popworklist.focus();
}
function switchSearchBox(type)
{
    window.location='viewGeneric.do?switch='+type;
}
function switchDelegationBox(type)
{
    window.location='viewConfig.do?actionType=delegation&switch='+type;
}
function maxlength(textField, maxlength)
{
    if (textField.value.length > maxlength)
    {
        textField.value= textField.value.substring(0, maxlength);
        textField.blur();
        alert("Please do not exceed " + maxlength + " characters.");
    }
}
function refresh()
{
    window.location='home.do';
}

var _selectedColor = "#000000";
var _borderColor = "#ffffff";

function changehighlight(thisitem)
{
    thisitem.style.borderColor = _selectedColor;
}

function cancelhighlight(thisitem)
{
    thisitem.style.borderColor = _borderColor;
}

function isObject(o)
{
    return (typeof(o)=="object");
}

function isString(o)
{
    return (typeof(o)=="string");
}

function isArray(o)
{
    return (isObject(o) && (o.length) &&(!isString(o)));
}

function select(id)
{
    if (isArray(document.forms[0].selections))
    {
        for (i = 0; i < document.forms[0].selections.length; i++)
        {
            document.forms[0].selections[i].checked = (document.forms[0].selections[i].value == id);
        }
    }
    else
    {
        document.forms[0].selections.checked = true;
    }
}

function clearall()
{
    if ( document.forms[0].selections != null )
    {
        for (i = 0; i < document.forms[0].selections.length; i++)
        {
            document.forms[0].selections[i].checked = false;
        }
    }
}

function operateitem(itemname, actionname, actionpath)
{
    document.forms[0].actionName.value = actionname;

    if (actionname == 'add')
    {
        document.forms[0].action = '../'+actionpath;
        document.forms[0].submit();
    }
    else if (document.forms[0].selections != null)
    {
        if (isArray(document.forms[0].selections))
        {
            count = 0;
            for (i = 0; i < document.forms[0].selections.length; i++)
            {
                if (document.forms[0].selections[i].checked == true) count++;
            }
            if (count == 1)
            {
                document.forms[0].action = '../'+actionpath;
                document.forms[0].submit();
            }
            else if (count == 0) {
                alert('no ' + itemname + ' selected to ' + actionname);
            }
            else {
                alert('can only ' + actionname + ' one ' + itemname + ' a time');
            }
        }
        else
        {
            document.forms[0].selections.checked = true;
            document.forms[0].action = '../'+actionpath;
            document.forms[0].submit();
        }
    }
    else
    {
        alert('no ' + itemname + ' to ' + actionname);
    }
}
function refreshOpener()
{
}

function popupDialog(port, id)
{
	document.getElementById('wlframe').setAttribute('src', 'http://localhost:'+port+'/'+id);
}

function getRealWidgetObjectList()
{
    var list = new Array(0);
    var size = 0;

    if(this.formId == "_tbcn_empty_formId_")
    {
        var formList = document.forms;

        for(i=0; i<formList.length; i++)
        {
        	var thisForm = formList[i];

            for(var j=0; j<thisForm.elements.length; j++)
            {
                if(thisForm.elements[j].name == this.widgetId)
                {
                    list[size++] = thisForm.elements[j];
                }
            }
        }
    }
    else
    {
        var myForm = eval('document.all.'+this.formId);

        if(this.formId == this.widgetId)
        {
            list[size++] = myForm;
        }

        for(var i=0; i<myForm.elements.length; i++)
        {
            if(this.realId == "_tbcn_empty_realId_" && myForm.elements[i].name == this.widgetId)
            {
                list[size++] = myForm.elements[i];
            }
            else if(myForm.elements[i].name == this.realId)
            {
                list[size++] = myForm.elements[i];
            }
        }
    }
	return list;
}

function Widget(formId, widgetId, realId)
{
    this.formId = formId;
    this.widgetId = widgetId;
    this.realId = realId;
    this.getObjectList = getRealWidgetObjectList;
}

function getElement(formId, widgetId)
{
    var list = new Array(0);
    var size = 0;

    var hasValue = false;

    for(var i=0; i<widgetList.length; i++)
    {
        if(widgetList[i].formId == formId && widgetList[i].widgetId == widgetId)
        {
            hasValue = true;
            var list2 = widgetList[i].getObjectList();

            for(var j=0; j<list2.length; j++)
            {
                list[size++] = list2[j];
            }
        }
    }

    if(!hasValue)
    {
    	var finalTest = new Widget(formId, widgetId, "_tbcn_empty_realId_");
        var list2 = finalTest.getObjectList();

        for(var j=0; j<list2.length; j++)
        {
            list[size++] = list2[j];
        }
    }

    if(list.length == 0)
    {
        return null;
    }
    else if(list.length == 1)
    {
        return list[0];
    }
    else
    {
        return list;
    }
}

function getElementByWidgetId(widgetId)
{
    var list = new Array(0);
    var size = 0;
    var hasValue = false;

    for(var i=0; i<widgetList.length; i++)
    {
        if(widgetList[i].widgetId == widgetId)
        {
            hasValue = true;
            var list2 = widgetList[i].getObjectList();
            for(var j=0; j<list2.length; j++)
            {
                list[size++] = list2[j];
            }
        }
    }
    if(!hasValue)
    {
    	var finalTest = new Widget("_tbcn_empty_formId_", widgetId, "_tbcn_empty_realId_");
        var list2 = finalTest.getObjectList();

        for(var j=0; j<list2.length; j++)
        {
            list[size++] = list2[j];
        }
    }

    if(list.length == 0)
    {
        return null;
    }
    else if(list.length == 1)
    {
        return list[0];
    }
    else
    {
        return list;
    }
}

function focusElement(formId, elementId)
{
    var myForm = eval('document.all.'+formId);

    for(var i=0; i<myForm.elements.length; i++)
    {
        if(myForm.elements[i].name == elementId)
        {
            myForm.elements[i].focus();
            return true;
        }
    }
}


if(window.top!=null)
{
    window.top.document.title = window.document.title;
}

