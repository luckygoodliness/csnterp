Ext.define("Cashreq.model.FadCashReqBudgetModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'fadCashReqUuid', 'budgetSubjectCode', 'budgetMoney', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'seqNo']
    }
);