Ext.define("Transfer.controller.TransferController", {
	extend: 'ErpMvc.controller.ErpAbstractCrudController',
	viewClass: 'Transfer.view.TransferView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto',
	queryAction: 'transfer-query',
	loadAction: 'transfer-load',
	addAction: 'transfer-add',
	modifyAction: 'transfer-modify',
	deleteAction: 'transfer-delete',
    exportXlsAction:"transfer-exportxls",
	afterInit: function () {
		var me = this;
},
beforeAdd: function () {
var me = this;
return true;
},
doAdd: function(){
    var me = this;
    me.callParent(arguments);
    me.view.getCmp('state').sotValue('未保存');
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