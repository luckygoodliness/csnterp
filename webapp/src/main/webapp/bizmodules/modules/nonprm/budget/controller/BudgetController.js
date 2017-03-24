Ext.define("Budget.controller.BudgetController", {
        extend: 'ErpMvc.controller.ErpAbstractCrudController',
        viewClass: 'Budget.view.BudgetView',
        uniqueValidateFields: ['officeId', 'year'],
        extraEvents: [
            {cid: 'nonProjectBudgetHDto->year', name: 'blur', fn: 'checkoutYear'},
            {cid: 'grantAdjustment', name: 'click', fn: 'doGrantAdjustment'},
            {cid: 'budgetAdjustment', name: 'click', fn: 'doBudgetAdjustment'},
            {cid: 'nonProjectBudgetCDto', name: 'beforeedit', fn: 'doBeforeEdit'}//改变grid可编辑状态
        ],
        dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.budget.dto.NonProjectBudgetHDto',
        queryAction: 'budget-query',
        loadAction: 'budget-load',
        addAction: 'budget-add',
        modifyAction: 'budget-modify',
        deleteAction: 'budget-delete',
        exportXlsAction: "budget-exportxls",
        afterInit: function () {
            var me = this;
            me.callParent(arguments);

            var role = Erp.Util.getCurrentUserRoleName();
            me.isFinancialVp = false;
            if (Scdp.ObjUtil.isNotEmpty(role)) {
                me.isFinancialVp = role.ROLE.indexOf("计财部主任") > -1 || role.ROLE.indexOf("计财部分管副主任") > -1;
            }

        },
        beforeAdd: function () {
            var me = this;
            return true;
        },
        afterAdd: function () {
            var me = this;
            var view = me.view;
            var d = new Date();
            var year = d.getFullYear();
            view.getCmp('nonProjectBudgetHDto->year').sotValue(year);
            var state = '0';
            view.getCmp('nonProjectBudgetHDto->state').sotValue(state);
        },
        beforeCopyAdd: function () {
            var me = this;
            return true;
        },
        afterCopyAdd: function () {
            var me = this;
            var state = '0';
            me.view.getCmp('nonProjectBudgetHDto->state').sotValue(state);
            var nonProjectBudgetCGrid = me.view.getCmp("nonProjectBudgetCDto");
            nonProjectBudgetCGrid.clearData();
        },
        beforeModify: function () {
            var me = this;
            return true;
        },
        afterModify: function () {
            var me = this;
            me.view.getCmp("nonProjectBudgetHDto->officeId").setReadOnly(true);
            me.view.getCmp("nonProjectBudgetHDto->year").setReadOnly(true);
        },
        beforeSave: function () {
            var me = this;
            var view = me.view;
            return true;
        },
        afterSave: function (retData) {
            var me = this;
            me.callParent(arguments);
        },
        beforeLoadItem: function () {
            var me = this;
            return true;
        },
        checkoutYear: function () {
            var me = this;
            var d = new Date();
            var year = me.view.getCmp("nonProjectBudgetHDto->year").lastValue;
            var y = parseInt(year);
            var yy = d.getFullYear();
            var nextYear = yy + 1;
            if (y != yy && y != nextYear) {
                Scdp.MsgUtil.info("年份不是当年或下一年，请重新输入！");
                me.view.getCmp("nonProjectBudgetHDto->year").focus();
                return;
            }
        },
        afterLoadItem: function (result) {
            var me = this;
            me.callParent(arguments);
            var view = me.view;
            view.getCmp('nonProjectBudgetHDto');
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
            var state = view.getCmp('nonProjectBudgetHDto->state').getValue();
            if (state != '0' && state != '5') {
                Scdp.MsgUtil.info("只能删除新增或退回状态的单据！");
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

//    编辑之前执行
        doBeforeEdit: function (editor, context) {
            var me = this;
            var view = me.view;
            if (context.field == 'thisYearApplyed') {
                if (context.record.data.subjectName == '管理费用' ||
                    context.record.data.subjectName == '固定资产添置') {
                    return false
                }
            }
            var state = view.getCmp('nonProjectBudgetHDto->state').getValue();
            if (state == '0' || state == '5') {
                if (context.field == 'thisYearFirstInstance' || context.field == 'thisYearAssigned') {
                    return false
                }
                if (!me.isFinancialVp && context.field == 'thisYearPreappropriation') {
                    return false
                }
            }
            else {
                if (context.field == 'thisYearApplyed') {
                    return false
                }
            }
        },

        //预算调整跳转
        doBudgetAdjustment: function () {
            var me = this;
            var selectedSubject = me.view.getResultPanel().getSelectionModel().getSelection();
            if (selectedSubject.length == 0) {
                Scdp.MsgUtil.warn("请选择一条记录");
                return;
            }
            if (selectedSubject.length > 1) {
                Scdp.MsgUtil.warn("只能选择一条记录");
                return;
            }

            var date = new Date();
            var yy = date.getFullYear();
            var year = selectedSubject[0].data.year;
            if (year != yy) {
                Scdp.MsgUtil.warn("不是当年预算计划，无法进行预算变更！")
                return;
            }
            var state = selectedSubject[0].data.codeDesc;
            if (state != '已审核') {
                Scdp.MsgUtil.warn("该计划未审核通过，无法进行预算变更！");
                return;
            }

            //目标页面的代码
            var menuCode = 'BUDGETAJUST';
            var param = {};
            param.year = year;
            param.officeId = selectedSubject[0].data.officeId;

            Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },

    //拨款调整跳转
    doGrantAdjustment
:
function () {
    var me = this;
    var selectedSubject = me.view.getResultPanel().getSelectionModel().getSelection();
    if (selectedSubject.length == 0) {
        Scdp.MsgUtil.warn("请选择一条记录");
        return;
    }
    if (selectedSubject.length > 1) {
        Scdp.MsgUtil.warn("只能选择一条记录");
        return;
    }
    var date = new Date();
    var yy = date.getFullYear();
    var year = selectedSubject[0].data.year;
    if (year != yy) {
        Scdp.MsgUtil.warn("不是当年预算计划，无法进行预算办理！")
        return;
    }
    var state = selectedSubject[0].data.codeDesc;
    if (state != '已审核') {
        Scdp.MsgUtil.warn("该计划未审核通过，无法进行预算办理！");
        return;
    }

    //页面跳转
    var menuCode = 'BUDGETH';
    var param = {};
    param.uuid = selectedSubject[0].data.uuid;

    Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
}
,
loadWorkFlowProcessDeptCode: function () {
    var me = this;
    var processDeptCode = me.view.getCmp('nonProjectBudgetHDto->officeId').gotValue();
    return processDeptCode;
}
})
;


