Ext.define("Budgeth.controller.BudgethController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Budgeth.view.BudgethView',
    uniqueValidateFields: ['officeId', 'year'],
    extraEvents: [
        {cid: 'appropriationDetail', name: 'click', fn: 'appropriationDetail'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto',
    queryAction: 'budgeth-query',
    loadAction: 'budgeth-load',
    addAction: 'budgeth-add',
    modifyAction: 'budgeth-modify',
    deleteAction: 'budgeth-delete',
    exportXlsAction: "budgeth-exportxls",
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var uuid = actionParams.uuid;
            if (Scdp.ObjUtil.isNotEmpty(uuid)) {
                me.loadItem(uuid);
            }
        }
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;

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
        var view = me.view;
        var year = view.getCmp('nonProjectBudgetHDto->year').gotValue();
        var date = new Date();
        var yy = date.getFullYear();
        if (year != yy) {
            Scdp.MsgUtil.warn("不是当年预算计划，无法进行预算办理！")
            return false
        }
        var state = view.getCmp('nonProjectBudgetHDto->state').gotValue();
        if (state != 2) {
            Scdp.MsgUtil.warn("该计划未审核通过，无法进行预算办理！")
            return false
        }
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("nonProjectBudgetHDto->officeId").setReadOnly(true);
        me.view.getCmp("nonProjectBudgetHDto->year").setReadOnly(true);
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
        var view = me.view;
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
        return false;
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
    //拨款明细
    appropriationDetail: function () {
        var me = this;
        var view = me.view;

        var selectedRow = view.getCmp("nonProjectBudgetCDto").getSelectionModel().getSelection()
        if (selectedRow.length > 0) {
            var budgetCUuid = selectedRow[0].get("uuid");
            var postData = {budgetCUuid: budgetCUuid};
            Scdp.doAction("budgeth-approdetail", postData, function (result) {
                var lstBudgetAppro = result.lstBudgetAppro;
                var callback = function (subView) {
                    win.close();
                }
                var controller = Ext.create("Budgeth.controller.BudgetapproController");
                var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '拨款明细', "确定");
                var childView = win.down('IView');

                childView.getCmp("nonProjectBudgetApproDto").sotValue(lstBudgetAppro);
            }, null, null, null);
        } else {
            Scdp.MsgUtil.info("请选择一条记录");
            return false;
        }
    }
});


