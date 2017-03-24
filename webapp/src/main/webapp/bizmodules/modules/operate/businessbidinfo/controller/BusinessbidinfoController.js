Ext.define("Businessbidinfo.controller.BusinessbidinfoController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Businessbidinfo.view.BusinessbidinfoView',
    uniqueValidateFields: ['projectName'],
    extraEvents: [
        {cid: 'btnAddPreSale', name: 'click', fn: 'doAddPreSale'},
        {cid: 'btnAddCompleteBid', name: 'click', fn: 'doAddCompleteBid'},
        {cid: 'bod', name: 'blur', fn: 'doCompare'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto',
    queryAction: 'businessbidinfo-query',
    loadAction: 'businessbidinfo-load',
    addAction: 'businessbidinfo-add',
    modifyAction: 'businessbidinfo-modify',
    deleteAction: 'businessbidinfo-delete',
    exportXlsAction: "businessbidinfo-exportxls",
    afterInit: function () {
        var me = this;
        me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(false);
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("operateBusinessBidInfoDto->projectType").sotValue("1");
        view.getCmp("operateBusinessBidInfoDto->projectSourceType").sotValue("2");
        var deptCode = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE);
        var deptName = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        view.getCmp("operateBusinessBidInfoDto->contractorOffice").sotCodeAndDesc(deptCode, deptName);

        view.getCmp("operateBusinessBidInfoDto->state").sotValue('0');
        view.getCmp("operateBusinessBidInfoDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
        view.getCmp("operateBusinessBidInfoDto->operateBy").sotValue(Scdp.getCurrentUserId());
        view.getCmp("operateBusinessBidInfoDto->operateByName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
    },
    beforeCopyAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("operateBusinessBidInfoDto->state").sotValue('0');
        view.getCmp("operateBusinessBidInfoDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
        view.getCmp("operateBusinessBidInfoDto->operateBy").sotValue(Scdp.getCurrentUserId());
        view.getCmp("operateBusinessBidInfoDto->operateByName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
    },
    beforeModify: function () {
        var me = this;
        var view = me.view;
        var canBeModified = view.getCmp("operateBusinessBidInfoDto->canBeModified").gotValue();
        if (canBeModified === "0") {
            Scdp.MsgUtil.warn("该投标信息已在合同新增中被引用，且新增合同已生效或在流程中，无法修改!")
            return false;
        }
        var canMoneyBeModified = view.getCmp("operateBusinessBidInfoDto->canMoneyBeModified").gotValue();
        if (canMoneyBeModified === "0") {
            Scdp.MsgUtil.warn("该投标信息已在保证金关联，无法修改保证金金额!")
        }
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        var time = new Date();
        var bod = me.view.getCmp("bod").gotValue();
        if (bod) {
            var today = new Date();
            if (time >= bod) {
                me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(true);
            } else {
                me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(false);
            }
        }
        var canMoneyBeModifiedV = view.getCmp("operateBusinessBidInfoDto->canMoneyBeModified").gotValue();
        var canMoneyBeModified = Scdp.ObjUtil.isEmpty(canMoneyBeModifiedV) || canMoneyBeModifiedV === "1";
        view.getCmp("operateBusinessBidInfoDto->bidBond").setReadOnly(!canMoneyBeModified);
    },
    beforeSave: function () {
        var me = this;
        return me.validateDate();
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var canBeDeleted = view.getCmp("operateBusinessBidInfoDto->canBeDeleted").gotValue();
        if (canBeDeleted === "0") {
            Scdp.MsgUtil.warn("该投标信息已在合同新增或保证金中被引用，无法删除!")
            return false;
        }
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
    doCopyAdd: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var enableNull = me.workFlowFormData.indexOf("wf_checked_flag=1");
        if (enableNull > -1) {
            var bidResult = view.getCmp("operateBusinessBidInfoDto->bidResult").gotValue();
            if (bidResult == null) {
                Scdp.MsgUtil.warn("投标结果不能为空!");
                return false;
            }
        }
        return true;
    },
    refreshUIStatusBasedOnWorkFlow: function (returnData) {
        var me = this;
        var view = me.view;
        var isInWorkFlow = view.getCmp("operateBusinessBidInfoDto->state").gotValue() == "1";
        var canBeDeleted = isInWorkFlow || view.getCmp("operateBusinessBidInfoDto->canBeDeleted").gotValue() === "0";
        view.getCmp("editPanel->deleteBtn").setDisabled(canBeDeleted);

        var canBeModifiedV = view.getCmp("operateBusinessBidInfoDto->canBeModified").gotValue();
        var canBeModified = Scdp.ObjUtil.isEmpty(canBeModifiedV) || canBeModifiedV == 1;
        view.getCmp("editPanel->modifyBtn").setDisabled(!canBeModified);
    },
    doAdd: function () {
        var me = this;
        me.callParent(arguments);
        var curDate = new Date()
        var time = Ext.Date.format(curDate, 'Y-m-d');
        me.view.getCmp("bidingDocStart").sotValue(time);
        me.view.getCmp("bidingDocEnd").sotValue(time);
        me.view.getCmp("bod").sotValue(time);
        var bod = me.view.getCmp("bod").gotValue();
        if (bod) {
            if (curDate >= bod) {
                me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(true);
            } else {
                me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(false);
            }
        }
    },
    doQuery: function () {
        var me = this;
        me.callParent(arguments);
        var fieldValue = me.view.getCmp("field").value;
        var selectResults = me.view.getResultPanel();
        if (!fieldValue.length == 0) {
            for (var i = 1; i < selectResults.resultColumns.length + 1; i++) {
                var record = selectResults.getColumnByIndex(i);
                //判断哪个字段隐藏
                if (fieldValue.indexOf(record.dataIndex.toUpperCase()) < 0) {
                    record.hide();
                } else {
                    record.show();
                }
            }
        } else {
            for (var i = 1; i < selectResults.resultColumns.length + 1; i++) {
                if (selectResults.getColumnByIndex(i).hidden) {
                    var initialConfig = selectResults.getColumnByIndex(i).initialConfig;
                    !initialConfig.hidden && selectResults.getColumnByIndex(i).show();
                }
            }
        }
    },

    dobtnAddImportant: function (value) {
        //页面跳转并传值
        var me = this;
        var fid = me.view.getCmp("operateBusinessBidInfoDto->uuid").gotValue();
        var projectName = me.view.getCmp("operateBusinessBidInfoDto->projectName").gotValue();
        var customerId = me.view.getCmp("operateBusinessBidInfoDto->customerId").gotValue();
        var comBidUnit = me.view.getCmp("operateBusinessBidInfoDto->comBidUnit").gotValue();
        var comBidNumber = me.view.getCmp("operateBusinessBidInfoDto->comBidNumber").gotValue();
        var bidingDocStart = me.view.getCmp("operateBusinessBidInfoDto->bidingDocStart").gotValue();
        var bidingDocEnd = me.view.getCmp("operateBusinessBidInfoDto->bidingDocEnd").gotValue();
        var bidingDocPrice = me.view.getCmp("operateBusinessBidInfoDto->bidingDocPrice").gotValue();
        var bidBond = me.view.getCmp("operateBusinessBidInfoDto->bidBond").gotValue();
        var eotm = me.view.getCmp("operateBusinessBidInfoDto->eotm").gotValue();
        var bod = me.view.getCmp("operateBusinessBidInfoDto->bod").gotValue();
        var operationState = value;
        var param = {
            fid: fid,
            //contractorOffice:deptName,
            projectName: projectName,
            customerId: customerId,
            comBidUnit: comBidUnit,
            comBidNumber: comBidNumber,
            bidingDocStart: bidingDocStart,
            bidingDocEnd: bidingDocEnd,
            bidingDocPrice: bidingDocPrice,
            bidBond: bidBond,
            eotm: eotm,
            bod: bod,
            operationState: operationState
        };
        var menuCode = 'OPERATEKEYPROJECTSINFO';

        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);

    },
    doAddPreSale: function () {
        var me = this;
        var value = 0;
        me.dobtnAddImportant(value);
    },
    doAddCompleteBid: function () {
        var me = this;
        var value = 1;
        me.dobtnAddImportant(value);
    },
    //当系统日期到达开标时间后,投标结果可以编辑
    doCompare: function () {
        var me = this;
        var view = me.view;
        if (view.uistatus != Scdp.Const.UI_INFO_STATUS_VIEW) {
            var time = new Date()
            var bod = me.view.getCmp("bod").gotValue();
            if (bod) {
                if (time >= bod) {
                    me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(true);
                } else {
                    me.view.getCmp('operateBusinessBidInfoDto->bidResult').sotEditable(false);
                }
            }
        }
    },
    validateDate: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("operateBusinessBidInfoDto->state").gotValue();
        //已处理的数据，不允许将投标结果清空
        if (state == "2") {
            var bidResult = view.getCmp("operateBusinessBidInfoDto->bidResult");
            if (Scdp.ObjUtil.isEmpty(bidResult)) {
                Scdp.MsgUtil.warn('已处理的投标信息，投标结果不能为空！');
                return false;
            }
        }

        var bidingDocStart = view.getCmp("bidingDocStart").gotValue();
        var bidingDocEnd = view.getCmp("bidingDocEnd").gotValue();
        var bod = me.view.getCmp("bod").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(bidingDocStart) && Scdp.ObjUtil.isNotEmpty(bidingDocEnd)) {
            if (bidingDocStart > bidingDocEnd) {
                Scdp.MsgUtil.info('标书购买开始日期不能晚于标书购买截止日期！');
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(bidingDocEnd) && Scdp.ObjUtil.isNotEmpty(bod)) {
            if (bod < bidingDocEnd) {
                Scdp.MsgUtil.info('开标日期不能早于标书购买截止日期！');
                return false;
            }
        }
        return true;
    }
});
