Ext.define('Budgeth.view.BudgethView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/budgeth',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'budgeth-query-layout.xml',
    editLayoutFile: 'budgeth-edit-layout.xml',
    //extraLayoutFile: 'budgeth-extra-layout.xml',
    bindings: ['nonProjectBudgetHDto', 'nonProjectBudgetHDto.nonProjectBudgetCDto',
        'nonProjectBudgetHDto.nonProjectBudgetCDDto',
        'nonProjectBudgetHDto.nonProjectBudgetCD2Dto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("queryPanel->addNew1Btn").hide();
        me.getCmp("editPanel->addNew2Btn").hide();
        me.getCmp("editPanel->copyAddBtn").hide();
        me.getCmp("editPanel->deleteBtn").hide();

        me.getCmp("nonProjectBudgetCDDto").getCmp("toolbar").hide();
        me.getCmp("nonProjectBudgetCD2Dto").getCmp("toolbar").hide();
        var cGrid = me.getCmp("nonProjectBudgetCDto");

        var grid = me.getCmp("nonProjectBudgetCDDto");
        grid.beforeEditGrid = function (eventObj) {
            return false;
        }

        var grid2 = me.getCmp("nonProjectBudgetCD2Dto");
        grid2.beforeEditGrid = function (eventObj) {
            return false;
        }
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
    }
});