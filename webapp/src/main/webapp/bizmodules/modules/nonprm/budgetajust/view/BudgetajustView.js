Ext.define('Budgetajust.view.BudgetajustView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/budgetajust',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'budgetajust-query-layout.xml',
    editLayoutFile: 'budgetajust-edit-layout.xml',
    //extraLayoutFile: 'budgetajust-extra-layout.xml',
    bindings: ['nonProjectBudgetAjustHDto', 'nonProjectBudgetHDto',
        'nonProjectBudgetAjustHDto.nonProjectBudgetAjustCDto',
        'nonProjectBudgetAjustHDto.nonProjectBudgetAjustCDDto',
        'nonProjectBudgetAjustHDto.nonProjectBudgetAjustCD2Dto'
    ],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_Budget_Adjustment',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        //隐藏查询页面的新增按钮
        me.getCmp("nonProjectBudgetAjustCDDto").getCmp("toolbar").hide();
        me.getCmp("nonProjectBudgetAjustCD2Dto").getCmp("toolbar").hide();

        me.getCmp("nonProjectBudgetAjustCDDto").getCmp("copyAddRowBtn").hide();
        me.getCmp("nonProjectBudgetAjustCD2Dto").getCmp("copyAddRowBtn").hide();

        var cGrid = me.getCmp("nonProjectBudgetAjustCDto");
        cGrid.afterSelect = function (d, rowIndex) {
            //获取选中model
            var selectedSubject = me.getResultPanel().getSelectionModel().getSelection();

            var nonProjectBudgetAjustCDGrid = me.getCmp("nonProjectBudgetAjustCDDto");
            var nonProjectBudgetAjustCD2Grid = me.getCmp("nonProjectBudgetAjustCD2Dto");
            if (d.data.subjectName == '管理费用') {
                nonProjectBudgetAjustCDGrid.getCmp("toolbar").show();
                nonProjectBudgetAjustCD2Grid.getCmp("toolbar").hide();
            } else if (d.data.subjectName == '固定资产添置') {
                nonProjectBudgetAjustCD2Grid.getCmp("toolbar").show();
                nonProjectBudgetAjustCDGrid.getCmp("toolbar").hide();
            } else {
                nonProjectBudgetAjustCDGrid.getCmp("toolbar").hide();
                nonProjectBudgetAjustCD2Grid.getCmp("toolbar").hide();
            }
        }
        me.initDetailGrid();
        me.overrideDeleteToolbar();

        var deptCmp = me.getCmp("nonProjectBudgetAjustHDto->officeId");
        deptCmp.afterSotValue = me.depatementChange;
        Ext.Array.each(cGrid.columns, function (item) {
            if (item.dataIndex == "subjectCode") {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red"> 合计</strong>';
                    }
                })
            }
            if (item.summaryType) {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">' + Ext.util.Format.number(value, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },

    depatementChange: function () {
        var me = this;
        var view = me.up("IView");
        var orgCode = view.getCmp('nonProjectBudgetAjustHDto->officeId').gotValue();
        var year = view.getCmp('nonProjectBudgetAjustHDto->year').lastValue;
        var nonProjectBudgetCGrid = view.getCmp("nonProjectBudgetAjustCDto");
        var nonProjectBudgetCDGrid = view.getCmp("nonProjectBudgetAjustCDDto");
        var nonProjectBudgetCD2Grid = view.getCmp("nonProjectBudgetAjustCD2Dto");
        nonProjectBudgetCGrid.store.removeAll();
        nonProjectBudgetCDGrid.store.removeAll();
        nonProjectBudgetCD2Grid.store.removeAll();

        if (year != null && orgCode != null) {
            var postData = {orgCode: orgCode, year: year};
            Scdp.doAction("budgetajust-fill", postData, function (out) {
                var lstObjsBudgetAjustC = out.lstObjsBudgetAjustC; //拿费用代码
                var lstObjsBudgetAjustCD = out.lstObjsBudgetAjustCD;
                var lstObjsBudgetAjustCD2 = out.lstObjsBudgetAjustCD2;
                var nonProjectBudgetAjustCGrid = view.getCmp("nonProjectBudgetAjustCDto");
                var nonProjectBudgetAjustCDGrid = view.getCmp("nonProjectBudgetAjustCDDto");
                var nonProjectBudgetAjustCD2Grid = view.getCmp("nonProjectBudgetAjustCD2Dto");
                //每次查询之前删除上一次grid数据
                nonProjectBudgetAjustCGrid.sotValue(lstObjsBudgetAjustC);
                nonProjectBudgetAjustCDGrid.sotValue(lstObjsBudgetAjustCD);
                nonProjectBudgetAjustCD2Grid.sotValue(lstObjsBudgetAjustCD2);
            });
        }
    },
    overrideDeleteToolbar: function () {
        var me = this;
        var budgetAjustCGrid = me.getCmp("nonProjectBudgetAjustCDto");
        budgetAjustCGrid.afterEditGrid = me.afterEditWithBudgetAjustCGrid;

        var budgetAjustCDGrid = me.getCmp("nonProjectBudgetAjustCDDto");
        budgetAjustCDGrid.beforeBeforeEditGrid = me.beforeBeforeEditGrid;
        budgetAjustCDGrid.afterDeleteRow = me.afterDeleteRowWithCDDto;
        budgetAjustCDGrid.beforeDeleteRow = me.beforeDeleteRowWithCDDetail;

        var budgetAjustCD2Grid = me.getCmp("nonProjectBudgetAjustCD2Dto");
        budgetAjustCD2Grid.beforeBeforeEditGrid = me.beforeBeforeEditGrid;
        budgetAjustCD2Grid.afterDeleteRow = me.afterDeleteRowWithCD2Dto;
        budgetAjustCD2Grid.beforeDeleteRow = me.beforeDeleteRowWithCDDetail;
    },

    initDetailGrid: function () {
        var me = this;
        var managementSubjectGrid = me.getCmp("nonProjectBudgetAjustCDDto");
        var fixedAssetsGrid = me.getCmp("nonProjectBudgetAjustCD2Dto");
        managementSubjectGrid.afterEditGrid = me.managementSubjectGridChange;
        fixedAssetsGrid.afterEditGrid = me.fixedAssetsGridChange;

    },
    //更改管理费用
    managementSubjectGridChange: function (eventObj, isChanged) {
        var me = this;
        if (!isChanged) {
            return;
        }
        var budgetChanged = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var appliedAmount = rowData.appliedAmount ? rowData.appliedAmount : 0;
                var price = rowData.price ? rowData.price : 0;
                var orrigalBudgetAssigned = rowData.orrigalBudgetAssigned ? rowData.orrigalBudgetAssigned : 0;
                var changedValie = appliedAmount * price - orrigalBudgetAssigned;
                items[i].set("changedValie", changedValie);
                budgetChanged = budgetChanged + changedValie;
            }
        }
        var firstStore = eventObj.grid.up("IView").getCmp("nonProjectBudgetAjustCDto").getStore();
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '管理费用') {
                item.set("budgetChanged", budgetChanged);
            }
        });
    },

    //更改固定资产添置
    fixedAssetsGridChange: function (eventObj, isChanged) {
        var me = this;
        if (!isChanged) {
            return;
        }
        var budgetChanged = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var appliedAmount = rowData.appliedAmount ? rowData.appliedAmount : 0;
                var price = rowData.price ? rowData.price : 0;
                if (price != null && price != '') {
                    if (price < Erp.Const.FIXED_ASSET_LOW_LIMIT) {
                        Scdp.MsgUtil.info("固定资产单价不能小于2000！");
                        return;
                    }
                }
                var orrigalBudgetAssigned = rowData.orrigalBudgetAssigned ? rowData.orrigalBudgetAssigned : 0;
                var changedValie = appliedAmount * price - orrigalBudgetAssigned;
                items[i].set("changedValie", changedValie);
                budgetChanged = budgetChanged + changedValie;
            }
        }
        var firstStore = eventObj.grid.up("IView").getCmp("nonProjectBudgetAjustCDto").getStore();
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '固定资产添置') {
                item.set("budgetChanged", budgetChanged);
            }
        });
    },

    beforeBeforeEditGrid: function (a, b, c) {
        var me = this;
        if (c.field == "item" && Scdp.ObjUtil.isNotEmpty(c.record.get("orrigalBudgetAssigned"))) {
            return false;
        } else {
            return true;
        }
    },
    beforeDeleteRowWithCDDetail: function (a) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(a[0].get("orrigalBudgetAssigned"))) {
            Scdp.MsgUtil.warn("无法删除预算计划中的存在项！");
            return false;
        }
        else {
            return true;
//            Scdp.MsgUtil.confirm("是否确认删除？", function (e) {
//                if ("yes" == e) {
//                    return true;
//                }
//                else {
//                    return false;
//                }
//            });
        }
    },

    afterDeleteRowWithCDDto: function (a, b) {
        var me = this;
        var budgetChanged = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var appliedAmount = rowData.appliedAmount ? rowData.appliedAmount : 0;
                var price = rowData.price ? rowData.price : 0;
                var orrigalBudgetAssigned = rowData.orrigalBudgetAssigned ? rowData.orrigalBudgetAssigned : 0;
                var changedValie = appliedAmount * price - orrigalBudgetAssigned;

                budgetChanged = budgetChanged + changedValie;
            }
        }
        var firstStore = me.up("IView").getCmp("nonProjectBudgetAjustCDto").getStore();
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '管理费用') {
                item.set("budgetChanged", budgetChanged);
            }
        });
    },

    afterDeleteRowWithCD2Dto: function () {
        var me = this;
        var budgetChanged = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var appliedAmount = rowData.appliedAmount ? rowData.appliedAmount : 0;
                var price = rowData.price ? rowData.price : 0;
                var orrigalBudgetAssigned = rowData.orrigalBudgetAssigned ? rowData.orrigalBudgetAssigned : 0;
                var changedValie = appliedAmount * price - orrigalBudgetAssigned;
                budgetChanged = budgetChanged + changedValie;
            }
        }
        var firstStore = me.up("IView").getCmp("nonProjectBudgetAjustCDto").getStore();
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '固定资产添置') {
                item.set("budgetChanged", budgetChanged);
            }
        });
    },

    afterEditWithBudgetAjustCGrid: function (a, b) {
        if (b && a.field == "budgetChanged") {
            var budgetChanged = a.record.get("budgetChanged");
            budgetChanged = budgetChanged ? budgetChanged : 0;
            var orrigalBudgetAssigned = a.record.get("orrigalBudgetAssigned");
            orrigalBudgetAssigned = orrigalBudgetAssigned ? orrigalBudgetAssigned : 0;
            var newBugdet = orrigalBudgetAssigned + budgetChanged;
            a.record.set("afterAdjustMoney", newBugdet);
        }
    }
})
;