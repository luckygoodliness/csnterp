Ext.define("Scmsaetask.controller.ScmsaetaskController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Scmsaetask.view.ScmsaetaskView',
    uniqueValidateFields: [],
	extraEvents: [
		{cid:'taskCommit',name:'click',fn:'taskCommit'},
		{cid:'taskCancel',name:'click',fn:'taskCancel'}
	],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto',
	queryAction: 'scmsaetask-query',
	loadAction: 'scmsaetask-load',
	addAction: 'scmsaetask-add',
	modifyAction: 'scmsaetask-modify',
	deleteAction: 'scmsaetask-delete',
	exportXlsAction:"scmsaetask-exportxls",
	afterInit: function () {
		var me = this;
		me.callParent(arguments);
},
	taskCommit:function() {
		var me = this;
		var view = me.view;
		var myDate = Scdp.DateUtil.formatDate(new Date(), 1);
		var endTime = me.view.getCmp('scmSaeTaskDto->endTime').gotValue();
		var endDate = Scdp.DateUtil.formatDate(endTime, 1);
		if(Scdp.DateUtil.dateDiff(myDate, endDate) == 1) {
			Scdp.MsgUtil.warn("该考评已经过期，不能提交！");
			return false;
		}
		Scdp.MsgUtil.confirm("提交后将不能修改,是否确定提交？", function (e) {
			if ("yes" == e) {
				var scmSaeTaskId = view.getCmp("scmSaeTaskDto->uuid").gotValue();
				var postData = {scmSaeTaskId: scmSaeTaskId};
				Scdp.doAction("scmsaetask-taskcommit", postData, function (result) {
					if(result.success) {
						// 不可编辑
						me.loadItem(scmSaeTaskId);
						view.getCmp("editPanel->modifyBtn").setDisabled(true);
						view.getCmp("editPanel->taskCommit").setDisabled(true);
					}
				}, false);
			}
		});


	},
	taskCancel:function() {
		var me = this;
		var view = me.view;
		var scmSaeTaskId = view.getCmp("scmSaeTaskDto->uuid").gotValue();
		var postData = {scmSaeTaskId: scmSaeTaskId};
		Scdp.doAction("scmsaetask-taskcancel", postData, function (result) {
			if(result.success) {
				// 可编辑
				me.loadItem(scmSaeTaskId);
				view.getCmp("editPanel->modifyBtn").setDisabled(false);
				view.getCmp("editPanel->taskCommit").setDisabled(false);
			}
		}, false);
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
	var view = me.view;
	view.getCmp("scmSaeTaskDto->curYear").sotEditable(false);
	view.getCmp("scmSaeTaskDto->title").sotEditable(false);
	view.getCmp("scmSaeTaskDto->materialType").sotEditable(false);
	view.getCmp("scmSaeTaskDto->state").sotEditable(false);
	view.getCmp("scmSaeTaskDto->beginTime").sotEditable(false);
	view.getCmp("scmSaeTaskDto->endTime").sotEditable(false);
	view.getCmp("scmSaeTaskDto->userCode").sotEditable(false);
	view.getCmp("scmSaeTaskDto->createTime").sotEditable(false);
	// 点击考评-提交按钮灰掉
	view.getCmp("editPanel->taskCommit").setDisabled(true);
},
beforeSave: function () {
var me = this;
return true;
},
afterSave: function (retData) {
	var me = this;
	var view = me.view;
	me.callParent(arguments);
	// 保存完，提交按钮点亮
	view.getCmp("editPanel->taskCommit").setDisabled(false);
},
beforeLoadItem: function () {
var me = this;
return true;
},
afterLoadItem: function () {
	var me = this;
	me.callParent(arguments);
	var view = me.view;
	view.getCmp("editPanel->taskCancel").setDisabled(false);
	var state = view.getCmp("scmSaeTaskDto->state").gotValue();
	if("已提交"==state) {
		view.getCmp("editPanel->modifyBtn").setDisabled(true);
		view.getCmp("editPanel->taskCommit").setDisabled(true);
		view.getCmp("editPanel->deleteBtn").setDisabled(true);
	} else {
		view.getCmp("editPanel->modifyBtn").setDisabled(false);
		view.getCmp("editPanel->taskCommit").setDisabled(false);
		view.getCmp("editPanel->deleteBtn").setDisabled(false);
	}
	me.resetColumns(view);
	var role = Erp.Util.getCurrentUserRoleName();
	if(!(role.ROLE.indexOf("供应链部供应商管理专员") > -1)){
		view.getCmp("editPanel->deleteBtn").hide();
		view.getCmp("editPanel->taskCancel").hide();
	}


},
	resetColumns:function(view) {
		// 初始化评价项
		var dataIndexArr = ["item1","item2","item3","item4","item5",
			                 "item6","item7","item8","item9","item10",
			                 "item11","item12","item13","item14","item15"];
		var scmSaeTaskId = view.getCmp("scmSaeTaskDto->uuid").gotValue();
		var postData = {scmSaeTaskId: scmSaeTaskId};
		Scdp.doAction("scmsaetask-getcasename", postData, function (result) {
			if(result.success) {
				var items = result.items;
				var scmSaeFormGrid = view.getCmp("scmSaeFormDto");
				for(var dataIndex in dataIndexArr) {
					var itemName = dataIndexArr[dataIndex];
					var column = scmSaeFormGrid.getColumnBydataIndex(itemName);
					if(items.hasOwnProperty(itemName)) {
						var columnName = items[itemName];
						column.show();
						column.setText(columnName);
					} else {
						column.hide();
					}
				}
			}
		},false);
	},
beforeCancel: function () {
var me = this;
return true;
},
afterCancel: function () {
	var me = this;
	var view = me.view;
	me.callParent(arguments);
	view.getCmp("editPanel->taskCommit").setDisabled(false);
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
	},
});