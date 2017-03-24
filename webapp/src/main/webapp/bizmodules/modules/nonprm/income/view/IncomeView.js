Ext.define('Income.view.IncomeView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/income',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'income-query-layout.xml',
    editLayoutFile: 'income-edit-layout.xml',
    //extraLayoutFile: 'income-extra-layout.xml',
    bindings: ['nonProjectIncomeBalanceDto',
        'nonProjectIncomeBalanceDto.nonProjectIncomeDto',
        'nonProjectIncomeBalanceDto.nonProjectIncomeInDto'],
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

        me.getCmp("nonProjectIncomeBalanceDto");
//
//        var grid = me.getCmp("nonProjectIncomeDto");
//        grid.beforeEditGrid = function (eventObj) {
//            return false;
//        }
//
//        var grid2 = me.getCmp("nonProjectIncome2Dto");
//        grid2.beforeEditGrid = function (eventObj) {
//            return false;
//        }
        me.initDetailGrid();
    },
    initDetailGrid: function () {
        var me = this;
        var budgetInGrid = me.getCmp("nonProjectIncomeInDto");
        var budgetOutGrid = me.getCmp("nonProjectIncomeDto");
        budgetInGrid.afterEditGrid = me.budgetInTotalValueChange;
        budgetOutGrid.afterEditGrid = me.budgetOutTotalValueChange;
    },
    //预算收入总计值改变
    budgetInTotalValueChange: function (eventObj, isChanged) {
        var me = this;
        if (!isChanged) {
            return;
        }
        var applyedTotalValue = 0;
        var firstInstanceTotalValue = 0;
        var assignedTotalValue = 0;
        var occurredTotalValue = 0;
        var lastAppliedTotalValue = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var subjectName = items[i].data.subject;
                if (subjectName == '收入总计') {
                    items[i].set('appliedValue', applyedTotalValue);
                    items[i].set('firstInstance', firstInstanceTotalValue);
                    items[i].set('assignedValue', assignedTotalValue);
                    items[i].set('occurredValue', occurredTotalValue);
                    items[i].set('lastAppliedValue', lastAppliedTotalValue);
                }
                var rowData = items[i].data;
                var appliedValue = rowData.appliedValue ? rowData.appliedValue : 0;
                var firstInstance = rowData.firstInstance ? rowData.firstInstance : 0;
                var assignedValue = rowData.assignedValue ? rowData.assignedValue : 0;
                var occurredValue = rowData.occurredValue ? rowData.occurredValue : 0;
                var lastAppliedValue = rowData.lastAppliedValue ? rowData.lastAppliedValue : 0;
                applyedTotalValue = applyedTotalValue + appliedValue;
                firstInstanceTotalValue = firstInstanceTotalValue + firstInstance;
                assignedTotalValue = assignedTotalValue + assignedValue;
                occurredTotalValue = occurredTotalValue + occurredValue;
                lastAppliedTotalValue = lastAppliedTotalValue + lastAppliedValue;
            }
        }
    },
    budgetOutTotalValueChange:function(eventObj, isChanged){
        var me = this;
        if (!isChanged) {
            return;
        }
        var applyedTotalValue = 0;
        var firstInstanceTotalValue = 0;
        var assignedTotalValue = 0;
        var occurredTotalValue = 0;
        var items = me.store.data.items;
        if (items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                var subjectName = items[i].data.subject;
                if (subjectName == '非项目支出总计') {
                    items[i].set('appliedValue', applyedTotalValue);
                    items[i].set('firstInstance', firstInstanceTotalValue);
                    items[i].set('assignedValue', assignedTotalValue);
                    items[i].set('occurredValue', occurredTotalValue);
                }
                var rowData = items[i].data;
                var appliedValue = rowData.appliedValue ? rowData.appliedValue : 0;
                var firstInstance = rowData.firstInstance ? rowData.firstInstance : 0;
                var assignedValue = rowData.assignedValue ? rowData.assignedValue : 0;
                var occurredValue = rowData.occurredValue ? rowData.occurredValue : 0;
                applyedTotalValue = applyedTotalValue + appliedValue;
                firstInstanceTotalValue = firstInstanceTotalValue + firstInstance;
                assignedTotalValue = assignedTotalValue + assignedValue;
                occurredTotalValue = occurredTotalValue + occurredValue;
            }
        }
    }
});