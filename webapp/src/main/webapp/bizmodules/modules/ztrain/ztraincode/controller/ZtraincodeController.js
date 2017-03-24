Ext.define("Ztraincode.controller.ZtraincodeController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Ztraincode.view.ZtraincodeView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.ztrain.ztraincode.dto.TestTableDto',
	queryAction: 'ztraincode-query',
	loadAction: 'ztraincode-load',
	addAction: 'ztraincode-add',
	modifyAction: 'ztraincode-modify',
	deleteAction: 'ztraincode-delete',
    exportXlsAction:"ztraincode-exportxls",
	afterInit: function () {
		var me = this;
},
beforeAdd: function () {
var me = this;

return true;
},
afterAdd: function () {
var me = this;
},
beforeCopyAdd: function () {
var me = this;
return true;
},
afterCopyAdd: function () {
var me = this;
},
beforeModify: function () {
var me = this;
return true;
},
afterModify: function () {
var me = this;
},
beforeSave: function () {
var me = this;
var view = me.view;
var code = view.getCmp("testTableDto->code").gotValue(); //取到前台code值。
if(code=="1") {
	Scdp.MsgUtil.warn("学号不能为1，保存失败")
	return false;
}else{
	return true
}
return true;
},
afterSave: function (retData) {
var me = this;
},
beforeLoadItem: function () {
var me = this;
return true;
},
afterLoadItem: function () {
var me = this;
},
beforeCancel: function () {
var me = this;
return true;
},
afterCancel: function () {
var me = this;
},
beforeDelete: function () {
var me = this;
return true;
},
afterDelete: function () {
var me = this;
},
beforeBatchDel: function () {
var me = this;
return true;
},
afterBatchDel: function () {
var me = this;
},
beforeExport: function () {
var me = this;
return true;
},
afterExport: function () {
var me = this;
	}
});