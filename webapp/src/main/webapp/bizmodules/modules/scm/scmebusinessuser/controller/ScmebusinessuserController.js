Ext.define("Scmebusinessuser.controller.ScmebusinessuserController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Scmebusinessuser.view.ScmebusinessuserView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.dto.ScmEbusinessUserDto',
	queryAction: 'scmebusinessuser-query',
	loadAction: 'scmebusinessuser-load',
	addAction: 'scmebusinessuser-add',
	modifyAction: 'scmebusinessuser-modify',
	deleteAction: 'scmebusinessuser-delete',
    exportXlsAction:"scmebusinessuser-exportxls",
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