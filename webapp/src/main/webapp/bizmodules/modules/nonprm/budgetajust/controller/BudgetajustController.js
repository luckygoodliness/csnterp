Ext.define("Budgetajust.controller.BudgetajustController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Budgetajust.view.BudgetajustView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'nonProjectBudgetAjustCDto', name: 'beforeedit', fn: 'doBeforeEdit'}//改变grid可编辑状态
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto',
    queryAction: 'budgetajust-query',
    loadAction: 'budgetajust-load',
    addAction: 'budgetajust-add',
    modifyAction: 'budgetajust-modify',
    deleteAction: 'budgetajust-delete',
    exportXlsAction: "budgetajust-exportxls",
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        var actionParams = me.actionParams;
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var year = actionParams.year;
            var officeId = actionParams.officeId;
            if (Scdp.ObjUtil.isNotEmpty(year) && Scdp.ObjUtil.isNotEmpty(officeId)) {
                view.getCmp('yearQ').sotValue(year);
                view.getCmp('officeIdQ').putValue(officeId);
                me.doQuery();
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
        var d = new Date();
        var year = d.getFullYear();
        view.getCmp('nonProjectBudgetAjustHDto->year').sotValue(year);
        view.getCmp('nonProjectBudgetAjustHDto->state').sotValue("0");
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
        me.view.getCmp("nonProjectBudgetAjustHDto->officeId").setReadOnly(true);
        me.view.getCmp("nonProjectBudgetAjustHDto->year").setReadOnly(true);
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var nonProjectBudgetAjustCD2Grid = view.getCmp("nonProjectBudgetAjustCD2Dto");
        var existsErrorData = false;
        if (nonProjectBudgetAjustCD2Grid.store.data.items.length > 0) {
            Ext.Array.each(nonProjectBudgetAjustCD2Grid.store.data.items, function (item) {
                if (Scdp.ObjUtil.isEmpty(item.get("price")) || item.get("price") < Erp.Const.FIXED_ASSET_LOW_LIMIT) {
                    existsErrorData = true;
                    return false;
                }
            });
        }
        if (existsErrorData) {
            Scdp.MsgUtil.info("存在单价小于2000元的固定资产明细，请检查确认！");
            return false;
        } else {
            return true;
        }
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
        var state = me.view.getCmp('nonProjectBudgetAjustHDto->state').gotValue();
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
    //更改部门即获取数据
    treeComboFieldChange: function () {
        var me = this;
        var view = me.view;
        var lstOfficeId = me.view.getCmp('nonProjectBudgetAjustHDto->officeId').ids;
        var year = me.view.getCmp('nonProjectBudgetAjustHDto->year').value;
        if (lstOfficeId.length == 0) {
            return;
        }
        if (year == 0 || year == null || year == "") {
            return;
        }
        var orgCode = lstOfficeId[0];
        var postData = {orgCode: orgCode, year: year};

        //获取数据显到Grid
        Scdp.doAction("budgetajust-fill", postData, function (out) {
//            console.log(out);
            var lstObjsBudgetAjustC = out.lstObjsBudgetAjustC; //拿费用代码
            var lstObjsBudgetAjustCD = out.lstObjsBudgetAjustCD;
            var lstObjsBudgetAjustCD2 = out.lstObjsBudgetAjustCD2;
            var nonProjectBudgetAjustCGrid = view.getCmp("nonProjectBudgetAjustCDto");
            var nonProjectBudgetAjustCDGrid = view.getCmp("nonProjectBudgetAjustCDDto");
            var nonProjectBudgetAjustCD2Grid = view.getCmp("nonProjectBudgetAjustCD2Dto");
            //每次查询之前删除上一次grid数据
            nonProjectBudgetAjustCGrid.sotValue(lstObjsBudgetAjustC)
            nonProjectBudgetAjustCDGrid.sotValue(lstObjsBudgetAjustCD)
            nonProjectBudgetAjustCD2Grid.sotValue(lstObjsBudgetAjustCD2)
        });
    },

    //    编辑之前执行
    doBeforeEdit: function (editor, context) {
        var me = this;
        if (context.field == 'budgetChanged') {
            if (context.record.data.subjectName == '管理费用' ||
                context.record.data.subjectName == '固定资产添置') {
                return false
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('nonProjectBudgetAjustHDto->officeId').gotValue();
        return processDeptCode;
    }
});

