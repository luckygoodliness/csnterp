Ext.define("Income.controller.IncomeController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Income.view.IncomeView',
    uniqueValidateFields: [],
    extraEvents: [
//        {cid: 'btnFillData', name: 'click', fn: 'fillData'},
        {cid: 'nonProjectIncomeDto', name: 'beforeedit', fn: 'doBeforeEdit'},//改变grid可编辑状态
        {cid: 'nonProjectIncomeInDto', name: 'beforeedit', fn: 'doBeforeEdit'}//改变grid可编辑状态
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeBalanceDto',
    queryAction: 'income-query',
    loadAction: 'income-load',
    addAction: 'income-add',
    modifyAction: 'income-modify',
    deleteAction: 'income-delete',
    exportXlsAction: "income-exportxls",
    afterInit: function () {
        var me = this;
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
        var postData = {year: year};
        Scdp.doAction("income-filldataAction", postData, function (out) {

            var abstractBudgetIn = out.namesIn;
            var abstractBudgetOut = out.namesOut;

//          var lastYearOccValue = out.lastYearOccValue;//前一年发生的总值
            var nonProjectIncomeInGrid = view.getCmp("nonProjectIncomeInDto");
            //每次查询之前删除上一次grid数据
            nonProjectIncomeInGrid.store.removeAll();
            for (var i = 0; i < abstractBudgetIn.length; i++) {
                var name = abstractBudgetIn[i].subject;
//              dataGrid.getSelectionModel().selectRow(grid.getStore().getCount()-1, true);
                var c = nonProjectIncomeInGrid.getStore();
                var d = Ext.ModelManager.create({}, nonProjectIncomeInGrid.store.model.modelName);
                d.set("subject", name);
                d.set("subjectOfficeName", abstractBudgetIn[i].subjectOfficeName);
                d.set("subjectOfficeId", abstractBudgetIn[i].subjectOfficeId);
                if (name == '收款计划' && abstractBudgetIn[i].subjectOfficeName == null) {
                    var schemingReceiptMoney = out.schemingReceiptMoney;
                    var srMoney = schemingReceiptMoney[0].srmoney;
                    d.set("appliedValue", srMoney);
                }
                if (name == '结题计划' && abstractBudgetIn[i].subjectOfficeName == null) {
                    var schemingSquare = out.schemingSquare;
                    var ssMoney = schemingSquare[0].ssmoney;
                    d.set("appliedValue", ssMoney);
                }
                nonProjectIncomeInGrid.store.insert(c.getCount(), d);
            }
            var sumValue = out.sumValue;//部门非项目支出各种值的和
            var lstValue = out.lstValues;//其他费用
            var nonProjectIncomeGrid = view.getCmp("nonProjectIncomeDto");
            nonProjectIncomeGrid.store.removeAll();
            for (var i = 0; i < abstractBudgetOut.length; i++) {
                var name = abstractBudgetOut[i].name;
                var otherValue;
                if (i == 0) {
                    otherValue = lstValue[i];
                } else {
                    otherValue = lstValue[i - 1];
                }
                var c = nonProjectIncomeGrid.getStore();
                var d = Ext.ModelManager.create({}, nonProjectIncomeGrid.store.model.modelName);
                d.set("subject", name);
                if (Scdp.ObjUtil.isNotEmpty(lstValue)) {

                    if (name == '部门非项目支出') {
//                  d.set("occurredValue", lastYearOccValue[0].occured);//前一年实际
                        d.set("occurredValue", sumValue[0].lastoccured);//前一年实际
                        d.set("appliedValue", sumValue[0].applyed);//当年上报
                        d.set("firstInstance", sumValue[0].instanced);//当年初审
                        d.set("assignedValue", sumValue[0].assigned);//当年终审
                    } else if (name == '非项目支出总计') {
                    } else {
                        d.set("occurredValue", otherValue[0].occurredValue);
                        d.set("appliedValue", otherValue[0].appliedValue);//当年上报
                        d.set("firstInstance", otherValue[0].firstInstance);//当年初审
                        d.set("assignedValue", otherValue[0].assignedValue);//当年终审
                    }
                    nonProjectIncomeGrid.store.insert(c.getCount(), d);
                } else {
                    if (name == '部门非项目支出') {
//                  d.set("occurredValue", lastYearOccValue[0].occured);//前一年实际
                        d.set("occurredValue", sumValue[0].lastoccured);//前一年实际
                        d.set("appliedValue", sumValue[0].applyed);//当年上报
                        d.set("firstInstance", sumValue[0].instanced);//当年初审
                        d.set("assignedValue", sumValue[0].assigned);//当年终审
                    }
                    nonProjectIncomeGrid.store.insert(c.getCount(), d);
                }
            }
        });
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
        var items = me.view.getCmp('nonProjectIncomeInDto').store.data.items;
        var incomeItem = me.view.getCmp('nonProjectIncomeDto').store.data.items;
        var nonProjectIncome2Grid = me.view.getCmp("nonProjectIncomeInDto");
        var nonProjectIncomeGrid = me.view.getCmp("nonProjectIncomeDto");
        var occTotalValue = 0;//前一年实际发生
        var appTotalValue = 0;//当年上报值
        var firTotalValue = 0;//当年初审值
        var assTotalValue = 0;//当年终审值
        if (Scdp.ObjUtil.isNotEmpty(incomeItem)) {
            var occ = 0;
            var app = 0;
            var fir = 0;
            var ass = 0;
            for (var i = 0; i < incomeItem.length; i++) {
                var subjectName = incomeItem[i].data.subject;
                occ = incomeItem[i].data.occurredValue;
                app = incomeItem[i].data.appliedValue;
                fir = incomeItem[i].data.firstInstance;
                ass = incomeItem[i].data.assignedValue;
                occTotalValue = occTotalValue + occ;
                appTotalValue = appTotalValue + app;
                firTotalValue = firTotalValue + fir;
                assTotalValue = assTotalValue + ass;
                if (subjectName == '非项目支出总计') {
                    incomeItem[i].set('occurredValue', occTotalValue);
                    incomeItem[i].set('appliedValue', appTotalValue);
                    incomeItem[i].set('firstInstance', firTotalValue);
                    incomeItem[i].set('assignedValue', assTotalValue);
                }
            }
        }
        var appTotalMoney = 0.0;//当年上报总值
        var lasAppTotalMoney = 0.0;//前一年上报总值
        var occTotalMoney = 0.0;//前一年实际发生总值
        var firTotalMoney = 0.0;//当年初审总值
        var assTotalMoney = 0.0;//当年终审总值
        if (Scdp.ObjUtil.isNotEmpty(items)) {
            var appliedMoney = 0.0;//当年上报
            var lastAppliedMoney = 0.0;//前一年上报
            var firstInstanceMoney = 0.0;//当年初审
            var occurredMoney = 0.0;//前一年实际
            var assignedMoney = 0.0;//当年终审
            for (var j = 0; j < items.length; j++) {
                var subjectName = items[j].data.subject;
                if (items[j].data.appliedValue != '') {
                    appliedMoney = items[j].data.appliedValue;
                    appTotalMoney = appTotalMoney + appliedMoney;
                }
                if (items[j].data.lastAppliedValue != '') {
                    lastAppliedMoney = items[j].data.lastAppliedValue;
                    lasAppTotalMoney = lasAppTotalMoney + lastAppliedMoney;
                }
                if (items[j].data.firstInstance != '') {
                    firstInstanceMoney = items[j].data.firstInstance;
                    firTotalMoney = firTotalMoney + firstInstanceMoney;
                }
                if (items[j].data.occurredValue != '') {
                    occurredMoney = items[j].data.occurredValue;
                    occTotalMoney = occTotalMoney + occurredMoney;
                }
                if (items[j].data.assignedValue != '') {
                    assignedMoney = items[j].data.assignedValue;
                    assTotalMoney = assTotalMoney + assignedMoney;
                }
                if (subjectName == '收入总计') {
                    items[j].set('appliedValue', appTotalMoney);
                    items[j].set('lastAppliedValue', lasAppTotalMoney);
                    items[j].set('occurredValue', occTotalMoney);
                    items[j].set('firstInstance', firTotalMoney);
                    items[j].set('assignedValue', assTotalMoney);
                }
            }
        }

        //form表单数据
        var applied = appTotalMoney - appTotalValue;
        var first = firTotalMoney - firTotalValue;
        var assigned = assTotalMoney - assTotalValue;
        var occurred = occTotalMoney - occTotalValue;
        me.view.getCmp("nonProjectIncomeBalanceDto->appliedValue").sotValue(applied);
        me.view.getCmp("nonProjectIncomeBalanceDto->firstInstance").sotValue(first);
        me.view.getCmp("nonProjectIncomeBalanceDto->assignedValue").sotValue(assigned);
        me.view.getCmp("nonProjectIncomeBalanceDto->occurredValue").sotValue(occurred);
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
        var title = me.view.getCmp('nonProjectIncomeBalanceDto->tit').rawValue;
        me.view.getCmp('nonProjectIncomeBalanceDto').down('JDisplay').setValue(title);

        var items = me.view.getCmp('nonProjectIncomeInDto').store.data.items;
        var incomeItem = me.view.getCmp('nonProjectIncomeDto').store.data.items;
//        var lastOcc = me.view.getCmp('lastOcc').store.data.items;
        var occTotalValue = 0;//前一年实际发生
        var appTotalValue = 0;//当年上报值
        var firTotalValue = 0;//当年初审值
        var assTotalValue = 0;//当年终审值
        if (Scdp.ObjUtil.isNotEmpty(incomeItem)) {
            var occ = 0;
            var app = 0;
            var fir = 0;
            var ass = 0;
//            var lastO = 0;
            for (var i = 0; i < incomeItem.length; i++) {
                var subject = incomeItem[i].data.subject;
                occ = incomeItem[i].data.occurredValue;
                app = incomeItem[i].data.appliedValue;
                fir = incomeItem[i].data.firstInstance;
                ass = incomeItem[i].data.assignedValue;
//                lastO = lastOcc[i].data.occ;
                occTotalValue = occTotalValue + occ;
                appTotalValue = appTotalValue + app;
                firTotalValue = firTotalValue + fir;
                assTotalValue = assTotalValue + ass;
                if (subject == '非项目支出总计') {
                    incomeItem[i].set('occurredValue', occTotalValue);
                    incomeItem[i].set('appliedValue', appTotalValue);
                    incomeItem[i].set('firstInstance', firTotalValue);
                    incomeItem[i].set('assignedValue', assTotalValue);
                }
            }
        }

        var appTotalMoney = 0.0;//当年上报总值
        var lasAppTotalMoney = 0.0;//前一年上报总值
        var occTotalMoney = 0.0;//前一年实际发生总值
        var firTotalMoney = 0.0;//当年初审总值
        var assTotalMoney = 0.0;//当年终审总值
        if (Scdp.ObjUtil.isNotEmpty(items)) {
            var appliedMoney = 0.0;//当年上报
            var lastAppliedMoney = 0.0;//前一年上报
            var firstInstanceMoney = 0.0;//当年初审
            var occurredMoney = 0.0;//前一年实际
            var assignedMoney = 0.0;//当年终审
            var completion = 0;
            for (var j = 0; j < items.length; j++) {
                var subjectName = items[j].data.subject;
                if (items[j].data.appliedValue != '') {
                    appliedMoney = items[j].data.appliedValue;
                    appTotalMoney = appTotalMoney + appliedMoney;
                }
                if (items[j].data.lastAppliedValue != '') {
                    lastAppliedMoney = items[j].data.lastAppliedValue;
                    lasAppTotalMoney = lasAppTotalMoney + lastAppliedMoney;
                }
                if (items[j].data.firstInstance != '') {
                    firstInstanceMoney = items[j].data.firstInstance;
                    firTotalMoney = firTotalMoney + firstInstanceMoney;
                }
                if (items[j].data.occurredValue != '') {
                    occurredMoney = items[j].data.occurredValue;
                    occTotalMoney = occTotalMoney + occurredMoney;
                }
                if (items[j].data.assignedValue != '') {
                    assignedMoney = items[j].data.assignedValue;
                    assTotalMoney = assTotalMoney + assignedMoney;
                }
                if(items[j].data.occurredValue != '' &&  items[j].data.lastAppliedValue != ''){
                    completion = (items[j].data.occurredValue)/(items[j].data.lastAppliedValue);
                    items[j].set('completion', completion);
                }

                if (subjectName == '收入总计') {
                    items[j].set('appliedValue', appTotalMoney);
                    items[j].set('lastAppliedValue', lasAppTotalMoney);
                    items[j].set('occurredValue', occTotalMoney);
                    items[j].set('firstInstance', firTotalMoney);
                    items[j].set('assignedValue', assTotalMoney);
                }
            }
        }

        //form表单数据
        var applied = appTotalMoney - appTotalValue;
        var first = firTotalMoney - firTotalValue;
        var assigned = assTotalMoney - assTotalValue;
        var occurred = occTotalMoney - occTotalValue;
        me.view.getCmp("nonProjectIncomeBalanceDto->appliedValue").sotValue(applied);
        me.view.getCmp("nonProjectIncomeBalanceDto->firstInstance").sotValue(first);
        me.view.getCmp("nonProjectIncomeBalanceDto->assignedValue").sotValue(assigned);
        me.view.getCmp("nonProjectIncomeBalanceDto->occurredValue").sotValue(occurred);
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

//    fillData: function () {
//        var me = this;
//        var view = me.view;
//        var d = new Date();
//        var year = d.getFullYear();
//        var postData = {year: year};
//        Scdp.doAction("income-filldataAction", postData, function (out) {
//            var abstractBudgetIn = out.namesIn;
//            var sumValue = out.sumValue;//部门非项目支出各种值的和
//            var lstValue = out.lstValues;//其他费用
////        var lastYearOccValue = out.lastYearOccValue;//前一年发生的总值
//            var abstractBudgetOut = out.namesOut;
//            var nonProjectIncomeInGrid = view.getCmp("nonProjectIncome2Dto");
////        //每次查询之前删除上一次grid数据
//            nonProjectIncomeInGrid.store.removeAll();
//            for (var i = 0; i < abstractBudgetIn.length; i++) {
//                var name = abstractBudgetIn[i].name;
////            dataGrid.getSelectionModel().selectRow(grid.getStore().getCount()-1, true);
//                var c = nonProjectIncomeInGrid.getStore();
//                var d = Ext.ModelManager.create({}, nonProjectIncomeInGrid.store.model.modelName);
//                d.set("subject", name);
//                if (name == '收款计划') {
//                    var schemingReceiptMoney = out.schemingReceiptMoney;
//                    var srMoney = schemingReceiptMoney[0].srmoney;
//                    d.set("appliedValue", srMoney);
//                }
//                nonProjectIncomeInGrid.store.insert(c.getCount(), d);
////            nonProjectIncomeGrid.store.insert(i, d);
//            }
//
//            var nonProjectIncomeGrid = view.getCmp("nonProjectIncomeDto");
//            nonProjectIncomeGrid.store.removeAll();
//            for (var i = 0; i < abstractBudgetOut.length; i++) {
//                var name = abstractBudgetOut[i].name;
//                var otherVlaue = lstValue[i];
//                var c = nonProjectIncomeGrid.getStore();
//                var d = Ext.ModelManager.create({}, nonProjectIncomeGrid.store.model.modelName);
//                d.set("subject", name);
//                if (name == '部门非项目支出') {
////                d.set("occurredValue", lastYearOccValue[0].occured);//前一年实际
//                    d.set("occurredValue", sumValue[0].lastoccured);//前一年实际
//                    d.set("appliedValue", sumValue[0].applyed);//当年上报
//                    d.set("firstInstance", sumValue[0].instanced);//当年初审
//                    d.set("assignedValue", sumValue[0].assigned);//当年终审
//                } else {
//                    d.set("occurredValue", otherVlaue[0].occurredValue);
//                    d.set("appliedValue", otherVlaue[0].appliedValue);//当年上报
//                    d.set("firstInstance", otherVlaue[0].firstInstance);//当年初审
//                    d.set("assignedValue", otherVlaue[0].assignedValue);//当年终审
//                }
//                nonProjectIncomeGrid.store.insert(c.getCount(), d);
//            }
//        });
//    },

//编辑之前执行，根据条件改变grid单元格可编辑状态
    doBeforeEdit: function (editor, context) {
        var me = this;
        //获取选中model
        var selectedIncome = me.view.getResultPanel().getSelectionModel().getSelection();
        var nonProjectIncomeInGrid = me.view.getCmp("nonProjectIncomeInDto");
        var nonProjectIncomeGrid = me.view.getCmp("nonProjectIncomeDto");
        if (context.field == 'occurredValue' || context.field == 'appliedValue'
            || context.field == 'firstInstance' || context.field == 'assignedValue'
            || context.field == 'completion' || context.field == 'lastAppliedValue') {

            if (context.record.data.subject == '部门非项目支出') {
                return false;
            }
//            context.record.data.subject == '经营合同计划'
            if (context.record.data.subject == '收款计划'
                || context.record.data.subject == '结题计划'
                || context.record.data.subject == '收入总计') {
                return false;
            }

        }
    }

});