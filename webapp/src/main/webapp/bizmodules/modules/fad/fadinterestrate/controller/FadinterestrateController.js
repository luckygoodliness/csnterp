Ext.define("Fadinterestrate.controller.FadinterestrateController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Fadinterestrate.view.FadinterestrateView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.dto.FadInterestRateDto',
	queryAction: 'fadinterestrate-query',
	loadAction: 'fadinterestrate-load',
	addAction: 'fadinterestrate-add',
	modifyAction: 'fadinterestrate-modify',
	deleteAction: 'fadinterestrate-delete',
    exportXlsAction:"fadinterestrate-exportxls",
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
	var validityDateFrom = view.getCmp("fadInterestRateDto->validityDateFrom").gotValue();
	var validityDateTo = view.getCmp("fadInterestRateDto->validityDateTo").gotValue();
	if ((validityDateTo - validityDateFrom) < 0){
		Scdp.MsgUtil.info("保存失败！<br\>有效结束时间不能早于有效起始时间！");
		return false;
	}
	var flag = true;
	var uuid = view.getCmp('fadInterestRateDto->uuid').gotValue();
	var postData = {dateFrom:validityDateFrom, dateTo:validityDateTo, uuid:uuid};
	Scdp.doAction("fadinterestrate-validate", postData, function (res) {
		if(Scdp.ObjUtil.isNotEmpty(res) && res.result>0){
			Scdp.MsgUtil.warn("保存失败！<br\>当前有效开始时间或有效结束时间不能跟现有利率的有效时间重叠！");
			flag = false;
		}
	},false,false,false,null);

	return flag;
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