Ext.define("Budgetajust.model.NonProjectBudgetAjustHModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'year', 'officeId', 'state', 'budgetType', 'budgetAjustTotal', 'officeIdDesc', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion']
    }
);