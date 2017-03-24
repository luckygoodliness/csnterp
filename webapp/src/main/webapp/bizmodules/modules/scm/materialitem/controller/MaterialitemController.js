Ext.define("Materialitem.controller.MaterialitemController", {
	extend: 'ErpMvc.controller.ErpAbstractCrudController',
	viewClass: 'Materialitem.view.MaterialitemView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.materialitem.dto.ScmMaterialItemDto',
	queryAction: 'materialitem-query',
	loadAction: 'materialitem-load',
	addAction: 'materialitem-add',
	modifyAction: 'materialitem-modify',
	deleteAction: 'materialitem-delete',
    exportXlsAction:"materialitem-exportxls",
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