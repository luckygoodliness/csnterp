Ext.define("Operatekeyprojectsinfo.controller.OperatekeyprojectsinfoController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Operatekeyprojectsinfo.view.OperatekeyprojectsinfoView',
    uniqueValidateFields: ['projectName'],
    extraEvents: [
        //{cid:'btnShowAll',name:'click',fn:'doShowAll'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.dto.OperateKeyProjectsInfoDto',
    queryAction: 'operatekeyprojectsinfo-query',
    loadAction: 'operatekeyprojectsinfo-load',
    addAction: 'operatekeyprojectsinfo-add',
    modifyAction: 'operatekeyprojectsinfo-modify',
    deleteAction: 'operatekeyprojectsinfo-delete',
    exportXlsAction: "operatekeyprojectsinfo-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        //经营开发表传过来的数据
        var obj = this.actionParams;
        var fid = obj.fid;
        var projectName = obj.projectName;
        var customerId = obj.customerId;
        var comBidUnit = obj.comBidUnit;
        var comBidNumber = obj.comBidNumber;
        var bidingDocStart = obj.bidingDocStart;
        var bidingDocEnd = obj.bidingDocEnd;
        var bidingDocPrice = obj.bidingDocPrice;
        var bidBond = obj.bidBond;
        var eotm = obj.eotm;
        var bod = obj.bod;
        var operationState = obj.operationState;
        if (Scdp.ObjUtil.isNotEmpty(fid)) {
            me.doAdd();
            me.view.getCmp('operateKeyProjectsInfoDto->fid').sotValue(fid);
            me.view.getCmp('operateKeyProjectsInfoDto->projectName').sotValue(projectName);
            me.view.getCmp('operateKeyProjectsInfoDto->proprietorUnit').sotValue(customerId);
            me.view.getCmp('operateKeyProjectsInfoDto->comBidUnit').sotValue(comBidUnit);
            me.view.getCmp('operateKeyProjectsInfoDto->comBidNumber').sotValue(comBidNumber);
            me.view.getCmp('operateKeyProjectsInfoDto->bidingDocStart').sotValue(bidingDocStart);
            me.view.getCmp('operateKeyProjectsInfoDto->bidingDocEnd').sotValue(bidingDocEnd);
            me.view.getCmp('operateKeyProjectsInfoDto->bidingDocPrice').sotValue(bidingDocPrice);
            me.view.getCmp('operateKeyProjectsInfoDto->bidBond').sotValue(bidBond);
            me.view.getCmp('operateKeyProjectsInfoDto->eotm').sotValue(eotm);
            me.view.getCmp('operateKeyProjectsInfoDto->bod').sotValue(bod);
        }
        var col = me.view.getResultPanel();
//默认经营状况显示为"售前"的界面布局
        me.view.getCmp('operationState_query').sotValue("0");
        //col.getColumnByIndex(5).show();
        //col.getColumnByIndex(6).show();
        //col.getColumnByIndex(7).show();
        //col.getColumnByIndex(8).hide();
        //col.getColumnByIndex(9).hide();
        me.view.getCmp('operateKeyProjectsInfoDto->competitor').setVisible(true);
        me.view.getCmp('operateKeyProjectsInfoDto->projectOc').setVisible(true);
        me.view.getCmp('operateKeyProjectsInfoDto->questions').setVisible(true);
        me.view.getCmp('operateKeyProjectsInfoDto->projectId').setVisible(false);
        me.view.getCmp('operateKeyProjectsInfoDto->operationSummary').setVisible(false);
//判断经营状况以显示不同的界面布局
        var state = operationState;
        if (state == 0 || state == "售前") {
            //col.getColumnByIndex(5).show();
            //col.getColumnByIndex(6).show();
            //col.getColumnByIndex(7).show();
            //col.getColumnByIndex(8).hide();
            //col.getColumnByIndex(9).hide();
            me.view.getCmp('operateKeyProjectsInfoDto->competitor').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->projectOc').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->questions').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->projectId').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->operationSummary').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->operationState_query').sotValue("0");
        }
        if (state == 1 || state == "完成竞标") {
            //col.getColumnByIndex(5).hide();
            //col.getColumnByIndex(6).hide();
            //col.getColumnByIndex(7).hide();
            //col.getColumnByIndex(8).show();
            //col.getColumnByIndex(9).show();
            me.view.getCmp('operateKeyProjectsInfoDto->competitor').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->projectOc').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->questions').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->projectId').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->operationSummary').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->operationState_query').sotValue("1");
        }

    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this
        me.view.getCmp("operateKeyProjectsInfoDto->recordDate").sotValue(new Date());
        //me.doShowAll();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("operateKeyProjectsInfoDto->recordDate").sotValue(new Date());
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
//me.view.getCmp('btnShowAll').setDisabled(true);
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
    },
    //重新查询
    doQuery: function () {
        var me = this;
        me.callParent(arguments);
        var operationState_query = me.view.getCmp('operationState_query').gotValue();
        var col = me.view.getResultPanel();
        if (operationState_query == "0") {
            //col.getColumnByIndex(5).show();
            //col.getColumnByIndex(6).show();
            //col.getColumnByIndex(7).show();
            //col.getColumnByIndex(8).hide();
            //col.getColumnByIndex(9).hide();
            me.view.getCmp('operateKeyProjectsInfoDto->competitor').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->projectOc').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->questions').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->projectId').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->operationSummary').setVisible(false);
        }
        if (operationState_query == "1") {
            //col.getColumnByIndex(5).hide();
            //col.getColumnByIndex(6).hide();
            //col.getColumnByIndex(7).hide();
            //col.getColumnByIndex(8).show();
            //col.getColumnByIndex(9).show();
            me.view.getCmp('operateKeyProjectsInfoDto->competitor').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->projectOc').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->questions').setVisible(false);
            me.view.getCmp('operateKeyProjectsInfoDto->projectId').setVisible(true);
            me.view.getCmp('operateKeyProjectsInfoDto->operationSummary').setVisible(true);
        }
    },
    doShowAll: function () {
        //弹出新窗口byController
        var me = this;
        var view = this.view;
        var callBack = function (subView) {
            var edit = subView.getCmp("operateKeyProjectsInfoDto");
            return true;
        };
        var fid = me.view.getCmp("operateKeyProjectsInfoDto->fid").gotValue();
        var projectName = me.view.getCmp("operateKeyProjectsInfoDto->projectName").gotValue();
        var proprietorUnit = me.view.getCmp("operateKeyProjectsInfoDto->proprietorUnit").gotValue();
        var comBidUnit = me.view.getCmp("operateKeyProjectsInfoDto->comBidUnit").gotValue();
        var comBidNumber = me.view.getCmp("operateKeyProjectsInfoDto->comBidNumber").gotValue();
        var bidingDocStart = me.view.getCmp("operateKeyProjectsInfoDto->bidingDocStart").gotValue();
        var bidingDocEnd = me.view.getCmp("operateKeyProjectsInfoDto->bidingDocEnd").gotValue();
        var bidingDocPrice = me.view.getCmp("operateKeyProjectsInfoDto->bidingDocPrice").gotValue();
        var bidBond = me.view.getCmp("operateKeyProjectsInfoDto->bidBond").gotValue();
        var eotm = me.view.getCmp("operateKeyProjectsInfoDto->eotm").gotValue();
        var bod = me.view.getCmp("operateKeyProjectsInfoDto->bod").gotValue();
        var officeId = me.view.getCmp("operateKeyProjectsInfoDto->officeId").gotValue();
        var createBy = me.view.getCmp("operateKeyProjectsInfoDto->createBy").gotValue();
        var createTime = me.view.getCmp("operateKeyProjectsInfoDto->createTime").gotValue();
        var updateBy = me.view.getCmp("operateKeyProjectsInfoDto->updateBy").gotValue();
        var updateTime = me.view.getCmp("operateKeyProjectsInfoDto->updateTime").gotValue();
        var uuid = me.view.getCmp("operateKeyProjectsInfoDto->uuid").gotValue();
        var postData = {
            fid: fid,
            projectName: projectName,
            proprietorUnit: proprietorUnit,
            comBidUnit: comBidUnit,
            comBidNumber: comBidNumber,
            bidingDocStart: bidingDocStart,
            bidingDocEnd: bidingDocEnd,
            bidingDocPrice: bidingDocPrice,
            bidBond: bidBond,
            eotm: eotm,
            bod: bod,
            officeId: officeId,
            createBy: createBy,
            createTime: createTime,
            updateBy: updateBy,
            updateTime: updateTime,
            uuid: uuid
        }

        var controller = Ext.create("Operatekeyprojectsinfo.controller.ShowAllController");
        controller.postData = postData;
        Scdp.openNewWinByController(controller, callBack, 'temp_icon_16');
    }
});