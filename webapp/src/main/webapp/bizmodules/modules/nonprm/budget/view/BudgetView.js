Ext.define('Budget.view.BudgetView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/budget',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'budget-query-layout.xml',
    editLayoutFile: 'budget-edit-layout.xml',
    //extraLayoutFile: 'budget-extra-layout.xml',
    bindings: ['nonProjectBudgetHDto',
        'nonProjectBudgetHDto.nonProjectBudgetCDto',
        'nonProjectBudgetHDto.nonProjectBudgetCDDto',
        'nonProjectBudgetHDto.nonProjectBudgetCD2Dto'],

    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_Budget_Plan_Apply',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
//        me.getCmp("queryPanel->addNew1Btn").hide();
        me.getCmp("nonProjectBudgetCDDto").getCmp("toolbar").hide();
        me.getCmp("nonProjectBudgetCD2Dto").getCmp("toolbar").hide();
        var queryToolbar = me.getQueryToolbar();

        queryToolbar.add({
            text: '预算变更',
            cid: 'budgetAdjustment',
            iconCls: 'file_download_icon'
        });

        queryToolbar.add({
            text: '预算办理',
            cid: 'grantAdjustment',
            iconCls: 'temp_icon_16'
        });

        var cGrid = me.getCmp("nonProjectBudgetCDto");
        cGrid.afterSelect = function (d, rowIndex) {
            //获取选中model
            var selectedSubject = me.getResultPanel().getSelectionModel().getSelection();
            var nonProjectBudgetCDGrid = me.getCmp("nonProjectBudgetCDDto");
            var nonProjectBudgetCD2Grid = me.getCmp("nonProjectBudgetCD2Dto");
            if (d.data.subjectName == '管理费用') {
                nonProjectBudgetCDGrid.getCmp("toolbar").show();
                nonProjectBudgetCD2Grid.getCmp("toolbar").hide();
            } else if (d.data.subjectName == '固定资产添置') {
                nonProjectBudgetCD2Grid.getCmp("toolbar").show();
                nonProjectBudgetCDGrid.getCmp("toolbar").hide();
            } else {
                nonProjectBudgetCDGrid.getCmp("toolbar").hide();
                nonProjectBudgetCD2Grid.getCmp("toolbar").hide();
            }
        }
        me.getCmp("nonProjectBudgetHDto");
        me.initDetailGrid();
        //重写grid删除事件
        me.overrideDeleteToolbar();
        var deptCmp = me.getCmp("nonProjectBudgetHDto->officeId");
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
        var orgCode = view.getCmp('nonProjectBudgetHDto->officeId').gotValue();
        var year = view.getCmp('nonProjectBudgetHDto->year').lastValue;
        var nonProjectBudgetCGrid = view.getCmp("nonProjectBudgetCDto");
        var nonProjectBudgetCDGrid = view.getCmp("nonProjectBudgetCDDto");
        var nonProjectBudgetCD2Grid = view.getCmp("nonProjectBudgetCD2Dto");
        if (year == null || orgCode == null) {
            nonProjectBudgetCGrid.store.removeAll();
            nonProjectBudgetCDGrid.store.removeAll();
            nonProjectBudgetCD2Grid.store.removeAll();
            return;
        } else {
            var postData = {orgCode: orgCode, year: year};
            nonProjectBudgetCGrid.store.removeAll();
            nonProjectBudgetCDGrid.store.removeAll();
            nonProjectBudgetCD2Grid.store.removeAll();
            Scdp.doAction("budget-fill", postData, function (result) {
                //每次查询之前删除上一次grid数据
                var lstBudgetC = result.lstBudgetC;
                nonProjectBudgetCGrid.sotValue(lstBudgetC)
            });
        }
    },
    overrideDeleteToolbar: function () {
        var me = this;
        var budgetCDGrid = me.getCmp("nonProjectBudgetCDDto");
        budgetCDGrid.afterDeleteRow = me.afterDeleteRowWithCDDto;
        var budgetCD2Grid = me.getCmp("nonProjectBudgetCD2Dto");
        budgetCD2Grid.afterDeleteRow = me.afterDeleteRowWithCD2Dto;
    },
    initDetailGrid: function () {
        var me = this;
        var managementSubjectGrid = me.getCmp("nonProjectBudgetCDDto");
        var fixedAssetsGrid = me.getCmp("nonProjectBudgetCD2Dto");
        managementSubjectGrid.afterEditGrid = me.managementSubjectGridChange;
        fixedAssetsGrid.afterEditGrid = me.fixedAssetsGridChange;

    },
    //更改管理费用
    managementSubjectGridChange: function (eventObj, isChanged) {
        if (isChanged) {
            eventObj.grid.up("IView").fillFromCDDtoToCDto(eventObj, "管理费用")
        }
    },

    //更改固定资产添置
    fixedAssetsGridChange: function (eventObj, isChanged) {
        if (isChanged && (eventObj.field == "amount" || eventObj.field == "price")) {
            if (eventObj.field == "price" && eventObj.value < 2000) {
                Scdp.MsgUtil.info("固定资产单价不能小于2000！");
                return;
            }
            eventObj.grid.up("IView").fillFromCDDtoToCDto(eventObj, "固定资产添置")
        }
    },

    fillFromCDDtoToCDto: function (eventObj, name) {
        var me = this;
        var applyedTotalValue = 0;
        var firstStore = me.getCmp("nonProjectBudgetCDto").getStore();
        Ext.Array.each(eventObj.store.data.items, function (item) {
            var price = item.get("price") ? item.get("price") : 0;
            var amount = item.get("amount") ? item.get("amount") : 0;
            var totalValue = amount * price;
            item.set("totalValue", totalValue);
            applyedTotalValue = applyedTotalValue + totalValue;
        });
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == name) {
                var state = me.getCmp('nonProjectBudgetHDto->state').getValue();
                if (state == '0' || state == '5') {
                    item.set("thisYearApplyed", applyedTotalValue);
                } else {
                    item.set("thisYearFirstInstance", applyedTotalValue);
                    item.set("thisYearAssigned", applyedTotalValue);
                }
                return;
            }
        });
    },
    afterDeleteRowWithCDDto: function () {
        var me = this;
        var applyedTotalValue = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var amount = rowData.amount ? rowData.amount : 0;
                var price = rowData.price ? rowData.price : 0;
                var totalValue = amount * price;
                items[i].set("totalValue", totalValue);
                applyedTotalValue = applyedTotalValue + totalValue;
            }
        }
        var firstStore = me.up("IView").getCmp("nonProjectBudgetCDto").getStore();
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '管理费用') {
                item.set("thisYearApplyed", applyedTotalValue);
            }
        });
    },
    afterDeleteRowWithCD2Dto: function () {
        var me = this;
        var applyedTotalValue = 0;
        var items = me.store.data.items;
        var firstStore = me.up("IView").getCmp("nonProjectBudgetCDto").getStore();
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var rowData = items[i].data;
                var amount = rowData.amount ? rowData.amount : 0;
                var price = rowData.price ? rowData.price : 0;
                var totalValue = amount * price;
                items[i].set("totalValue", totalValue);
                applyedTotalValue = applyedTotalValue + totalValue;
            }
        }
        Ext.Array.each(firstStore.data.items, function (item) {
            if (item.get("subjectName") == '固定资产添置') {
                item.set("thisYearApplyed", applyedTotalValue);
                return;
            }
        });
    }
});