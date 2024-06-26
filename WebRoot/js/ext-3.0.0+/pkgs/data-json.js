/*
 * Ext JS Library 3.0.0+
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
/*
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.data.JsonWriter=function(a){Ext.data.JsonWriter.superclass.constructor.call(this,a);if(this.returnJson!=undefined){this.encode=this.returnJson}};Ext.extend(Ext.data.JsonWriter,Ext.data.DataWriter,{returnJson:undefined,encode:true,render:function(c,a,d,b){Ext.apply(d,b);if(this.encode===true){if(Ext.isArray(a)&&b[this.meta.idProperty]){d[this.meta.idProperty]=Ext.encode(d[this.meta.idProperty])}d[this.meta.root]=Ext.encode(d[this.meta.root])}},createRecord:function(a){return this.toHash(a)},updateRecord:function(a){return this.toHash(a)},destroyRecord:function(a){return a.id}});
/*
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.data.JsonReader=function(a,b){a=a||{};Ext.applyIf(a,{idProperty:"id",successProperty:"success",totalProperty:"total"});Ext.data.JsonReader.superclass.constructor.call(this,a,b||a.fields)};Ext.extend(Ext.data.JsonReader,Ext.data.DataReader,{read:function(a){var b=a.responseText;var c=Ext.decode(b);if(!c){throw {message:"JsonReader.read: Json object not found"}}return this.readRecords(c)},onMetaChange:function(a,c,b){},simpleAccess:function(b,a){return b[a]},getJsonAccessor:function(){var a=/[\[\.]/;return function(c){try{return(a.test(c))?new Function("obj","return obj."+c):function(d){return d[c]}}catch(b){}return Ext.emptyFn}}(),readRecords:function(a){this.jsonData=a;if(a.metaData){delete this.ef;this.meta=a.metaData;this.recordType=Ext.data.Record.create(a.metaData.fields);this.onMetaChange(this.meta,this.recordType,a)}var u=this.meta,m=this.recordType,h=m.prototype.fields,t=h.items,p=h.length,q;this.buildExtractors();var l=this.getRoot(a),k=l.length,j=k,r=true;if(u.totalProperty){q=parseInt(this.getTotal(a),10);if(!isNaN(q)){j=q}}if(u.successProperty){q=this.getSuccess(a);if(q===false||q==="false"){r=false}}var d=[];for(var e=0;e<k;e++){var b=l[e];var g=new m(this.extractValues(b,t,p),this.getId(b));g.json=b;d[e]=g}return{success:r,records:d,totalRecords:j}},buildExtractors:function(){if(this.ef){return}var l=this.meta,h=this.recordType,e=h.prototype.fields,k=e.items,j=e.length;if(l.totalProperty){this.getTotal=this.getJsonAccessor(l.totalProperty)}if(l.successProperty){this.getSuccess=this.getJsonAccessor(l.successProperty)}this.getRoot=l.root?this.getJsonAccessor(l.root):function(f){return f};if(l.id||l.idProperty){var d=this.getJsonAccessor(l.id||l.idProperty);this.getId=function(g){var f=d(g);return(f===undefined||f==="")?null:f}}else{this.getId=function(){return null}}var c=[];for(var b=0;b<j;b++){e=k[b];var a=(e.mapping!==undefined&&e.mapping!==null)?e.mapping:e.name;c.push(this.getJsonAccessor(a))}this.ef=c},extractValues:function(h,d,a){var g,c={};for(var e=0;e<a;e++){g=d[e];var b=this.ef[e](h);c[g.name]=g.convert((b!==undefined)?b:g.defaultValue,h)}return c},readResponse:function(b,a){var c=(a.responseText!==undefined)?Ext.decode(a.responseText):a;if(!c){throw new Ext.data.JsonReader.Error("response")}if(Ext.isEmpty(c[this.meta.successProperty])){throw new Ext.data.JsonReader.Error("successProperty-response",this.meta.successProperty)}if((b===Ext.data.Api.actions.create||b===Ext.data.Api.actions.update)){if(Ext.isEmpty(c[this.meta.root])){throw new Ext.data.JsonReader.Error("root-emtpy",this.meta.root)}else{if(c[this.meta.root]===undefined){throw new Ext.data.JsonReader.Error("root-undefined-response",this.meta.root)}}}this.ef=this.buildExtractors();return c}});Ext.data.JsonReader.Error=Ext.extend(Ext.Error,{constructor:function(b,a){this.arg=a;Ext.Error.call(this,b)},name:"Ext.data.JsonReader"});Ext.apply(Ext.data.JsonReader.Error.prototype,{lang:{response:"An error occurred while json-decoding your server response","successProperty-response":'Could not locate your "successProperty" in your server response.  Please review your JsonReader config to ensure the config-property "successProperty" matches the property in your server-response.  See the JsonReader docs.',"root-undefined-response":'Could not locate your "root" property in your server response.  Please review your JsonReader config to ensure the config-property "root" matches the property your server-response.  See the JsonReader docs.',"root-undefined-config":'Your JsonReader was configured without a "root" property.  Please review your JsonReader config and make sure to define the root property.  See the JsonReader docs.',"idProperty-undefined":'Your JsonReader was configured without an "idProperty"  Please review your JsonReader configuration and ensure the "idProperty" is set (e.g.: "id").  See the JsonReader docs.',"root-emtpy":'Data was expected to be returned by the server in the "root" property of the response.  Please review your JsonReader configuration to ensure the "root" property matches that returned in the server-response.  See JsonReader docs.'}});
/*
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.data.ArrayReader=Ext.extend(Ext.data.JsonReader,{readRecords:function(q){this.arrayData=q;var h=this.meta,d=h?Ext.num(h.idIndex,h.id):null,b=this.recordType,p=b.prototype.fields,y=[],e;if(!this.getRoot){this.getRoot=h.root?this.getJsonAccessor(h.root):function(f){return f};if(h.totalProperty){this.getTotal=this.getJsonAccessor(h.totalProperty)}}var t=this.getRoot(q);for(var x=0;x<t.length;x++){var r=t[x];var a={};var m=((d||d===0)&&r[d]!==undefined&&r[d]!==""?r[d]:null);for(var w=0,l=p.length;w<l;w++){var z=p.items[w];var u=z.mapping!==undefined&&z.mapping!==null?z.mapping:w;e=r[u]!==undefined?r[u]:z.defaultValue;e=z.convert(e,r);a[z.name]=e}var c=new b(a,m);c.json=r;y[y.length]=c}var g=y.length;if(h.totalProperty){e=parseInt(this.getTotal(q),10);if(!isNaN(e)){g=e}}return{records:y,totalRecords:g}}});
/*
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.data.ArrayStore=Ext.extend(Ext.data.Store,{constructor:function(a){Ext.data.ArrayStore.superclass.constructor.call(this,Ext.apply(a,{reader:new Ext.data.ArrayReader(a)}))},loadData:function(e,b){if(this.expandData===true){var d=[];for(var c=0,a=e.length;c<a;c++){d[d.length]=[e[c]]}e=d}Ext.data.ArrayStore.superclass.loadData.call(this,e,b)}});Ext.reg("arraystore",Ext.data.ArrayStore);Ext.data.SimpleStore=Ext.data.ArrayStore;Ext.reg("simplestore",Ext.data.SimpleStore);
/*
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.data.JsonStore=Ext.extend(Ext.data.Store,{constructor:function(a){Ext.data.JsonStore.superclass.constructor.call(this,Ext.apply(a,{reader:new Ext.data.JsonReader(a)}))}});Ext.reg("jsonstore",Ext.data.JsonStore);