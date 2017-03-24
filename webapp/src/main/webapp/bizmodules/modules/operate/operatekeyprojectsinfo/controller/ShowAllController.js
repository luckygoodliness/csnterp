Ext.define("Operatekeyprojectsinfo.controller.ShowAllController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
	viewClass: 'Operatekeyprojectsinfo.view.ShowAllView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.dto.OperateKeyProjectsInfoDto',
	queryAction: 'operatekeyprojectsinfo-query',
	loadAction: 'operatekeyprojectsinfo-load',
	addAction: 'operatekeyprojectsinfo-add',
	modifyAction: 'operatekeyprojectsinfo-modify',
	deleteAction: 'operatekeyprojectsinfo-delete',
    exportXlsAction:"operatekeyprojectsinfo-exportxls",
	afterInit: function () {
		var me = this;

        me.doAdd()
        var postData = me.postData;
        var uuid = postData.uuid;
        if(Scdp.ObjUtil.isNotEmpty(uuid)){
                    me.loadItem(uuid,'view');
        }
        if(Scdp.ObjUtil.isNotEmpty(postData)){
            me.view.getCmp('fid').sotValue(postData.fid);
            me.view.getCmp('projectName').sotValue(postData.projectName);
            me.view.getCmp('proprietorUnit').sotValue(postData.proprietorUnit);
            me.view.getCmp('comBidUnit').sotValue(postData.comBidUnit);
            me.view.getCmp('comBidNumber').sotValue(postData.comBidNumber);
            me.view.getCmp('bidingDocStart').sotValue(postData.bidingDocStart);
            me.view.getCmp('bidingDocEnd').sotValue(postData.bidingDocEnd);
            me.view.getCmp('bidingDocPrice').sotValue(postData.bidingDocPrice);
            me.view.getCmp('bidBond').sotValue(postData.bidBond);
            me.view.getCmp('eotm').sotValue(postData.eotm);
            me.view.getCmp('bod').sotValue(postData.bod);
            me.view.getCmp('officeId').sotValue(postData.officeId);
            me.view.getCmp('createBy').sotValue(postData.createBy);
            me.view.getCmp('createTime').sotValue(postData.createTime);
            me.view.getCmp('updateBy').sotValue(postData.updateBy);
            me.view.getCmp('updateTime').sotValue(postData.updateTime);
        }


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